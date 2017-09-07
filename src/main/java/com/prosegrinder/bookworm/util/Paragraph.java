package com.prosegrinder.bookworm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Paragraph {

  private final List<Sentence> sentences = new ArrayList<Sentence>();
  private final Map<Word, Integer> wordFrequency = new HashMap<Word, Integer>();
  private final String initialText;
  private final String normalizedText;
  private final Dictionary2 dictionary;
  private final Integer wordCharacterCount;
  private final Integer syllableCount;
  private final Integer wordCount;
  private final Integer complexWordCount;
  private final Integer longWordCount;
  private final Integer povWordCount;
  private final Integer firstPersonWordCount;
  private final Integer secondPersonWordCount;
  private final Integer thirdPersonWordCount;
  private final Integer sentenceCount;

  /** Pattern for slicing text into paragraphs. **/
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
    this.initialText = text;
    this.normalizedText = WordContainer.normalizeText(text);
    this.dictionary = dictionary;
    Matcher sentenceMatcher = Sentence.getPattern().matcher(text);
    while (sentenceMatcher.find()) {
      this.sentences.add(new Sentence(sentenceMatcher.group(), this.getDictionary()));
    }
    this.wordCharacterCount = this.sentences.stream()
        .mapToInt( sentence -> sentence.getWordCharacterCount())
        .sum();
    this.syllableCount = this.sentences.stream()
        .mapToInt( sentence -> sentence.getSyllableCount())
        .sum();
    this.wordCount = this.sentences.stream()
        .mapToInt( sentence -> sentence.getWordCount())
        .sum();
    this.complexWordCount = this.sentences.stream()
        .mapToInt( sentence -> sentence.getComplexWordCount())
        .sum();
    this.longWordCount = this.sentences.stream()
        .mapToInt( sentence -> sentence.getLongWordCount())
        .sum();
    this.povWordCount = this.sentences.stream()
        .mapToInt( sentence -> sentence.getPovWordCount())
        .sum();
    this.firstPersonWordCount = this.sentences.stream()
        .mapToInt( sentence -> sentence.getFirstPersonWordCount())
        .sum();
    this.secondPersonWordCount = this.sentences.stream()
        .mapToInt( sentence -> sentence.getSecondPersonWordCount())
        .sum();
    this.thirdPersonWordCount = this.sentences.stream()
        .mapToInt( sentence -> sentence.getThirdPersonWordCount())
        .sum();
    this.sentenceCount = this.sentences.size();
  
    this.sentences.stream().forEach( fragment -> {
      Set<Word> uniqueWords = fragment.getUniqueWords();
      uniqueWords.stream().forEach( word -> {
        int count = (wordFrequency.containsKey(word))
            ? wordFrequency.get(word) : 0;
        count += fragment.getWordFrequency(word);
        wordFrequency.put(word, count);
      });
    });
  
  }

  public final Integer getComplexWordCount() {
    return this.complexWordCount;
  }

  public final Dictionary2 getDictionary() {
    return this.dictionary;
  }

  public final Integer getFirstPersonWordCount() {
    return this.firstPersonWordCount;
  }

  /**
   * Returns the initial text used to create the WordContainer.
   *
   * @return the String used to create the WordContainer.
   *
   */
  public final String getInitialText() {
    return this.initialText;
  }

  public final Integer getLongWordCount() {
    return this.longWordCount;
  }

  public final String getNormalizedText() {
    return this.normalizedText;
  }

  public static final Pattern getPattern() {
    return Paragraph.PARAGRAPH_PATTERN;
  }

  public final Integer getPovWordCount() {
    return this.povWordCount;
  }

  public final Integer getSecondPersonWordCount() {
    return this.secondPersonWordCount;
  }

  public final Integer getSentenceCount() {
    return this.sentenceCount;
  }

  public final List<Sentence> getSentences() {
    return this.sentences;
  }

  public final Integer getSyllableCount() {
    return this.syllableCount;
  }

  public final Integer getThirdPersonWordCount() {
    return this.thirdPersonWordCount;
  }

  /**
   * Returns the count of unique Words found in the WordContainer.
   *
   * @return the count of unique Words found in the WordContainer.
   *
   */
  public final Integer getUniqueWordCount() {
    return this.getUniqueWords().size();
  }

  public final Set<Word> getUniqueWords() {
    return this.getWordFrequency().keySet();
  }

  public final Integer getWordCharacterCount() {
    return this.wordCharacterCount;
  }

  public final Integer getWordCount() {
    return this.wordCount;
  }

  public final Map<Word, Integer> getWordFrequency() {
    return this.wordFrequency;
  }

  /**
   * Returns the number of times a Word appears in the Paragraph.
   *
   * @param word  the word you want the frequency for.
   * @return the number of times a Word appears in the WordContainer.
   *
   */
  public final Integer getWordFrequency(Word word) {
    return (this.getWordFrequency().containsKey(word))
        ? this.getWordFrequency().get(word)
        : 0;
  }

  public final List<Word> getWords() {
    List<Word> words = new ArrayList<Word>();
    this.getSentences().stream().forEach( sentence -> {
      words.addAll(sentence.getWords());
    });
    return words;
  }

}
