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
public final class Prose {

  private final List<Paragraph> paragraphs = new ArrayList<Paragraph>();
  private final Map<Word, Integer> wordFrequency = new HashMap<Word, Integer>();
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
   */
  @Deprecated
  public Prose(final String text) {
    this(text, Dictionary2.getDefaultDictionary());
  }

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
    this.initialText = text;
    this.normalizedText = WordContainer.normalizeText(text);
    this.dictionary = dictionary;
    Matcher paragraphMatcher = Paragraph.getPattern().matcher(text);
    while (paragraphMatcher.find()) {
      Paragraph paragraph = new Paragraph(paragraphMatcher.group(), this.getDictionary());
      this.paragraphs.add(paragraph);
    }
    final Pattern dialoguePattern = DialogueFragment.getPattern();
    Matcher dialogueMatcher = dialoguePattern.matcher(
        WordContainer.convertSmartQuotes(this.getInitialText())
    );
    while (dialogueMatcher.find()) {
      this.dialogueFragments.add(
          new DialogueFragment(dialogueMatcher.group(), this.getDictionary())
      );
    }
    for (String narrative: dialoguePattern.split(
        WordContainer.convertSmartQuotes(this.getInitialText()))) {
      this.narrativeFragments.add(
          new NarrativeFragment(narrative, this.getDictionary())
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
    this.firstPersonWordCount = this.paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getFirstPersonWordCount())
        .sum();
    this.secondPersonWordCount = this.paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getSecondPersonWordCount())
        .sum();
    this.thirdPersonWordCount = this.paragraphs.stream()
        .mapToInt( paragraph -> paragraph.getThirdPersonWordCount())
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

  public final Integer getComplexWordCount() {
    return this.complexWordCount;
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
  
  public final Integer getFirstPersonWordCount() {
    return this.firstPersonWordCount;
  }
  
  public final Map<Word, Integer> getFirstPersonWordFrequency() {
    return this.getWordFrequency(Word.POV_FIRST);
  }

  public final Integer getLongWordCount() {
    return this.longWordCount;
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
  
  public final Integer getPovWordCount() {
    return this.povWordCount;
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
  
  public final Integer getSecondPersonWordCount() {
    return this.secondPersonWordCount;
  }

  public final Map<Word, Integer> getSecondPersonWordFrequency() {
    return this.getWordFrequency(Word.POV_SECOND);
  }

  public final Integer getSentenceCount() {
    return this.sentenceCount;
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

  public final Integer getSyllableCount() {
    return this.syllableCount;
  }

  public final Integer getThirdPersonIndicatorCount() {
    return this.thirdPersonIndicatorCount;
  }

  public final Map<Word, Integer> getThirdPersonIndicatorFrequency() {
    return this.thirdPersonIndicatorFrequency;
  }
  
  public final Integer getThirdPersonWordCount() {
    return this.thirdPersonWordCount;
  }

  public final Map<Word, Integer> getThirdPersonWordFrequency() {
    return this.getWordFrequency(Word.POV_THIRD);
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

  // Convenience method for building out word frequency maps.
  public final Map<Word, Integer> getWordFrequency(Set<String> wordSet) {
    Map<Word, Integer> wordMap = new HashMap<Word, Integer>();
    for (String wordString: wordSet) {
      Word word = this.getDictionary().getWord(wordString);
      wordMap.put(word, this.getWordFrequency(word));
    }
    return wordMap;
  }
  
  /**
   * Returns the number of times a Word appears in the WordContainer.
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

  /**
   * Returns the initial text used to create the WordContainer.
   *
   * @return the String used to create the WordContainer.
   *
   */
  public final String getInitialText() {
    return this.initialText;
  }

  public final Dictionary2 getDictionary() {
    return this.dictionary;
  }

  public final String getNormalizedText() {
    return this.normalizedText;
  }

  /**
   * Returns a set of unique Words found in the Prose.
   *
   * @return a set of unique Words found in the WordContainer.
   *
   */
  public final Set<Word> getUniqueWords() {
    return this.getWordFrequency().keySet();
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
  
}
