package com.prosegrinder.bookworm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Paragraph extends StoryFragment {

  private final Integer wordCount;
  private final List<Sentence> sentences = new ArrayList<Sentence>();

  public Paragraph(String text) {
    super(text);
    int wCount = 0;
    Matcher sentenceMatcher = this.getSentencePattern().matcher(text);
    while (sentenceMatcher.find()) {
      Sentence sentence = new Sentence(sentenceMatcher.group());
      this.sentences.add(sentence);
      wCount += sentence.getWordCount();
    }
    this.wordCount = wCount;
  }

  public List<Sentence> getSentences() {
    return this.sentences;
  }

  public final Integer getWordCount() {
    return this.wordCount;
  }

}
