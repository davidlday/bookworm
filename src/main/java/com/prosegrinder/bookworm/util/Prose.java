package com.prosegrinder.bookworm.util;

import com.prosegrinder.bookworm.enums.PovType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Prose extends ProseFragment {

  private final List<Paragraph> paragraphs = new ArrayList<Paragraph>();
  private final List<DialogueFragment> dialogueFragments
      = new ArrayList<DialogueFragment>();
  private final List<NarrativeFragment> narrativeFragments
      = new ArrayList<NarrativeFragment>();

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
  private final Integer paragraphCount;
  private final Integer dialogueSyllableCount;
  private final Integer dialogueWordCount;
  private final Integer narrativeSyllableCount;
  private final Integer narrativeWordCount;

  public Prose(final String text) {
    super(text);
    Matcher paragraphMatcher = this.getParagraphPattern().matcher(text);
    while (paragraphMatcher.find()) {
      Paragraph paragraph = new Paragraph(paragraphMatcher.group());
      this.paragraphs.add(paragraph);
    }
    final Pattern dialoguePattern = this.getDialoguePattern();
    Matcher dialogueMatcher = dialoguePattern.matcher(
        ProseFragment.convertSmartQuotes(this.getInitialText())
    );
    while (dialogueMatcher.find()) {
      this.dialogueFragments.add(
          new DialogueFragment(dialogueMatcher.group())
      );
    }
    for (String narrative: dialoguePattern.split(
        ProseFragment.convertSmartQuotes(this.getInitialText()))) {
      this.narrativeFragments.add(
          new NarrativeFragment(narrative)
      );
    }
    this.dialogueSyllableCount = dialogueFragments.stream()
        .mapToInt( fragment -> fragment.getSyllableCount())
        .sum();
    this.dialogueWordCount = dialogueFragments.stream()
        .mapToInt( fragment -> fragment.getWordCount())
        .sum();
    this.narrativeSyllableCount = narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getSyllableCount())
        .sum();
    this.narrativeWordCount = narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getWordCount())
        .sum();
    this.wordCharacterCount = paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getWordCharacterCount())
        .sum();
    this.syllableCount = paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getSyllableCount())
        .sum();
    this.wordCount = paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getWordCount())
        .sum();
    this.complexWordCount = paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getComplexWordCount())
        .sum();
    this.longWordCount = paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getLongWordCount())
        .sum();
    this.povWordCount = paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getPovWordCount())
        .sum();
    this.sentenceCount = paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getSentenceCount())
        .sum();
    this.paragraphCount = paragraphs.size();
    /** We only consider POV words found in narrative since dialogue is always first person. **/
    this.firstPersonWordCount = narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getFirstPersonWordCount())
        .sum();
    this.secondPersonWordCount = narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getSecondPersonWordCount())
        .sum();
    this.thirdPersonWordCount = narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getThirdPersonWordCount())
        .sum();
  }

  public final List<DialogueFragment> getDialogueFragments() {
    return this.dialogueFragments;
  }

  public final Integer getDialogueWordCount() {
    return this.dialogueWordCount;
  }

  public final List<NarrativeFragment> getNarrativeFragments() {
    return this.narrativeFragments;
  }

  public final Integer getNarrativeWordCount() {
    return this.narrativeWordCount;
  }

  public final Enum getPov() {
    if (this.getFirstPersonWordCount() > 0) {
      return PovType.FIRST;
    } else if (this.getSecondPersonWordCount() > 0) {
      return PovType.SECOND;
    } else if (this.getThirdPersonWordCount() > 0) {
      return PovType.THIRD;
    } else {
      return PovType.UNKNOWN;
    }
  }

  public final List<Paragraph> getParagraphs() {
    return this.paragraphs;
  }

  public final Integer getParagraphCount() {
    return this.paragraphCount;
  }

  public final Integer getSentenceCount() {
    return this.sentenceCount;
  }

  public final Double getAverageSyllablesPerWord() {
    return (double) this.getSyllableCount() / (double) this.getWordCount();
  }

  public final Double getAverageSyllablesPerSentence() {
    return (double) this.getSyllableCount() / (double) this.getSentenceCount();
  }

  public final Double getAverageSyllablesPerParagraph() {
    return (double) this.getSyllableCount() / (double) this.getParagraphCount();
  }

  public final Double getAverageWordsPerSentence() {
    return (double) this.getWordCount() / (double) this.getSentenceCount();
  }

  public final Double getAverageWordsPerParagraph() {
    return (double) this.getWordCount() / (double) this.getParagraphCount();
  }

  public final Double getAverageSentencesPerParagraph() {
    return (double) this.getSentenceCount() / (double) this.getParagraphCount();
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