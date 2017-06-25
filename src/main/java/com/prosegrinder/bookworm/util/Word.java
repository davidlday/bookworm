package com.prosegrinder.bookworm.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * A Word, the base unit for measuring fiction prose.
 */
public final class Word {

  /** Magic number for determining complex words. **/
  public static final int MIN_SYLLABLES_COMPLEX_WORD = 3;
  /**
   * TODO: Implement full logic.
   *
   * <p>
   * Complex words are: 1) Those with three or more syllables. 2) Do not include proper nouns,
   * familiar jargon, or compound words. 3) Do not include common suffixes (such as -es, -ed, or
   * -ing) as a syllable.
   *
   * <p>
   * The definition for Complex Word is unclear and difficult to implement. Any calculations
   * using Complex Word Count should be considered experimental.
   *
   * <p>
   * See: - https://en.wikipedia.org/wiki/Gunning_fog_index -
   * http://www.readabilityformulas.com/gunning-fog-readability-formula.php
   **/
  
  /** Magic number for determining long words. **/
  public static final int MIN_CHARS_LONG_WORD = 7;

  /** Words indicating first person point of view. **/
  public static final Set<String> POV_FIRST =
      new HashSet<String>(Arrays.asList("I", "I'm", "I'll", "I'd", "I've", "me", "mine", "myself",
          "we", "we're", "we'll", "we'd", "we've", "us", "ours", "ourselves"));
  /** Words indicating second person point of view. **/
  public static final Set<String> POV_SECOND = new HashSet<String>(Arrays.asList("you", "you're",
      "you'll", "you'd", "you've", "yours", "yourself", "yourselves"));
  /** Words indicating third person point of view. **/
  public static final Set<String> POV_THIRD = new HashSet<String>(
      Arrays.asList("he", "he's", "he'll", "he'd", "him", "his", "himself", "she", "she's",
          "she'll", "she'd", "her", "hers", "herself", "it", "it's", "it'll", "it'd", "itself",
          "they", "they're", "they'll", "they'd", "they've", "them", "theirs", "themselves"));

  /** Private member variables. **/
  private final String initialWord;
  private final String normalizedWord;
  private final Boolean isComplexWord;
  private final Boolean isLongWord;
  private final Boolean isPovWord;
  private final Boolean isFirstPersonWord;
  private final Boolean isSecondPersonWord;
  private final Boolean isThirdPersonWord;
  private final Boolean isNumeric;
  private final Integer wordCharacterCount;
  private final Integer syllableCount;
  private final Boolean isDictionaryWord;


  /**
   * Creates a new Word.
   *
   * <p>
   * String is not currently validates since Words should only be created by a Sentence using
   * WordContainer.WORD_PATTERN.
   *
   * @param wordString a String representing a single word
   * @param syllableCount number of syllables in word
   * @param inDictionary Boolean of whether the word is in a real dictionary
   * @param isNumeric Boolean of whether the word is a number
   */
  public Word(final String wordString, final Integer syllableCount, final Boolean inDictionary,
      final Boolean isNumeric) {
    this.initialWord = wordString;
    this.syllableCount = syllableCount;
    this.isDictionaryWord = inDictionary;
    this.isNumeric = isNumeric;
    this.normalizedWord = WordContainer.normalizeText(this.initialWord);
    this.wordCharacterCount = this.getNormalizedText().length();
    if (this.syllableCount >= MIN_SYLLABLES_COMPLEX_WORD) {
      this.isComplexWord = true;
    } else {
      this.isComplexWord = false;
    }
    this.isLongWord = (this.getWordCharacterCount() >= MIN_CHARS_LONG_WORD) ? true : false;
    /** Figure out if the word indicates a point of view. **/
    this.isFirstPersonWord = Word.POV_FIRST.contains(this.getNormalizedText());
    this.isSecondPersonWord = Word.POV_SECOND.contains(this.getNormalizedText());
    this.isThirdPersonWord = Word.POV_THIRD.contains(this.getNormalizedText());
    this.isPovWord = (this.isFirstPersonWord || this.isSecondPersonWord || this.isThirdPersonWord);
  }

