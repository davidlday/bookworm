package com.prosegrinder.bookworm;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public final class Parser {
  // A Utility class for parsing prose text, specifically fiction.
  // All rules for parsing are derived from either industry practice or William Shunn's
  // "Proper Manuscript Formatting" site (//www.shunn.net/format/).

  // Regular Expression Strings
  public static final String RE_SMART_QUOTES = new String("[\u201c\u201d]");
  public static final String RE_WORDS = new String("[^\\w\u2019\']+");
  public static final String RE_SENTENCES = new String("(?<=[.?!\"])\\s+(?=[\"A-Z])");
  public static final String RE_PARAGRAPHS = new String("\\n+");
  // http://www.metaltoad.com/blog/regex-quoted-string-escapable-quotes
  // ((?<![\\])["])((?:.(?!(?<![\\])\1))*.?)\1
  public static final String RE_DIALOGUE = new String("((?<![\\\\])[\"])((?:.(?!(?<![\\\\])\\1))*.?)\\1");

  // Point of View indicators
//   public static final List<String> POV_FIRST = Collections.unmodifiableList(
//     new ArrayList<String>() {{
//       addAll("I", "I'm", "I'll", "I'd", "I've", "me", "mine", "myself");
//       addAll("we", "we're", "we'll", "we'd", "we've");
//       addAll("us", "ours", "ourselves");
//     }}
//   );
  public static final List<String> POV_FIRST = Collections.unmodifiableList(
    Arrays.asList(
      "I", "I'm", "I'll", "I'd", "I've", "me", "mine", "myself",
      "we", "we're", "we'll", "we'd", "we've",
      "us", "ours", "ourselves"
    )
  );
  public static final List<String> POV_SECOND = Collections.unmodifiableList(
    Arrays.asList(
      "you", "you're", "you'll", "you'd", "you've", "yours", "yourself", "yourselves"
    )
  );
  public static final List<String> POV_THIRD = Collections.unmodifiableList(
    Arrays.asList(
      "he", "he's", "he'll", "he'd", "him", "his", "himself",
      "she", "she's", "she'll", "she'd", "her", "hers", "herself",
      "it", "it's", "it'll", "it'd", "itself",
      "they", "they're", "they'll", "they'd", "they've", "them", "theirs", "themselves"
    )
  );
//   public static final Map<String, String[]> POV_INDICATORS = Collections.unmodifiableMap(
//     new HashMap<String, String[]>() {{
//       put("first", Parser.POV_FIRST.toArray());
//       put("second", Parser.POV_SECONDtoArray());
//       put("third", Parser.POV_THIRDtoArray());
//     }}
//   );
  public static final List<String> POV_ALL = Collections.unmodifiableList(
    new ArrayList<String>() {{
      addAll( Parser.POV_FIRST );
      addAll( Parser.POV_SECOND );
      addAll( Parser.POV_THIRD );
    }}
  );

  // Prohibit instantiation.
  private Parser() {
    throw new AssertionError();
  }

  // Convert smart quotes to ascii quotes
  public static final String convertQuotes(String text) {
    return text.replaceAll(Parser.RE_SMART_QUOTES, "\"");
  }

  // Trim text and convert everything to lower case.
  public static final String normalizeText(String text) {
    return text.trim().toLowerCase();
  }

  // Parse a String of text into an immutable List of words.
  public static final List<String> parseWords(String text, Boolean lowerCase) {
    if (lowerCase)
      text = Parser.normalizeText(text);
    String[] words = Arrays.stream( text.split(Parser.RE_WORDS) )
      .filter( word -> word != "" )
      .toArray( String[]::new );
    return Collections.unmodifiableList( Arrays.asList( words ) );
  }

  // Parse a String of text into an immutable List of words.
  public static final List<String> parseWords(String text) {
    return Parser.parseWords(text, true);
  }

  // Parse a List of words into an immutable Set of unique words.
  public static final Set<String> parseUniqueWords(List<String> words) {
    Set<String> uniqueWords = words.stream()
		  .collect( Collectors.toSet() );
		return Collections.unmodifiableSet( uniqueWords );
  }

  // Parse a String of text into an immutable Set of unique words.
  public static final Set<String> parseUniqueWords(String text) {
    List<String> words = Parser.parseWords( text );
    return Parser.parseUniqueWords( words );
  }

  // Parse a String of text into an immutable List of sentences.
  public static final List<String> parseSentences(String text) {
    // Don't normalize. Case sensitive.
    String[] sentences = text.trim().split(Parser.RE_SENTENCES);
    return Collections.unmodifiableList( Arrays.asList( sentences ) );
  }

  // Parse a String of text into an immutable List of paragraphs.
  // Paragraphs are separated by one or more new line characters.
  public static final List<String> parseParagraphs(String text) {
    String[] paragraphs = text.split(Parser.RE_PARAGRAPHS);
    return Collections.unmodifiableList( Arrays.asList( paragraphs ) );
  }

  // Parse a String of text into an immutable List of dialogue.
  // Anything between double quotation marks is considered dialogue.
  public static final List<String> parseDialogue(String text) {
    // http://stackoverflow.com/questions/6020384/create-array-of-regex-matches#6020436
    Pattern p = Pattern.compile(Parser.RE_DIALOGUE);
    Matcher m = p.matcher(text);
    List<String> dialogue = new ArrayList<String>();
    while (m.find()) {
      dialogue.add( m.group().replaceAll("\"", "") );
    }
    return Collections.unmodifiableList( dialogue );
  }

  // Parse out an array of all POV indicators found in text.
  public static final List<String> parsePovIndicators(String text) {
    List<String> words = Parser.parseWords( text );
    String[] povIndicators = words.stream()
      .filter( word -> Parser.POV_ALL.contains( word ) )
      .toArray( String[]::new );
    return Collections.unmodifiableList( Arrays.asList( povIndicators ) );
  }

}