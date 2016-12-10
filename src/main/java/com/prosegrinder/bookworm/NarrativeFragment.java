package com.prosegrinder.bookworm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NarrativeFragment extends StoryFragment {

  private final List<Word> words = new ArrayList<Word>();
  private final Integer wordCharacterCount;
  private final Integer syllableCount;
  private final Integer wordCount;
  private final Integer complexWordCount;
  private final Integer longWordCount;
  private final Integer povWordCount;
  private final Integer firstPersonWordCount;
  private final Integer secondPersonWordCount;
  private final Integer thirdPersonWordCount;

  public NarrativeFragment(String text) {
    super(text);
    Matcher wordMatcher = this.getWordPattern().matcher(text);
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
  }

  public final List<Word> getWords() {
    return this.words;
  }

  public final Integer getWordCharacterCount() {
    return this.wordCharacterCount;
  }

  public final Integer getSyllableCount() {
    return this.syllableCount;
  }

  public final Integer getWordCount() {
    return this.wordCount;
  }

  public final Integer getComplexWordCount() {
    return this.complexWordCount;
  }

  public final Integer getLongWordCount() {
    return this.longWordCount;
  }

  public final Integer getFirstPersonWordCount() {
    return this.firstPersonWordCount;
  }

  public final Integer getSecondPersonWordCount() {
    return this.secondPersonWordCount;
  }

  public final Integer getThirdPersonWordCount() {
    return this.thirdPersonWordCount;
  }

  public final Integer getPovWordCount() {
    return this.povWordCount;
  }

}