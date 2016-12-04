package com.prosegrinder.bookworm;

import java.util.List;

public final class Paragraph {

  public static final String RE_PARAGRAPHS = new String("\\n+");


  public static final String RE_SENTENCE = new String("(?<=[.?!\"])\\s+(?=[\"A-Z])");
  private final Boolean isParagraph;
  private final String paragraph;
  private final List<Word> sentences;

  public Paragraph(String text) {
    this.paragraph = text;
    this.isParagraph = true;
    this.sentences = Sentence.parseSentences(text);
  }

  public List<Word> getWords() {
    return this.words;
  }


  // Parse a String of text into a List of sentences.
  public static final List<Sentence> parseSentences(final String text) {
    // Don't normalize. Case sensitive.
    String[] sentences = text.trim().split(Parser.RE_SENTENCES);
    return Arrays.asList(sentences);
  }

  // Parse a List of paragraphs into a List of sentences.
  public static final List<Sentence> parseSentences(final List<String> paragraphs) {
    List<Sentence> sentences = new ArrayList<String>();
    for (String paragraph: paragraphs) {
      sentences.addAll(Sentence.parseSentences(paragraph));
    }
    return sentences;
  }

  public static void List<Sentence> parseSentences(String text) {

  }

}