  /**
   * Returns a new Word from a string.
   *
   * <p>
   * String is not currently validates since Words should only be created by a Sentence using
   * WordContainer.WORD_PATTERN.
   *
   * @param wordString a single word
   */
  @Deprecated
  public Word(final String wordString) {
    this(wordString, SyllableDictionary.getInstance().getSyllableCount(wordString), SyllableDictionary.getInstance().inDictionary(wordString), SyllableDictionary.getInstance().isNumeric(wordString));
  }

  /**
   * Static method for building word frequency from a list of fragments.
   *
   * @param words a list of WordContainers.
   * @return a map of Word with counts.
   *
   */
  public static final Map<Word, Integer> getWordFrequency(List<Word> words) {
    Set<Word> uniqueWords = new HashSet<Word>(words);
    Map<Word, Integer> wordFrequency = new HashMap<Word, Integer>();
    uniqueWords.stream().forEach(word -> {
      wordFrequency.put(word, Collections.frequency(words, word));
    });
    return wordFrequency;
  }

  /**
   * Returns a String representation of the Word.
   *
   * @return a String representation of the Word.
   *
   */
  public final String toString() {
    return this.getNormalizedText();
  }

  /**
   * Returns the normalized version of the text used to create the Word.
   *
   * @return a normalized version the String representation of the Word.
   *
   */
  public final String getNormalizedText() {
    return this.normalizedWord;
  }

  /**
   * Returns the initial text used to create the Word.
   *
   * @return the String used to create the Word.
   *
   */
  public final String getInitialText() {
    return this.initialWord;
  }

  public static final Pattern getPattern() {
    return WordContainer.getWordPattern();
  }

  /**
   * Indicates whether the word is a Complex Word.
   *
   * <p>
   * The definition for Complex Word is unclear and difficult to implement. Any calculations using
   * Complex Word Count should be considered experimental.
   *
   * @return Boolean indicating whether the word is considered complex.
   *
   */
  public final Boolean isComplexWord() {
    return this.isComplexWord;
  }

  public final Boolean isDictionaryWord() {
    return this.isDictionaryWord;
  }

  public final Boolean isLongWord() {
    return this.isLongWord;
  }

  public final Boolean isNumericWord() {
    return this.isNumeric;
  }

  public final Boolean isFirstPersonWord() {
    return this.isFirstPersonWord;
  }

  public final Boolean isSecondPersonWord() {
    return this.isSecondPersonWord;
  }

  public final Boolean isThirdPersonWord() {
    return this.isThirdPersonWord;
  }

  public final Boolean isPovWord() {
    return this.isPovWord;
  }

  public final Integer getSyllableCount() {
    return this.syllableCount;
  }

  public final Integer getWordCharacterCount() {
    return this.wordCharacterCount;
  }

  public final Integer getWordCount() {
    return 1;
  }

  public final Integer getComplexWordCount() {
    return (this.isComplexWord()) ? 1 : 0;
  }

  public final Integer getLongWordCount() {
    return (this.isLongWord()) ? 1 : 0;
  }

  public final Integer getFirstPersonWordCount() {
    return (this.isFirstPersonWord()) ? 1 : 0;
  }

  public final Integer getSecondPersonWordCount() {
    return (this.isSecondPersonWord()) ? 1 : 0;
  }

  public final Integer getThirdPersonWordCount() {
    return (this.isThirdPersonWord()) ? 1 : 0;
  }

  public final Integer getPovWordCount() {
    return (this.isPovWord()) ? 1 : 0;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Word)) {
      return false;
    }
    if (object == this) {
      return true;
    }
    Word rhs = (Word) object;
    return this.normalizedWord.equals(rhs.getNormalizedText());
  }

  @Override
  public int hashCode() {
    return this.normalizedWord.hashCode();
  }

}
