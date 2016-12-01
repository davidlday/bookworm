package com.prosegrinder.bookworm;

import java.util.List;

public final class ReadabilityScores {
  // Calculates all Readability Scores
  // See: https://en.wikipedia.org/wiki/Readability_test

  private Integer characterCount;
  private Integer complexWordCount;
  private Integer longWordCount;
  private Integer sentenceCount;
  private Integer syllableCount;
  private Integer wordCount;

  private Double automatedReadabilityIndex;
  private Double colmaneLiauIndex;
  private Double fleschKincaidGradeLevel;
  private Double fleschReadingEase;
  private Double gunningFogIndex;
  private Double lix;
  private Double rix;
  private Double smog;

  /**
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
   *
   */
  public ReadabilityScores(final Integer characterCount, final Integer complexWordCount,
      final Integer longWordCount, final Integer sentenceCount,
      final Integer syllableCount, final Integer wordCount) {
    this.calculateScores(characterCount, complexWordCount, longWordCount,
        sentenceCount, syllableCount, wordCount);
  }

  /**
   *
   */
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
    this.colmaneLiauIndex =
        Calculator.colmaneLiauIndex(characterCount, wordCount, sentenceCount);
    this.fleschKincaidGradeLevel =
        Calculator.fleschKincaidGradeLeve(averageWordsPerSentence, syllableCount, wordCount);
    this.fleschReadingEase =
        Calculator.fleschReadingEase(averageWordsPerSentence, syllableCount, wordCount);
    this.gunningFogIndex =
        Calculator.gunningFogIndex(averageWordsPerSentence, complexWordCount, wordCount);
    this.lix =
        Calculator.lix(wordCount, longWordCount, sentenceCount);
    this.rix =
        Calculator.rix(longWordCount, sentenceCount);
    this.smog =
        Calculator.smog(complexWordCount, sentenceCount);

  }


}
