package com.prosegrinder.bookworm;

import java.util.List;


/**
 * Encapsulates common readability scores for a piece of text.
 *
 * <p>See: https://en.wikipedia.org/wiki/Readability_test
 */
public final class ReadabilityScores {

  private Integer characterCount;
  private Integer complexWordCount;
  private Integer longWordCount;
  private Integer sentenceCount;
  private Integer syllableCount;
  private Integer wordCount;

  private Double automatedReadabilityIndex;
  private Double colemanLiauIndex;
  private Double fleschKincaidGradeLevel;
  private Double fleschReadingEase;
  private Double gunningFogIndex;
  private Double lix;
  private Double smog;

  /**
   * Default constructor for ReadabilityScores.
   *
   * @param text  source text for score computation
   *
   */
  public ReadabilityScores(final String text) {

    Integer characterCount = 0;
    Integer complexWordCount = 0;
    Integer longWordCount = 0;
    Integer sentenceCount = 0;
    Integer syllableCount = 0;
    Integer wordCount = 0;

    List<String> sentences = Parser.parseSentences(text);
    for (String sentence: sentences) {
      sentenceCount++;
      // Complex word counting uses a sentence context.
      complexWordCount += Counter.countComplexWords(sentence);
      List<String> words = Parser.parseWords(sentence);
      longWordCount += Counter.countLongWords(words);
      for (String word: words) {
        wordCount++;
        syllableCount += Counter.countSyllables(word);
        characterCount += Counter.countWordCharacters(word);
      }
    }
    this.calculateScores(characterCount, complexWordCount, longWordCount,
        sentenceCount, syllableCount, wordCount);
  }

  /**
   * Efficient constructor for ReadabilityScores.
   *
   * <p>This constructor is used in larger processes where the basic
   * underlying inputs have already been calculated. All inputs must come from
   * the same source text or the resulting scores have no meaning.
   *
   * <p>All necessary inputs can be calculated by corresponding methods
   * found in the Calculator utility class.
   *
   * @param characterCount  count of word characters in the source text
   * @param complexWordCount  count of complex words in the source text
   * @param longWordCount count of long words in the source text
   * @param sentenceCount count of sentences in the source text
   * @param syllableCount count of syllables in the source text
   * @param wordCount count of words in the source text
   *
   */
  public ReadabilityScores(final Integer characterCount, final Integer complexWordCount,
      final Integer longWordCount, final Integer sentenceCount,
      final Integer syllableCount, final Integer wordCount) {
    this.calculateScores(characterCount, complexWordCount, longWordCount,
        sentenceCount, syllableCount, wordCount);
  }

  /** Calculates all scores for the source text. **/
  private void calculateScores(final Integer characterCount, final Integer complexWordCount,
      final Integer longWordCount, final Integer sentenceCount,
      final Integer syllableCount, final Integer wordCount) {

    this.characterCount = characterCount;
    this.complexWordCount = complexWordCount;
    this.longWordCount = longWordCount;
    this.sentenceCount = sentenceCount;
    this.syllableCount = syllableCount;
    this.wordCount = wordCount;

    Double averageWordsPerSentence = (double) wordCount / (double) sentenceCount;

    this.automatedReadabilityIndex =
        Calculator.automatedReadabilityIndex(characterCount, wordCount, sentenceCount);
    this.colemanLiauIndex =
        Calculator.colemanLiauIndex(characterCount, wordCount, sentenceCount);
    this.fleschKincaidGradeLevel =
        Calculator.fleschKincaidGradeLeve(averageWordsPerSentence, syllableCount, wordCount);
    this.fleschReadingEase =
        Calculator.fleschReadingEase(averageWordsPerSentence, syllableCount, wordCount);
    this.gunningFogIndex =
        Calculator.gunningFogIndex(averageWordsPerSentence, complexWordCount, wordCount);
    this.lix =
        Calculator.lix(wordCount, longWordCount, sentenceCount);
    this.smog =
        Calculator.smog(complexWordCount, sentenceCount);

  }

  /** Returns the Automated Readability Index score for the analyzed text. **/
  public final Double getAutomatedReadabilityIndex() {
    return this.automatedReadabilityIndex;
  }

  /** Returns the Coleman-Liau Index score for the analyzed text. **/
  public final Double getColemanLiauIndex() {
    return this.colemanLiauIndex;
  }

  /** Returns the Flesch-Kincaid Grade Level score for the analyzed text. **/
  public final Double getFleschKincaidGradeLevel() {
    return this.fleschKincaidGradeLevel;
  }

  /** Returns the Flesch Reading Ease score for the analyzed text. **/
  public final Double getFleschReadingEase() {
    return this.fleschReadingEase;
  }

  /** Returns the Gunning-Fog Index score for the analyzed text. **/
  public final Double getGunningFogIndex() {
    return this.gunningFogIndex;
  }

  /** Returns the LIX score for the analyzed text. **/
  public final Double getLix() {
    return this.lix;
  }

  /** Returns the SMOG (Simple Measure of Gobbledygood) score for the analyzed text. **/
  public final Double getSmog() {
    return this.smog;
  }

}
