package com.prosegrinder.bookworm.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * A cache of Words.
 *
 * <p>Currently uses cmudict (https://github.com/cmusphinx/cmudict) for lookups 
 * and implements heuristics based on the Lingua::EN::Syllable PERL module by NEILB
 * (https://github.com/neilb/Lingua-EN-Syllable). Original author comments preserved
 * and noted with 'nielb:'.
 *
 */
public final class Dictionary2 {

  /** Location of cmudict.dict file. **/
  private static String cmudictFile;
  /** Maximum number of entries in wordCache. **/
  private static Long maxWordCacheSize;
  /** TTL of non-words. **/
  private static Long ttlSecondsNonWordCache;
  /** Whether or not numbers are cached. **/
  private static Boolean cacheNumbers;
  /** Cache for Words. **/
  private static LoadingCache<String, Word> wordCache;
  /** Cache for Non-Words. **/
  private static LoadingCache<String, Word> nonWordCache;

  /** Concurrent Hash Map for storing Cmudict lines. **/
  private static final Map<String, String> phonemeStringMap =
      new ConcurrentHashMap<String, String>();

  /** Patterns used to find stressed syllables in cmudict (phonemes that end in a digit). **/
  private static final Pattern cmudictSyllablePattern = Pattern.compile("\\d$");

  /** Log4j Logger. **/
  private static final Logger logger = LogManager.getLogger(Dictionary2.class);

  /** Regex used to test if a string represents a number. **/
  private static final String RE_NUMERIC = "^[+-]{0,1}\\d{1,3}(?:[,]\\d{3})*(?:[.]\\d*)*$";

  /** Patterns that subtract from the syllable count. **/
  private static final Pattern[] subSyl =
      new Pattern[] {Pattern.compile("cial"), Pattern.compile("tia"), Pattern.compile("cius"),
          Pattern.compile("cious"), Pattern.compile("giu"), // neilb: belgium!
          Pattern.compile("ion"), Pattern.compile("iou"), Pattern.compile("sia$"),
          Pattern.compile(".ely$"), // neilb: absolutely! (but not ely!)
          Pattern.compile("[^td]ed$") // neilb: accused is 2, but executed is 4
      };
  /** Patterns that add to the syllable count. **/
  private static final Pattern[] addSyl =
      new Pattern[] {Pattern.compile("ia"), Pattern.compile("riet"), Pattern.compile("dien"),
          Pattern.compile("iu"), Pattern.compile("io"), Pattern.compile("ii"),
          Pattern.compile("microor"), Pattern.compile("[aeiouym]bl$"), // neilb: -Vble, plus -mble
          Pattern.compile("[aeiou]{3}"), // neilb: agreeable
          Pattern.compile("^mc"), Pattern.compile("ism$"), // neilb: -ism
          Pattern.compile("isms$"), // neilb: -isms
          Pattern.compile("([^aeiouy])\1l$"), // neilb: middle twiddle battle bottle, etc.
          Pattern.compile("[^l]lien"), // neilb: alien, salient [1]
          Pattern.compile("^coa[dglx]."), // neilb: [2]
          Pattern.compile("[^gq]ua[^auieo]"), // neilb: i think this fixes more than it breaks
          Pattern.compile("dnt$") // neilb: couldn't
      };

  /**
   * Get a new Dictionary2 using a configuration object.
   * 
   * <p>This is a convenience method, and will likely be deprecated
   * or made private to enforce dependency injection vie the constructors.
   * 
   * @param config A Typesafe Config containing necessary parameters for construction.
   * @return a new Dictionary2
   *  
   */
  public static final Dictionary2 getDictionary(Config config) {
    String cmudictFile = config.getString("cmudict.file");
    Long maxWordCacheSize = config.getLong("wordCache.maxentries");
    Long ttlSecondsNonWordCache = config.getLong("nonWordCache.ttlSeconds");
    Boolean cacheNumbers = config.getBoolean("nonWordCache.cacheNumbers");
    Dictionary2 dictionary =
        new Dictionary2(cmudictFile, maxWordCacheSize, ttlSecondsNonWordCache, cacheNumbers);
    return dictionary;
  }

  public static final Dictionary2 getDefaultDictionary() {
    Config config = ConfigFactory.load().getConfig("com.prosegrinder.bookworm.util.dictionary");
    return Dictionary2.getDictionary(config);
  }

  public static final Boolean cacheNumbers() {
    return Dictionary2.cacheNumbers;
  }

  public static final String getCmudictfile() {
    return Dictionary2.cmudictFile;
  }

  public static final Long getMaxWordCacheSize() {
    return Dictionary2.maxWordCacheSize;
  }

  public static final Long getTtlSecondsNonWordCache() {
    return Dictionary2.ttlSecondsNonWordCache;
  }

  /**
   * Create a new Dictionary2 instance.
   * 
   * <p>Dictionary2 is a collection of caches that coordinate
   * to speed up creation of Words. It employs a multilevel
   * cache strategy to balance memory and performance.
   * 
   * <p>The lowest level cache permanently holds all of the
   * phoneme strings found in the cmudict.dict file to avoid
   * multiple reads from disk.
   * 
   * <p>The word cache holds all words generated from the 
   * phoneme strings find found in cmudict.dict file. These
   * are cached eternally, but the size of this cache is limited
   * to 10,000 by default. The cmudict.dict file contains roughly
   * 135,000 entries.
   * 
   * <p>The non-word cache contains any words created that are
   * not generated by phoneme strings. These are cached for 10s
   * by default, as the assumption is they're not likely to be
   * seen frequently across texts, but may be used repeatedly
   * within a piece of text.
   * 
   * @param cmudictFile resource path to cmudict.dict file
   * @param maxWordCacheSize maximum number of words to cache 
   * @param ttlSecondsNonWordCache how long non-words live in cache
   * @param cacheNumbers whether or not to cache numbers
   */
  public Dictionary2(String cmudictFile, Long maxWordCacheSize, Long ttlSecondsNonWordCache,
      Boolean cacheNumbers) {
    if (!cmudictFile.equals(Dictionary2.cmudictFile)) {
      this.loadCmudictFile(cmudictFile);
      if (Dictionary2.wordCache instanceof LoadingCache) {
        Dictionary2.wordCache.invalidateAll();
      }
    }
    if (!maxWordCacheSize.equals(Dictionary2.maxWordCacheSize)) {
      this.initializeWordCache(maxWordCacheSize);
    }
    if (!ttlSecondsNonWordCache.equals(Dictionary2.ttlSecondsNonWordCache)) {
      this.initializeNonWordCache(ttlSecondsNonWordCache, cacheNumbers);
    }
  }

  public Dictionary2() throws IOException {
    this(Dictionary2.getCmudictfile(), Dictionary2.getMaxWordCacheSize(),
        Dictionary2.getTtlSecondsNonWordCache(), Dictionary2.cacheNumbers());
  }

  /**
   * Get the number of syllables by looking up the word in the underlying cmudict.
   *
   * @param wordString a single word
   * @return the number of syllables in the word
   * @throws IllegalArgumentException throws if the word is not in the underlying dictionary
   *
   */
  public final Integer getCmudictSyllableCount(final String wordString)
      throws IllegalArgumentException {
    Integer syllables = 0;
    List<String> phonemes = this.getPhonemes(wordString);
    for (String phoneme : phonemes) {
      Matcher matcher = Dictionary2.cmudictSyllablePattern.matcher(phoneme);
      if (matcher.find()) {
        syllables++;
      }
    }
    return syllables;
  }

  /**
   * Estimates the number of syllables by heuristic.
   *
   * <p>Minimal testing shows this has about 80%+ match against cmudict.
   *
   * @param wordString a single word
   * @return the approximate number of syllables in the word
   *
   */
  public final Integer getHeuristicSyllableCount(final String wordString) {
    // Lower case, fold contractions, and strip silent e off the end.
    String strippedWord = wordString.trim().toLowerCase().replaceAll("'", "").replaceAll("e$", "");
    int syllableCount = 0;

    if (strippedWord == null || "".equals(strippedWord)) {
      syllableCount = 0;
    } else if ("w".equals(strippedWord)) {
      syllableCount = 2;
    } else if (strippedWord.length() == 1) {
      syllableCount = 1;
    } else if (isNumeric(strippedWord)) {
      /**
       * Is the word a number?
       *
       * <p>neilb: Syllables for all-digit words (eg, "1998"; some call them "numbers")
       * are often counted as the number of digits. A cooler solution would be 
       * converting "1998" to "nineteen eighty eight" (or "one thousand nine hundred 
       * eighty eight", or...), but that is left as an exercise for the reader.
       *
       * <p>dld: In fiction, people don't usually "read" numbers. It's okay to 
       * simply count the number of digits and sign.
       */
      return strippedWord.replaceAll("[,.]", "").length();
    } else {
      String[] scrugg = strippedWord.split("[^aeiouy]+"); // neilb: perhaps - should be added?
      for (Pattern pattern : addSyl) {
        Matcher matcher = pattern.matcher(strippedWord);
        if (matcher.matches()) {
          syllableCount--;
        }
      }
      for (Pattern pattern : subSyl) {
        Matcher matcher = pattern.matcher(strippedWord);
        if (matcher.matches()) {
          syllableCount++;
        }
      }

      /** Count vowel groupings. **/
      if (scrugg.length > 0 && "".equals(scrugg[0])) {
        syllableCount += scrugg.length - 1;
      } else {
        syllableCount += scrugg.length;
      }

      /** If there are no vowles, assume 1 syllable. **/
      if (syllableCount == 0) {
        syllableCount = 1;
      }
    }
    return syllableCount;
  }

  /**
   * Get cmudict phonemes for a word.
   * 
   * @param wordString a single word
   * @return List of Stings representing the phonemes
   * @throws IllegalArgumentException thrown if the word is not in cmudict
   *
   */
  public final List<String> getPhonemes(final String wordString) throws IllegalArgumentException {
    return Arrays.asList(this.getPhonemeString(wordString).split("\\s+"));
  }

  /**
   * Get the number of syllables by looking up the word in the underlying cmudict.
   *
   * @param wordString a single word
   * @return the number of syllables in the word
   * @throws IllegalArgumentException throws if the word is not in the underlying dictionary
   *
   */
  public final String getPhonemeString(final String wordString) throws IllegalArgumentException {
    if (phonemeStringMap.containsKey(wordString)) {
      return phonemeStringMap.get(wordString);
    } else {
      String msg = "cmudict does not contain an entry for " + wordString + ".";
      throw new IllegalArgumentException(msg);
    }
  }

  /**
   * Get number of syllables using all methods in order of precedence.
   *
   * <p>Currently, tries to find the number of syllables by lookup, and falls back to estimation by
   * heuristics if the word is not in any of the underlying sources.
   *
   * @param wordString a single word
   * @return the approximate number of syllables in the word
   *
   */
  public final Integer getSyllableCount(final String wordString) {
    if (this.inCmudict(wordString)) {
      return this.getCmudictSyllableCount(wordString);
    } else {
      return this.getHeuristicSyllableCount(wordString);
    }
  }

  /**
   * Public word loader. Pulls from cache first.
   * 
   * @param wordString a single word
   * @return a Word object represented by wordString
   */
  public final Word getWord(final String wordString) throws IllegalArgumentException {
    Boolean inCmudict = this.inCmudict(wordString);
    Boolean isNumeric = this.isNumeric(wordString);
    try {
      if (inCmudict) {
        return wordCache.get(wordString);
      } else if (!isNumeric || Dictionary2.cacheNumbers) {
        return nonWordCache.get(wordString);
      } else {
        return new Word(wordString, this.getSyllableCount(wordString), inCmudict, isNumeric);
      }
    } catch (ExecutionException ee) {
      throw new IllegalArgumentException(ee.getCause());
    }
  }

  /**
   * Test if a String is in an underlying real dictionary.
   * 
   * @param wordString a string representing a single word
   * @return boolean representing whether the word is found in the underlying dictionary
   */
  public final Boolean inCache(final String wordString) {
    return this.inWordCache(wordString) || this.inNonWordCache(wordString);
  }

  /**
   * Test if a String is in the underlying cmudict dictionary.
   * 
   * @param wordString a string representing a single word
   * @return boolean representing whether the word is found in the underlying dictionary
   * 
   */
  public final Boolean inCmudict(final String wordString) {
    try {
      this.getPhonemeString(wordString);
      return true;
    } catch (IllegalArgumentException iae) {
      // logger.warn(iae.getMessage());
      return false;
    }
  }


  private void initializeNonWordCache(Long ttlNonWordCache, Boolean cacheNumbers) {
    if (nonWordCache instanceof LoadingCache) {
      logger.warn("Resetting non-word cache ttl " + ttlNonWordCache
          + " seconds. All existing entries will be lost.");
    } else {
      logger.info("Initializing non-word cache ttl: " + ttlNonWordCache + " seconds");
    }
    Dictionary2.cacheNumbers = cacheNumbers;
    Dictionary2.ttlSecondsNonWordCache = ttlNonWordCache;
    Dictionary2.nonWordCache =
        CacheBuilder.newBuilder().expireAfterAccess(ttlNonWordCache, TimeUnit.SECONDS)
            .build(new CacheLoader<String, Word>() {
              public Word load(String wordString) {
                return loadWord(wordString);
              }
            });
  }

  private void initializeWordCache(Long maxWordCacheSize) {
    if (wordCache instanceof LoadingCache) {
      logger.warn("Resizing word cache max size " + maxWordCacheSize
          + " entries. All existing entries will be lost.");
    } else {
      logger.info("Initializing word cache max size: " + maxWordCacheSize);
    }
    Dictionary2.maxWordCacheSize = maxWordCacheSize;
    Dictionary2.wordCache = CacheBuilder.newBuilder().maximumSize(maxWordCacheSize)
        .build(new CacheLoader<String, Word>() {
          public Word load(String wordString) {
            return loadWord(wordString);
          }
        });
  }

  /**
   * Test if a String is in an underlying real dictionary.
   * 
   * @param wordString a string representing a single word
   * @return boolean representing whether the word is found in the underlying dictionary
   */
  public final Boolean inNonWordCache(final String wordString) {
    return nonWordCache.asMap().containsKey(wordString);
  }

  /**
   * Test if a String is in an underlying real dictionary.
   * 
   * @param wordString a string representing a single word
   * @return boolean representing whether the word is found in the underlying dictionary
   */
  public final Boolean inWordCache(final String wordString) {
    return wordCache.asMap().containsKey(wordString);
  }

  /**
   * Test if a String is a typical number.
   * 
   * @param wordString a string representing a single word
   * @return boolean representing whether the word is a number
   */
  public final boolean isNumeric(final String wordString) {
    return wordString.matches(Dictionary2.RE_NUMERIC);
  }

  private void loadCmudictFile(String cmudictFile) {
    Dictionary2.cmudictFile = cmudictFile;
    logger.info("Loading CMU Dictionary file: " + cmudictFile);
    Dictionary2.phonemeStringMap.clear();
    ClassLoader classLoader = Dictionary2.class.getClassLoader();
    InputStream in = classLoader.getResourceAsStream(cmudictFile);
    try {
      // Updated based on: https://stackoverflow.com/questions/20389255/reading-a-resource-file-from-within-jar
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      Stream<String> stream = reader.lines();
      stream.filter(line -> !line.startsWith(";;;")).forEach(line -> {
        String[] parts = line.split("\\s+", 2);
        String wordString = parts[0];
        phonemeStringMap.put(wordString, parts[1]);
      });
    } catch (NullPointerException npe) {
      logger.warn("CMU Dictionary file not found: " + Dictionary2.cmudictFile);
    }
  }

  /**
   * Private word loader used to create a new word if it's not in the cache.
   * 
   * @param wordString a string representing a single word
   * @return a Word object represented by wordString
   */
  private final Word loadWord(final String wordString) {
    Word word = new Word(wordString, this.getSyllableCount(wordString), this.inCmudict(wordString),
        this.isNumeric(wordString));
    return word;
  }

}
