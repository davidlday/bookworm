package com.prosegrinder.bookworm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Sentence extends StoryFragment {

  public static final String RE_SMART_QUOTES = new String("[“”]");

  private final List<Word> words = new ArrayList<Word>();
  private final List<DialogueFragment> dialogueFragments
      = new ArrayList<DialogueFragment>();
  private final Integer wordCharacterCount;
  private final Integer wordCount;
  private final Integer complexWordCount;
  private final Integer longWordCount;

  public Sentence(String text) {
    super(text);
    int wordcharcnt = 0;
    int wordcnt = 0;
    int cwordcnt = 0;
    int lwordcnt = 0;
    int firstpovcnt = 0;
    int secondpovcnt = 0;
    int thirdpovcnt = 0;
    Matcher dialogueMatcher = this.getDialoguePattern().matcher(
        this.getInitialText().replaceAll(this.RE_SMART_QUOTES, "\"")
    );
    while (dialogueMatcher.find()) {
      this.dialogueFragments.add(
          new DialogueFragment(dialogueMatcher.group())
      );
    }
    Matcher wordMatcher = Word.getWordPattern().matcher(text);
    while (wordMatcher.find()) {
      Word word = new Word(wordMatcher.group());
      this.words.add(word);
      wordcharcnt += word.getWordCharacterCount();
      wordcnt += word.getWordCount();
      if (word.isComplexWord()) {
        cwordcnt += 1;
      }
      if (word.isLongWord()) {
        lwordcnt += 1;
      }
    }
    this.wordCharacterCount = wordcharcnt;
    this.wordCount = wordcnt;
    this.complexWordCount = cwordcnt;
    this.longWordCount = lwordcnt;
  }

  public static final Pattern getPattern() {
    return StoryFragment.getSentencePattern();
  }

  public final List<Word> getWords() {
    return this.words;
  }

  public final Integer getWordCount() {
    return this.wordCount;
  }

  public final List<DialogueFragment> getDialogueFragments() {
    return this.dialogueFragments;
  }

  public final Integer getComplexWordCount() {
    return this.complexWordCount;
  }

  public final Integer getLongWordCount() {
    return this.longWordCount;
  }

  public final Integer getWordCharacterCount() {
    return this.wordCharacterCount;
  }

}
