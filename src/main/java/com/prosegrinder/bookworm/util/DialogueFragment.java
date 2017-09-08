package com.prosegrinder.bookworm.util;

import java.util.regex.Pattern;

/**
 * Represents a fragment of dialogue.
 * 
 * <p>In fiction, dialogue and narrative can be interspersed with each other, 
 * meaning they may or may not be found in complete sentences.
 */
public final class DialogueFragment extends WordContainer {

  /**
   * Variation on http://www.metaltoad.com/blog/regex-quoted-string-escapable-quotes
   * Only need double quotes when searching out dialogue in fiction.
   */
  private static final Pattern DIALOGUE_PATTERN = Pattern.compile(
      "[\"]((?:.(?![\"]))*.?)[\"\\n]"
  );
  
  /**
   * Returns a new DialogueFragment from a string.
   *
   * <p>Dialogue slices out independently of sentences and paragraphs. A
   * single sentence may contain a mix of dialogue and narrative, or a chunk of dialogue
   * may span multiple paragraphs. The only place dialogue can effectively be parsed out
   * is from the entirety of the original Prose.
   *
   * <p>String is not currently validated since DialogueFragments should
   * only be created by a Prose object using WordContainer.DIALOGUE_PATTERN.
   * 
   * @param text    a string of text representing a piece of dialogue
   * @param dictionary  dictionary used for word reference (cache)
   */
  public DialogueFragment(final String text, Dictionary2 dictionary) {
    super(text, dictionary);
  }

  public static final Pattern getPattern() {
    return DialogueFragment.DIALOGUE_PATTERN;
  }

}
