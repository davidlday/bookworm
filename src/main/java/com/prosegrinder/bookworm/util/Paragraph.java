package com.prosegrinder.bookworm.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class Paragraph extends WordContainer {

  private final List<Sentence> sentences = new ArrayList<Sentence>();
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

  /**
   * Returns a new Paragraph from a string.
   *
   * <p>String is not currently validated since Paragraphs should
   * only be created by a Prose object using WordContainer.PARAGRAPH_PATTERN.
   */
  public Paragraph(final String text) {
    super(text);
    Matcher sentenceMatcher = Sentence.getPattern().matcher(text);
    while (sentenceMatcher.find()) {
      this.sentences.add(new Sentence(sentenceMatcher.group()));
    }
    this.wordCharacterCount = this.sentences.stream()
        .mapToInt( sentence -> sentence.getWordCharacterCount())
        .sum();
    this.syllableCount = this.sentences.stream()
        .mapToInt( sentence -> sentence.getSyllableCount())
        .sum();
    this.wordCount = this.sentences.stream()
        .mapToInt( sentence -> sentence.getWordCount())
        .sum();
    this.complexWordCount = this.sentences.stream()
        .mapToInt( sentence -> sentence.getComplexWordCount())
        .sum();
    this.longWordCount = this.sentences.stream()
        .mapToInt( sentence -> sentence.getLongWordCount())
        .sum();
    this.povWordCount = this.sentences.stream()
        .mapToInt( sentence -> sentence.getPovWordCount())
        .sum();
    this.firstPersonWordCount = this.sentences.stream()
        .mapToInt( sentence -> sentence.getFirstPersonWordCount())
        .sum();
    this.secondPersonWordCount = this.sentences.stream()
        .mapToInt( sentence -> sentence.getSecondPersonWordCount())
        .sum();
    this.thirdPersonWordCount = this.sentences.stream()
        .mapToInt( sentence -> sentence.getThirdPersonWordCount())
        .sum();
    this.sentenceCount = this.sentences.size();

    this.sentences.stream().forEach( fragment -> {
      Set<Word> uniqueWords = fragment.getUniqueWords();
      uniqueWords.stream().forEach( word -> {
        int count = (wordFrequency.containsKey(word))
            ? wordFrequency.get(word) : 0;
        count += fragment.getWordFrequency(word);
        wordFrequency.put(word, count);
      });
    });

  }

  public static final Pattern getPattern() {
    return WordContainer.getParagraphPattern();
  }

  public final List<Sentence> getSentences() {
    return this.sentences;
  }

  public final Integer getSentenceCount() {
    return this.sentenceCount;
  }

  @Override
  public final Map<Word, Integer> getWordFrequency() {
    return this.wordFrequency;
  }

 @Override
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
