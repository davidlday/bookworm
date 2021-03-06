package com.prosegrinder.bookworm.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.typesafe.config.ConfigFactory;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;

public class Dictionary2Test {

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @After
  public void resetDictionary() {
    Dictionary2.getDefaultDictionary();
  }
  
  @Test
  public final void testGetHeuristicSyllableCount() {
    Dictionary2 dictionary = Dictionary2.getDefaultDictionary();
    assertEquals("frowning: ", 2, dictionary.getHeuristicSyllableCount("frowning").intValue());
    assertEquals("zurkuhlen:", 3, dictionary.getHeuristicSyllableCount("zurkuhlen").intValue());
    assertNotEquals("cafe: ", 2, dictionary.getHeuristicSyllableCount("cafe").intValue());
    assertEquals("20,012.12: ", 7, dictionary.getHeuristicSyllableCount("20,012.12").intValue());
    assertEquals("1,904: ", 4, dictionary.getHeuristicSyllableCount("1,904").intValue());
    assertEquals("0.2315: ", 5, dictionary.getHeuristicSyllableCount("0.2315").intValue());
    assertEquals("-503,012.12: ", 9,
        dictionary.getHeuristicSyllableCount("-503,012.12").intValue());
  }

  @Test
  public final void testGetPhonemes() {
    Dictionary2 dictionary = Dictionary2.getDefaultDictionary();
    List<String> frowningPhonemes = Arrays.asList("F", "R", "AW1", "N", "IH0", "NG");
    assertEquals("frowning:", frowningPhonemes, dictionary.getPhonemes("frowning"));
    List<String> zurkuhlenPhonemes = Arrays.asList("Z", "ER0", "K", "Y", "UW1", "L", "AH0", "N");
    assertEquals("zurkuhlen:", zurkuhlenPhonemes, dictionary.getPhonemes("zurkuhlen"));
    List<String> cafePhonemes = Arrays.asList("K", "AH0", "F", "EY1");
    assertEquals("cafe:", cafePhonemes, dictionary.getPhonemes("cafe"));
  }

  @Test
  public final void testGetPhonemeString() {
    Dictionary2 dictionary = Dictionary2.getDefaultDictionary();
    assertEquals("frowning:", "F R AW1 N IH0 NG", dictionary.getPhonemeString("frowning"));
    assertEquals("zurkuhlen:", "Z ER0 K Y UW1 L AH0 N", dictionary.getPhonemeString("zurkuhlen"));
    assertEquals("cafe:", "K AH0 F EY1", dictionary.getPhonemeString("cafe"));
    String notWord = "1,904";
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("cmudict does not contain an entry for " + notWord + ".");
    dictionary.getPhonemeString(notWord);
  }

  @Test
  public final void testGetSyllableCount() {
    Dictionary2 dictionary = Dictionary2.getDefaultDictionary();
    assertEquals("frowning: ", 2, dictionary.getSyllableCount("frowning").intValue());
    assertEquals("zurkuhlen:", 3, dictionary.getSyllableCount("zurkuhlen").intValue());
    assertEquals("cafe: ", 2, dictionary.getSyllableCount("cafe").intValue());
    assertEquals("20,012.12: ", 7, dictionary.getSyllableCount("20,012.12").intValue());
    assertEquals("1,904: ", 4, dictionary.getSyllableCount("1,904").intValue());
    assertEquals("0.2315: ", 5, dictionary.getSyllableCount("0.2315").intValue());
    assertEquals("-503,012.12: ", 9, dictionary.getSyllableCount("-503,012.12").intValue());
  }

  @Test
  public final void testGetCmudictSyllableCount() {
    Dictionary2 dictionary = Dictionary2.getDefaultDictionary();
    assertEquals("frowning:", 2, dictionary.getSyllableCount("frowning").intValue());
    assertEquals("zurkuhlen:", 3, dictionary.getSyllableCount("zurkuhlen").intValue());
    assertEquals("cafe: ", 2, dictionary.getSyllableCount("cafe").intValue());
  }

