package com.prosegrinder.bookworm.util;

import java.util.regex.Pattern;

/**
 * A Sentence.
 */
public final class Sentence extends WordContainer {

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
  
  /**
   * Returns a new Sentence from a string.
   *
   * <p>String is not currently validates since Sentences should
   * only be created by a Paragraph using WordContainer.SENTENCE_PATTERN.
   * 
   * @param text    a string of text representing a complete sentence
   */
  @Deprecated
  public Sentence(final String text) {
    this(text, Dictionary2.getDefaultDictionary());
  }

  /**
   * Returns a new Sentence from a string.
   *
   * <p>String is not currently validates since Sentences should
   * only be created by a Paragraph using WordContainer.SENTENCE_PATTERN.
   * 
   * @param text    a string of text representing a complete sentence
   * @param dictionary  dictionary used for word reference (cache)
   */
  public Sentence(final String text, Dictionary2 dictionary) {
    super(text, dictionary);
  }

  public static final Pattern getPattern() {
    return Sentence.SENTENCE_PATTERN;
  }


}
