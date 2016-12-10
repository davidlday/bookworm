package com.prosegrinder.bookworm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Prose extends ProseFragment {

  private final List<Paragraph> paragraphs = new ArrayList<Paragraph>();

  private final Integer wordCharacterCount;
  private final Integer syllableCount;
  private final Integer wordCount;
  private final Integer complexWordCount;
  private final Integer longWordCount;
  private final Integer povWordCount;
  private final Integer firstPersonWordCount;
  private final Integer secondPersonWordCount;
  private final Integer thirdPersonWordCount;
  private final Integer sentenceCount;
  private final Integer paragraphCount;

  public Prose(final String text) {
    super(text);
    Matcher paragraphMatcher = this.getParagraphPattern().matcher(text);
    while (paragraphMatcher.find()) {
      Paragraph paragraph = new Paragraph(paragraphMatcher.group());
      this.paragraphs.add(paragraph);
    }
    this.wordCharacterCount = paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getWordCharacterCount())
        .sum();
    this.syllableCount = paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getSyllableCount())
        .sum();
    this.wordCount = paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getWordCount())
        .sum();
    this.complexWordCount = paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getComplexWordCount())
        .sum();
    this.longWordCount = paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getLongWordCount())
        .sum();
    this.povWordCount = paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getPovWordCount())
        .sum();
    this.firstPersonWordCount = paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getFirstPersonWordCount())
        .sum();
    this.secondPersonWordCount = paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getSecondPersonWordCount())
        .sum();
    this.thirdPersonWordCount = paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getThirdPersonWordCount())
        .sum();
    this.sentenceCount = paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getSentenceCount())
        .sum();
    this.paragraphCount = paragraphs.size();
  }

  public final List<Paragraph> getParagraphs() {
    return this.paragraphs;
  }

  public final Integer getParagraphCount() {
    return this.paragraphCount;
  }

  public final Integer getSentenceCount() {
    return this.sentenceCount;
  }

  public final Double getAverageSyllablesPerWord() {
    return (double) this.getSyllableCount() / (double) this.getWordCount();
  }

  public final Double getAverageSyllablesPerSentence() {
    return (double) this.getSyllableCount() / (double) this.getSentenceCount();
  }

  public final Double getAverageSyllablesPerParagraph() {
    return (double) this.getSyllableCount() / (double) this.getParagraphCount();
  }

  public final Double getAverageWordsPerSentence() {
    return (double) this.getWordCount() / (double) this.getSentenceCount();
  }

  public final Double getAverageWordsPerParagraph() {
    return (double) this.getWordCount() / (double) this.getParagraphCount();
  }

  public final Double getAverageSentencesPerParagraph() {
    return (double) this.getSentenceCount() / (double) this.getParagraphCount();
  }

  @Override
  public final Integer getWordCharacterCount() {
    return this.wordCharacterCount;
  }

  @Override
  public final Integer getSyllableCount() {
    return this.syllableCount;
  }

  @Override
  public final Integer getWordCount() {
    return this.wordCount;
  }

  @Override
  public final Integer getComplexWordCount() {
    return this.complexWordCount;
  }

  @Override
  public final Integer getLongWordCount() {
    return this.longWordCount;
  }

  @Override
  public final Integer getFirstPersonWordCount() {
    return this.firstPersonWordCount;
  }

  @Override
  public final Integer getSecondPersonWordCount() {
    return this.secondPersonWordCount;
  }

  @Override
  public final Integer getThirdPersonWordCount() {
    return this.thirdPersonWordCount;
  }

  @Override
  public final Integer getPovWordCount() {
    return this.povWordCount;
  }

}