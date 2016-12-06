package com.prosegrinder.bookworm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Sentence extends StoryFragment {

  private final List<Word> words = new ArrayList<Word>();
  private final List<DialogueFragment> dialogueFragments
      = new ArrayList<DialogueFragment>();

  public Sentence(String text) {
    super(text);
    Matcher dialogueMatcher = this.getDialoguePattern().matcher(
        this.getInitialText().replaceAll(Parser.RE_SMART_QUOTES, "\"")
    );
    while (dialogueMatcher.find()) {
      this.dialogueFragments.add(
          new DialogueFragment(dialogueMatcher.group())
      );
    }
    Matcher wordMatcher = this.getWordPattern().matcher(text);
    while (wordMatcher.find()) {
      this.words.add(new Word(wordMatcher.group()));
    }
  }

  public final List<Word> getWords() {
    return this.words;
  }

  public final Integer getWordCount() {
    return this.words.size();
  }

  public final List<DialogueFragment> getDialogueFragments() {
    return this.dialogueFragments;
  }

  public final Integer getComplexWordCount() {
    int complexWordCount = 0;
    for (Word word: this.getWords()) {
      if (word.isComplexWord()) {
        complexWordCount += 1;
      }
    }
    return complexWordCount;
  }

  public final Integer getLongWordCount() {
    int longWordCount = 0;
    for (Word word: this.getWords()) {
      if (word.isLongWord()) {
        longWordCount += 1;
      }
    }
    return longWordCount;
  }

  public final Integer getWordCharacterCount() {
    int characterCount = 0;
    for (Word word: this.getWords()) {
      characterCount += word.getWordCharacterCount();
    }
    return characterCount;
  }

}
