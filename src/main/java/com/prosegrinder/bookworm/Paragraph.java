package com.prosegrinder.bookworm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Paragraph extends StoryFragment {

  private final List<Sentence> sentences = new ArrayList<Sentence>();
  private final Integer wordCharacterCount;
  private final Integer wordCount;
  private final Integer complexWordCount;
  private final Integer longWordCount;
  private final Integer sentenceCount;

  public Paragraph(String text) {
    super(text);
    int wordcharcnt = 0;
    int wordcnt = 0;
    int cwordcnt = 0;
    int lwordcnt = 0;
    int sentcnt = 0;
    Matcher sentenceMatcher = Sentence.getPattern().matcher(text);
    while (sentenceMatcher.find()) {
      Sentence sentence = new Sentence(sentenceMatcher.group());
      this.sentences.add(sentence);
      wordcharcnt += sentence.getWordCharacterCount();
      wordcnt += sentence.getWordCount();
      cwordcnt += sentence.getComplexWordCount();
      lwordcnt += sentence.getLongWordCount();
      sentcnt++;
    }
    this.wordCharacterCount = wordcharcnt;
    this.wordCount = wordcnt;
    this.complexWordCount = cwordcnt;
    this.longWordCount = lwordcnt;
    this.sentenceCount = sentcnt;
  }

  public static final Pattern getPattern() {
    return StoryFragment.getParagraphPattern();
  }

  public List<Sentence> getSentences() {
    return this.sentences;
  }

  public final Integer getWordCount() {
    return this.wordCount;
  }

  public final Integer getComplexWordCount() {
    return this.complexWordCount;
  }

  public final Integer getLongWordCount() {
    return this.longWordCount;
  }

  public final Integer getWordCharacterCount() {
    return this.wordCharacterCount;
  }

  public final Integer getSentenceCount() {
    return this.sentenceCount;
  }

}
