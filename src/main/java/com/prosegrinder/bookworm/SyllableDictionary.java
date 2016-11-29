package com.prosegrinder.bookworm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// A Singleton Class for looking up syllables from a dictionary
// Current implementation uses cmudict.0.7a
// Based on: http://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples
public final class SyllableDictionary {

  private static volatile SyllableDictionary INSTANCE;
  private static volatile Map<String, Integer> syllableMap;

  // Syllable heuristics ported from https://github.com/neilb/Lingua-EN-Syllable/
  // Comments from original source preserved
  private static final Pattern[] subSyl = new Pattern[] {
    Pattern.compile("cial"),
    Pattern.compile("tia"),
    Pattern.compile("cius"),
    Pattern.compile("cious"),
    Pattern.compile("giu"),     // neilb: belgium!
    Pattern.compile("ion"),
    Pattern.compile("iou"),
    Pattern.compile("sia$"),
    Pattern.compile(".ely$"),   // neilb: absolutely! (but not ely!)
    Pattern.compile("[^td]ed$") // neilb: accused is 2, but executed is 4
  };
  private static final Pattern[] addSyl = new Pattern[] {
    Pattern.compile("ia"),
    Pattern.compile("riet"),
    Pattern.compile("dien"),
    Pattern.compile("iu"),
    Pattern.compile("io"),
    Pattern.compile("ii"),
    Pattern.compile("microor"),
    Pattern.compile("[aeiouym]bl$"),    // neilb: -Vble, plus -mble
    Pattern.compile("[aeiou]{3}"),      // neilb: agreeable
    Pattern.compile("^mc"),
    Pattern.compile("ism$"),            // neilb: -ism
    Pattern.compile("isms$"),           // neilb: -isms
    Pattern.compile("([^aeiouy])\1l$"), // neilb: middle twiddle battle bottle, etc.
    Pattern.compile("[^l]lien"),        // neilb: alien, salient [1]
    Pattern.compile("^coa[dglx]."),     // neilb: [2]
    Pattern.compile("[^gq]ua[^auieo]"), // neilb: i think this fixes more than it breaks
    Pattern.compile("dnt$")             // neilb: couldn't
  };

  // Because String.matches() isn't quite what it seems.
  // This detects stressors in the cmudict (parts that end with a digit).
  private static final Pattern cmudictSyllablePattern = Pattern.compile( "\\d$" );

  // private constructor
  private SyllableDictionary() {
    syllableMap = new HashMap<String, Integer>();
    // Will fallback to heuristic method if we can't load cmudict.
    try {
      // http://javarevisited.blogspot.com/2015/09/how-to-read-file-into-string-in-java-7.html
      ClassLoader classLoader = SyllableDictionary.class.getClassLoader();
      Path cmudictPath = Paths.get(
        classLoader.getResource("cmudict/cmudict.0.7a").getFile()
      );
      try ( Stream<String> stream = Files.lines( cmudictPath ) ) {
        stream.filter( line -> !line.startsWith( ";;;" ) )
          .map( String::toLowerCase )
          .forEach( line -> {
            String[] parts = line.split( " +" );
            String word = parts[0];
            if ( !word.endsWith( ")" ) ) {
              Integer syllables = 0;
              for ( int i = 1; i < parts.length; i++ ) {
                Matcher m = cmudictSyllablePattern.matcher( parts[i] );
                if ( m.find() ) {
                  syllables++;
                }
              }
              if ( syllables > 0 )
                syllableMap.put( word, syllables );
            }
          });
      }
    } catch ( Exception e ) {
      // Log the fact we're relying solely on the heuristic method
    }
  }

  public static synchronized SyllableDictionary getInstance() {
    if (INSTANCE == null) {
      synchronized (SyllableDictionary.class) {
        INSTANCE = new SyllableDictionary();
      }
    }
    return INSTANCE;
  }

  public Integer getByLookup( String word ) throws NullPointerException {
    if ( !syllableMap.containsKey( word ) )
      throw new NullPointerException( "Dictionary does not contain an entry for " + word + "." );
    else
      return syllableMap.get( word );
  }

  public Integer getByHeuristics( String word ) {
    // Lower case, fold contractions, and strip silent e off the end.
    String strippedWord = word.toLowerCase()
      .replaceAll("'", "")
      .replaceAll("e$", "");
    int syllableCount = 0;

    if ( strippedWord.equals("") || strippedWord == null ) {
      syllableCount = 0;
    } else if ( strippedWord.equals("w") ) {
      syllableCount = 2;
    } else if ( strippedWord.length() == 1 ) {
      syllableCount = 1;
    } else if ( strippedWord.matches( "^\\d{1,3}(?:[,]\\d{3})*$" ) ) {
      // Is word a number?
      // See: https://stackoverflow.com/questions/1359147/regex-for-comma-separated-number#1359152
      // neilb: Syllables for all-digit words (eg, "1998";  some call them "numbers") are
      // often counted as the number of digits.  A cooler solution would be converting
      // "1998" to "nineteen eighty eight" (or "one thousand nine hundred eighty
      // eight", or...), but that is left as an exercise for the reader.
      // me: In fiction, people don't usually "read" numbers. It's okay
      // to count the number of digits.
      return strippedWord.replaceAll( "[,.]", "" ).length();
    } else {
      String[] scrugg = strippedWord.split("[^aeiouy]+"); // neilb: perhaps - should be added?
      for ( Pattern p: addSyl ) {
        Matcher m = p.matcher( strippedWord );
        if ( m.matches() )
          syllableCount--;
      }
      for ( Pattern p: subSyl ) {
        Matcher m = p.matcher( strippedWord );
        if ( m.matches() )
          syllableCount++;
      }

      // Count vowel groupings
      if ( scrugg.length > 0 && "".equals(scrugg[0]) )
        syllableCount += scrugg.length - 1;
      else
        syllableCount += scrugg.length;

      // No vowels?
      if ( syllableCount == 0 )
        syllableCount = 1;
    }
    return syllableCount;
  }

  public Integer getSyllableCount( String word ) {
    if ( syllableMap.containsKey( word ) )
      return syllableMap.get( word );
    else
      return getByHeuristics( word );
  }

  public Map<String, Integer> getSyllableMap() {
    return Collections.unmodifiableMap( syllableMap );
  }

}