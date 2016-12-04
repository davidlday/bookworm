package com.prosegrinder.bookworm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Story extends StoryFragment {

  private final List<Paragraph> paragraphs = new ArrayList<Paragraph>();
  private final Integer wordCount;

  public Story(String text) {
    super(text);
    int wCount = 0;
    Matcher paragraphMatcher = this.getParagraphPattern().matcher(text);
    while (paragraphMatcher.find()) {
      Paragraph paragraph = new Paragraph(paragraphMatcher.group());
      this.paragraphs.add(paragraph);
      wCount += paragraph.getWordCount();
    }
    this.wordCount = wCount;
  }

  public final List<Paragraph> getParagraphs() {
    return this.paragraphs;
  }

  public final Integer getParagraphCount() {
    return this.paragraphs.size();
  }

  public final Integer getWordCount() {
    return this.wordCount;
  }

}