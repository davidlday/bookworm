package com.prosegrinder.bookworm.util;

import com.prosegrinder.bookworm.enums.PovType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Prose extends WordContainer {

  private final List<Paragraph> paragraphs = new ArrayList<Paragraph>();
  private final Map<Word, Integer> wordFrequency = new HashMap<Word, Integer>();
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

  /** Log4j Logger. **/
  private static final Logger logger = LogManager.getLogger(SyllableDictionary.class);

  public Prose(final String text) {
    super(text);
    Matcher paragraphMatcher = this.getParagraphPattern().matcher(text);
    while (paragraphMatcher.find()) {
      Paragraph paragraph = new Paragraph(paragraphMatcher.group());
      this.paragraphs.add(paragraph);
    }
    final Pattern dialoguePattern = this.getDialoguePattern();
    Matcher dialogueMatcher = dialoguePattern.matcher(
        WordContainer.convertSmartQuotes(this.getInitialText())
    );
    while (dialogueMatcher.find()) {
      this.dialogueFragments.add(
          new DialogueFragment(dialogueMatcher.group())
      );
    }
    for (String narrative: dialoguePattern.split(
        WordContainer.convertSmartQuotes(this.getInitialText()))) {
      this.narrativeFragments.add(
          new NarrativeFragment(narrative)
      );
    }
    this.dialogueSyllableCount = this.dialogueFragments.stream()
        .mapToInt( fragment -> fragment.getSyllableCount())
        .sum();
    this.dialogueWordCount = this.dialogueFragments.stream()
        .mapToInt( fragment -> fragment.getWordCount())
        .sum();
    this.narrativeSyllableCount = this.narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getSyllableCount())
        .sum();
    this.narrativeWordCount = this.narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getWordCount())
        .sum();
    this.wordCharacterCount = this.paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getWordCharacterCount())
        .sum();
    this.syllableCount = this.paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getSyllableCount())
        .sum();
    this.wordCount = this.paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getWordCount())
        .sum();
    this.complexWordCount = this.paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getComplexWordCount())
        .sum();
    this.longWordCount = this.paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getLongWordCount())
        .sum();
    this.povWordCount = this.paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getPovWordCount())
        .sum();
    this.sentenceCount = this.paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getSentenceCount())
        .sum();
    this.paragraphCount = this.paragraphs.size();
    /** We only consider POV words found in narrative since dialogue is always first person. **/
    this.firstPersonWordCount = this.narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getFirstPersonWordCount())
        .sum();
    this.secondPersonWordCount = this.narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getSecondPersonWordCount())
        .sum();
    this.thirdPersonWordCount = this.narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getThirdPersonWordCount())
        .sum();
    this.paragraphs.stream().forEach( fragment -> {
      Set<Word> uniqueWords = fragment.getUniqueWords();
      uniqueWords.stream().forEach( word -> {
        int count = (wordFrequency.containsKey(word))
            ? wordFrequency.get(word) : 0;
        count += fragment.getWordFrequency(word);
        wordFrequency.put(word, count);
      });
    });
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

  public final List<Sentence> getSentences() {
    List<Sentence> sentences = new ArrayList<Sentence>();
    this.getParagraphs().stream().forEach( paragraph -> {
      sentences.addAll(paragraph.getSentences());
    });
    return sentences;
  }

  public final Integer getSentenceCount() {
    return this.sentenceCount;
  }

  public final List<Word> getWords() {
    List<Word> words = new ArrayList<Word>();
    this.getSentences().stream().forEach( sentence -> {
      words.addAll(sentence.getWords());
    });
    return words;
  }

  @Override
  public final Set<Word> getUniqueWords() {
    return this.wordFrequency.keySet();
  }

  public final Integer getUniqueWordCount() {
    return this.wordFrequency.keySet().size();
  }

  @Override
  public final Map<Word, Integer> getWordFrequency() {
    return this.wordFrequency;
  }

  @Override
  public final Integer getWordFrequency(Word word) {
    if (this.wordFrequency.containsKey(word)) {
      return this.wordFrequency.get(word);
    } else {
      return 0;
    }
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
