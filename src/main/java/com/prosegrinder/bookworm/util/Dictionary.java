package com.prosegrinder.bookworm.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Singleton Class for creating and caching Words.
 *
 * <p>
 * Currently uses cmudict (https://github.com/cmusphinx/cmudict) for lookups and implements
 * heuristics based on the Lingua::EN::Syllable PERL module by NEILB
 * (https://github.com/neilb/Lingua-EN-Syllable). Original author comments preserved and noted with
 * 'nielb:'.
 *
 * <p>
 * Singleton implementation based on:
 * http://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples
 */
@Deprecated
public final class Dictionary {

  private static Dictionary INSTANCE;
  private LoadingCache<String, Word> wordCache;

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
   * @return the SyllableDictionary Singleton for use
   */
  public static synchronized Dictionary getInstance() {
    if (INSTANCE == null) {
      synchronized (Dictionary.class) {
        if (INSTANCE == null) {
          INSTANCE = new Dictionary();
        }
      }
    }
    return INSTANCE;
  }

  /** Private constructor to enforce Singleton. **/
  private Dictionary() {
    wordCache = CacheBuilder.newBuilder()
        .maximumSize(100000)
        .build(
            new CacheLoader<String, Word>() {
              public Word load(String wordString) {
                return loadWord(wordString);
              }
            });
  }

  /**
   * Get the number of syllables by checking cmudict.
   *
   * @param wordString a single word
   * @return the number of syllables in the word
   * @throws IllegalArgumentException thrown if the word is not in the underlying dictionary
   *
   */
  public final Integer getCMUDictSyllableCount(final String wordString)
      throws IllegalArgumentException {
    CMUDict cmudict = CMUDict.getInstance();
    return cmudict.getSyllableCount(wordString);
  }

  /**
   * Estimates the number of syllables by heuristic.
   *
   * <p>
   * Minimal testing shows this has about 80%+ match against cmudict.
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
       * <p>
       * neilb: Syllables for all-digit words (eg, "1998"; some call them "numbers") are often
       * counted as the number of digits. A cooler solution would be converting "1998" to "nineteen
       * eighty eight" (or "one thousand nine hundred eighty eight", or...), but that is left as an
       * exercise for the reader.
       *
       * <p>
       * me: In fiction, people don't usually "read" numbers. It's okay to simply count the number
       * of digits and sign.
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
   * Get number of syllables using all methods in order of precedence.
   *
   * <p>
   * Currently, tries to find the number of syllables by lookup, and falls back to estimation by
   * heuristics if the word is not in any of the underlying sources.
   *
   * @param wordString a single word
   * @return the approximate number of syllables in the word
   *
   */
  public final Integer getSyllableCount(final String wordString) {
    CMUDict cmudict = CMUDict.getInstance();
    if (this.inDictionary(wordString)) {
      return cmudict.getSyllableCount(WordContainer.normalizeText(wordString));
    } else {
      return this.getHeuristicSyllableCount(wordString);
    }
  }

  /**
   * Public word loader. Pulls from cache first.
   * 
   * @param wordString  a single word
   * @return a Word object represented by wordString
   */
  public final Word getWord(final String wordString) throws IllegalArgumentException {
    try {
      return wordCache.get(wordString);
    } catch (ExecutionException e) {
      throw new IllegalArgumentException(e.getCause());
    }
  }

  /**
   * Private word loader used to create a new word if it's not in the cache.
   * 
   * @param wordString
   * @return a Word object represented by wordString
   */
  private final Word loadWord(final String wordString) {
    Word word =
        new Word(wordString, this.getSyllableCount(wordString),
            this.inDictionary(wordString), this.isNumeric(wordString));
    return word;
  }

  /**
   * Test if a String is in an underlying real dictionary.
   * 
   * @param wordString a string representing a single word
   * @return boolean representing whether the word is found in the underlying dictionary
   */
  public final Boolean inDictionary(final String wordString) {
    /** Only one reference dictionary for now. No numbers in the dictionary. **/
    if (!wordString.matches(Dictionary.RE_NUMERIC)) {
      return CMUDict.getInstance().inCMUDict(WordContainer.normalizeText(wordString));
    } else {
      return false;
    }
  }

  /**
   * Test if a String is a typical number.
   * 
   * @param wordString a string representing a single word
   * @return boolean representing whether the word is a number
   */
  public final boolean isNumeric(final String wordString) {
    return wordString.matches(Dictionary.RE_NUMERIC);
  }

}
