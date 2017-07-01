package com.prosegrinder.bookworm.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a fragment of dialogue.
 * 
 * <p>In fiction, dialogue and narrative can be interspersed with each other, 
 * meaning they may or may not be found in complete sentences.
 */
public final class DialogueFragment extends WordContainer {

  private final List<Word> words = new ArrayList<Word>();
  private final Map<Word, Integer> wordFrequency;

  private final Integer wordCharacterCount;
  private final Integer syllableCount;
  private final Integer wordCount;
  private final Integer complexWordCount;
  private final Integer longWordCount;
  private final Integer povWordCount;
  private final Integer firstPersonWordCount;
  private final Integer secondPersonWordCount;
  private final Integer thirdPersonWordCount;

  /**
   * Returns a new DialogueFragment from a string.
   *
   * <p>Dialogue slices out independently of sentences and paragraphs. A
   * single sentence may contain a mix of dialogue and narrative, or a chunk of dialogue
   * may span multiple paragraphs. The only place dialogue can effectively be parsed out
   * is from the entirety of the original Prose.
   *
   * <p>String is not currently validated since DialogueFragments should
   * only be created by a Prose object using WordContainer.DIALOGUE_PATTERN.
   * 
   * @param text    a string of text representing a piece of dialogue
   */
  public DialogueFragment(final String text) {
    super(text);
    Dictionary dictionary = Dictionary.getInstance();
    Matcher wordMatcher = WordContainer.getWordPattern().matcher(this.getNormalizedText());
    while (wordMatcher.find()) {
      this.words.add(dictionary.getWord(wordMatcher.group()));
      //this.words.add(new Word(wordMatcher.group()));
    }
    this.wordCharacterCount = words.stream()
        .mapToInt( word -> word.getWordCharacterCount())
        .sum();
    this.syllableCount = words.stream()
        .mapToInt( word -> word.getSyllableCount())
        .sum();
    this.wordCount = words.size();
    this.complexWordCount = words.stream()
        .mapToInt( word -> word.getComplexWordCount())
        .sum();
    this.longWordCount = words.stream()
        .mapToInt( word -> word.getLongWordCount())
        .sum();
    this.povWordCount = words.stream()
        .mapToInt( word -> word.getPovWordCount())
        .sum();
    this.firstPersonWordCount = words.stream()
        .mapToInt( word -> word.getFirstPersonWordCount())
        .sum();
    this.secondPersonWordCount = words.stream()
        .mapToInt( word -> word.getSecondPersonWordCount())
        .sum();
    this.thirdPersonWordCount = words.stream()
        .mapToInt( word -> word.getThirdPersonWordCount())
        .sum();
    this.wordFrequency = Word.getWordFrequency(this.words);
  }

  public static final Pattern getPattern() {
    return WordContainer.getDialoguePattern();
  }

  @Override
  public final List<Word> getWords() {
    return this.words;
  }

  @Override
  public final Map<Word, Integer> getWordFrequency() {
    return this.wordFrequency;
  }

 @Override
  public final Integer getWordCharacterCount() {
    return this.wordCharacterCount;
  }

  @Override
  public final Integer getSyllableCount() {
    return this.syllableCount;
  }

  @Override
  public final Integer getWordCount() {
    return this.wordCount;
  }

  @Override
  public final Integer getComplexWordCount() {
    return this.complexWordCount;
  }

  @Override
  public final Integer getLongWordCount() {
    return this.longWordCount;
  }

  @Override
  public final Integer getFirstPersonWordCount() {
    return this.firstPersonWordCount;
  }

  @Override
  public final Integer getSecondPersonWordCount() {
    return this.secondPersonWordCount;
  }

  @Override
  public final Integer getThirdPersonWordCount() {
    return this.thirdPersonWordCount;
  }

  @Override
  public final Integer getPovWordCount() {
    return this.povWordCount;
  }

}
