package com.prosegrinder.bookworm;

import com.prosegrinder.bookworm.enums.PovType;
import com.prosegrinder.bookworm.util.SyllableDictionary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * A Utility class for calculating statics about prose text, specifically fiction.
 * All rules for parsing are derived from either industry practice or William Shunn's
 * "Proper Manuscript Formatting" site (//www.shunn.net/format/).
 *
 * @deprecated use native objects instead
 *
 */

@Deprecated
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


}
