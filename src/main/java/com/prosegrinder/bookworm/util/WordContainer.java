package com.prosegrinder.bookworm.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * The WordContainer abstract class represents some fragment of text found in a piece of prose
 * fiction, and acts as a central point for managing common patterns and processes.
 *
 */
public abstract class WordContainer extends Container {

  private final List<Word> words = new ArrayList<Word>();

  /**
   * Constructs a new WordContainer, ensuring copies of the text in initial and normalized form are
   * available for subsequent processing.
   *
   * @param text String representing the fragment.
   *
   */
  @Deprecated
  public WordContainer(final String text) {
    this(text, Dictionary2.getDefaultDictionary());
  }

  /**
   * Constructs a new WordContainer, ensuring copies of the text in initial and normalized
   * form are available for subsequent processing.
   *
   * @param text  String representing the fragment.
   * @param dictionary Dictionary used for processing prose.
   *
   */
  public WordContainer(final String text, final Dictionary2 dictionary) {
    this.setText(text);
    this.setDictionary(dictionary);
    Matcher wordMatcher = Word.getPattern().matcher(this.getNormalizedText());
    while (wordMatcher.find()) {
      this.words.add(this.getDictionary().getWord(wordMatcher.group()));
    }
    this.setWordCharacterCount(this.words.stream()
        .mapToInt( word -> word.getWordCharacterCount())
        .sum());
    this.setSyllableCount(this.words.stream()
        .mapToInt( word -> word.getSyllableCount())
        .sum());
    this.setWordCount(words.size());
    this.setComplexWordCount(this.words.stream()
        .mapToInt( word -> word.getComplexWordCount())
        .sum());
    this.setLongWordCount(this.words.stream()
        .mapToInt( word -> word.getLongWordCount())
        .sum());
    this.setPovWordCount(this.words.stream()
        .mapToInt( word -> word.getPovWordCount())
        .sum());
    this.setFirstPersonWordCount(this.words.stream()
        .mapToInt( word -> word.getFirstPersonWordCount())
        .sum());
    this.setSecondPersonWordCount(this.words.stream()
        .mapToInt( word -> word.getSecondPersonWordCount())
        .sum());
    this.setThirdPersonWordCount(this.words.stream()
        .mapToInt( word -> word.getThirdPersonWordCount())
        .sum());
    Set<Word> uniqueWords = new HashSet<Word>(this.words);
    uniqueWords.stream().forEach(word -> {
      this.setWordFrequency(word, Collections.frequency(this.words, word));
    });   
  }

  // Maybe move this to Dictionary2?
  @Deprecated
  public static final List<Word> getWords(final String text, Dictionary2 dictionary) {
    Matcher wordMatcher = Word.getPattern().matcher(WordContainer.normalizeText(text));
    List<Word> words = new ArrayList<Word>();
    while (wordMatcher.find()) {
      words.add(dictionary.getWord(wordMatcher.group()));
    }
    return words;
  }

  /**
   * Returns a list of all Words found in the WordContainer.
   *
   * @return a list of all Words found in the WordContainer.
   *
   */
  @Override
  public final List<Word> getWords() {
    return this.words;
  }

}
