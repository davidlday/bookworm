package com.prosegrinder.bookworm.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * A Singleton Class for looking up syllables from a dictionary.
 *
 * <p>Currently uses cmudict (https://github.com/cmusphinx/cmudict) for lookups
 * and implements heuristics based on the Lingua::EN::Syllable PERL module
 * by NEILB (https://github.com/neilb/Lingua-EN-Syllable). Original author
 * comments preserved and noted with 'nielb:'.
 *
 * <p>Singleton implementation based on:
 * http://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples
 */
public final class SyllableDictionary {

  private static volatile SyllableDictionary INSTANCE;
  private static volatile Map<String, Integer> syllableMap;

  /** Patterns that subtract from the syllable count. **/
  private static final Pattern[] subSyl = new Pattern[] {
    Pattern.compile("cial"),
    Pattern.compile("tia"),
    Pattern.compile("cius"),
    Pattern.compile("cious"),
    Pattern.compile("giu"),     // neilb: belgium!
    Pattern.compile("ion"),
    Pattern.compile("iou"),
    Pattern.compile("sia$"),
    Pattern.compile(".ely$"),   // neilb: absolutely! (but not ely!)
    Pattern.compile("[^td]ed$") // neilb: accused is 2, but executed is 4
  };
  /** Patterns that add to the syllable count. **/
  private static final Pattern[] addSyl = new Pattern[] {
    Pattern.compile("ia"),
    Pattern.compile("riet"),
    Pattern.compile("dien"),
    Pattern.compile("iu"),
    Pattern.compile("io"),
    Pattern.compile("ii"),
    Pattern.compile("microor"),
    Pattern.compile("[aeiouym]bl$"),    // neilb: -Vble, plus -mble
    Pattern.compile("[aeiou]{3}"),      // neilb: agreeable
    Pattern.compile("^mc"),
    Pattern.compile("ism$"),            // neilb: -ism
    Pattern.compile("isms$"),           // neilb: -isms
    Pattern.compile("([^aeiouy])\1l$"), // neilb: middle twiddle battle bottle, etc.
    Pattern.compile("[^l]lien"),        // neilb: alien, salient [1]
    Pattern.compile("^coa[dglx]."),     // neilb: [2]
    Pattern.compile("[^gq]ua[^auieo]"), // neilb: i think this fixes more than it breaks
    Pattern.compile("dnt$")             // neilb: couldn't
  };
  /** Patterns used to find stressed syllables in cmudict (symbols that end in a digit). **/
  private static final Pattern cmudictSyllablePattern = Pattern.compile("\\d$");

  /** Log4j Logger. **/
  private static final Logger logger = LogManager.getLogger(SyllableDictionary.class);

  /** Private constructor to enforce Singelton. **/
  private SyllableDictionary() {
    syllableMap = new HashMap<String, Integer>();
    /** TODO: Externalize in a properties file. **/
    String cmudict = "cmudict/cmudict.dict";
    try {
      InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(cmudict);
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      Stream<String> stream = reader.lines();
      stream.filter(line -> !line.startsWith(";;;"))
          .map(String::toLowerCase)
          .forEach(line -> {
            String[] parts = line.split("\\s+");
            String word = parts[0];
            if (!word.endsWith(")")) {
              Integer syllables = 0;
              for (int i = 1; i < parts.length; i++) {
                Matcher matcher = cmudictSyllablePattern.matcher(parts[i]);
                if (matcher.find()) {
                  syllables++;
                }
              }
              if (syllables > 0) {
                syllableMap.put(word, syllables);
              }
            }
          });
    } catch (Exception e) {
      logger.error("Exception during cmudict load: " + e);
      logger.error("Continuing using Heuristics, but performance and accuracy will be affected.");
    }
  }

  /** Returns the SyllableDictionary Singleton for use. **/
  public static synchronized SyllableDictionary getInstance() {
    if (INSTANCE == null) {
      synchronized (SyllableDictionary.class) {
        INSTANCE = new SyllableDictionary();
      }
    }
    return INSTANCE;
  }

  /**
   * Get the number of syllables by looking up the word in the underlying dictionary.
   *
   * @param word  a single word
   * @return  the number of syllables in the word
   * @throws NullPointerException throws if the word is not in the underlying dictionary
   *
   */
  public final Integer getByLookup(final String word) throws NullPointerException {
    if (!syllableMap.containsKey(word)) {
      String msg = "Dictionary does not contain an entry for " + word + ".";
      logger.error(msg);
      throw new NullPointerException(msg);
    } else {
      return syllableMap.get(word);
    }
  }

  /**
   * Estimates the number of syllables by heuristic.
   *
   * <p>Minimal testing shows this has about 80%+ match against cmudict.
   *
   * @param word  a single word
   * @return  the approximate number of syllables in the word
   *
   */
  public final Integer getByHeuristics(final String word) {
    // Lower case, fold contractions, and strip silent e off the end.
    String strippedWord = word.trim()
        .toLowerCase()
        .replaceAll("'", "")
        .replaceAll("e$", "");
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
       * <p>neilb: Syllables for all-digit words (eg, "1998";  some call them "numbers") are
       * often counted as the number of digits.  A cooler solution would be converting
       * "1998" to "nineteen eighty eight" (or "one thousand nine hundred eighty
       * eight", or...), but that is left as an exercise for the reader.
       *
       * <p>me: In fiction, people don't usually "read" numbers. It's okay
       * to simply count the number of digits and sign.
       */
      return strippedWord.replaceAll("[,.]", "").length();
    } else {
      String[] scrugg = strippedWord.split("[^aeiouy]+"); // neilb: perhaps - should be added?
      for (Pattern pattern: addSyl) {
        Matcher matcher = pattern.matcher(strippedWord);
        if (matcher.matches()) {
          syllableCount--;
        }
      }
      for (Pattern pattern: subSyl) {
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
   * Get number of syllables using all methods in order or precedence.
   *
   * <p>Currently, tries to find the number of syllables by lookup,
   * and falls back to estimation by heuristics if the word is not
   * in the underlying dictionary.
   *
   * @param word  a single word
   * @return  the approximate number of syllables in the word
   *
   */
  public final Integer getSyllableCount(final String word) {
    if (syllableMap.containsKey(word)) {
      return syllableMap.get(word);
    } else {
      return getByHeuristics(word);
    }
  }

  /** Returns the underlying Map of word:syllable pairs used for lookup. **/
  public final Map<String, Integer> getSyllableMap() {
    return Collections.unmodifiableMap(syllableMap);
  }

  /** Test if a String is a typical number. **/
  public static final boolean isNumeric(String word) {
    if (SyllableDictionary.inDictionary(word)) {
      return false;
    } else {
      return word.matches("^[+-]{0,1}\\d{1,3}(?:[,]\\d{3})*(?:[.]\\d*)*$");
    }
  }

  public static final Boolean inDictionary(String text) {
    return syllableMap.containsKey(text);
  }

}
