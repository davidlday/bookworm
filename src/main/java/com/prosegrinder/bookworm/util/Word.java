package com.prosegrinder.bookworm.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Word extends ProseFragment {

  /** Magic number for determining complex words. **/
  public static final int MIN_SYLLABLES_COMPLEX_WORD = 3;
  /** Magic number for determining long words. **/
  public static final int MIN_CHARS_LONG_WORD = 7;

  /** Words indicating first person point of view. **/
  public static final Set<String> POV_FIRST =
      new HashSet<String>(Arrays.asList(
          "I", "I'm", "I'll", "I'd", "I've", "me", "mine", "myself",
          "we", "we're", "we'll", "we'd", "we've",
          "us", "ours", "ourselves"
      ));
  /** Words indicating second person point of view. **/
  public static final Set<String> POV_SECOND =
      new HashSet<String>(Arrays.asList(
          "you", "you're", "you'll", "you'd", "you've", "yours", "yourself", "yourselves"
      ));
  /** Words indicating third person point of view. **/
  public static final Set<String> POV_THIRD =
      new HashSet<String>(Arrays.asList(
          "he", "he's", "he'll", "he'd", "him", "his", "himself",
          "she", "she's", "she'll", "she'd", "her", "hers", "herself",
          "it", "it's", "it'll", "it'd", "itself",
          "they", "they're", "they'll", "they'd", "they've", "them", "theirs", "themselves"
      ));

  /** Private member variables. **/
  private final Boolean isComplexWord;
  private final Boolean isLongWord;
  private final Boolean isPovWord;
  private final Boolean isFirstPersonWord;
  private final Boolean isSecondPersonWord;
  private final Boolean isThirdPersonWord;
  private final Boolean isNumeric;
  private final Integer wordCharacterCount;
  private final Integer syllableCount;

  public Word(final String text) {
    super(text.trim()); /** For words, we don't care about surrounding space ever. **/
    final SyllableDictionary sd = SyllableDictionary.getInstance();
    this.syllableCount = sd.getSyllableCount(this.getNormalizedText());
    this.wordCharacterCount = this.getNormalizedText().length();
    if (this.syllableCount >= MIN_SYLLABLES_COMPLEX_WORD) {
      /**
       * TODO: Implement full logic.
       *
       * <p>Complex words are:
       * 1) Those with three or more syllables.
       * 2) Do not include proper nouns, familiar jargon, or compound words.
       * 3) Do not include common suffixes (such as -es, -ed, or -ing) as a syllable.
       *
       * <p>See:
       * - https://en.wikipedia.org/wiki/Gunning_fog_index
       * - http://www.readabilityformulas.com/gunning-fog-readability-formula.php
       **/
      this.isComplexWord = true;
    } else {
      this.isComplexWord = false;
    }
    this.isLongWord = (this.wordCharacterCount >= MIN_CHARS_LONG_WORD) ? true : false;
    this.isNumeric = sd.isNumeric(this.getNormalizedText());
    /** Figure out if the word indicates a point of view. **/
    this.isFirstPersonWord = Word.POV_FIRST.contains(this.getNormalizedText());
    this.isSecondPersonWord = Word.POV_SECOND.contains(this.getNormalizedText());
    this.isThirdPersonWord = Word.POV_THIRD.contains(this.getNormalizedText());
    this.isPovWord = (
        this.isFirstPersonWord
        || this.isSecondPersonWord
        || this.isThirdPersonWord
      );
  }

  public static final Pattern getPattern() {
    return ProseFragment.getWordPattern();
  }

  public final Boolean isComplexWord() {
    return this.isComplexWord;
  }

  public final Boolean isLongWord() {
    return this.isLongWord;
  }

  public final Boolean isNumericWord() {
    return this.isNumeric;
  }

  public final Boolean isFirstPersonWord() {
    return this.isFirstPersonWord;
  }

  public final Boolean isSecondPersonWord() {
    return this.isSecondPersonWord;
  }

  public final Boolean isThirdPersonWord() {
    return this.isThirdPersonWord;
  }

  public final Boolean isPovWord() {
    return this.isPovWord;
  }

  @Override
  public final Integer getSyllableCount() {
    return this.syllableCount;
  }

  @Override
  public final Integer getWordCharacterCount() {
    return this.wordCharacterCount;
  }

  @Override
  public final Integer getWordCount() {
    return 1;
  }

  @Override
  public final Integer getComplexWordCount() {
    return (this.isComplexWord()) ? 1 : 0;
  }

  @Override
  public final Integer getLongWordCount() {
    return (this.isLongWord()) ? 1 : 0;
  }

  @Override
  public final Integer getFirstPersonWordCount() {
    return (this.isFirstPersonWord()) ? 1 : 0;
  }

  @Override
  public final Integer getSecondPersonWordCount() {
    return (this.isSecondPersonWord()) ? 1 : 0;
  }

  @Override
  public final Integer getThirdPersonWordCount() {
    return (this.isThirdPersonWord()) ? 1 : 0;
  }

  @Override
  public final Integer getPovWordCount() {
    return (this.isPovWord()) ? 1 : 0;
  }

}
