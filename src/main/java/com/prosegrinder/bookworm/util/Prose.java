package com.prosegrinder.bookworm.util;

import com.prosegrinder.bookworm.enums.PovType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The top-level class representing a single work of prose fiction. The underlying text
 * is split out two ways:
 *   1) into a List of Paragraphs, and 
 *   2) into a List of DialogueFragments and a List of NarrativeFragments.
 * 
 */
public final class Prose extends WordContainer {

  private final List<Paragraph> paragraphs = new ArrayList<Paragraph>();
  private final Map<Word, Integer> wordFrequency = new HashMap<Word, Integer>();
  private final List<DialogueFragment> dialogueFragments
      = new ArrayList<DialogueFragment>();
  private final Map<Word, Integer> dialogueWordFrequency = new HashMap<Word, Integer>();
  private final List<NarrativeFragment> narrativeFragments
      = new ArrayList<NarrativeFragment>();
  private final Map<Word, Integer> narrativeWordFrequency = new HashMap<Word, Integer>();

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
  private final Integer dialogueFirstPersonWordCount;
  private final Integer dialogueSecondPersonWordCount;
  private final Integer dialogueThirdPersonWordCount;
  private final Integer narrativeSyllableCount;
  private final Integer narrativeWordCount;
  private final Integer narrativeFirstPersonWordCount;
  private final Integer narrativeSecondPersonWordCount;
  private final Integer narrativeThirdPersonWordCount;

