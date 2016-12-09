package com.prosegrinder.bookworm;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Word extends StoryFragment {

  /** Magic numbers for determining complex and long words. **/
  public static final int MIN_SYLLABLES_COMPLEX_WORD = 4;
  public static final int MIN_CHARS_LONG_WORD = 7;

  /** Words indicating point of view. **/
  public static final Set<String> POV_FIRST =
      new HashSet<String>(Arrays.asList(
          "I", "I'm", "I'll", "I'd", "I've", "me", "mine", "myself",
          "we", "we're", "we'll", "we'd", "we've",
          "us", "ours", "ourselves"
      ));
  public static final Set<String> POV_SECOND =
      new HashSet<String>(Arrays.asList(
          "you", "you're", "you'll", "you'd", "you've", "yours", "yourself", "yourselves"
      ));
  public static final Set<String> POV_THIRD =
      new HashSet<String>(Arrays.asList(
          "he", "he's", "he'll", "he'd", "him", "his", "himself",
          "she", "she's", "she'll", "she'd", "her", "hers", "herself",
          "it", "it's", "it'll", "it'd", "itself",
          "they", "they're", "they'll", "they'd", "they've", "them", "theirs", "themselves"
      ));
  public static final Set<String> POV_ALL =
      new HashSet<String>(){{
          addAll(Parser.POV_FIRST);
          addAll(Parser.POV_SECOND);
          addAll(Parser.POV_THIRD);
      }};

  /** Private member variables. **/
  private final Boolean isComplexWord;
  private final Boolean isLongWord;
  private final Boolean isPovWord;
//   private final Boolean isFirstPerson;
//   private final Boolean isSecondPerson;
//   private final Boolean isThirdPerson;
  private final Boolean isNumeric;
  private final Integer wordCharacterCount;
  private final Integer syllableCount;

  public Word(String text) {
    super(text);
    SyllableDictionary sd = SyllableDictionary.getInstance();
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
       * See: https://en.wikipedia.org/wiki/Gunning_fog_index
       * See: http://www.readabilityformulas.com/gunning-fog-readability-formula.php
       **/
      this.isComplexWord = true;
    } else {
      this.isComplexWord = false;
    }
    if (this.wordCharacterCount >= MIN_CHARS_LONG_WORD) {
      this.isLongWord = true;
    } else {
      this.isLongWord = false;
    }
    this.isNumeric = sd.isNumeric(this.getNormalizedText());
    /** Figure out if the word represents point of view. **/
    if (Word.POV_ALL.contains(this.getNormalizedText())) {
      this.isPovWord = true;
    } else {
      this.isPovWord = false;
    }
  }

  public static final Pattern getPattern() {
    return StoryFragment.getWordPattern();
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

  public final Integer getSyllableCount() {
    return this.syllableCount;
  }

  public final Integer getWordCount() {
    return 1;
  }

  public final Integer getComplexWordCount() {
    if (this.isComplexWord()) {
      return 1;
    } else {
      return 0;
    }
  }

  public final Integer getLongWordCount() {
    if (this.isLongWord()) {
      return 1;
    } else {
      return 0;
    }
  }

  public final Integer getWordCharacterCount() {
    return this.wordCharacterCount;
  }

}
