package com.prosegrinder.bookworm;

import java.util.List;

public final class Sentence {

  public static final String RE_SENTENCE = new String("(?<=[.?!\"])\\s+(?=[\"A-Z])");
  private final Boolean isSentence;
  private final String sentence;
  private final List<Word> words;

  public Sentence(String text) {
    this.sentence = text;
    this.isSentence = true;
    this.words = Word.parseWords(text);
  }

  public List<Word> getWords() {
    return this.words;
  }



}
