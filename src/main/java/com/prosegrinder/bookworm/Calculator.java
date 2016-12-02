package com.prosegrinder.bookworm;

import com.prosegrinder.bookworm.enums.PovType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * A Utility class for calculating statics about prose text, specifically fiction.
 * All rules for parsing are derived from either industry practice or William Shunn's
 * "Proper Manuscript Formatting" site (//www.shunn.net/format/).
 */

public final class Calculator {

  /** Prohibit instantiation. **/
  private Calculator() {
    throw new AssertionError();
  }

  /** Calculates the average number of syllables per word in a List of words. **/
  public static final Double averageSyllablesPerWord(final List<String> words) {
    int syllables = Counter.countSyllables(words);
    return (double) syllables / (double) words.size();
  }

  /** Calculates the average number of syllables per word in a String of text. **/
  public static final Double averageSyllablesPerWord(final String text) {
    List<String> words = Parser.parseWords(text);
    return Calculator.averageSyllablesPerWord(words);
  }

  /** Calculates the average number of syllables per sentence in a List of sentences. **/
  public static final Double averageSyllablesPerSentence(final List<String> sentences) {
    List<String> words = Parser.parseWords(sentences);
    int syllables = Counter.countSyllables(words);
    return (double) syllables / (double) sentences.size();
  }

  /** Calculates the average number of syllables per sentence in a String of text. **/
  public static final Double averageSyllablesPerSentence(final String text) {
    List<String> sentences = Parser.parseSentences(text);
    return Calculator.averageSyllablesPerSentence(sentences);
  }

  /** Calculates the average number of words per sentence in a List of sentences. **/
  public static final Double averageWordsPerSentence(final List<String> sentences) {
    List<String> words = Parser.parseWords(sentences);
    return (double) words.size() / (double) sentences.size();
  }

  /** Calculates the average number of syllables per paragraph in a List of paragraphs. **/
  public static final Double averageSyllablesPerParagraph(final List<String> paragraphs) {
    List<String> words = Parser.parseWords(paragraphs);
    int syllables = Counter.countSyllables(words);
    return (double) syllables / (double) paragraphs.size();
  }

  /** Calculates the average number of syllables per paragraph in a String of text. **/
  public static final Double averageSyllablesPerParagraph(final String text) {
    List<String> paragraphs = Parser.parseParagraphs(text);
    return Calculator.averageSyllablesPerParagraph(paragraphs);
  }

  /** Calculates the average number of words per paragraph in a List of paragraphs. **/
  public static final Double averageWordsPerParagraph(final List<String> paragraphs) {
    List<String> words = Parser.parseWords(paragraphs);
    return (double) words.size() / (double) paragraphs.size();
  }

  /** Calculates the average number of words per paragraph in a String of text. **/
  public static final Double averageWordsPerParagraph(final String text) {
    List<String> paragraphs = Parser.parseParagraphs(text);
    return Calculator.averageWordsPerParagraph(paragraphs);
  }

  /** Calculates the average number of sentences per paragraph in a List of paragraphs. **/
  public static final Double averageSentencesPerParagraph(final List<String> paragraphs) {
    List<String> sentences = Parser.parseSentences(paragraphs);
    return (double) sentences.size() / (double) paragraphs.size();
  }

  /** Calculates the average number of sentences per paragraph in a String of text. **/
  public static final Double averageSentencesPerParagraph(final String text) {
    List<String> paragraphs = Parser.parseParagraphs(text);
    return Calculator.averageSentencesPerParagraph(paragraphs);
  }

  /**
   * Estimates the most likely Point of View (first, second, or third).
   *
   * <p>Point of View (POV) is based on an order of precedence, not a quantity
   * of indicators. First person POV can and usually will contain second and
   * third person indicators. Second Person can and usually will contain third
   * person indicators.
   *
   * @params povIndicators  a list of povIndicators parsed from a source text
   * @returns one of the PovTypes indicating most likely POV
   *
   * @see com.prosegrinder.bookworm.enums.PovType
   *
   */
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

  /**
   * Returns the most likely Point of View (first, second, or third).
   *
   * <p>Point of View (POV) is based on an order of precedence, not a quantity
   * of indicators. First person POV can and usually will contain second and
   * third person indicators. Second Person can and usually will contain third
   * person indicators.
   *
   * <p>Dialogue is stripped out because it is always from the point of view
   * of the person speaking and doesn't reflect the point of view of the
   * narrative.
   *
   * @params text  source text to analyze
   * @returns one of the PovTypes indicating most likely POV
   *
   * @see com.prosegrinder.bookworm.enums.PovType
   *
   */
  public static final Enum pointOfView(final String text) {
    /** Get a list of all POV Indicators found in the text. **/
    List<String> prosePovIndicators = Parser.parsePovIndicators(text);
    /** Strip out all POV Indicators found in dialogue. **/
    List<String> dialoguePovIndicators = Parser.parsePovIndicators(Parser.parseDialogue(text));
    for (String indicator: dialoguePovIndicators) {
      prosePovIndicators.remove(indicator);
    }
    return pointOfView(prosePovIndicators);
  }

  /**
   * Calculates the Automated Readability Index score for the analyzed text.
   *
   * @param characterCount  the number of word characters found in the source text
   * @param wordCount the number of words found in the source text
   * @param sentenceCount the number of sentences found in the source text
   * @returns Automated Readability Index score
   *
   **/
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

  /**
   * Calculates the Flesch Reading Ease score for the analyzed text.
   *
   * @param averageWordsPerSentence the average number of words per sentence in the sourc text
   * @param syllableCount the number of syllables found in the source text
   * @param wordCount the number of words found in the source text
   * @returns Flesch Reading Ease score
   *
   **/
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

  /**
   * Calculates the Flesch Kincaid Grade Level score for the analyzed text.
   *
   * @param averageWordsPerSentence the average number of words per sentence in the sourc text
   * @param syllableCount the number of syllables found in the source text
   * @param wordCount the number of words found in the source text
   * @returns Flesch Kincaid Grade Level score
   *
   **/
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

  /**
   * Calculates the Gunning Fog Index score for the analyzed text.
   *
   * @param averageWordsPerSentence the average number of words per sentence in the sourc text
   * @param complexWordCount the number of complex words found in the source text
   * @param wordCount the number of words found in the source text
   * @returns Gunning Fog Index score
   *
   **/
  public static final Double gunningFogIndex(final Double averageWordsPerSentence,
                                              final Integer complexWordCount,
                                              final Integer wordCount) {
    Double score = 0.0;
    if (wordCount > 0) {
      score = 0.4 * ((averageWordsPerSentence) + (100 * (complexWordCount / wordCount)));
    }
    return score;
  }

  /**
   * Calculates the SMOG (Simple Measure Of Gobbledygook) score for the analyzed text.
   *
   * @param complexWordCount the number of complex words found in the source text
   * @param sentenceCount the number of sentences found in the source text
   * @returns SMOG score
   *
   **/
  public static final Double smog(final Integer complexWordCount, final Integer sentenceCount) {
    Double score = 0.0;
    if (sentenceCount > 0) {
      score = (Math.sqrt((double) complexWordCount * (30 / (double) sentenceCount)) + 3);
    }
    return score;
  }

  /**
   * Calculates the Coleman-Liau Index score for the analyzed text.
   *
   * @param characterCount  the number of word characters found in the source text
   * @param wordCount the number of words found in the source text
   * @param sentenceCount the number of sentences found in the source text
   * @returns Coleman-Liau Index score
   *
   **/
  public static final Double colemanLiauIndex(final Integer characterCount,
      final Integer wordCount, final Integer sentenceCount) {
    Double score = 0.0;
    if (wordCount > 0) {
      score = (5.89 * ((double) characterCount / (double) wordCount))
          - (30 * ((double) sentenceCount / (double) wordCount)) - 15.8;
    }
    return score;
  }

  /**
   * Calculates the LIX score for the analyzed text.
   *
   * @param wordCount the number of words found in the source text
   * @param longWordCount  the number of long word found in the source text
   * @param sentenceCount the number of sentences found in the source text
   * @returns LIX score
   *
   **/
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
