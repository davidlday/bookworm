package com.prosegrinder.bookworm;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public final class Parser {
  // A Utility class for parsing prose text, specifically fiction.
  // All rules for parsing are derived from either industry practice or William Shunn's
  // "Proper Manuscript Formatting" site (//www.shunn.net/format/).

  // Prohibit instantiation.
  private Parser() {
    throw new AssertionError();
  }

  // Regular Expression Strings
  public static final String RE_SMART_QUOTES = new String("[\u201c\u201d]");
  public static final String RE_WORDS = new String("[^\\w\u2019\']+");
  public static final String RE_SENTENCES = new String("(?<=[.?!\"])\\s+(?=[\"A-Z])");
  public static final String RE_PARAGRAPHS = new String("\\n+");
  // http://www.metaltoad.com/blog/regex-quoted-string-escapable-quotes
  // ((?<![\\])["])((?:.(?!(?<![\\])\1))*.?)\1
  public static final String RE_DIALOGUE = new String("((?<![\\\\])[\"])((?:.(?!(?<![\\\\])\\1))*.?)\\1");

  // Point of View indicators
  public static final String[] POV_FIRST = {
    "I", "I'm", "I'll", "I'd", "I've", "me", "mine", "myself",
    "we", "we're", "we'll", "we'd", "we've",
    "us", "ours", "ourselves"
  };
  public static final String[] POV_SECOND = {
    "you", "you're", "you'll", "you'd", "you've", "yours", "yourself", "yourselves"
  };
  public static final String[] POV_THIRD = {
    "he", "he's", "he'll", "he'd", "him", "his", "himself",
    "she", "she's", "she'll", "she'd", "her", "hers", "herself",
    "it", "it's", "it'll", "it'd", "itself",
    "they", "they're", "they'll", "they'd", "they've", "them", "theirs", "themselves"
  };
  public static final Map<String, String[]> POV_INDICATORS = Collections.unmodifiableMap(
    new HashMap<String, String[]>() {{
      put("first", Parser.POV_FIRST);
      put("second", Parser.POV_SECOND);
      put("third", Parser.POV_THIRD);
    }}
  );

  // Convert smart quotes to ascii quotes
  public static final String convertQuotes(String text) {
    return text.replaceAll(Parser.RE_SMART_QUOTES, "\"");
  }

  // Trim text and convert everything to lower case.
  public static final String normalizeText(String text) {
    return text.trim().toLowerCase();
  }

  // Parse a string of text into an ordered array of words.
  public static final String[] parseWords(String text, Boolean lowerCase) {
    if (lowerCase)
      text = Parser.normalizeText(text);
    String[] words = Arrays.stream( text.split(Parser.RE_WORDS) )
      .filter( word -> word != "" )
      .toArray( String[]::new );
    return words;
  }

  // Parse a string of text into an ordered array of words.
  public static final String[] parseWords(String text) {
    return Parser.parseWords(text, true);
  }

  // Parse a string of text into an array of unique words.
  public static final String[] parseUniqueWords(String text) {
    String[] words = Parser.parseWords( text );
    return Parser.parseUniqueWords( words );
  }

  // Parse a string of text into an array of unique words.
  public static final String[] parseUniqueWords(String[] words) {
    Set<String> uniqueWords = Arrays.stream( words )
		  .collect( Collectors.toSet() );
		return uniqueWords.toArray( new String[uniqueWords.size()] );
  }

  // Parse a string of text into an array of sentences.
  public static final String[] parseSentences(String text) {
    // Don't normalize. Case sensitive.
    String[] sentences = text.split(Parser.RE_SENTENCES);
    return sentences;
  }

  // Parse a string of text into an array of paragraphs. Paragraphs are separated by
  // one or more new line characters.
  public static final String[] parseParagraphs(String text) {
    String[] paragraphs = text.split(Parser.RE_PARAGRAPHS);
    return paragraphs;
  }

  // Parse out dialogue from a string of text. Anything between double quotation marks
  // is considered dialogue.
  public static final String[] parseDialogue(String text) {
    // http://stackoverflow.com/questions/6020384/create-array-of-regex-matches#6020436
    Pattern p = Pattern.compile(Parser.RE_DIALOGUE);
    Matcher m = p.matcher(text);
    List<String> dialogueList = new LinkedList<String>();
    while (m.find()) {
      dialogueList.add( m.group().replaceAll("\"", "") );
    }
    String[] dialogue = dialogueList.toArray( new String[dialogueList.size()] );
    return dialogue;
  }

}