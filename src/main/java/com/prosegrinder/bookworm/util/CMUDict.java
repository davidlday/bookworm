package com.prosegrinder.bookworm.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * A Singleton Class to access cmudict data.
 *
 * <p>
 * Parses phonemes and syllable counts from cmudict (https://github.com/cmusphinx/cmudict).
 *
 * <p>
 * Singleton implementation based on:
 * http://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples
 */
public final class CMUDict {

  private static CMUDict INSTANCE = new CMUDict();
  private Map<String, String> phonemeStringMap;

  /** Patterns used to find stressed syllables in cmudict (symbols that end in a digit). **/
  private static final Pattern cmudictSyllablePattern = Pattern.compile("\\d$");

  /** Log4j Logger. **/
  private static final Logger logger = LogManager.getLogger(CMUDict.class);

  /**
   * @return the CMUDict Singleton for use
   */
  public static synchronized CMUDict getInstance() {
    return INSTANCE;
  }

  /** Private constructor to enforce Singelton. **/
  private CMUDict() {
    phonemeStringMap = new ConcurrentHashMap<String, String>();
    String cmudict = "cmudict/cmudict.dict";
    try {
      InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(cmudict);
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      Stream<String> stream = reader.lines();
      stream.filter(line -> !line.startsWith(";;;")).map(String::toLowerCase).forEach(line -> {
        String[] parts = line.split("\\s+", 2);
        String wordString = parts[0];
        if (!wordString.endsWith(")")) {
          phonemeStringMap.put(wordString, parts[1]);
        }
      });
    } catch (Exception e) {
      logger.error("Exception during cmudict load: " + e);
    }
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
    if (!this.inCMUDict(wordString)) {
      String msg = "Dictionary does not contain an entry for " + wordString + ".";
      logger.error(msg);
      throw new IllegalArgumentException(msg);
    } else {
      return Arrays.asList(phonemeStringMap.get(wordString).split("\\s+"));
    }
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
    if (!this.inCMUDict(wordString)) {
      String msg = "Dictionary does not contain an entry for " + wordString + ".";
      logger.error(msg);
      throw new IllegalArgumentException(msg);
    } else {
      return phonemeStringMap.get(wordString);
    }
  }

  /**
   * @return the underlying Map of word:phoneme pairs.
   */
  public final Map<String, String> getPhonemeStringMap() {
    return Collections.unmodifiableMap(phonemeStringMap);
  }

  /**
   * Get the number of syllables by looking up the word in the underlying cmudict.
   *
   * @param wordString a single word
   * @return the number of syllables in the word
   * @throws IllegalArgumentException throws if the word is not in the underlying dictionary
   *
   */
  public final Integer getSyllableCount(final String wordString) throws IllegalArgumentException {
    Integer syllables = 0;
    if (!this.inCMUDict(wordString)) {
      String msg = "Dictionary does not contain an entry for " + wordString + ".";
      logger.error(msg);
      throw new IllegalArgumentException(msg);
    } else {
      List<String> phonemes = this.getPhonemes(wordString);
      for (String phoneme : phonemes) {
        Matcher matcher = cmudictSyllablePattern.matcher(phoneme);
        if (matcher.find()) {
          syllables++;
        }
      }
    }
    return syllables;
  }

  /**
   * Test if a String is in the underlying cmudict dictionary.
   * 
   * @param wordString a string representing a single word
   * @return boolean representing whether the word is found in the underlying dictionary
   * 
   */
  public final Boolean inCMUDict(final String wordString) {
    return phonemeStringMap.containsKey(wordString);
  }

}
