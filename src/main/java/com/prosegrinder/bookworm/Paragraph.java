package com.prosegrinder.bookworm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Paragraph extends StoryFragment {

  private final List<Sentence> sentences = new ArrayList<Sentence>();
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

  private final Integer sentenceCount;

  public Paragraph(String text) {
    super(text);
    Matcher sentenceMatcher = Sentence.getPattern().matcher(text);
    while (sentenceMatcher.find()) {
      Sentence sentence = new Sentence(sentenceMatcher.group());
      this.sentences.add(sentence);
    }
    this.wordCharacterCount = super.getWordCharacterCount(
        (List<StoryFragment>) (Object) sentences
    );
    this.syllableCount = super.getSyllableCount(
        (List<StoryFragment>) (Object) sentences
    );
    this.wordCount = super.getWordCount(
        (List<StoryFragment>) (Object) sentences
    );
    this.complexWordCount = super.getComplexWordCount(
        (List<StoryFragment>) (Object) sentences
    );
    this.longWordCount = super.getLongWordCount(
          (List<StoryFragment>) (Object) sentences
    );
    this.firstPersonWordCount = super.getFirstPersonWordCount(
          (List<StoryFragment>) (Object) sentences
    );
    this.secondPersonWordCount = super.getSecondPersonWordCount(
          (List<StoryFragment>) (Object) sentences
    );
    this.thirdPersonWordCount = super.getThirdPersonWordCount(
          (List<StoryFragment>) (Object) sentences
    );
    this.povWordCount = this.getPovWordCount(
          (List<StoryFragment>) (Object) sentences
    );
    this.sentenceCount = sentences.size();
  }

  public static final Pattern getPattern() {
    return StoryFragment.getParagraphPattern();
  }

  public List<Sentence> getSentences() {
    return this.sentences;
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

  public final Integer getSentenceCount() {
    return this.sentenceCount;
  }

}
