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

public final class Sentence extends ProseFragment {

  private final List<Word> words = new ArrayList<Word>();
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

  public Sentence(final String text) {
    super(text);
    Matcher wordMatcher = Word.getWordPattern().matcher(text);
    while (wordMatcher.find()) {
      this.words.add(new Word(wordMatcher.group()));
    }
    this.wordCharacterCount = words.stream()
        .mapToInt( word -> word.getWordCharacterCount())
        .sum();
    this.syllableCount = words.stream()
        .mapToInt( word -> word.getSyllableCount())
        .sum();
    this.wordCount = words.size();
    this.complexWordCount = words.stream()
        .mapToInt( word -> word.getComplexWordCount())
        .sum();
    this.longWordCount = words.stream()
        .mapToInt( word -> word.getLongWordCount())
        .sum();
    this.povWordCount = words.stream()
        .mapToInt( word -> word.getPovWordCount())
        .sum();
    this.firstPersonWordCount = words.stream()
        .mapToInt( word -> word.getFirstPersonWordCount())
        .sum();
    this.secondPersonWordCount = words.stream()
        .mapToInt( word -> word.getSecondPersonWordCount())
        .sum();
    this.thirdPersonWordCount = words.stream()
        .mapToInt( word -> word.getThirdPersonWordCount())
        .sum();
    this.uniqueWords.addAll(this.words);
    uniqueWords.stream().forEach(word -> {
        wordFrequency.put(word, Collections.frequency(this.words, word));
    });
  }

  public static final Pattern getPattern() {
    return ProseFragment.getSentencePattern();
  }

  public final List<Word> getWords() {
    return this.words;
  }

  public final Set<Word> getUniqueWords() {
    return this.uniqueWords;
  }

  public final Integer getUniqueWordCount() {
    return this.uniqueWords.size();
  }

  public final Map<Word, Integer> getWordFrequency() {
    return this.wordFrequency;
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
