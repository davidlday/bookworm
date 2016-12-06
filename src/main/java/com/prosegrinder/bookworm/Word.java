package com.prosegrinder.bookworm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Word extends StoryFragment {

  public static final int MIN_SYLLABLES_COMPLEX_WORD = 4;
  public static final int MIN_CHARS_LONG_WORD = 7;

  private final Boolean isComplexWord;
  private final Boolean isLongWord;
  private final Boolean isNumeric;
  private final Integer characterCount;
  private final Integer syllableCount;

  public Word(String text) {
    super(text);
    SyllableDictionary sd = SyllableDictionary.getInstance();
    this.syllableCount = sd.getSyllableCount(this.getNormalizedText());
    this.characterCount = this.getNormalizedText().length();
    if (this.syllableCount >= MIN_SYLLABLES_COMPLEX_WORD) {
      /**
       * TODO: Implement full logic.
       *
       * <p>Complex words are:
       * 1) Those with three or more syllables.
       * 2) Do not include proper nouns, familiar jargon, or compound words.
       * 3) Do not include common suffixes (such as -es, -ed, or -ing) as a syllable.
       *
       * See: https://en.wikipedia.org/wiki/Gunning_fog_index
       * See: http://www.readabilityformulas.com/gunning-fog-readability-formula.php
       **/
      this.isComplexWord = true;
    } else {
      this.isComplexWord = false;
    }
    if (this.characterCount >= MIN_CHARS_LONG_WORD) {
      this.isLongWord = true;
    } else {
      this.isLongWord = false;
    }
    this.isNumeric = sd.isNumeric(this.getNormalizedText());
  }

  public Boolean isComplexWord() {
    return this.isComplexWord;
  }

  public Boolean isLongWord() {
    return this.isLongWord;
  }

  public Boolean isNumericWord() {
    return this.isNumeric;
  }

  public Integer getSyllableCount() {
    return this.syllableCount;
  }

  public Integer getWordCount() {
    return 1;
  }

  public Integer getComplexWordCount() {
    if (this.isComplexWord()) {
      return 1;
    } else {
      return 0;
    }
  }

  public Integer getLongWordCount() {
    if (this.isLongWord()) {
      return 1;
    } else {
      return 0;
    }
  }

  public final Integer getWordCharacterCount() {
    return this.characterCount;
  }

}
