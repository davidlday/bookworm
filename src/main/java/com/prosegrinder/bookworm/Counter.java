package com.prosegrinder.bookworm;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * A Utility class for counting things in prose text, specifically fiction.
 * All rules for parsing are derived from either industry practice or William Shunn's
 * "Proper Manuscript Formatting" site (//www.shunn.net/format/).
 */
public final class Counter {

  private static final int MIN_SYLLABLES_COMPLEX_WORD = 4;
  private static final int MIN_CHARS_LONG_WORD = 7;

  /** Prohibit instantiation. **/
  private Counter() {
    throw new AssertionError();
  }

  /** Estimate the number of syllables in a single word. **/
  public static final Integer countSyllables(final String word) {
    SyllableDictionary syllableDictionary = SyllableDictionary.getInstance();
    return syllableDictionary.getSyllableCount(word);
  }

  /** Estimate the number of syllables in a list of words. **/
  public static final Integer countSyllables(final List<String> words) {
    Integer syllables = 0;
    for (String word: words) {
      syllables += Counter.countSyllables(word);
    }
    return syllables;
  }

  /** Count the number of words in a String of text. **/
  public static final Integer countWords(final String text) {
    return Parser.parseWords(text).size();
  }

  /**
   * Count the number of complex words in a String of text.
   *
   * <p>TODO: This seems like it has problems.
   * A Complex Word has three or more syllables, not including
   * proper nouns, familiar jargon or compound words, or
   * common suffixes such as -es, -ed, or -ing as a syllable.
   *
   */
  public static final Integer countComplexWords(final String text) {
    /** Case is important to finding complex words. **/
    List<String> words = Parser.parseWords(text, false);
    List<String> sentences = Parser.parseSentences(text);
    int complexWordCount = 0;
    /** Probably a much more elegant way to do this. **/
    for (String word: words) {
      if (Counter.countSyllables(word) >= MIN_SYLLABLES_COMPLEX_WORD) {
        if (!Character.isUpperCase(word.charAt(0))) {
          complexWordCount++;
        } else {
          for (String sentence: sentences) {
            if (sentence.startsWith(word)) {
              complexWordCount++;
              break;
            }
          }
        }
      }
    }
    return complexWordCount;
  }

  /**
   * Count the number of long words in a List of words.
   *
   * <p>A "Long Word" is defined as having more than 6 characters
   * and is defined and used in both the LIX and RIX (Raygor)
   * Readability tests.
   *
   * @param words a list of words to analyze
   * @return  the number of "long words" found in the list
   *
   */
  public static final Integer countLongWords(final List<String> words) {
    int longWordCount = 0;
    for (String word: words) {
      if (word.length() >= MIN_CHARS_LONG_WORD) {
        longWordCount++;
      }
    }
    return longWordCount;
  }

  /** Returns the number of sentences in a String of text. **/
  public static final Integer countSentences(final String text) {
    return Parser.parseSentences(text).size();
  }

  /** Returns the number of paragraphs in a String of text. **/
  public static final Integer countParagraphs(final String text) {
    return Parser.parseParagraphs(text).size();
  }

  /** Returns the number of word characters in a String of text. **/
  public static final Integer countWordCharacters(final String text) {
    List<String> words = Parser.parseWords(text);
    int characterCount = 0;
    for (String word: words) {
      characterCount += word.length();
    }
    return characterCount;
  }

  /** Returns the number of POV Indicators found in a String of text. **/
  private static final Integer countPovIndicators(final Set<String> povIndicators,
      final List<String> words) {
    Integer povCount = 0;
    for (String indicator: povIndicators) {
      povCount += Collections.frequency(words, indicator);
    }
    return povCount;
  }

  /** Returns the number of First Person Indicators found in a List of words. **/
  public static final Integer countFirstPersonIndicators(final List<String> words) {
    return Counter.countPovIndicators(Parser.POV_FIRST, words);
  }

  /** Returns the number of First Person Indicators found in a String of text. **/
  public static final Integer countFirstPersonIndicators(final String text) {
    List<String> words = Parser.parseWords(text);
    return Counter.countFirstPersonIndicators(words);
  }

  /** Returns the number of Second Person Indicators found in a List of words. **/
  public static final Integer countSecondPersonIndicators(final List<String> words) {
    return Counter.countPovIndicators(Parser.POV_SECOND, words);
  }

  /** Returns the number of Second Person Indicators found in a String of text. **/
  public static final Integer countSecondPersonIndicators(final String text) {
    List<String> words = Parser.parseWords(text);
    return Counter.countFirstPersonIndicators(words);
  }

  /** Returns the number of Third Person Indicators found in a List of words. **/
  public static final Integer countThirdPersonIndicators(final List<String> words) {
    return Counter.countPovIndicators(Parser.POV_THIRD, words);
  }

  /** Returns the number of Third Person Indicators found in a String of text. **/
  public static final Integer countThirdPersonIndicators(final String text) {
    List<String> words = Parser.parseWords(text);
    return Counter.countFirstPersonIndicators(words);
  }

}
