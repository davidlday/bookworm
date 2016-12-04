package com.prosegrinder.bookworm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StoryFragment {

  /** Patterns used throughout. **/
  public static final Pattern PARAGRAPH_PATTERN = Pattern.compile(
      ".*(?=\\n|$)"
  );
  // http://stackoverflow.com/questions/5553410/regular-expression-match-a-sentence#5553924
  private static final Pattern SENTENCE_PATTERN = Pattern.compile(
      "# Match a sentence ending in punctuation or EOS.\n" +
      "[^.!?\\s]    # First char is non-punct, non-ws\n" +
      "[^.!?]*      # Greedily consume up to punctuation.\n" +
      "(?:          # Group for unrolling the loop.\n" +
      "  [.!?]      # (special) inner punctuation ok if\n" +
      "  (?!['\"]?\\s|$)  # not followed by ws or EOS.\n" +
      "  [^.!?]*    # Greedily consume up to punctuation.\n" +
      ")*           # Zero or more (special normal*)\n" +
      "[.!?]?       # Optional ending punctuation.\n" +
      "['\"]?       # Optional closing quote.\n" +
      "(?=\\s|$)",
      Pattern.MULTILINE | Pattern.COMMENTS);
  private static final Pattern WORD_PATTERN = Pattern.compile(
      "[\\w’'-]+"
  );

  private final String initialText;
  private final String normalizedText;

  public StoryFragment(String text) {
    this.initialText = text;
    this.normalizedText = this.normalizeText(text);
  }

  public static final String normalizeText(String text) {
    return text.trim().toLowerCase();
  }

  public static final Pattern getWordPattern() {
    return StoryFragment.WORD_PATTERN;
  }

  public static final Pattern getSentencePattern() {
    return StoryFragment.SENTENCE_PATTERN;
  }

  public static final Pattern getParagraphPattern() {
    return StoryFragment.PARAGRAPH_PATTERN;
  }

  public final String toString() {
    return this.getInitialText();
  }

  public String getNormalizedText() {
    return this.normalizedText;
  }

  public String getInitialText() {
    return this.normalizedText;
  }

  public abstract Integer getWordCount();

}