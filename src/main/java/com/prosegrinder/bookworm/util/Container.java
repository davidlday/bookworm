package com.prosegrinder.bookworm.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The WordContainer abstract class represents some fragment of text found in a piece of prose
 * fiction, and acts as a central point for managing common patterns and processes.
 *
 */
public abstract class Container {

  private Dictionary2 dictionary;
  private String initialText;
  private String normalizedText;
  private Map<Word, Integer> wordFrequency = new HashMap<Word, Integer>();
  private Integer wordCharacterCount;
  private Integer syllableCount;
  private Integer wordCount;
  private Integer complexWordCount;
  private Integer longWordCount;
  private Integer povWordCount;
  private Integer firstPersonWordCount;
  private Integer secondPersonWordCount;
  private Integer thirdPersonWordCount;
  private static final String RE_SMART_QUOTES = "[“”]";
  
  /**
   * Normalize text for processing by trimming and converting to lower case.
   *
   * @param text source text to analyze
   * @return a normalized representation of text
   *
   */
  public static final String normalizeText(final String text) {
    return text.trim().toLowerCase();
  }

  /**
   * Returns the number of complex Words found in the WordContainer.
   *
   * <p>
   * The definition for Complex Word is unclear and difficult to implement. Any calculations using
   * Complex Word Count should be considered experimental.
   *
   * @return the number of complex Words in the WordContainer.
   *
   */
  public final Integer getComplexWordCount() {
    return this.complexWordCount;
  }
  
  protected void setComplexWordCount(final Integer complexWordCount) {
    this.complexWordCount = complexWordCount;
  }

  public final Dictionary2 getDictionary() {
    return this.dictionary;
  }

  /**
   * Returns the number of Words representing first person point of view (POV) found in the
   * WordContainer.
   *
   * @return the number of Words representing first person POV in the WordContainer.
   *
   */
  public final Integer getFirstPersonWordCount() {
    return this.firstPersonWordCount;
  }
  
  protected void setFirstPersonWordCount(final Integer firstPersonWordCount) {
    this.firstPersonWordCount = firstPersonWordCount;
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
  
  protected void setLongWordCount(final Integer longWordCount ) {
    this.longWordCount = longWordCount;
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
   * Returns the number of Words representing any point of view (POV) found in the WordContainer.
   *
   * @return the number of Words representing any POV in the WordContainer.
   *
   */
  public final  Integer getPovWordCount() {
    return this.povWordCount;
  }
  
  protected void setPovWordCount(final Integer povWordCount) {
    this.povWordCount = povWordCount;
  }

  /**
   * Returns the number of Words representing second person point of view (POV) found in the
   * WordContainer.
   *
   * @return the number of Words representing second person POV in the WordContainer.
   *
   */
  public final Integer getSecondPersonWordCount() {
    return this.secondPersonWordCount;
  }
  
  protected void setSecondPersonWordCount(final Integer secondPersonWordCount) {
    this.secondPersonWordCount = secondPersonWordCount;
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
  
  protected void setSyllableCount(final Integer syllableCount) {
    this.syllableCount = syllableCount;
  }

  /**
   * Returns the number of Words representing third person point of view (POV) found in the
   * WordContainer.
   *
   * @return the number of Words representing third person POV in the WordContainer.
   *
   */
  public final Integer getThirdPersonWordCount() {
    return this.thirdPersonWordCount;
  }
  
  protected void setThirdPersonWordCount(final Integer thirdPersonWordCount) {
    this.thirdPersonWordCount = thirdPersonWordCount;
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
  
  protected void setWordCharacterCount(final Integer wordCharacterCount) {
    this.wordCharacterCount = wordCharacterCount;
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
  
  protected void setWordCount(final Integer wordCount) {
    this.wordCount = wordCount;
  }

  /**
   * Returns a map of unique Words found in the WordContainer with a value of how many times that
   * Word occurs in the underlying text.
   *
   * @return a map with Word as key and the number of times Word appears as value.
   *
   * TODO: Refactor to getWordFrequencyMap
   */
  public final Map<Word, Integer> getWordFrequency() {
    return this.wordFrequency;
  }
  
  protected void setWordFrequencyMap(final Map<Word, Integer> wordFrequencyMap) {
    this.wordFrequency = wordFrequencyMap;
  }
  
  protected void setWordFrequency(final Word word, final Integer frequency) {
    this.wordFrequency.put(word, frequency);
  }

  /**
   * Returns the number of times a Word appears in the Container.
   *
   * @param word  the word you want the frequency for.
   * @return the number of times a Word appears in the Container.
   *
   */
  public final Integer getWordFrequency(Word word) {
    return (this.getWordFrequency().containsKey(word))
        ? this.getWordFrequency().get(word)
        : 0;
  }

  /**
   * Returns a new word frequency map based on a specific set of words. Used internally
   * to create PoV word frequency maps.
   * 
   * @param wordSet the set of words to build the map
   * @return a word frequency map based on the wordSet.
   */
  public final Map<Word, Integer> getWordFrequency(Set<String> wordSet) {
    Map<Word, Integer> wordMap = new HashMap<Word, Integer>();
    for (String wordString: wordSet) {
      Word word = this.getDictionary().getWord(wordString);
      wordMap.put(word, this.getWordFrequency(word));
    }
    return wordMap;
  }

  protected void setDictionary(Dictionary2 dictionary) {
    this.dictionary = dictionary;
  }

  protected void setInitialText(final String initialText) {
    this.initialText = initialText;
  }

  protected void setNormalizedText(final String normalizedText) {
    this.normalizedText = normalizedText;
  }

  protected void setText(final String initialText) {
    this.setInitialText(initialText);
    this.setNormalizedText(Container.normalizeText(Container.convertSmartQuotes(initialText)));
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
    return text.replaceAll(Container.RE_SMART_QUOTES, "\"");
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

}
