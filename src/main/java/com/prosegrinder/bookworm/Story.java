package com.prosegrinder.bookworm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Story extends StoryFragment {

  private final List<Paragraph> paragraphs = new ArrayList<Paragraph>();
  private final Integer wordCharacterCount;
  private final Integer wordCount;
  private final Integer complexWordCount;
  private final Integer longWordCount;
  private final Integer sentenceCount;
  private final Integer paragraphCount;
  private final Integer sceneCount;
  private final Integer chapterCount;

  public Story(String text) {
    super(text);
    int wordcharcnt = 0;
    int wordcnt = 0;
    int cwordcnt = 0;
    int lwordcnt = 0;
    int sentcnt = 0;
    int paracnt = 0;
    int scenecnt = 0;
    int chaptcnt = 0;
    int partcnt = 0;
    Matcher paragraphMatcher = this.getParagraphPattern().matcher(text);
    while (paragraphMatcher.find()) {
      Paragraph paragraph = new Paragraph(paragraphMatcher.group());
      this.paragraphs.add(paragraph);
      wordcharcnt += paragraph.getWordCharacterCount();
      wordcnt += paragraph.getWordCount();
      cwordcnt += paragraph.getComplexWordCount();
      lwordcnt += paragraph.getLongWordCount();
      sentcnt += paragraph.getSentenceCount();
      paracnt++;
    }
    this.wordCharacterCount = wordcharcnt;
    this.wordCount = wordcnt;
    this.complexWordCount = cwordcnt;
    this.longWordCount = lwordcnt;
    this.sentenceCount = sentcnt;
    this.paragraphCount = paracnt;
    this.sceneCount = scenecnt;
    this.chapterCount = chaptcnt;
  }

  public final List<Paragraph> getParagraphs() {
    return this.paragraphs;
  }

  public final Integer getParagraphCount() {
    return this.paragraphCount;
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

}