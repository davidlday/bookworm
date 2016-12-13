package com.prosegrinder.bookworm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The ProseFragment abstract class represents some fragment of text found in a piece of
 * prose fiction. It also provides a central point for managing all common text analysis
 * patterns and processes and enforcing all subclasses implement a standard set of methods.
 *
 */
public abstract class ProseFragment {

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
      + "[^.!?\\s]    # First char is non-punct, non-ws\n"
      + "[^.!?]*      # Greedily consume up to punctuation.\n"
      + "(?:          # Group for unrolling the loop.\n"
      + "  [.!?]      # (special) inner punctuation ok if\n"
      + "  (?!['\"]?\\s|$)  # not followed by ws or EOS.\n"
      + "  [^.!?]*    # Greedily consume up to punctuation.\n"
      + ")*           # Zero or more (special normal*)\n"
      + "[.!?]?       # Optional ending punctuation.\n"
      + "['\"]?       # Optional closing quote.\n"
      + "(?=\\s|$)",
      Pattern.MULTILINE | Pattern.COMMENTS);
  private static final Pattern WORD_PATTERN = Pattern.compile(
      "[\\w’'-]+"
  );

  private static final String RE_SMART_QUOTES = new String("[“”]");

  private final String initialText;
  private final String normalizedText;

  /**
   * Constructs a new ProseFragment, ensuring copies of the text in initial and normalized
   * form are available for subsequent processing.
   *
   * @param text  String representing the fragment.
   *
   */
  public ProseFragment(final String text) {
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
    return ProseFragment.WORD_PATTERN;
  }

  /**
   * Get the pattern used for finding sentences in text.
   *
   * @return pattern used for finding sentences in text
   *
   */
  public static final Pattern getSentencePattern() {
    return ProseFragment.SENTENCE_PATTERN;
  }

  /**
   * Get the pattern used for finding paragraphs in text.
   *
   * @return pattern used for finding paragraphs in text
   *
   */
  public static final Pattern getParagraphPattern() {
    return ProseFragment.PARAGRAPH_PATTERN;
  }

  /**
   * Get the pattern used for finding dialogue in text.
   *
   * @return pattern used for finding dialogue in text
   *
   */
  public static final Pattern getDialoguePattern() {
    return ProseFragment.DIALOGUE_PATTERN;
  }

//   public static final Pattern getDelimitedPattern(String delimiter) {
//     return Pattern.compile(
//         ".*(?:" + delimiter + "|$)"
//     );
//   }

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
    return text.replaceAll(ProseFragment.RE_SMART_QUOTES, "\"");
  }

  /**
   * Static method for building word frequency from a list of fragments.
   *
   * @param a list of ProseFragments.
   * @return a map of Word with counts.
   *
   */
  public static final Map<Word, Integer> getWordFrequency(List<ProseFragment> fragments) {
    Map<Word, Integer> wordFrequency = new HashMap<Word, Integer>();
    fragments.stream().forEach( fragment -> {
      Set<Word> uniqueWords = fragment.getUniqueWords();
      uniqueWords.stream().forEach( word -> {
        int count = (wordFrequency.containsKey(word)) ?
            wordFrequency.get(word) : 0;
        count += fragment.getWordFrequency(word);
        wordFrequency.put(word, count);
      });
    });
    return wordFrequency;
  }

  /**
   * Returns a String representation of the ProseFragement.
   *
   * @return a String representation of the ProseFragment.
   *
   */
  public final String toString() {
    return this.getInitialText();
  }

  /**
   * Returns the normalized version of the text used to create the ProseFragment.
   *
   * @return a normalized version the String representation of the ProseFragment.
   *
   */
  public final String getNormalizedText() {
    return this.normalizedText;
  }

  /**
   * Returns the initial text used to create the ProseFragment.
   *
   * @return the String used to create the ProseFragment.
   *
   */
  public final String getInitialText() {
    return this.initialText;
  }

  /**
   * Returns the number of word characters found in the ProseFragment.
   *
   * @return the number of work characters in the ProseFragment.
   *
   */
  public abstract Integer getWordCharacterCount();

  /**
   * Returns the number of syllables found in the ProseFragment.
   *
   * @return the number of syllables in the ProseFragment.
   *
   */
  public abstract Integer getSyllableCount();

  /**
   * Returns the number of Words found in the ProseFragment.
   *
   * @return the number of Words in the ProseFragment.
   * @see Word
   *
   */
  public abstract Integer getWordCount();

  /**
   * Returns the number of complex Words found in the ProseFragment.
   *
   * @return the number of complex Words in the ProseFragment.
   *
   */
  public abstract Integer getComplexWordCount();

  /**
   * Returns the number of long Words found in the ProseFragment.
   *
   * @return the number of long Words in the ProseFragment.
   *
   */
  public abstract Integer getLongWordCount();

  /**
   * Returns the number of Words representing first person point of view (POV)
   * found in the ProseFragment.
   *
   * @return the number of Words representing first person POV in the ProseFragment.
   *
   */
  public abstract Integer getFirstPersonWordCount();

  /**
   * Returns the number of Words representing second person point of view (POV)
   * found in the ProseFragment.
   *
   * @return the number of Words representing second person POV in the ProseFragment.
   *
   */
  public abstract Integer getSecondPersonWordCount();

  /**
   * Returns the number of Words representing third person point of view (POV)
   * found in the ProseFragment.
   *
   * @return the number of Words representing third person POV in the ProseFragment.
   *
   */
  public abstract Integer getThirdPersonWordCount();

  /**
   * Returns the number of Words representing any point of view (POV)
   * found in the ProseFragment.
   *
   * @return the number of Words representing any POV in the ProseFragment.
   *
   */
  public abstract Integer getPovWordCount();


  public abstract Set<Word> getUniqueWords();

  public abstract Map<Word, Integer> getWordFrequency();

  public abstract Integer getWordFrequency(Word word);

}
