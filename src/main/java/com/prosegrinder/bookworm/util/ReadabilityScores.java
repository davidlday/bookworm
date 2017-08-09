package com.prosegrinder.bookworm.util;

/**
 * Encapsulates common readability scores for a piece of text.
 *
 * <p>Note that for now, the scores that use Complex Word Count
 * are considered experimental. The definition for Complex Word is
 * not entirely clear and difficult to implement.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Readability_test">Readability Test</a>
 * @see Word#isComplexWord()
 * @see WordContainer#getComplexWordCount()
 *
 */
public final class ReadabilityScores {

  private final Integer characterCount;
  private final Integer complexWordCount;
  private final Integer longWordCount;
  private final Integer sentenceCount;
  private final Integer syllableCount;
  private final Integer wordCount;

  private final Double automatedReadabilityIndex;
  private final Double colemanLiauIndex;
  private final Double fleschKincaidGradeLevel;
  private final Double fleschReadingEase;
  private final Double gunningFogIndex;
  private final Double lix;
  private final Double rix;
  private final Double smog;

  /**
   * Granular constructor for ReadabilityScores.
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

    
    this.characterCount = characterCount;
    this.complexWordCount = complexWordCount;
    this.longWordCount = longWordCount;
    this.sentenceCount = sentenceCount;
    this.syllableCount = syllableCount;
    this.wordCount = wordCount;

    this.automatedReadabilityIndex = ReadabilityScores.automatedReadabilityIndex(
        this.getCharacterCount(), this.getWordCount(), this.getSentenceCount());
    this.colemanLiauIndex = ReadabilityScores.colemanLiauIndex(this.getCharacterCount(),
        this.getWordCount(), this.getSentenceCount());
    this.fleschKincaidGradeLevel = ReadabilityScores.fleschKincaidGradeLevel(
        this.getSentenceCount(), this.getWordCount(), this.getSyllableCount());
    this.fleschReadingEase = ReadabilityScores.fleschReadingEase(this.getSentenceCount(),
        this.getWordCount(), this.getSyllableCount());
    this.gunningFogIndex = ReadabilityScores.gunningFogIndex(this.getSentenceCount(),
        this.getWordCount(), this.getComplexWordCount());
    this.lix = ReadabilityScores.lix(this.getWordCount(), this.getLongWordCount(),
        this.getSentenceCount());
    this.rix = ReadabilityScores.rix(this.getLongWordCount(), this.getSentenceCount());
    this.smog = ReadabilityScores.smog(this.getComplexWordCount(), this.getSentenceCount());
  }

  /**
   * Convenience constructor for ReadabilityScores.
   *
   * <p>This constructor is used when only interested in readability scores
   * and not any of the other prose metrics.
   *
   * @param prose  prose to score
   *
   */
  public ReadabilityScores(Prose prose) {
    this(prose.getWordCharacterCount(), prose.getComplexWordCount(),
        prose.getLongWordCount(), prose.getSentenceCount(),
        prose.getSyllableCount(), prose.getWordCount());
  }

  public final Integer getCharacterCount() {
    return characterCount;
  }

  public final Integer getComplexWordCount() {
    return complexWordCount;
  }

  public final Integer getLongWordCount() {
    return longWordCount;
  }

  public final Integer getSentenceCount() {
    return sentenceCount;
  }

  public final Integer getSyllableCount() {
    return syllableCount;
  }

  public final Integer getWordCount() {
    return wordCount;
  }

  public final Double getAutomatedReadabilityIndex() {
    return this.automatedReadabilityIndex;
  }

  public final Double getColemanLiauIndex() {
    return this.colemanLiauIndex;
  }

  public final Double getFleschKincaidGradeLevel() {
    return this.fleschKincaidGradeLevel;
  }

  public final Double getFleschReadingEase() {
    return this.fleschReadingEase;
  }

  public final Double getGunningFogIndex() {
    return this.gunningFogIndex;
  }

  public final Double getLix() {
    return this.lix;
  }

  public final Double getRix() {
    return this.rix;
  }

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
   * @param sentenceCount the number of sentences found in the source text
   * @param wordCount the number of words found in the source text
   * @param syllableCount the number of syllables found in the source text
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
   * @param sentenceCount the number of sentences found in the source text
   * @param wordCount the number of words found in the source text
   * @param syllableCount the number of syllables found in the source text
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
   * @param sentenceCount the number of sentences found in the source text
   * @param wordCount the number of words found in the source text
   * @param complexWordCount the number of complex words found in the source text
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
      score = (1.0430 * Math.sqrt((double) complexWordCount
          * (30 / (double) sentenceCount))) + 3.1291;
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
      Double lettersPerWord = (double) characterCount / (double) wordCount * 100;
      Double sentencesPerWord = (double) sentenceCount / (double) wordCount * 100;
      score = (0.0588 * lettersPerWord) - (0.296 * sentencesPerWord) - 15.8;
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

  /**
   * Calculates the RIX score for the analyzed text.
   *
   * <p>This was implemented in the original bookworm experiment due to copying
   * a formula found in another readability library. So far, I've been unable
   * to find a clear definition of this readability score, so I'm marking it
   * as deprecated and will remove it in a future release.
   *
   * @param longWordCount  the number of long word found in the source text
   * @param sentenceCount the number of sentences found in the source text
   * @return RIX score
   *
   **/
  @Deprecated
  public static final Double rix(final Integer longWordCount,
      final Integer sentenceCount) {
    Double score = 0.0;
    if (sentenceCount > 0) {
      score = (double) longWordCount / (double) sentenceCount;
    }
    return score;
  }
}
