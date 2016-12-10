package com.prosegrinder.bookworm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StoryFragment {

  /** Patterns used throughout. Will eventually move to a properties file. **/
  private static final String CHAPTER_DELIMITER = "^\\s*Chapter\\s\\d+\\s*\\n";
//   private static final Pattern CHAPTER_PATTERN = Pattern.compile(
//       ".*(?:" + CHAPTER_DELIMITER + "|$)"
//   );
  private static final String SCENE_DELIMITER = "^\\s#\\s*\\n";
//   private static final Pattern SCENE_PATTERN = Pattern.compile(
//       ".*(?:" + SCENE_DELIMITER + "|$)"
//   );
  /**
   * Variation on http://www.metaltoad.com/blog/regex-quoted-string-escapable-quotes
   * Only need double quotes when searching out dialogue in fiction.
   */
  private static final Pattern DIALOGUE_PATTERN = Pattern.compile(
      "[\"]((?:.(?![\"]))*.?)[\"\\n]"
  );
  private static final Pattern PARAGRAPH_PATTERN = Pattern.compile(
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

  public static final Pattern getDialoguePattern() {
    return StoryFragment.DIALOGUE_PATTERN;
  }

  public static final String getSceneDelimiter() {
    return StoryFragment.SCENE_DELIMITER;
  }

  public static final Pattern getScenePattern() {
    return StoryFragment.getScenePattern(
      StoryFragment.getSceneDelimiter()
    );
  }

  public static final Pattern getScenePattern(String delimiter) {
    /** TODO: Scenes and Chapters are delimited differently. **/
    return StoryFragment.getDelimitedPattern(delimiter);
  }

  public static final String getChapterDelimiter() {
    return StoryFragment.CHAPTER_DELIMITER;
  }

  public static final Pattern getChapterPattern() {
    return StoryFragment.getChapterPattern(
      StoryFragment.getChapterDelimiter()
    );
  }

  public static final Pattern getChapterPattern(String delimiter) {
    /** TODO: Scenes and Chapters are delimited differently. **/
    return StoryFragment.getDelimitedPattern(delimiter);
  }


  public static final Pattern getDelimitedPattern(String delimiter) {
    return Pattern.compile(
        ".*(?:" + delimiter + "|$)"
    );
  }

  public final String toString() {
    return this.getInitialText();
  }

  public final String getNormalizedText() {
    return this.normalizedText;
  }

  public final String getInitialText() {
    return this.normalizedText;
  }

  public abstract Integer getWordCharacterCount();

  public abstract Integer getSyllableCount();

  public abstract Integer getWordCount();

  public abstract Integer getComplexWordCount();

  public abstract Integer getLongWordCount();

  public abstract Integer getFirstPersonWordCount();

  public abstract Integer getSecondPersonWordCount();

  public abstract Integer getThirdPersonWordCount();

  public abstract Integer getPovWordCount();

}