  /**
   * Returns a new Prose object from a string.
   *
   * <p>Prose is currently considered the top level WordContainer. The String is not validated
   * as it is assumed to be an arbitrary block of text representing some kind of story.
   * 
   * @param text    a string of text representing a complete work of prose fiction
   */
  @Deprecated
  public Prose(final String text) {
    super(text);
    Matcher paragraphMatcher = WordContainer.getParagraphPattern().matcher(text);
    while (paragraphMatcher.find()) {
      Paragraph paragraph = new Paragraph(paragraphMatcher.group());
      this.paragraphs.add(paragraph);
    }
    final Pattern dialoguePattern = WordContainer.getDialoguePattern();
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
    this.dialogueFirstPersonWordCount = this.dialogueFragments.stream()
        .mapToInt( fragment -> fragment.getFirstPersonWordCount())
        .sum();
    this.dialogueSecondPersonWordCount = this.dialogueFragments.stream()
        .mapToInt( fragment -> fragment.getSecondPersonWordCount())
        .sum();
    this.dialogueThirdPersonWordCount = this.dialogueFragments.stream()
        .mapToInt( fragment -> fragment.getThirdPersonWordCount())
        .sum();
    this.narrativeSyllableCount = this.narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getSyllableCount())
        .sum();
    this.narrativeWordCount = this.narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getWordCount())
        .sum();
    this.narrativeFirstPersonWordCount = this.narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getFirstPersonWordCount())
        .sum();
    this.narrativeSecondPersonWordCount = this.narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getSecondPersonWordCount())
        .sum();
    this.narrativeThirdPersonWordCount = this.narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getThirdPersonWordCount())
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
    this.paragraphs.stream().forEach( paragraph -> {
      Set<Word> uniqueWords = paragraph.getUniqueWords();
      uniqueWords.stream().forEach( word -> {
        int count = (this.wordFrequency.containsKey(word))
            ? this.wordFrequency.get(word) : 0;
        count += paragraph.getWordFrequency(word);
        this.wordFrequency.put(word, count);
      });
    });
    this.dialogueFragments.stream().forEach( fragment -> {
      Set<Word> uniqueWords = fragment.getUniqueWords();
      uniqueWords.stream().forEach( word -> {
        int count = (this.dialogueWordFrequency.containsKey(word))
            ? this.dialogueWordFrequency.get(word) : 0;
        count += fragment.getWordFrequency(word);
        this.dialogueWordFrequency.put(word, count);
      });
    });
    this.narrativeFragments.stream().forEach( fragment -> {
      Set<Word> uniqueWords = fragment.getUniqueWords();
      uniqueWords.stream().forEach( word -> {
        int count = (this.narrativeWordFrequency.containsKey(word))
            ? this.narrativeWordFrequency.get(word) : 0;
        count += fragment.getWordFrequency(word);
        this.narrativeWordFrequency.put(word, count);
      });
    });
  }

  /**
   * Returns a new Prose object from a string.
   *
   * <p>Prose is currently considered the top level WordContainer. The String is not validated
   * as it is assumed to be an arbitrary block of text representing some kind of story.
   * 
   * @param text    a string of text representing a complete work of prose fiction
   */
  public Prose(final String text, final Dictionary2 dictionary) {
    super(text, dictionary);
    Matcher paragraphMatcher = WordContainer.getParagraphPattern().matcher(text);
    while (paragraphMatcher.find()) {
      Paragraph paragraph = new Paragraph(paragraphMatcher.group(), WordContainer.getDictionary());
      this.paragraphs.add(paragraph);
    }
    final Pattern dialoguePattern = WordContainer.getDialoguePattern();
    Matcher dialogueMatcher = dialoguePattern.matcher(
        WordContainer.convertSmartQuotes(this.getInitialText())
    );
    while (dialogueMatcher.find()) {
      this.dialogueFragments.add(
          new DialogueFragment(dialogueMatcher.group(), WordContainer.getDictionary())
      );
    }
    for (String narrative: dialoguePattern.split(
        WordContainer.convertSmartQuotes(this.getInitialText()))) {
      this.narrativeFragments.add(
          new NarrativeFragment(narrative, WordContainer.getDictionary())
      );
    }
    this.dialogueSyllableCount = this.dialogueFragments.stream()
        .mapToInt( fragment -> fragment.getSyllableCount())
        .sum();
    this.dialogueWordCount = this.dialogueFragments.stream()
        .mapToInt( fragment -> fragment.getWordCount())
        .sum();
    this.dialogueFirstPersonWordCount = this.dialogueFragments.stream()
        .mapToInt( fragment -> fragment.getFirstPersonWordCount())
        .sum();
    this.dialogueSecondPersonWordCount = this.dialogueFragments.stream()
        .mapToInt( fragment -> fragment.getSecondPersonWordCount())
        .sum();
    this.dialogueThirdPersonWordCount = this.dialogueFragments.stream()
        .mapToInt( fragment -> fragment.getThirdPersonWordCount())
        .sum();
    this.narrativeSyllableCount = this.narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getSyllableCount())
        .sum();
    this.narrativeWordCount = this.narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getWordCount())
        .sum();
    this.narrativeFirstPersonWordCount = this.narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getFirstPersonWordCount())
        .sum();
    this.narrativeSecondPersonWordCount = this.narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getSecondPersonWordCount())
        .sum();
    this.narrativeThirdPersonWordCount = this.narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getThirdPersonWordCount())
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
    this.paragraphs.stream().forEach( paragraph -> {
      Set<Word> uniqueWords = paragraph.getUniqueWords();
      uniqueWords.stream().forEach( word -> {
        int count = (this.wordFrequency.containsKey(word))
            ? this.wordFrequency.get(word) : 0;
        count += paragraph.getWordFrequency(word);
        this.wordFrequency.put(word, count);
      });
    });
    this.dialogueFragments.stream().forEach( fragment -> {
      Set<Word> uniqueWords = fragment.getUniqueWords();
      uniqueWords.stream().forEach( word -> {
        int count = (this.dialogueWordFrequency.containsKey(word))
            ? this.dialogueWordFrequency.get(word) : 0;
        count += fragment.getWordFrequency(word);
        this.dialogueWordFrequency.put(word, count);
      });
    });
    this.narrativeFragments.stream().forEach( fragment -> {
      Set<Word> uniqueWords = fragment.getUniqueWords();
      uniqueWords.stream().forEach( word -> {
        int count = (this.narrativeWordFrequency.containsKey(word))
            ? this.narrativeWordFrequency.get(word) : 0;
        count += fragment.getWordFrequency(word);
        this.narrativeWordFrequency.put(word, count);
      });
    });
  }

  public final List<DialogueFragment> getDialogueFragments() {
    return this.dialogueFragments;
  }

  public final Integer getDialogueWordCount() {
    return this.dialogueWordCount;
  }

  public final Integer getDialogueSyllableCount() {
    return this.dialogueSyllableCount;
  }

  public final List<NarrativeFragment> getNarrativeFragments() {
    return this.narrativeFragments;
  }

  public final Integer getNarrativeWordCount() {
    return this.narrativeWordCount;
  }

  public final Integer getNarrativeSyllableCount() {
    return this.narrativeSyllableCount;
  }

  /**
   * Returns the Point of View of the prose as an PovType, as determined
   * by the number of PoV words found in the narrative. Dialogue is,
   * by nature, always in first person.
   *
   * @return the Point of View of the prose as an PovType.
   *
   * @see com.prosegrinder.bookworm.enums.PovType
   *
   */
  public final PovType getPov() {
    if (this.getNarrativeFirstPersonWordCount() > 0) {
      return PovType.FIRST;
    } else if (this.getNarrativeSecondPersonWordCount() > 0) {
      return PovType.SECOND;
    } else if (this.getNarrativeThirdPersonWordCount() > 0) {
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

  /**
   * Returns a list of all Sentences found in the Paragraph.
   *
   * @return a list of all Sentences found in the Paragraph.
   *
   */
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

  @Override
  public final List<Word> getWords() {
    List<Word> words = new ArrayList<Word>();
    this.getSentences().stream().forEach( sentence -> {
      words.addAll(sentence.getWords());
    });
    return words;
  }

  @Override
  public final Map<Word, Integer> getWordFrequency() {
    return this.wordFrequency;
  }

 public final Map<Word, Integer> getDialogueWordFrequency() {
    return this.dialogueWordFrequency;
  }

  public final Integer getDialogueWordFrequency(Word word) {
    if (this.dialogueWordFrequency.containsKey(word)) {
      return this.dialogueWordFrequency.get(word);
    } else {
      return 0;
    }
  }
  
  public final Integer getDialogueFirstPersonWordCount() {
    return this.dialogueFirstPersonWordCount;
  }
  
  public final Integer getDialogueSecondPersonWordCount() {
    return this.dialogueSecondPersonWordCount;
  }
  
  public final Integer getDialogueThirdPersonWordCount() {
    return this.dialogueThirdPersonWordCount;
  }

  public final Map<Word, Integer> getNarrativeWordFrequency() {
    return this.narrativeWordFrequency;
  }

  public final Integer getNarrativeWordFrequency(Word word) {
    if (this.narrativeWordFrequency.containsKey(word)) {
      return this.narrativeWordFrequency.get(word);
    } else {
      return 0;
    }
  }
  
  public final Integer getNarrativeFirstPersonWordCount() {
    return this.narrativeFirstPersonWordCount;
  }
  
  public final Integer getNarrativeSecondPersonWordCount() {
    return this.narrativeSecondPersonWordCount;
  }
  
  public final Integer getNarrativeThirdPersonWordCount() {
    return this.narrativeThirdPersonWordCount;
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
