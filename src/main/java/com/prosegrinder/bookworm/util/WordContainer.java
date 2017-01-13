package com.prosegrinder.bookworm.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

  private static final String RE_SMART_QUOTES = "[“”]";

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
   * Normalize text for processing by trimming and converting to lower case.
   *
   * @param text  source text to analyze
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
   * <p>The definition for Complex Word is unclear and difficult to implement.
   * Any calculations using Complex Word Count should be considered experimental.
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

  /**
   * Returns the count of unique Words found in the WordContainer.
   *
   * @return the count of unique Words found in the WordContainer.
   *
   */
  public final Integer getUniqueWordCount() {
    return this.getUniqueWords().size();
  }

  /**
   * Returns a set of unique Words found in the WordContainer
   *
   * @return a set of unique Words found in the WordContainer.
   *
   */
  public final Set<Word> getUniqueWords() {
    return this.getWordFrequency().keySet();
  }

  /**
   * Returns a map of unique Words found in the WordContainer
   * with a value of how many times that Word occurs in the underlying text.
   *
   * @return a map with Word as key and the number of times Word appears as value.
   *
   */
  public abstract Map<Word, Integer> getWordFrequency();

  /**
   * Returns the number of times a Word appears in the Wordcontainer.
   *
   * @return the number of times a Word appears in the Wordcontainer.
   *
   */
  public final Integer getWordFrequency(Word word) {
    return (this.getWordFrequency().containsKey(word))
        ? this.getWordFrequency().get(word)
        : 0;
  }

  /**
   * Returns a list of all Words found in the WordContainer.
   *
   * @return a list of all Words found in the WordContainer.
   *
   */
  public abstract List<Word> getWords();

}
