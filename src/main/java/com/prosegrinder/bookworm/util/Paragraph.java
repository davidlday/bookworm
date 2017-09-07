package com.prosegrinder.bookworm.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Paragraph extends AggregateContainer {

  private final List<Sentence> sentences = new ArrayList<Sentence>();
  private final Integer sentenceCount;
  private static final Pattern PARAGRAPH_PATTERN = Pattern.compile(
      ".*(?=\\n|$)"
  );

  /**
   * Returns a new Paragraph from a string.
   *
   * <p>String is not currently validated since Paragraphs should
   * only be created by a Prose object using WordContainer.PARAGRAPH_PATTERN.
   * 
   * @param text    a string of text representing a complete paragraph
   */
  @Deprecated
  public Paragraph(final String text) {
    this(text, Dictionary2.getDefaultDictionary());
  }

  /**
   * Returns a new Paragraph from a string.
   *
   * <p>String is not currently validated since Paragraphs should
   * only be created by a Prose object using WordContainer.PARAGRAPH_PATTERN.
   * 
   * @param text    a string of text representing a complete paragraph
   * @param dictionary  dictionary used for word reference (cache)
   */
  public Paragraph(final String text, Dictionary2 dictionary) {
    this.setText(text);
    this.setDictionary(dictionary);
    Matcher sentenceMatcher = Sentence.getPattern().matcher(text);
    while (sentenceMatcher.find()) {
      this.sentences.add(new Sentence(sentenceMatcher.group(), this.getDictionary()));
    }
    List<Container> containers = new ArrayList<Container>();
    sentences.stream().forEach( sentence -> {
      containers.add((Container) sentence);
    });
    this.aggregateContainers(containers);
    this.sentenceCount = this.sentences.size();
  }

  public static final Pattern getPattern() {
    return Paragraph.PARAGRAPH_PATTERN;
  }

  public final Integer getSentenceCount() {
    return this.sentenceCount;
  }

  public final List<Sentence> getSentences() {
    return this.sentences;
  }

  public final List<Word> getWords() {
    List<Word> words = new ArrayList<Word>();
    this.getSentences().stream().forEach( sentence -> {
      words.addAll(sentence.getWords());
    });
    return words;
  }

}
