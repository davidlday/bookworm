package com.prosegrinder.bookworm.util;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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

  private static CMUDict INSTANCE;
  private Map<String, String> phonemeStringMap;

  /** Location of cmudict.dict file. **/
  private String CMUDictFile;
  /** Patterns used to find stressed syllables in cmudict (symbols that end in a digit). **/
  private Pattern cmudictSyllablePattern = Pattern.compile("\\d$");

  /** Log4j Logger. **/
  private static final Logger logger = LogManager.getLogger(CMUDict.class);

  /**
   * @return the CMUDict Singleton for use
   */
  public static synchronized CMUDict getInstance() {
    if (INSTANCE == null) {
      synchronized (CMUDict.class) {
        if (INSTANCE == null) {
          INSTANCE = new CMUDict();
        }
      }
    }
    return INSTANCE;
  }

  private CMUDict() {
    Config config = ConfigFactory.load();
    config.checkValid(ConfigFactory.defaultReference(), "com.prosegrinder.bookworm.util");
    phonemeStringMap = new ConcurrentHashMap<String, String>();
    loadCmudictFile(config.getString("com.prosegrinder.bookworm.util.cmudict.file"),
        config.getString("com.prosegrinder.bookworm.util.cmudict.syllablePattern"));
  }

  private void loadCmudictFile(String cmudictfile, String syllablePattern) {
    logger.info("Loading " + cmudictfile);
    CMUDictFile = cmudictfile;
    cmudictSyllablePattern = Pattern.compile(syllablePattern);
    try {
      InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(CMUDictFile);
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      Stream<String> stream = reader.lines();
      stream.filter(line -> !line.startsWith(";;;")).forEach(line -> {
        String[] parts = line.split("\\s+", 2);
        String wordString = parts[0];
        if (!wordString.endsWith(")")) {
          phonemeStringMap.put(wordString, parts[1]);
        }
      });
      in.close();
    } catch (IOException ioe) {
      logger.error(ioe.getMessage());
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
  public final Integer getSyllableCount(final String wordString) throws IllegalArgumentException {
    Integer syllables = 0;
    List<String> phonemes = this.getPhonemes(wordString);
    for (String phoneme : phonemes) {
      Matcher matcher = cmudictSyllablePattern.matcher(phoneme);
      if (matcher.find()) {
        syllables++;
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
    try {
      this.getPhonemeString(wordString);
      return true;
    } catch (IllegalArgumentException iae) {
      // logger.warn(iae.getMessage());
      return false;
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
    if (phonemeStringMap.containsKey(wordString)) {
      return phonemeStringMap.get(wordString);
    } else {
      // This is all bypassed for now since the whole file is parsed and cached on instantiation.
      // Will revisit performance vs. memory trade-off later.
      String phoneme = "";
      try {
        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(CMUDictFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        Stream<String> stream = reader.lines();
        List<String> phonemeStrings =
            stream.filter(line -> line.startsWith(wordString + " ")).collect(Collectors.toList());
        in.close();
        if (phonemeStrings.size() == 1) {
          String[] parts = phonemeStrings.get(0).split("\\s+", 2);
          phoneme = parts[1];
        } else if (phonemeStrings.size() < 1) {
          String msg = "cmudict does not contain an entry for " + wordString + ".";
          throw new IllegalArgumentException(msg);
        } else {
          String msg = "cmudict contains multiple entries for " + wordString + ".";
          throw new IllegalArgumentException(msg);
        }
      } catch (IOException ioe) {
        logger.error(ioe.getMessage());
      }
      return phoneme;
    }
  }

}
