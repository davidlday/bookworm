package com.prosegrinder.bookworm.util;

import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;

public class CMUDictTest {
  private static final CMUDict cmudict = CMUDict.getInstance();

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @Test
  public void testGetPhonemes() {
    List<String> frowningPhonemes = Arrays.asList("F", "R", "AW1", "N", "IH0", "NG");
    assertEquals("frowning:", frowningPhonemes, cmudict.getPhonemes( "frowning" ));
    List<String> zurkuhlenPhonemes = Arrays.asList("Z", "ER0", "K", "Y", "UW1", "L", "AH0", "N");
    assertEquals("zurkuhlen:", zurkuhlenPhonemes, cmudict.getPhonemes( "zurkuhlen" ));
    List<String> cafePhonemes = Arrays.asList("K", "AH0", "F", "EY1");
    assertEquals("cafe:", cafePhonemes, cmudict.getPhonemes( "cafe" ));
  }

  @Test
  public void testGetSyllableCount() {
    assertEquals("frowning:", 2, cmudict.getSyllableCount( "frowning" ).intValue());
    assertEquals("zurkuhlen:", 3, cmudict.getSyllableCount( "zurkuhlen" ).intValue());
    assertEquals( "cafe: ", 2, cmudict.getSyllableCount( "cafe" ).intValue());
  }

  @Test
  public void testInCMUDict() {
    assertTrue("frowning:", cmudict.inCMUDict( "frowning" ));
    assertTrue("zurkuhlen:", cmudict.inCMUDict( "zurkuhlen" ));
    assertTrue( "cafe: ", cmudict.inCMUDict( "cafe" ));
    assertFalse("1,904", cmudict.inCMUDict("1,904"));
  }

  @Test
  public void testGetPhonemeString() {
    assertEquals("frowning:", "F R AW1 N IH0 NG", cmudict.getPhonemeString( "frowning" ));
    assertEquals("zurkuhlen:", "Z ER0 K Y UW1 L AH0 N", cmudict.getPhonemeString( "zurkuhlen" ));
    assertEquals("cafe:", "K AH0 F EY1", cmudict.getPhonemeString( "cafe" ));
    String notWord = "1,904";
    thrown.expect( IllegalArgumentException.class );
    thrown.expectMessage( "cmudict does not contain an entry for " + notWord + "." );
    cmudict.getPhonemeString( notWord );
  }

}
