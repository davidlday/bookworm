package com.prosegrinder.bookworm.util;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CMUDictTest {
  private static final CMUDict cmudict = CMUDict.getInstance();

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @Test
  public void testGetPhonemes() {
    fail("Not yet implemented");
  }

  @Test
  public void testGetPhonemeString() {
    fail("Not yet implemented");
  }

  @Test
  public void testGetPhonemeStringMap() {
    fail("Not yet implemented");
  }

  @Test
  public void testGetSyllableCount() {
    fail("Not yet implemented");
  }

  @Test
  public void testInCMUDict() {
    fail("Not yet implemented");
  }

  @Test
  public void testScanPhonemeString() {
    assertEquals("frowning:", cmudict.scanPhonemeString( "frowning" ), "F R AW1 N IH0 NG");
    assertEquals("zurkuhlen:", cmudict.scanPhonemeString( "zurkuhlen" ), "Z ER0 K Y UW1 L AH0 N");
    String notWord = "1,904";
    thrown.expect( IllegalArgumentException.class );
    thrown.expectMessage( "Dictionary does not contain an entry for " + notWord + "." );
    cmudict.scanPhonemeString( notWord );
  }

}
