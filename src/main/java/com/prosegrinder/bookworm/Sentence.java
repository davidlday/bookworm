package com.prosegrinder.bookworm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Sentence extends StoryFragment {

  public static final String RE_SMART_QUOTES = new String("[“”]");

  private final List<Word> words = new ArrayList<Word>();
  private final List<DialogueFragment> dialogueFragments
      = new ArrayList<DialogueFragment>();
  private final Integer wordCharacterCount;
  private final Integer syllableCount;
  private final Integer wordCount;
  private final Integer complexWordCount;
  private final Integer longWordCount;
  private final Integer povWordCount;
  private final Integer firstPersonWordCount;
  private final Integer secondPersonWordCount;
  private final Integer thirdPersonWordCount;

  public Sentence(String text) {
    super(text);
    Matcher dialogueMatcher = super.getDialoguePattern().matcher(
        this.getInitialText().replaceAll(this.RE_SMART_QUOTES, "\"")
    );
    while (dialogueMatcher.find()) {
      this.dialogueFragments.add(
          new DialogueFragment(dialogueMatcher.group())
      );
    }
    Matcher wordMatcher = Word.getWordPattern().matcher(text);
    while (wordMatcher.find()) {
      Word word = new Word(wordMatcher.group());
      this.words.add(word);
    }
    this.wordCharacterCount = super.getWordCharacterCount(
        (List<StoryFragment>) (Object) words
    );
    this.syllableCount = super.getSyllableCount(
        (List<StoryFragment>) (Object) words
    );
    this.wordCount = super.getWordCount(
        (List<StoryFragment>) (Object) words
    );
    this.complexWordCount = super.getComplexWordCount(
        (List<StoryFragment>) (Object) words
    );
    this.longWordCount = super.getLongWordCount(
          (List<StoryFragment>) (Object) words
    );
    this.firstPersonWordCount = super.getFirstPersonWordCount(
          (List<StoryFragment>) (Object) words
    );
    this.secondPersonWordCount = super.getSecondPersonWordCount(
          (List<StoryFragment>) (Object) words
    );
    this.thirdPersonWordCount = super.getThirdPersonWordCount(
          (List<StoryFragment>) (Object) words
    );
    this.povWordCount = this.getPovWordCount(
          (List<StoryFragment>) (Object) words
    );
  }

  public static final Pattern getPattern() {
    return StoryFragment.getSentencePattern();
  }

  public final List<Word> getWords() {
    return this.words;
  }

  public final List<DialogueFragment> getDialogueFragments() {
    return this.dialogueFragments;
  }

  public final Integer getWordCharacterCount() {
    return this.wordCharacterCount;
  }

  public final Integer getSyllableCount() {
    return this.syllableCount;
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

  public final Integer getFirstPersonWordCount() {
    return this.firstPersonWordCount;
  }

  public final Integer getSecondPersonWordCount() {
    return this.secondPersonWordCount;
  }

  public final Integer getThirdPersonWordCount() {
    return this.thirdPersonWordCount;
  }

  public final Integer getPovWordCount() {
    return this.povWordCount;
  }

}
