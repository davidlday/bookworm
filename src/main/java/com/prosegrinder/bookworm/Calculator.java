package com.prosegrinder.bookworm;

import com.prosegrinder.bookworm.enums.PovType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Calculator {
  // A Utility class for calculating things in prose text, specifically fiction.
  // All rules for parsing are derived from either industry practice or William Shunn's
  // "Proper Manuscript Formatting" site (//www.shunn.net/format/).

  // Prohibit instantiation.
  private Calculator() {
    throw new AssertionError();
  }

  // Average syllables per word.
  public static final Double averageSyllablesPerWord(final List<String> words) {
    int syllables = Counter.countSyllables(words);
    return (double) syllables / (double) words.size();
  }

  // Average syllables per word.
  public static final Double averageSyllablesPerWord(final String text) {
    List<String> words = Parser.parseWords(text);
    return Calculator.averageSyllablesPerWord(words);
  }

  // Average number of syllables per sentence.
  public static final Double averageSyllablesPerSentence(final List<String> sentences) {
    List<String> words = Parser.parseWords(sentences);
    int syllables = Counter.countSyllables(words);
    return (double) syllables / (double) sentences.size();
  }

  // Average number of syllables per sentence.
  public static final Double averageSyllablesPerSentence(final String text) {
    List<String> sentences = Parser.parseSentences(text);
    return Calculator.averageSyllablesPerSentence(sentences);
  }

  // Average number of words per sentence.
  public static final Double averageWordsPerSentence(final List<String> sentences) {
    List<String> words = Parser.parseWords(sentences);
    return (double) words.size() / (double) sentences.size();
  }

  // Average number of syllables per paragraph.
  public static final Double averageSyllablesPerParagraph(final List<String> paragraphs) {
    List<String> words = Parser.parseWords(paragraphs);
    int syllables = Counter.countSyllables(words);
    return (double) syllables / (double) paragraphs.size();
  }

  // Average number of syllables per paragraph.
  public static final Double averageSyllablesPerParagraph(final String text) {
    List<String> paragraphs = Parser.parseParagraphs(text);
    return Calculator.averageSyllablesPerParagraph(paragraphs);
  }

  // Average number of words per paragraph.
  public static final Double averageWordsPerParagraph(final List<String> paragraphs) {
    List<String> words = Parser.parseWords(paragraphs);
    return (double) words.size() / (double) paragraphs.size();
  }

  // Average number of words per paragraph.
  public static final Double averageWordsPerParagraph(final String text) {
    List<String> paragraphs = Parser.parseParagraphs(text);
    return Calculator.averageWordsPerParagraph(paragraphs);
  }

  // Average number of sentences per paragraph.
  public static final Double averageSentencesPerParagraph(final List<String> paragraphs) {
    List<String> sentences = Parser.parseSentences(paragraphs);
    return (double) sentences.size() / (double) paragraphs.size();
  }

  // Average number of sentences per paragraph.
  public static final Double averageSentencesPerParagraph(final String text) {
    List<String> paragraphs = Parser.parseParagraphs(text);
    return Calculator.averageSentencesPerParagraph(paragraphs);
  }

  // POV isn't a matter of most indicators wins, but a
  // matter of precedence. First Person can and usually will contain second and
  // third person indicators. Second Person can and usually will contain third
  // person indicators.
  // Estimate POV for a list of indicators.
  public static final Enum pointOfView(final List<String> povIndicators) {
    if (Counter.countFirstPersonIndicators(povIndicators) > 0) {
      return PovType.FIRST;
    } else if (Counter.countSecondPersonIndicators(povIndicators) > 0) {
      return PovType.SECOND;
    } else if (Counter.countThirdPersonIndicators(povIndicators) > 0) {
      return PovType.THIRD;
    } else {
      return PovType.UNKNOWN;
    }
  }

  // Estimate POV for a string of text.
  public static final Enum pointOfView(final String text) {
    // Get a list of all POV Indicators found in the text.
    List<String> prosePovIndicators = Parser.parsePovIndicators(text);
    // Strip out all POV Indicators found in dialogue.
    List<String> dialoguePovIndicators = Parser.parsePovIndicators(Parser.parseDialogue(text));
    for (String indicator: dialoguePovIndicators) {
      prosePovIndicators.remove(indicator);
    }
    return pointOfView(prosePovIndicators);
  }

  /**
   *  Readability Calculations
   */

  // Automated Readability Index (ARI)
  // See: https://en.wikipedia.org/wiki/Automated_readability_index
  public static final Double automatedReadabilityIndex(final Integer characterCount,
                                                        final Integer wordCount,
                                                        final Integer sentenceCount) {
    Double score = 0.0;
    if (wordCount > 0) {
      score = 4.71 * (characterCount / wordCount) + 0.5
              * (wordCount / sentenceCount) - 21.43;
    }
    return score;
  }

  // Flesch Reading Ease
  // See: https://en.wikipedia.org/wiki/Flesch%E2%80%93Kincaid_readability_tests
  public static final Double fleschReadingEase(final Double averageWordsPerSentence,
                                                final Integer syllableCount,
                                                final Integer wordCount) {
    Double score = 0.0;
    if (wordCount > 0) {
      score = 206.835 - (1.015 * (averageWordsPerSentence))
              - (84.6 * (syllableCount / wordCount));
    }
    return score;
  }

  // Flesch Kincaid Grade Level
  // See: https://en.wikipedia.org/wiki/Flesch%E2%80%93Kincaid_readability_tests
  public static final Double fleschKincaidGradeLeve(final Double averageWordsPerSentence,
                                                    final Integer syllableCount,
                                                    final Integer wordCount) {
    Double score = 0.0;
    if (wordCount > 0) {
      score = 0.39 * (averageWordsPerSentence) + 11.8
              * (syllableCount / wordCount) - 15.59;
    }
    return score;
  }

  // Gunning Fog Index
  // See: https://en.wikipedia.org/wiki/Gunning_fog_index
  public static final Double gunningFogIndex(final Double averageWordsPerSentence,
                                              final Integer complexWordCount,
                                              final Integer wordCount) {
    Double score = 0.0;
    if (wordCount > 0) {
      score = 0.4 * ((averageWordsPerSentence) + (100 * (complexWordCount / wordCount)));
    }
    return score;
  }

  // SMOG (Simple Measure Of Gobbledygook)
  // See: https://en.wikipedia.org/wiki/SMOG
  public static final Double smog(final Integer complexWordCount, final Integer sentenceCount) {
    Double score = 0.0;
    if (sentenceCount > 0) {
      score = (Math.sqrt((double) complexWordCount * (30 / (double) sentenceCount)) + 3);
    }
    return score;
  }

  // Coleman-Liau Index
  // See: https://en.wikipedia.org/wiki/Coleman%E2%80%93Liau_index
  public static final Double colmaneLiauIndex(final Integer characterCount,
      final Integer wordCount, final Integer sentenceCount) {
    Double score = 0.0;
    if (wordCount > 0) {
      score = (5.89 * ((double) characterCount / (double) wordCount))
          - (30 * ((double) sentenceCount / (double) wordCount)) - 15.8;
    }
    return score;
  }

  // RIX
  // See: https://en.wikipedia.org/wiki/Raygor_readability_estimate
  public static final Double rix(final Integer longWordCount, final Integer sentenceCount) {
    Double score = 0.0;
    if (sentenceCount > 0) {
      score = (double) longWordCount / (double) sentenceCount;
    }
    return score;
  }

  // LIX
  // See: https://en.wikipedia.org/wiki/LIX
  public static final Double lix(final Integer wordCount,
      final Integer longWordCount, final Integer sentenceCount) {
    Double score = 0.0;
    if (wordCount > 0) {
      score = (double) wordCount / (double) sentenceCount
          + (100 * (double) longWordCount) / (double) wordCount;
    }
    return score;
  }

}
