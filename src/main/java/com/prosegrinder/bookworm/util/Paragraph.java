package com.prosegrinder.bookworm.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Paragraph extends ProseFragment {

  private final List<Sentence> sentences = new ArrayList<Sentence>();
  private final Set<Word> uniqueWords = new HashSet<Word>();
  private final Map<Word, Integer> wordFrequency = new HashMap<Word, Integer>();

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

  public Paragraph(final String text) {
    super(text);
    Matcher sentenceMatcher = Sentence.getPattern().matcher(text);
    while (sentenceMatcher.find()) {
      this.sentences.add(new Sentence(sentenceMatcher.group()));
    }
    this.wordCharacterCount = sentences.stream()
        .mapToInt( sentence -> sentence.getWordCharacterCount())
        .sum();
    this.syllableCount = sentences.stream()
        .mapToInt( sentence -> sentence.getSyllableCount())
        .sum();
    this.wordCount = sentences.stream()
        .mapToInt( sentence -> sentence.getWordCount())
        .sum();
    this.complexWordCount = sentences.stream()
        .mapToInt( sentence -> sentence.getComplexWordCount())
        .sum();
    this.longWordCount = sentences.stream()
        .mapToInt( sentence -> sentence.getLongWordCount())
        .sum();
    this.povWordCount = sentences.stream()
        .mapToInt( sentence -> sentence.getPovWordCount())
        .sum();
    this.firstPersonWordCount = sentences.stream()
        .mapToInt( sentence -> sentence.getFirstPersonWordCount())
        .sum();
    this.secondPersonWordCount = sentences.stream()
        .mapToInt( sentence -> sentence.getSecondPersonWordCount())
        .sum();
    this.thirdPersonWordCount = sentences.stream()
        .mapToInt( sentence -> sentence.getThirdPersonWordCount())
        .sum();
    this.sentenceCount = sentences.size();
  }

  public static final Pattern getPattern() {
    return ProseFragment.getParagraphPattern();
  }

  public final List<Sentence> getSentences() {
    return this.sentences;
  }

  public final Integer getSentenceCount() {
    return this.sentenceCount;
  }

  public final Set<Word> getUniqueWords() {
    Set<Word> uniqueWords = new HashSet<Word>();
    sentences.stream().forEach(sentence -> {
      uniqueWords.addAll(sentence.getUniqueWords());
    });
    return uniqueWords;
  }

  public final List<Word> getWords() {
    List<Word> words = new ArrayList<Word>();
    this.getSentences().stream().forEach( sentence -> {
      words.addAll(sentence.getWords());
    });
    return words;
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
