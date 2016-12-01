package com.prosegrinder.bookworm;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public final class Counter {
  // A Utility class for counting things in prose text, specifically fiction.
  // All rules for parsing are derived from either industry practice or William Shunn's
  // "Proper Manuscript Formatting" site (//www.shunn.net/format/).

  private static final int MIN_SYLLABLES_COMPLEX_WORD = 4;
  private static final int MIN_CHARS_LONG_WORD = 7;

  // Prohibit instantiation.
  private Counter() {
    throw new AssertionError();
  }

  // Estimate the number of syllables in a single word.
  public static final Integer countSyllables(final String word) {
    SyllableDictionary syllableDictionary = SyllableDictionary.getInstance();
    return syllableDictionary.getSyllableCount(word);
  }

  // Estimate the number of syllables in a list of words.
  public static final Integer countSyllables(final List<String> words) {
    Integer syllables = 0;
    for (String word: words) {
      syllables += Counter.countSyllables(word);
    }
    return syllables;
  }

  // Count the number of words in a String
  public static final Integer countWords(final String text) {
    return Parser.parseWords(text).size();
  }

  // Count the number of complex words in a String.
  // TODO: This seems like it has problems.
  // A Complex Word has three or more syllables, not including
  // proper nouns, familiar jargon or compound words, or
  // common suffixes such as -es, -ed, or -ing as a syllable.
  public static final Integer countComplexWords(final String text) {
    // Case is important to finding complex words.
    List<String> words = Parser.parseWords(text, false);
    List<String> sentences = Parser.parseSentences(text);
    int complexWordCount = 0;
    // Probably a much more elegant way to do this.
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

  // Count the number of long words in a List of words.
  // Used in LIX and RIX readability scores.
  // See:
  // * https://en.wikipedia.org/wiki/LIX
  // * https://en.wikipedia.org/wiki/Raygor_readability_estimate
  public static final Integer countLongWords(final List<String> words) {
    int longWordCount = 0;
    for (String word: words) {
      if (word.length() >= MIN_CHARS_LONG_WORD) {
        longWordCount++;
      }
    }
    return longWordCount;
  }

  // Number of sentences.
  public static final Integer countSentences(final String text) {
    return Parser.parseSentences(text).size();
  }

  // Number of paragraphs.
  public static final Integer countParagraphs(final String text) {
    return Parser.parseParagraphs(text).size();
  }

  // Number of word characters in a String.
  public static final Integer countWordCharacters(final String text) {
    List<String> words = Parser.parseWords(text);
    int characterCount = 0;
    for (String word: words) {
      characterCount += word.length();
    }
    return characterCount;
  }

  // Counting Point of View Indicators.
  private static final Integer countPovIndicators(final Set<String> povIndicators,
      final List<String> words) {
    Integer povCount = 0;
    for (String indicator: povIndicators) {
      povCount += Collections.frequency(words, indicator);
    }
    return povCount;
  }

  public static final Integer countFirstPersonIndicators(final List<String> words) {
    return Counter.countPovIndicators(Parser.POV_FIRST, words);
  }

  public static final Integer countFirstPersonIndicators(final String text) {
    List<String> words = Parser.parseWords(text);
    return Counter.countFirstPersonIndicators(words);
  }

  public static final Integer countSecondPersonIndicators(final List<String> words) {
    return Counter.countPovIndicators(Parser.POV_SECOND, words);
  }

  public static final Integer countSecondPersonIndicators(final String text) {
    List<String> words = Parser.parseWords(text);
    return Counter.countFirstPersonIndicators(words);
  }

  public static final Integer countThirdPersonIndicators(final List<String> words) {
    return Counter.countPovIndicators(Parser.POV_THIRD, words);
  }

  public static final Integer countThirdPersonIndicators(final String text) {
    List<String> words = Parser.parseWords(text);
    return Counter.countFirstPersonIndicators(words);
  }

}
