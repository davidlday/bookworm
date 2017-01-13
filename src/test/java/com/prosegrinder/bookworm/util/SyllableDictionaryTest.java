package com.prosegrinder.bookworm.util;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SyllableDictionaryTest {

  private static final SyllableDictionary syllableDictionary = SyllableDictionary.getInstance();
  /** Log4j Logger. **/
  private static final Logger logger = LogManager.getLogger(SyllableDictionaryTest.class);

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @Test
  public void testGetByLookup() {
    // Frowning is correct by lookup.
    assertEquals( "Frowning: ", 2, syllableDictionary.getByLookup( "frowning" ).intValue());
    // Cafe is correct by lookup.
    assertEquals( "Cafe: ", 2, syllableDictionary.getByLookup( "cafe" ).intValue());
    // No numbers are in the dictionary. Throws a NullPointerException.
    logger.info("Expecting NullPointerException...");
    String notWord = "1,904";
    thrown.expect( NullPointerException.class );
    thrown.expectMessage( "Dictionary does not contain an entry for " + notWord + "." );
    syllableDictionary.getByLookup( notWord );
  }

  @Test
  public void testGetByHeuristics() {
    // Frowning is correct by heuristics.
    assertEquals( "Frowning: ", 2, syllableDictionary.getByHeuristics( "frowning" ).intValue());
    // Cafe is incorrect by heuristics.
    assertNotEquals( "Cafe: ", 2, syllableDictionary.getByHeuristics( "cafe" ).intValue());
    // Numbers are a count of digits by heuristics.
    assertEquals( "20,012.12: ", 7, syllableDictionary.getByHeuristics( "20,012.12" ).intValue());
    // Numbers are a count of digits by heuristics.
    assertEquals( "1,904: ", 4, syllableDictionary.getByHeuristics( "1,904" ).intValue());
    // Numbers are a count of digits by heuristics.
    assertEquals( "0.2315: ", 5, syllableDictionary.getByHeuristics( "0.2315" ).intValue());
    // Numbers are a count of digits by heuristics. Sign counts as a syllable, too.
    assertEquals( "-503,012.12: ", 9, syllableDictionary.getByHeuristics( "-503,012.12" ).intValue());
  }

  @Test
  public void testGetSyllableCount() {
    // All should come back with correct answers.
    assertEquals( "Frowning: ", 2, syllableDictionary.getByLookup( "frowning" ).intValue());
    assertEquals( "Cafe: ", 2, syllableDictionary.getSyllableCount( "cafe" ).intValue());
    // Numbers are a count of digits by heuristics.
    assertEquals( "20,012.12: ", 7, syllableDictionary.getByHeuristics( "20,012.12" ).intValue());
    // Numbers are a count of digits by heuristics.
    assertEquals( "1,904: ", 4, syllableDictionary.getByHeuristics( "1,904" ).intValue());
    // Numbers are a count of digits by heuristics.
    assertEquals( "0.2315: ", 5, syllableDictionary.getByHeuristics( "0.2315" ).intValue());
    // Numbers are a count of digits by heuristics. Sign counts as a syllable, too.
    assertEquals( "-503,012.12: ", 9, syllableDictionary.getByHeuristics( "-503,012.12" ).intValue());
  }

  @Test
  public void testHeuristicesVsLookup() {
    // Test heuristics is at least 80%
    double hits = 0.0;
    Map<String, Integer> map = syllableDictionary.getSyllableMap();
    for ( Map.Entry<String, Integer> e: map.entrySet() ) {
      String word = e.getKey();
      Integer byLookup = e.getValue();
      Integer byHeuristics = syllableDictionary.getByHeuristics( word );
      if ( byHeuristics.equals(byLookup) ) {
        hits++;
      }
    }
    double ratio = (double) hits / (double) map.size();
    assertTrue( "Expected at least 0.8 hit ratio on Heuristics. Ratio=", ratio > 0.8 );
  }

}
