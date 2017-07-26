package com.prosegrinder.bookworm.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Sentence.
 */
public final class Sentence extends WordContainer {

  private final List<Word> words = new ArrayList<Word>();
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

  /**
   * Returns a new Sentence from a string.
   *
   * <p>String is not currently validates since Sentences should
   * only be created by a Paragraph using WordContainer.SENTENCE_PATTERN.
   * 
   * @param text    a string of text representing a complete sentence
   */
  @Deprecated
  public Sentence(final String text) {
    super(text);
    Dictionary dictionary = Dictionary.getInstance();
    Matcher wordMatcher = Sentence.getWordPattern().matcher(this.getNormalizedText());
    while (wordMatcher.find()) {
      this.words.add(dictionary.getWord(wordMatcher.group()));
      //this.words.add(new Word(wordMatcher.group()));
    }
    this.wordCharacterCount = this.words.stream()
        .mapToInt( word -> word.getWordCharacterCount())
        .sum();
    this.syllableCount = this.words.stream()
        .mapToInt( word -> word.getSyllableCount())
        .sum();
    this.wordCount = words.size();
    this.complexWordCount = this.words.stream()
        .mapToInt( word -> word.getComplexWordCount())
        .sum();
    this.longWordCount = this.words.stream()
        .mapToInt( word -> word.getLongWordCount())
        .sum();
    this.povWordCount = this.words.stream()
        .mapToInt( word -> word.getPovWordCount())
        .sum();
    this.firstPersonWordCount = this.words.stream()
        .mapToInt( word -> word.getFirstPersonWordCount())
        .sum();
    this.secondPersonWordCount = this.words.stream()
        .mapToInt( word -> word.getSecondPersonWordCount())
        .sum();
    this.thirdPersonWordCount = this.words.stream()
        .mapToInt( word -> word.getThirdPersonWordCount())
        .sum();
    Set<Word> uniqueWords = new HashSet<Word>(this.words);
    uniqueWords.stream().forEach(word -> {
      wordFrequency.put(word, Collections.frequency(this.words, word));
    });
  }

  /**
   * Returns a new Sentence from a string.
   *
   * <p>String is not currently validates since Sentences should
   * only be created by a Paragraph using WordContainer.SENTENCE_PATTERN.
   * 
   * @param text    a string of text representing a complete sentence
   * @param dictionary  dictionary used for word reference (cache)
   */
  public Sentence(final String text, Dictionary2 dictionary) {
    super(text, dictionary);
    Matcher wordMatcher = Sentence.getWordPattern().matcher(this.getNormalizedText());
    while (wordMatcher.find()) {
      this.words.add(this.getDictionary().getWord(wordMatcher.group()));
    }
    this.wordCharacterCount = this.words.stream()
        .mapToInt( word -> word.getWordCharacterCount())
        .sum();
    this.syllableCount = this.words.stream()
        .mapToInt( word -> word.getSyllableCount())
        .sum();
    this.wordCount = words.size();
    this.complexWordCount = this.words.stream()
        .mapToInt( word -> word.getComplexWordCount())
        .sum();
    this.longWordCount = this.words.stream()
        .mapToInt( word -> word.getLongWordCount())
        .sum();
    this.povWordCount = this.words.stream()
        .mapToInt( word -> word.getPovWordCount())
        .sum();
    this.firstPersonWordCount = this.words.stream()
        .mapToInt( word -> word.getFirstPersonWordCount())
        .sum();
    this.secondPersonWordCount = this.words.stream()
        .mapToInt( word -> word.getSecondPersonWordCount())
        .sum();
    this.thirdPersonWordCount = this.words.stream()
        .mapToInt( word -> word.getThirdPersonWordCount())
        .sum();
    Set<Word> uniqueWords = new HashSet<Word>(this.words);
    uniqueWords.stream().forEach(word -> {
      wordFrequency.put(word, Collections.frequency(this.words, word));
    });
  }

  public static final Pattern getPattern() {
    return Sentence.getSentencePattern();
  }

  @Override
  public final List<Word> getWords() {
    return this.words;
  }

  @Override
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
