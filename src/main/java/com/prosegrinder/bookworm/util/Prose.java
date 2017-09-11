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
public final class Prose extends AggregateContainer {

  private final List<Paragraph> paragraphs = new ArrayList<Paragraph>();
  private final List<DialogueFragment> dialogueFragments
      = new ArrayList<DialogueFragment>();
  private final Map<Word, Integer> dialogueWordFrequency = new HashMap<Word, Integer>();
  private final List<NarrativeFragment> narrativeFragments
      = new ArrayList<NarrativeFragment>();
  private final Map<Word, Integer> narrativeWordFrequency = new HashMap<Word, Integer>();
  private final Map<Word, Integer> povWordFrequency = new HashMap<Word, Integer>();
  
  private final Map<Word, Integer> firstPersonIndicatorFrequency = new HashMap<Word, Integer>();
  private final Map<Word, Integer> secondPersonIndicatorFrequency = new HashMap<Word, Integer>();
  private final Map<Word, Integer> thirdPersonIndicatorFrequency = new HashMap<Word, Integer>();
  private final Map<Word, Integer> povIndicatorFrequency = new HashMap<Word, Integer>();
  
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
  
  private final Integer povIndicatorCount;
  private final Integer firstPersonIndicatorCount;
  private final Integer secondPersonIndicatorCount;
  private final Integer thirdPersonIndicatorCount;

  /**
   * Returns a new Prose object from a string.
   *
   * <p>Prose is currently considered the top level WordContainer. The String is not validated
   * as it is assumed to be an arbitrary block of text representing some kind of story.
   * 
   * @param text    a string of text representing a complete work of prose fiction
   * @param dictionary  dictionary used for word reference (cache)
   */
  public Prose(final String text, final Dictionary2 dictionary) {
    
    this.setText(text);
    this.setDictionary(dictionary);
    Matcher paragraphMatcher = Paragraph.getPattern().matcher(text);
    while (paragraphMatcher.find()) {
      Paragraph paragraph = new Paragraph(paragraphMatcher.group(), this.getDictionary());
      this.paragraphs.add(paragraph);
    }
    List<Container> containers = new ArrayList<Container>();
    paragraphs.stream().forEach( paragraph -> {
      containers.add((Container) paragraph);
    });
    this.aggregateContainers(containers);
    this.sentenceCount = this.paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getSentenceCount())
        .sum();
    this.paragraphCount = this.paragraphs.size();

