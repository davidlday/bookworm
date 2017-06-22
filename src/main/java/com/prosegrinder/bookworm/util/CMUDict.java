package com.prosegrinder.bookworm.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * A Singleton Class to access cmudict data.
 *
 * <p>Parses phonemes and syllable counts from 
 * cmudict (https://github.com/cmusphinx/cmudict).
 *
 * <p>Singleton implementation based on:
 * http://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples
 */
public final class CMUDict {

  private static volatile CMUDict INSTANCE;
  private static volatile Map<String, String> phonemeStringMap;
  
  /** Patterns used to find stressed syllables in cmudict (symbols that end in a digit). **/
  private static final Pattern cmudictSyllablePattern = Pattern.compile("\\d$");

  /** Log4j Logger. **/
  private static final Logger logger = LogManager.getLogger(CMUDict.class);

  /**
   * @return    the CMUDict Singleton for use
   */
  public static synchronized CMUDict getInstance() {
    if (INSTANCE == null) {
      synchronized (CMUDict.class) {
        INSTANCE = new CMUDict();
      }
    }
    return INSTANCE;
  }

  /** Private constructor to enforce Singelton. **/
  private CMUDict() {
    phonemeStringMap = new HashMap<String, String>();
    String cmudict = "cmudict/cmudict.dict";
    try {
      InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(cmudict);
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      Stream<String> stream = reader.lines();
      stream.filter(line -> !line.startsWith(";;;"))
          .map(String::toLowerCase)
          .forEach(line -> {
            String[] parts = line.split("\\s+", 2);
            String word = parts[0];
            if (!word.endsWith(")")) {
              phonemeStringMap.put(word, parts[1]);
            }
          });
    } catch (Exception e) {
      logger.error("Exception during cmudict load: " + e);
    }
  }

  /**
   * Get the number of syllables by looking up the word in the underlying cmudict.
   *
   * @param word  a single word
   * @return  the number of syllables in the word
   * @throws NullPointerException throws if the word is not in the underlying dictionary
   *
   */
  public final String getPhonemeString(final String word) throws NullPointerException {
    if (!this.inCMUDict(word)) {
      String msg = "Dictionary does not contain an entry for " + word + ".";
      logger.error(msg);
      throw new NullPointerException(msg);
    } else {
      return phonemeStringMap.get(word);
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
   * @param word  a single word
   * @return  the number of syllables in the word
   * @throws NullPointerException throws if the word is not in the underlying dictionary
   *
   */
  public final Integer getSyllableCount(final String word) throws NullPointerException {
    Integer syllables = 0;
    if (!this.inCMUDict(word)) {
      String msg = "Dictionary does not contain an entry for " + word + ".";
      logger.error(msg);
      throw new NullPointerException(msg);
    } else {
      List<String> phonemes = this.getPhonemes(word);
      for (String phoneme: phonemes) {
        Matcher matcher = cmudictSyllablePattern.matcher(phoneme);
        if (matcher.find()) {
          syllables++;
        }
      }
    }
    return syllables;
  }

  /**
   * Get cmudict phonemes for a word.
   * 
   * @param word    a single word
   * @return List of Stings representing the phonemes
   * @throws NullPointerException throws if the word is not in cmudict
   *
   */
  public final List<String> getPhonemes(final String word) throws NullPointerException {
    if (!this.inCMUDict(word)) {
      String msg = "Dictionary does not contain an entry for " + word + ".";
      logger.error(msg);
      throw new NullPointerException(msg);
    } else {
      return Arrays.asList(phonemeStringMap.get(word).split("\\s+"));
    }
  }
  
  /**
   * Test if a String is in the underlying cmudict dictionary.
   * 
   * @param word    a string representing a single word
   * @return boolean representing whether the word is found in the underlying dictionary
   * 
   */
  public final Boolean inCMUDict(final String word) {
    return phonemeStringMap.containsKey(word);
  }

}
