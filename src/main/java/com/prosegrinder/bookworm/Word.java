package com.prosegrinder.bookworm;

import java.util.Arrays;
import java.util.List;

public class Word {

  private static final String RE_WORD = new String("^[\\w’'-]+$");

  private final Boolean inDictionary;
  private final Boolean isNumeric;
  private final Boolean isWord;
  private final Integer syllables;
  private final String word;

  public Word(String text) {
    /** Make sure this is a single word. **/
    SyllableDictionary sd = SyllableDictionary.getInstance();
    if (sd.inDictionary(text)) {
      this.inDictionary = true;
      this.isNumeric = true;
      this.isWord = true;
    } else if (sd.isNumeric(text)) {
      this.inDictionary = false;
      this.isNumeric = true;
      this.isWord = true;
    } else if (text.matches(this.RE_WORD)) {
      this.inDictionary = false;
      this.isNumeric = false;
      this.isWord = true;
    } else {
      /** TODO: Throw an exception. **/
      this.inDictionary = false;
      this.isNumeric = false;
      this.isWord = false;
    }
    this.word = text;
    this.syllables = sd.getSyllableCount(this.word);
  }

  public String toString() {
    return this.word;
  }

  public static final Boolean isNumericWord(String text) {
    /** TODO: Validate it's a word first. **/
    return SyllableDictionary.getInstance().isNumeric(text);
  }

  public Boolean isNumericWord() {
    return this.isNumeric;
  }

  public Integer getSyllableCount() {
    return this.syllables;
  }

  public Boolean inDictionary() {
    return this.inDictionary;
  }

  // Parse a String of text into a List of words.
  public static final List<Word> parseWords(final Sentence sentence, final Boolean lowerCase) {
    String localtext = sentence;
    if (lowerCase) {
      localtext = Parser.normalizeText(sentence);
    }
    Word[] words = Arrays.stream(localtext.split(Word.RE_WORD))
      .filter(word -> word != "")
      .toArray(Word[]::new);
    return Arrays.asList(words);
  }

  // Parse a String of text into a List of words.
  public static final List<Word> parseWords(final Sentence sentence) {
    return Word.parseWords(sentence, true);
  }

}
