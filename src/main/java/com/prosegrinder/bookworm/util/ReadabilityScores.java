package com.prosegrinder.bookworm.util;

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
  //   public ReadabilityScores(final String text) {
  //
  //     Integer characterCount = 0;
  //     Integer complexWordCount = 0;
  //     Integer longWordCount = 0;
  //     Integer sentenceCount = 0;
  //     Integer syllableCount = 0;
  //     Integer wordCount = 0;
  //
  //     List<String> sentences = Parser.parseSentences(text);
  //     for (String sentence: sentences) {
  //       sentenceCount++;
  //       // Complex word counting uses a sentence context.
  //       complexWordCount += Counter.countComplexWords(sentence);
  //       List<String> words = Parser.parseWords(sentence);
  //       longWordCount += Counter.countLongWords(words);
  //       for (String word: words) {
  //         wordCount++;
  //         syllableCount += Counter.countSyllables(word);
  //         characterCount += Counter.countWordCharacters(word);
  //       }
  //     }
  //     this.calculateScores(characterCount, complexWordCount, longWordCount,
  //         sentenceCount, syllableCount, wordCount);
  //   }

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
   * @param prose  prose to score.
   *
   */
  public ReadabilityScores(Prose prose) {
    this.calculateScores(prose.getWordCharacterCount(), prose.getComplexWordCount(), prose.getLongWordCount(),
        prose.getSentenceCount(), prose.getSyllableCount(), prose.getWordCount());
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

    this.automatedReadabilityIndex =
        this.automatedReadabilityIndex(characterCount, wordCount, sentenceCount);
    this.colemanLiauIndex =
        this.colemanLiauIndex(characterCount, wordCount, sentenceCount);
    this.fleschKincaidGradeLevel =
        this.fleschKincaidGradeLevel(sentenceCount, wordCount, syllableCount);
    this.fleschReadingEase =
        this.fleschReadingEase(sentenceCount, wordCount, syllableCount);
    this.gunningFogIndex =
        this.gunningFogIndex(sentenceCount, wordCount, complexWordCount);
    this.lix =
        this.lix(wordCount, longWordCount, sentenceCount);
    this.smog =
        this.smog(complexWordCount, sentenceCount);

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

  /**
   * Calculates the Automated Readability Index score for the analyzed text.
   *
   * @param characterCount  the number of word characters found in the source text
   * @param wordCount the number of words found in the source text
   * @param sentenceCount the number of sentences found in the source text
   * @return Automated Readability Index score
   *
   **/
  public static final Double automatedReadabilityIndex(final Integer characterCount,
                                                        final Integer wordCount,
                                                        final Integer sentenceCount) {
    Double score = 0.0;
    if (wordCount > 0) {
      Double averageCharactersPerWord = (double) characterCount / (double) wordCount;
      Double averageWordsPerSentence = (double) wordCount / (double) sentenceCount;
      score = (4.71 * averageCharactersPerWord)
              + (0.5 * averageWordsPerSentence) - 21.43;
    }
    return score;
  }

  /**
   * Calculates the Flesch Reading Ease score for the analyzed text.
   *
   * @param averageWordsPerSentence the average number of words per sentence in the sourc text
   * @param syllableCount the number of syllables found in the source text
   * @param wordCount the number of words found in the source text
   * @return Flesch Reading Ease score
   *
   **/
  public static final Double fleschReadingEase(final Integer sentenceCount,
                                                final Integer wordCount,
                                                final Integer syllableCount) {
    Double score = 0.0;
    if (wordCount > 0) {
      Double averageSentenceLength = (double) wordCount / (double) sentenceCount;
      Double averageSyllablesPerWord = (double) syllableCount / (double) wordCount;
      score = 206.835 - (1.015 * averageSentenceLength) - (84.6 * averageSyllablesPerWord);
    }
    return score;
  }

  /**
   * Calculates the Flesch Kincaid Grade Level score for the analyzed text.
   *
   * @param averageWordsPerSentence the average number of words per sentence in the sourc text
   * @param syllableCount the number of syllables found in the source text
   * @param wordCount the number of words found in the source text
   * @return Flesch Kincaid Grade Level score
   *
   **/
  public static final Double fleschKincaidGradeLevel(final Integer sentenceCount,
                                                final Integer wordCount,
                                                final Integer syllableCount) {
    Double score = 0.0;
    if (wordCount > 0) {
      Double averageSentenceLength = (double) wordCount / (double) sentenceCount;
      Double averageSyllablesPerWord = (double) syllableCount / (double) wordCount;
      score = (0.39 * averageSentenceLength) + (11.8 * averageSyllablesPerWord) - 15.59;
    }
    return score;
  }

  /**
   * Calculates the Gunning Fog Index score for the analyzed text.
   *
   * @param averageWordsPerSentence the average number of words per sentence in the sourc text
   * @param complexWordCount the number of complex words found in the source text
   * @param wordCount the number of words found in the source text
   * @return Gunning Fog Index score
   *
   **/
  public static final Double gunningFogIndex(final Integer sentenceCount,
                                              final Integer wordCount,
                                              final Integer complexWordCount) {
    Double score = 0.0;
    if (wordCount > 0) {
      Double averageSentenceLength = (double) wordCount / (double) sentenceCount;
      Double percentageHardWords = (double) complexWordCount / (double) wordCount * 100;
      score = 0.4 * (averageSentenceLength + percentageHardWords);
    }
    return score;
  }

  /**
   * Calculates the SMOG (Simple Measure Of Gobbledygook) score for the analyzed text.
   *
   * @param complexWordCount the number of complex words found in the source text
   * @param sentenceCount the number of sentences found in the source text
   * @return SMOG score
   *
   **/
  public static final Double smog(final Integer complexWordCount, final Integer sentenceCount) {
    Double score = 0.0;
    if (sentenceCount > 0) {
      score = (1.0430 * Math.sqrt((double) complexWordCount * (30 / (double) sentenceCount))) + 3.1291;
    }
    return score;
  }

  /**
   * Calculates the Coleman-Liau Index score for the analyzed text.
   *
   * @param characterCount  the number of word characters found in the source text
   * @param wordCount the number of words found in the source text
   * @param sentenceCount the number of sentences found in the source text
   * @return Coleman-Liau Index score
   *
   **/
  public static final Double colemanLiauIndex(final Integer characterCount,
                                              final Integer wordCount,
                                              final Integer sentenceCount) {
    Double score = 0.0;
    if (wordCount > 0) {
      Double L = (double) characterCount / (double) wordCount * 100;
      Double S = (double) sentenceCount / (double) wordCount * 100;
      score = (0.0588 * L) - (0.296 * S) - 15.8;
    }
    return score;
  }

  /**
   * Calculates the LIX score for the analyzed text.
   *
   * @param wordCount the number of words found in the source text
   * @param longWordCount  the number of long word found in the source text
   * @param sentenceCount the number of sentences found in the source text
   * @return LIX score
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
