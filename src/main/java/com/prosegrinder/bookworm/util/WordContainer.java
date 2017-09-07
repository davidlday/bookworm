package com.prosegrinder.bookworm.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * The WordContainer abstract class represents some fragment of text found in a piece of
 * prose fiction, and acts as a central point for managing common patterns and processes.
 *
 */
public abstract class WordContainer {

  private final String initialText;
  private final String normalizedText;
  private final Dictionary2 dictionary;
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

  /** Patterns used throughout. Will eventually move to a properties file. **/
  private static final String RE_SMART_QUOTES = "[“”]";

  /**
   * Converts left and right double quotation marks (“”) to
   * neutral quotation marks ("). These smart quotation marks
   * create more opportunity for error when trying to parse out
   * dialogue.
   *
   * @param text  source text to analyze
   * @return a copy of text using neutral quotation marks in place of smart quotation marks
   *
   */
  public static final String convertSmartQuotes(final String text) {
    return text.replaceAll(WordContainer.RE_SMART_QUOTES, "\"");
  }

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
   * Normalize text for processing by trimming and converting to lower case.
   *
   * @param text  source text to analyze
   * @return a normalized representation of text
   *
   */
  public static final String normalizeText(final String text) {
    return text.trim().toLowerCase();
  }
  
  /**
   * Constructs a new WordContainer, ensuring copies of the text in initial and normalized
   * form are available for subsequent processing.
   *
   * @param text  String representing the fragment.
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
    this.initialText = text;
    this.normalizedText = WordContainer.normalizeText(this.initialText);
    this.dictionary = dictionary;
    Matcher wordMatcher = Word.getPattern().matcher(this.getNormalizedText());
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

  /**
   * Returns the number of complex Words found in the WordContainer.
   *
   * <p>The definition for Complex Word is unclear and difficult to implement.
   * Any calculations using Complex Word Count should be considered experimental.
   *
   * @return the number of complex Words in the WordContainer.
   *
   */
  public final Integer getComplexWordCount() { 
    return this.complexWordCount;
  }

  public final Dictionary2 getDictionary() {
    return this.dictionary;
  }

  /**
   * Returns the number of Words representing first person point of view (POV)
   * found in the WordContainer.
   *
   * @return the number of Words representing first person POV in the WordContainer.
   *
   */
  public final Integer getFirstPersonWordCount() {
    return this.firstPersonWordCount;
  }

  /**
   * Returns the initial text used to create the WordContainer.
   *
   * @return the String used to create the WordContainer.
   *
   */
  public final String getInitialText() {
    return this.initialText;
  }

  /**
   * Returns the number of long Words found in the WordContainer.
   *
   * @return the number of long Words in the WordContainer.
   *
   */
  public final Integer getLongWordCount() {
    return this.longWordCount;
  }

  /**
   * Returns the normalized version of the text used to create the WordContainer.
   *
   * @return a normalized version the String representation of the WordContainer.
   *
   */
  public final String getNormalizedText() {
    return this.normalizedText;
  }

  /**
   * Returns the number of Words representing any point of view (POV)
   * found in the WordContainer.
   *
   * @return the number of Words representing any POV in the WordContainer.
   *
   */
  public final Integer getPovWordCount() {
    return this.povWordCount;
  }

  /**
   * Returns the number of Words representing second person point of view (POV)
   * found in the WordContainer.
   *
   * @return the number of Words representing second person POV in the WordContainer.
   *
   */
  public final Integer getSecondPersonWordCount() {
    return this.secondPersonWordCount;
  }

  /**
   * Returns the number of syllables found in the WordContainer.
   *
   * @return the number of syllables in the WordContainer.
   *
   */
  public final Integer getSyllableCount() {
    return this.syllableCount;
  }

  /**
   * Returns the number of Words representing third person point of view (POV)
   * found in the WordContainer.
   *
   * @return the number of Words representing third person POV in the WordContainer.
   *
   */
  public final Integer getThirdPersonWordCount() {
    return this.thirdPersonWordCount;
  }

  /**
   * Returns the count of unique Words found in the WordContainer.
   *
   * @return the count of unique Words found in the WordContainer.
   *
   */
  public final Integer getUniqueWordCount() {
    return this.getUniqueWords().size();
  }

  /**
   * Returns a set of unique Words found in the WordContainer
   *
   * @return a set of unique Words found in the WordContainer.
   *
   */
  public final Set<Word> getUniqueWords() {
    return this.getWordFrequency().keySet();
  }

  /**
   * Returns the number of word characters found in the WordContainer.
   *
   * @return the number of work characters in the WordContainer.
   *
   */
  public final Integer getWordCharacterCount() {
    return this.wordCharacterCount;
  }

  /**
   * Returns the number of Words found in the WordContainer.
   *
   * @return the number of Words in the WordContainer.
   * @see Word
   *
   */
  public final Integer getWordCount() {
    return this.wordCount;
  }

  /**
   * Returns a map of unique Words found in the WordContainer
   * with a value of how many times that Word occurs in the underlying text.
   *
   * @return a map with Word as key and the number of times Word appears as value.
   *
   */
  public final Map<Word, Integer> getWordFrequency() {
    return this.wordFrequency;
  }

  // Convenience method for building out word frequency maps.
  public final Map<Word, Integer> getWordFrequency(Set<String> wordSet) {
    Map<Word, Integer> wordMap = new HashMap<Word, Integer>();
    for (String wordString: wordSet) {
      Word word = this.getDictionary().getWord(wordString);
      wordMap.put(word, this.getWordFrequency(word));
    }
    return wordMap;
  }

  /**
   * Returns the number of times a Word appears in the WordContainer.
   *
   * @param word  the word you want the frequency for.
   * @return the number of times a Word appears in the WordContainer.
   *
   */
  public final Integer getWordFrequency(Word word) {
    return (this.getWordFrequency().containsKey(word))
        ? this.getWordFrequency().get(word)
        : 0;
  }

  /**
   * Returns a list of all Words found in the WordContainer.
   *
   * @return a list of all Words found in the WordContainer.
   *
   */
  public final List<Word> getWords() {
    return this.words;
  }
  
  /**
   * Returns a String representation of the ProseFragement.
   *
   * @return a String representation of the WordContainer.
   *
   */
  public final String toString() {
    return this.getInitialText();
  }

}