    // Process Dialogue
    final Pattern dialoguePattern = DialogueFragment.getPattern();
    Matcher dialogueMatcher = dialoguePattern.matcher(
        WordContainer.convertSmartQuotes(this.getInitialText())
    );
    while (dialogueMatcher.find()) {
      this.dialogueFragments.add(
          new DialogueFragment(dialogueMatcher.group(), this.getDictionary())
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
    this.dialogueFragments.stream().forEach( fragment -> {
      Set<Word> uniqueWords = fragment.getUniqueWords();
      uniqueWords.stream().forEach( word -> {
        int count = (this.dialogueWordFrequency.containsKey(word))
            ? this.dialogueWordFrequency.get(word) : 0;
        count += fragment.getWordFrequency(word);
        this.dialogueWordFrequency.put(word, count);
      });
    });

    // Process Narrative
    for (String narrative: dialoguePattern.split(
        WordContainer.convertSmartQuotes(this.getInitialText()))) {
      this.narrativeFragments.add(
          new NarrativeFragment(narrative, this.getDictionary())
      );
    }
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
    this.narrativeFragments.stream().forEach( fragment -> {
      Set<Word> uniqueWords = fragment.getUniqueWords();
      uniqueWords.stream().forEach( word -> {
        int count = (this.narrativeWordFrequency.containsKey(word))
            ? this.narrativeWordFrequency.get(word) : 0;
        count += fragment.getWordFrequency(word);
        this.narrativeWordFrequency.put(word, count);
        // Populate povIndicatorFrequency as well.
        if (word.isPovWord()) {
          this.povIndicatorFrequency.put(word, count);
          if (word.isFirstPersonWord() ) {
            this.firstPersonIndicatorFrequency.put(word, count);
          } else if (word.isSecondPersonWord()) {
            this.secondPersonIndicatorFrequency.put(word, count);
          } else if (word.isThirdPersonWord()) {
            this.thirdPersonIndicatorFrequency.put(word, count);
          }
        }
      });
    });

    // PoV Indicators are PoV Words fount in Narrative.
    this.firstPersonIndicatorCount = this.narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getFirstPersonWordCount())
        .sum();
    this.secondPersonIndicatorCount = this.narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getSecondPersonWordCount())
        .sum();
    this.thirdPersonIndicatorCount= this.narrativeFragments.stream()
        .mapToInt( fragment -> fragment.getThirdPersonWordCount())
        .sum();
    this.povIndicatorCount = this.firstPersonIndicatorCount 
        + this.secondPersonIndicatorCount 
        + this.thirdPersonIndicatorCount;
  }

  public final Integer getSentenceCount() {
    return this.sentenceCount;
  }
  
  public final Double getAverageSentencesPerParagraph() {
    return (double) this.getSentenceCount() / (double) this.getParagraphCount();
  }

  public final Double getAverageSyllablesPerParagraph() {
    return (double) this.getSyllableCount() / (double) this.getParagraphCount();
  }

  public final Double getAverageSyllablesPerSentence() {
    return (double) this.getSyllableCount() / (double) this.getSentenceCount();
  }

  public final Double getAverageSyllablesPerWord() {
    return (double) this.getSyllableCount() / (double) this.getWordCount();
  }

  public final Double getAverageWordsPerParagraph() {
    return (double) this.getWordCount() / (double) this.getParagraphCount();
  }

  public final Double getAverageWordsPerSentence() {
    return (double) this.getWordCount() / (double) this.getSentenceCount();
  }

  public final Integer getDialogueFirstPersonWordCount() {
    return this.dialogueFirstPersonWordCount;
  }

  public final List<DialogueFragment> getDialogueFragments() {
    return this.dialogueFragments;
  }
  
  public final Integer getDialogueSecondPersonWordCount() {
    return this.dialogueSecondPersonWordCount;
  }
  
  public final Integer getDialogueSyllableCount() {
    return this.dialogueSyllableCount;
  }

  public final Integer getDialogueThirdPersonWordCount() {
    return this.dialogueThirdPersonWordCount;
  }

  public final Integer getDialogueWordCount() {
    return this.dialogueWordCount;
  }

  public final Map<Word, Integer> getDialogueWordFrequency() {
    return this.dialogueWordFrequency;
  }
  
  /**
   * Get the number of times a word appears in dialogue.
   * 
   * @param word word to check
   * @return the number of times word appears in text's dialogue
   */
  public final Integer getDialogueWordFrequency(Word word) {
    if (this.dialogueWordFrequency.containsKey(word)) {
      return this.dialogueWordFrequency.get(word);
    } else {
      return 0;
    }
  }
  
  public final Integer getFirstPersonIndicatorCount() {
    return this.firstPersonIndicatorCount;
  }

  public final Map<Word, Integer> getFirstPersonIndicatorFrequency() {
    return this.firstPersonIndicatorFrequency;
  }
  
  
  public final Map<Word, Integer> getFirstPersonWordFrequency() {
    return this.getWordFrequency(Word.POV_FIRST);
  }

  public final Integer getNarrativeFirstPersonWordCount() {
    return this.narrativeFirstPersonWordCount;
  }

  public final List<NarrativeFragment> getNarrativeFragments() {
    return this.narrativeFragments;
  }

  public final Integer getNarrativeSecondPersonWordCount() {
    return this.narrativeSecondPersonWordCount;
  }

  public final Integer getNarrativeSyllableCount() {
    return this.narrativeSyllableCount;
  }

  public final Integer getNarrativeThirdPersonWordCount() {
    return this.narrativeThirdPersonWordCount;
  }

  public final Integer getNarrativeWordCount() {
    return this.narrativeWordCount;
  }
  
  public final Map<Word, Integer> getNarrativeWordFrequency() {
    return this.narrativeWordFrequency;
  }
  
  /**
   * Get the number of times a word appears in narrative.
   * 
   * @param word word to check
   * @return the number of times word appears in text's narrative
   */
  public final Integer getNarrativeWordFrequency(Word word) {
    if (this.narrativeWordFrequency.containsKey(word)) {
      return this.narrativeWordFrequency.get(word);
    } else {
      return 0;
    }
  }
  
  public final Integer getParagraphCount() {
    return this.paragraphCount;
  }

  public final List<Paragraph> getParagraphs() {
    return this.paragraphs;
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
  
  public final Integer getPovIndicatorCount() {
    return this.povIndicatorCount;
  }
  
  public final Map<Word, Integer> getPovWordFrequency() {
    return this.povWordFrequency;
  }

  public final Integer getPovWordFrequency(Word word) {
    if (word.isPovWord() && this.povWordFrequency.containsKey(word)) {
      return this.povWordFrequency.get(word);
    } else {
      return 0;
    }
  }

  public final Integer getSecondPersonIndicatorCount() {
    return this.secondPersonIndicatorCount;
  }

  public final Map<Word, Integer> getSecondPersonIndicatorFrequency() {
    return this.secondPersonIndicatorFrequency;
  }
  
  public final Map<Word, Integer> getSecondPersonWordFrequency() {
    return this.getWordFrequency(Word.POV_SECOND);
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

  public final Integer getThirdPersonIndicatorCount() {
    return this.thirdPersonIndicatorCount;
  }

  public final Map<Word, Integer> getThirdPersonIndicatorFrequency() {
    return this.thirdPersonIndicatorFrequency;
  }
  
  public final Map<Word, Integer> getThirdPersonWordFrequency() {
    return this.getWordFrequency(Word.POV_THIRD);
  }

  public final List<Word> getWords() {
    List<Word> words = new ArrayList<Word>();
    this.getSentences().stream().forEach( sentence -> {
      words.addAll(sentence.getWords());
    });
    return words;
  }
 
}
