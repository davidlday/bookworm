package com.prosegrinder.bookworm.util;

import static org.junit.Assert.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DictionaryTest {

  private static final Dictionary dictionary = Dictionary.getInstance();

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @Test
  public void testGetCMUDictSyllableCount() {
    assertEquals("frowning:", 2, dictionary.getCMUDictSyllableCount( "frowning" ).intValue());
    assertEquals("zurkuhlen:", 3, dictionary.getCMUDictSyllableCount( "zurkuhlen" ).intValue());
    assertEquals( "cafe: ", 2, dictionary.getCMUDictSyllableCount( "cafe" ).intValue());
    String notWord = "1,904";
    thrown.expect( IllegalArgumentException.class );
    thrown.expectMessage( "cmudict does not contain an entry for " + notWord + "." );
    dictionary.getCMUDictSyllableCount( notWord );
  }

  @Test
  public void testGetHeuristicSyllableCount() {
    assertEquals( "frowning: ", 2, dictionary.getHeuristicSyllableCount( "frowning" ).intValue());
    assertEquals("zurkuhlen:", 3, dictionary.getHeuristicSyllableCount( "zurkuhlen" ).intValue());
    assertNotEquals( "cafe: ", 2, dictionary.getHeuristicSyllableCount( "cafe" ).intValue());
    assertEquals( "20,012.12: ", 7, dictionary.getHeuristicSyllableCount( "20,012.12" ).intValue());
    assertEquals( "1,904: ", 4, dictionary.getHeuristicSyllableCount( "1,904" ).intValue());
    assertEquals( "0.2315: ", 5, dictionary.getHeuristicSyllableCount( "0.2315" ).intValue());
    assertEquals( "-503,012.12: ", 9, dictionary.getHeuristicSyllableCount( "-503,012.12" ).intValue());
  }

  @Test
  public void testGetSyllableCount() {
    assertEquals( "frowning: ", 2, dictionary.getSyllableCount( "frowning" ).intValue());
    assertEquals("zurkuhlen:", 3, dictionary.getSyllableCount( "zurkuhlen" ).intValue());
    assertEquals( "cafe: ", 2, dictionary.getSyllableCount( "cafe" ).intValue());
    assertEquals( "20,012.12: ", 7, dictionary.getSyllableCount( "20,012.12" ).intValue());
    assertEquals( "1,904: ", 4, dictionary.getSyllableCount( "1,904" ).intValue());
    assertEquals( "0.2315: ", 5, dictionary.getSyllableCount( "0.2315" ).intValue());
    assertEquals( "-503,012.12: ", 9, dictionary.getSyllableCount( "-503,012.12" ).intValue());
  }

  @Test
  public void testGetWord() {
    Word frowning = new Word(WordContainer.normalizeText("frowning"), 2, Boolean.TRUE, Boolean.FALSE);
    assertEquals("frowning:", frowning, dictionary.getWord("frowning"));
    Word zurkuhlen = new Word(WordContainer.normalizeText("zurkuhlen"), 3, Boolean.TRUE, Boolean.FALSE);
    assertEquals("zurkuhlen:", zurkuhlen, dictionary.getWord("zurkuhlen"));
    Word cafe = new Word(WordContainer.normalizeText("cafe"), 2, Boolean.TRUE, Boolean.FALSE);
    assertEquals("cafe:", cafe, dictionary.getWord("cafe"));
    Word number = new Word(WordContainer.normalizeText("1,904"), 4, Boolean.FALSE, Boolean.TRUE);
    assertEquals("1,904:", number, dictionary.getWord("1,904"));
  }

  @Test
  public void testInDictionary() {
    assertTrue( "frowning: ", dictionary.inDictionary( "frowning" ));
    assertTrue("zurkuhlen:", dictionary.inDictionary( "zurkuhlen" ));
    assertTrue( "cafe: ", dictionary.inDictionary( "cafe" ));
    assertFalse( "20,012.12: ", dictionary.inDictionary( "20,012.12" ));
    assertFalse( "1,904: ", dictionary.inDictionary( "1,904" ));
    assertFalse( "0.2315: ", dictionary.inDictionary( "0.2315" ));
    assertFalse( "-503,012.12: ", dictionary.inDictionary( "-503,012.12" ));
  }

  @Test
  public void testIsNumeric() {
    assertFalse( "frowning: ", dictionary.isNumeric( "frowning" ));
    assertFalse("zurkuhlen:", dictionary.isNumeric( "zurkuhlen" ));
    assertFalse( "cafe: ", dictionary.isNumeric( "cafe" ));
    assertTrue( "20,012.12: ", dictionary.isNumeric( "20,012.12" ));
    assertTrue( "1,904: ", dictionary.isNumeric( "1,904" ));
    assertTrue( "0.2315: ", dictionary.isNumeric( "0.2315" ));
    assertTrue( "-503,012.12: ", dictionary.isNumeric( "-503,012.12" ));
  }

}
