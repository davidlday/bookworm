package com.prosegrinder.bookworm;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import java.util.Map;

public class SyllableDictionaryTest {

  private static final SyllableDictionary syllableDictionary = SyllableDictionary.getInstance();

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @Test
  public void testGetByLookup() {
    // Frowning is correct by lookup.
    assertEquals( "Frowning: ", syllableDictionary.getByLookup( "frowning" ).intValue(), 2 );
    // Cafe is correct by lookup.
    assertEquals( "Cafe: ", syllableDictionary.getByLookup( "cafe" ).intValue(), 2 );
    // No numbers are in the dictionary. Throws a NullPointerException.
    String notWord = "1,904";
    thrown.expect( NullPointerException.class );
    thrown.expectMessage( "Dictionary does not contain an entry for " + notWord + "." );
    syllableDictionary.getByLookup( notWord );
  }

  @Test
  public void testGetByHeuristics() {
    // Frowning is correct by heuristics.
    assertEquals( "Frowning: ", syllableDictionary.getByHeuristics( "frowning" ).intValue(), 2 );
    // Cafe is incorrect by heuristics.
    assertNotEquals( "Cafe: ", syllableDictionary.getByHeuristics( "cafe" ).intValue(), 2 );
    // Numbers are a count of digits by heuristics.
    assertEquals( "1,904: ", syllableDictionary.getByHeuristics( "1,904" ).intValue(), 4 );
  }

  @Test
  public void testGetSyllableCount() {
    // All should come back with correct answers.
    assertEquals( "Frowning: ", syllableDictionary.getByLookup( "frowning" ).intValue(), 2 );
    assertEquals( "Cafe: ", syllableDictionary.getSyllableCount( "cafe" ).intValue(), 2 );
    assertEquals( "1,904: ", syllableDictionary.getSyllableCount( "1,904" ).intValue(), 4 );
  }

  @Test
  public void testHeuristicesVsLookup() {
    // Test heuristics is at least 80%
    double hits = 0.0;
    double misses = 0.0;
    Map<String, Integer> map = syllableDictionary.getSyllableMap();
    for ( Map.Entry<String, Integer> e: map.entrySet() ) {
      String word = e.getKey();
      Integer byLookup = e.getValue();
      Integer byHeuristics = syllableDictionary.getByHeuristics( word );
      if ( byHeuristics != byLookup )
        misses++;
      else
        hits++;
    }
    double ratio = hits / ( hits + misses );
    assertTrue( "Expected at least 0.8 hit ratio on Heuristics. Ratio=", ratio > 0.8 );
  }

}