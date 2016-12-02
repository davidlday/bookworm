package com.prosegrinder.bookworm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class Parser {
  // A Utility class for parsing prose text, specifically fiction.
  // All rules for parsing are derived from either industry practice or William Shunn's
  // "Proper Manuscript Formatting" site (//www.shunn.net/format/).
  // TODO: I don't need to use unmodifiable things everywhere, probably just
  // on the class variables. Even those are declared final, so probably not even there.

  // Regular Expression Strings
  public static final String RE_SMART_QUOTES = new String("[“”]");
  // public static final String RE_SMART_QUOTES = new String("[\u201c\u201d]");
  public static final String RE_WORDS = new String("[^\\w’\\']+");
  // public static final String RE_WORDS = new String("[^\\w\u2019\']+");
  public static final String RE_SENTENCES = new String("(?<=[.?!\"])\\s+(?=[\"A-Z])");
  public static final String RE_PARAGRAPHS = new String("\\n+");
  /** Variation on http://www.metaltoad.com/blog/regex-quoted-string-escapable-quotes **/
  /** Only need double quotes when searching out dialogue. **/
  public static final String RE_DIALOGUE =
      new String("((?<![\\\\])[\"])((?:.(?!(?<![\\\\])\\1))*.?)\\1");


  // TODO: Look into moving all these POV strings to an Enum
  public static final Set<String> POVS =
      new HashSet<String>(Arrays.asList("first", "second", "third"));
  public static final Set<String> POV_FIRST =
      new HashSet<String>(Arrays.asList("I", "I'm", "I'll", "I'd", "I've", "me", "mine", "myself",
          "we", "we're", "we'll", "we'd", "we've",
          "us", "ours", "ourselves"));
  public static final Set<String> POV_SECOND =
      new HashSet<String>(Arrays.asList(
          "you", "you're", "you'll", "you'd", "you've", "yours", "yourself", "yourselves"
      ));
  public static final Set<String> POV_THIRD =
      new HashSet<String>(Arrays.asList("he", "he's", "he'll", "he'd", "him", "his", "himself",
          "she", "she's", "she'll", "she'd", "her", "hers", "herself",
          "it", "it's", "it'll", "it'd", "itself",
          "they", "they're", "they'll", "they'd", "they've", "them", "theirs", "themselves"));
  public static final Map<String, Set<String>> POV_INDICATORS =
      new HashMap<String, Set<String>>(){{
          put("first", Parser.POV_FIRST);
          put("second", Parser.POV_SECOND);
          put("third", Parser.POV_THIRD);
      }};
  public static final Set<String> POV_ALL =
      new HashSet<String>(){{
          addAll(Parser.POV_FIRST);
          addAll(Parser.POV_SECOND);
          addAll(Parser.POV_THIRD);
      }};

  // Prohibit instantiation.
  private Parser() {
    throw new AssertionError();
  }

  // Convert smart quotes to ascii quotes
  public static final String convertQuotes(final String text) {
    return text.replaceAll(Parser.RE_SMART_QUOTES, "\"");
  }

  // Trim text and convert everything to lower case.
  public static final String normalizeText(final String text) {
    return text.trim().toLowerCase();
  }

  // Parse a String of text into a List of words.
  public static final List<String> parseWords(final String text, final Boolean lowerCase) {
    String localtext = text;
    if (lowerCase) {
      localtext = Parser.normalizeText(text);
    }
    String[] words = Arrays.stream(localtext.split(Parser.RE_WORDS))
      .filter(word -> word != "")
      .toArray(String[]::new);
    return Arrays.asList(words);
  }

  // Parse a String of text into a List of words.
  public static final List<String> parseWords(final String text) {
    return Parser.parseWords(text, true);
  }

  // Parse a List of sentences or paragraphs into a List of words.
  public static final List<String> parseWords(final List<String> sentences) {
    List<String> words = new ArrayList<String>();
    for (String sentence: sentences) {
      words.addAll(Parser.parseWords(sentence));
    }
    return words;
  }

  // Parse a List of words into a Set of unique words.
  public static final Set<String> parseUniqueWords(final List<String> words) {
    Set<String> uniqueWords = words.stream()
        .collect(Collectors.toSet());
    return uniqueWords;
  }

  // Parse a String of text into a Set of unique words.
  public static final Set<String> parseUniqueWords(final String text) {
    List<String> words = Parser.parseWords(text);
    return Parser.parseUniqueWords(words);
  }

  // Parse a String of text into a List of sentences.
  public static final List<String> parseSentences(final String text) {
    // Don't normalize. Case sensitive.
    String[] sentences = text.trim().split(Parser.RE_SENTENCES);
    return Arrays.asList(sentences);
  }

  // Parse a List of paragraphs into a List of sentences.
  public static final List<String> parseSentences(final List<String> paragraphs) {
    List<String> sentences = new ArrayList<String>();
    for (String paragraph: paragraphs) {
      sentences.addAll(Parser.parseSentences(paragraph));
    }
    return sentences;
  }

  // Parse a String of text into a List of paragraphs.
  // Paragraphs are separated by one or more new line characters.
  public static final List<String> parseParagraphs(final String text) {
    // Don't normalize. Case sensitive.
    String[] paragraphs = text.split(Parser.RE_PARAGRAPHS);
    return Arrays.asList(paragraphs);
  }

  /***
   * Parses a String of text into a List of dialogue fragments.
   *
   * <p>Anything between double quotation marks (") is considered dialogue.
   */
  public static final List<String> parseDialogue(final String text) {
    // http://stackoverflow.com/questions/6020384/create-array-of-regex-matches#6020436
    Pattern pattern = Pattern.compile(Parser.RE_DIALOGUE);
    Matcher matcher = pattern.matcher(text);
    List<String> dialogue = new ArrayList<String>();
    while (matcher.find()) {
      dialogue.add(matcher.group().replaceAll("\"", ""));
    }
    return dialogue;
  }

//   /**
//    * Parse a String of text into a String of dialogue.
//    *
//    * <p>Primarily used to facilitate assembling dialogue fragments
//    * into a list of complete dialogue sentences.
//    */
//   public static final String parseDialogue(final String text) {
//     List<String> dialogue = Parser.parseDialogue(text);
//     String dialogueString = new String();
//     for (String dialogueFragment: dialogue) {
//       dialogueString.concat(matcher.group().replaceAll("\"", "") + " ");
//     }
//     return dialogueString;
//   }
//
//   /**
//    * Parse a String of text into a List of dialogue sentences.
//    *
//    * <p>Primarily used to facilitate eliminating Parts of Speech tags
//    * found in dialogue from the text in order to figure out overall
//    * tense of the prose (PAST, PRESENT, FUTURE).
//    */
//   // Need to do this differently. Take the list of fragements and only concatenate
//   // if the current fragment starts with a lower case.
//   public static final List<String> parseDialogueSentences(final String text) {
//     String dialogueString = Parser.parseDialogue(text);
//     return Parser.parseSentences(dialogueString);
//   }

  // Parse out an array of all POV indicators found in text.
  public static final List<String> parsePovIndicators(final String text) {
    List<String> words = Parser.parseWords(text);
    String[] povIndicators = words.stream()
      .filter(word -> Parser.POV_ALL.contains(word))
      .toArray(String[]::new);
    return Arrays.asList(povIndicators);
  }

  public static final List<String> parsePovIndicators(final List<String> lines) {
    List<String> povIndicators = new ArrayList<String>();
    for (String text: lines) {
      povIndicators.addAll(Parser.parsePovIndicators(text));
    }
    return povIndicators;
  }

}
