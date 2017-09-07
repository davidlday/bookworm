package com.prosegrinder.bookworm.util;

/**
 * Represents a fragment of narrative (i.e. not dialogue).
 * 
 * <p>
 * In fiction, dialogue and narrative can be interspersed with each other, meaning they may or may
 * not be found in complete sentences.
 */
public final class NarrativeFragment extends WordContainer {

  /**
   * Returns a new NarrativeFragment from a string.
   *
   * <p>
   * As with Dialogue, Narrative slices out independently of sentences and paragraphs. A single
   * sentence may contain a mix of dialogue and narrative, or a chunk of dialogue may span multiple
   * paragraphs. The only place narrative can effectively be parsed out is from the entirety of the
   * original Prose.
   *
   * <p>
   * String is not currently validated since NarrativeFragment should only be created by a Prose
   * object splitting its underlying text using WordContainer.DIALOGUE_PATTERN.
   * 
   * @param text a string of text representing a piece of narrative
   */
  @Deprecated
  public NarrativeFragment(final String text) {
    this(text, Dictionary2.getDefaultDictionary());
  }

  /**
   * Returns a new NarrativeFragment from a string.
   *
   * <p>
   * As with Dialogue, Narrative slices out independently of sentences and paragraphs. A single
   * sentence may contain a mix of dialogue and narrative, or a chunk of dialogue may span multiple
   * paragraphs. The only place narrative can effectively be parsed out is from the entirety of the
   * original Prose.
   *
   * <p>
   * String is not currently validated since NarrativeFragment should only be created by a Prose
   * object splitting its underlying text using WordContainer.DIALOGUE_PATTERN.
   * 
   * @param text a string of text representing a piece of narrative
   * @param dictionary dictionary used for word reference (cache)
   */
  public NarrativeFragment(final String text, Dictionary2 dictionary) {
    super(text, dictionary);
  }

}
