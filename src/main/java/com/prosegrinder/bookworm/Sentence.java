package com.prosegrinder.bookworm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Sentence extends StoryFragment {

  private final List<Word> words = new ArrayList<Word>();

  public Sentence(String text) {
    super(text);
    Matcher wordMatcher = this.getWordPattern().matcher(text);
    while (wordMatcher.find()) {
      this.words.add(new Word(wordMatcher.group()));
    }
  }

  public final List<Word> getWords() {
    return this.words;
  }

  public final Integer getWordCount() {
    return this.words.size();
  }

}