  @Test
  public final void testGetWord() {
    Dictionary2 dictionary = Dictionary2.getDefaultDictionary();
    Word frowning =
        new Word(WordContainer.normalizeText("frowning"), 2, Boolean.TRUE, Boolean.FALSE);
    assertEquals("frowning:", frowning, dictionary.getWord("frowning"));
    Word zurkuhlen =
        new Word(WordContainer.normalizeText("zurkuhlen"), 3, Boolean.TRUE, Boolean.FALSE);
    assertEquals("zurkuhlen:", zurkuhlen, dictionary.getWord("zurkuhlen"));
    Word cafe = new Word(WordContainer.normalizeText("cafe"), 2, Boolean.TRUE, Boolean.FALSE);
    assertEquals("cafe:", cafe, dictionary.getWord("cafe"));
    Word number = new Word(WordContainer.normalizeText("1,904"), 4, Boolean.FALSE, Boolean.TRUE);
    assertEquals("1,904:", number, dictionary.getWord("1,904"));
  }

  @Test
  public final void testInCmudict() {
    Dictionary2 dictionary = Dictionary2.getDefaultDictionary();
    assertTrue("frowning:", dictionary.inCmudict("frowning"));
    assertTrue("zurkuhlen:", dictionary.inCmudict("zurkuhlen"));
    assertTrue("cafe: ", dictionary.inCmudict("cafe"));
    assertFalse("1,904", dictionary.inCmudict("1,904"));
  }

  @Test
  public final void testInCache() {
    /** Force reset on dictionary using a non-existent cmudict.dict file. **/
    Dictionary2 dictionary = Dictionary2.getDictionary(ConfigFactory.load("application-missingcmudict")
        .getConfig("com.prosegrinder.bookworm.util.dictionary"));

    /** Real words should cache. **/
    assertFalse("frowning: ", dictionary.inCache("frowning"));
    dictionary.getWord("frowning");
    assertTrue("frowning: ", dictionary.inCache("frowning"));

    assertFalse("zurkuhlen:", dictionary.inCache("zurkuhlen"));
    dictionary.getWord("zurkuhlen");
    assertTrue("zurkuhlen:", dictionary.inCache("zurkuhlen"));

    assertFalse("cafe: ", dictionary.inCache("cafe"));
    dictionary.getWord("cafe");
    assertTrue("cafe: ", dictionary.inCache("cafe"));

    /** Numbers should not cache. **/
    assertFalse("20,012.12: ", dictionary.inCache("20,012.12"));
    dictionary.getWord("20,012.12");
    assertFalse("20,012.12: ", dictionary.inCache("20,012.12"));

    assertFalse("1,904: ", dictionary.inCache("1,904"));
    dictionary.getWord("1,904");
    assertFalse("1,904: ", dictionary.inCache("1,904"));

    assertFalse("0.2315: ", dictionary.inCache("0.2315"));
    dictionary.getWord("0.2315");
    assertFalse("0.2315: ", dictionary.inCache("0.2315"));

    assertFalse("-503,012.12: ", dictionary.inCache("-503,012.12"));
    dictionary.getWord("-503,012.12");
    assertFalse("-503,012.12: ", dictionary.inCache("-503,012.12"));
  }

  @Test
  public final void testIsNumeric() {
    Dictionary2 dictionary = Dictionary2.getDefaultDictionary();
    assertFalse("frowning: ", dictionary.isNumeric("frowning"));
    assertFalse("zurkuhlen:", dictionary.isNumeric("zurkuhlen"));
    assertFalse("cafe: ", dictionary.isNumeric("cafe"));
    assertTrue("20,012.12: ", dictionary.isNumeric("20,012.12"));
    assertTrue("1,904: ", dictionary.isNumeric("1,904"));
    assertTrue("0.2315: ", dictionary.isNumeric("0.2315"));
    assertTrue("-503,012.12: ", dictionary.isNumeric("-503,012.12"));
  }

}
