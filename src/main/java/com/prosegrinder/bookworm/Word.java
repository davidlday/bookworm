package com.prosegrinder.bookworm;

public final class Word {

  private static final String RE_WORD = new String("^[\\w'-]+$");

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

  public Boolean isNumeric() {
    return this.isNumeric;
  }

  public Integer getSyllableCount() {
    return this.syllables;
  }

  public Boolean inDictionary() {
    return this.inDictionary;
  }

}
