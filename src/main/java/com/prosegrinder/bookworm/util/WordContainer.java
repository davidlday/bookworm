package com.prosegrinder.bookworm.util;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The WordContainer abstract class represents some fragment of text found in a piece of
 * prose fiction. It also provides a central point for managing all common text analysis
 * patterns and processes and enforcing all subclasses implement a standard set of methods.
 *
 */
public abstract class WordContainer {

  /** Patterns used throughout. Will eventually move to a properties file. **/
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
      "# Match a sentence ending in punctuation or EOS.\n"
      + "[^.!?…\\s]    # First char is non-punct, non-ws\n"
      + "[^.!?…]*      # Greedily consume up to punctuation.\n"
      + "(?:          # Group for unrolling the loop.\n"
      + "  [.!?…]      # (special) inner punctuation ok if\n"
      + "  (?!['\")]?\\s|$)  # not followed by ws or EOS.\n"
      + "  [^.!?…]*    # Greedily consume up to punctuation.\n"
      + ")*           # Zero or more (special normal*)\n"
      + "[.!?…]       # Ending punctuation.\n"
      + "['\")]?       # Optional closing quote.\n"
      + "(?=\\s|$)",
      Pattern.MULTILINE | Pattern.COMMENTS);
  private static final Pattern WORD_PATTERN = Pattern.compile(
      "[\\w’']+"
  );

  private static final String RE_SMART_QUOTES = new String("[“”]");

  private final String initialText;
  private final String normalizedText;

  /**
   * Constructs a new WordContainer, ensuring copies of the text in initial and normalized
   * form are available for subsequent processing.
   *
   * @param text  String representing the fragment.
   *
   */
  public WordContainer(final String text) {
    this.initialText = text;
    this.normalizedText = this.normalizeText(text);
  }

  /**
   * Normalizes text for processing by trimming and converting to lower case.
   *
   * @params text  source text to analyze
   * @return a normalized representation of text
   *
   */
  public static final String normalizeText(final String text) {
    return text.trim().toLowerCase();
  }

  /**
   * Get the pattern used for finding words in text.
   *
   * @return pattern used for finding words in text
   *
   */
  public static final Pattern getWordPattern() {
    return WordContainer.WORD_PATTERN;
  }

  /**
   * Get the pattern used for finding sentences in text.
   *
   * @return pattern used for finding sentences in text
   *
   */
  public static final Pattern getSentencePattern() {
    return WordContainer.SENTENCE_PATTERN;
  }

  /**
   * Get the pattern used for finding paragraphs in text.
   *
   * @return pattern used for finding paragraphs in text
   *
   */
  public static final Pattern getParagraphPattern() {
    return WordContainer.PARAGRAPH_PATTERN;
  }

  /**
   * Get the pattern used for finding dialogue in text.
   *
   * @return pattern used for finding dialogue in text
   *
   */
  public static final Pattern getDialoguePattern() {
    return WordContainer.DIALOGUE_PATTERN;
  }

  /**
   * Converts left and right double quotation marks (“”) to
   * neutral quotation marks ("). These smart quotation marks
   * create more opportunity for error when trying to parse out
   * dialogue.
   *
   * @param text  source text to analyze
   * @return a copy of text using neutral quotation marks in place of smart quotation marks
   *
   */
  public static final String convertSmartQuotes(final String text) {
    return text.replaceAll(WordContainer.RE_SMART_QUOTES, "\"");
  }

  /**
   * Returns a String representation of the ProseFragement.
   *
   * @return a String representation of the WordContainer.
   *
   */
  public final String toString() {
    return this.getInitialText();
  }

  /**
   * Returns the normalized version of the text used to create the WordContainer.
   *
   * @return a normalized version the String representation of the WordContainer.
   *
   */
  public final String getNormalizedText() {
    return this.normalizedText;
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

  /**
   * Returns the number of word characters found in the WordContainer.
   *
   * @return the number of work characters in the WordContainer.
   *
   */
  public abstract Integer getWordCharacterCount();

  /**
   * Returns the number of syllables found in the WordContainer.
   *
   * @return the number of syllables in the WordContainer.
   *
   */
  public abstract Integer getSyllableCount();

  /**
   * Returns the number of Words found in the WordContainer.
   *
   * @return the number of Words in the WordContainer.
   * @see Word
   *
   */
  public abstract Integer getWordCount();

  /**
   * Returns the number of complex Words found in the WordContainer.
   *
   * @return the number of complex Words in the WordContainer.
   *
   */
  public abstract Integer getComplexWordCount();

  /**
   * Returns the number of long Words found in the WordContainer.
   *
   * @return the number of long Words in the WordContainer.
   *
   */
  public abstract Integer getLongWordCount();

  /**
   * Returns the number of Words representing first person point of view (POV)
   * found in the WordContainer.
   *
   * @return the number of Words representing first person POV in the WordContainer.
   *
   */
  public abstract Integer getFirstPersonWordCount();

  /**
   * Returns the number of Words representing second person point of view (POV)
   * found in the WordContainer.
   *
   * @return the number of Words representing second person POV in the WordContainer.
   *
   */
  public abstract Integer getSecondPersonWordCount();

  /**
   * Returns the number of Words representing third person point of view (POV)
   * found in the WordContainer.
   *
   * @return the number of Words representing third person POV in the WordContainer.
   *
   */
  public abstract Integer getThirdPersonWordCount();

  /**
   * Returns the number of Words representing any point of view (POV)
   * found in the WordContainer.
   *
   * @return the number of Words representing any POV in the WordContainer.
   *
   */
  public abstract Integer getPovWordCount();


  public abstract Set<Word> getUniqueWords();

  public abstract Map<Word, Integer> getWordFrequency();

  public abstract Integer getWordFrequency(Word word);

}
