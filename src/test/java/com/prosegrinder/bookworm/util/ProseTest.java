package com.prosegrinder.bookworm.util;

import com.prosegrinder.bookworm.enums.PovType;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProseTest {

  private Prose prose;
  private Prose narrativeProse;

  /** Log4j Logger. **/
  private static final int COMPLEX_WORD_COUNT = 202;
  private static final int LONG_WORD_COUNT = 275;
  private static final int SENTENCE_COUNT = 90;
  private static final int SYLLABLE_COUNT = 2287;
  private static final int UNIQUE_WORD_COUNT = 526;
  private static final int WORD_COUNT = 1528;
  private static final int POV_INDICATOR_COUNT = 104;
  private static final int FIRST_PERSON_INDICATOR_COUNT = 6;
  private static final int SECOND_PERSON_INDICATOR_COUNT = 73;
  private static final int THIRD_PERSON_INDICATOR_COUNT = 25;
  

  @Before
  public void loadProse() throws IOException, URISyntaxException {
    // String proseResource = "shunn/shortstory.txt";
    Dictionary2 dictionary = Dictionary2.getDefaultDictionary();
    ClassLoader classLoader = ProseTest.class.getClassLoader();

    Path prosePath = Paths.get(classLoader.getResource("shunn/shortstory.txt").toURI());
    this.prose = new Prose(String.join("\n", Files.readAllLines(prosePath)), dictionary);

    Path narrativeProsePath =
        Paths.get(classLoader.getResource("shunn/shortstory_narrative.txt").toURI());
    this.narrativeProse =
        new Prose(String.join("\n", Files.readAllLines(narrativeProsePath)), dictionary);

  }

  @Test
  public void testSyllableCount() {
    assertEquals("Syllable Count: ", ProseTest.SYLLABLE_COUNT, prose.getSyllableCount().intValue());
  }

  @Test
  public void testWordCount() {
    assertEquals("Word Count: ", ProseTest.WORD_COUNT, prose.getWordCount().intValue());
  }

  @Test
  public void testLongWordCount() {
    assertEquals("Long Word Count: ", ProseTest.LONG_WORD_COUNT,
        prose.getLongWordCount().intValue());
  }

  @Test
  public void testComplexWordCount() {
    assertEquals("Complex Word Count: ", ProseTest.COMPLEX_WORD_COUNT,
        prose.getComplexWordCount().intValue());
  }

  @Test
  public void testUniqueWordCount() {
    assertEquals("Unique Word Count: ", ProseTest.UNIQUE_WORD_COUNT,
        prose.getUniqueWordCount().intValue());
  }

  @Test
  public void testSentenceCount() {
    assertEquals("Sentence Count: ", ProseTest.SENTENCE_COUNT, prose.getSentenceCount().intValue());
  }

  @Test
  public void testPov() {
    assertEquals("Point of View internally consistent: ", narrativeProse.getPov(), prose.getPov());
    assertEquals("Point of View: ", PovType.FIRST, prose.getPov());
  }

  @Test
  public void testPovIndicators() {
    assertEquals("First Person PoV Indicator Count: ", ProseTest.FIRST_PERSON_INDICATOR_COUNT, prose.getFirstPersonIndicatorCount().intValue());
    assertEquals("Second Person PoV Indicator Count: ", ProseTest.SECOND_PERSON_INDICATOR_COUNT, prose.getSecondPersonIndicatorCount().intValue());
    assertEquals("Third Person PoV Indicator Count: ", ProseTest.THIRD_PERSON_INDICATOR_COUNT, prose.getThirdPersonIndicatorCount().intValue());
    assertEquals("Point of View Indicator Count: ", ProseTest.POV_INDICATOR_COUNT, prose.getPovIndicatorCount().intValue());
  }

  @Test
  public void testSumFragmentWordCount() {
    assertEquals("Dialogue word count + narrative word count = total word count: ",
        ProseTest.WORD_COUNT,
        prose.getDialogueWordCount().intValue() + prose.getNarrativeWordCount().intValue());
  }
  
  @Test
  public void testSumWordFrequency() {
    int sumUniqueWordCounts = 0;
    for (Word word: prose.getUniqueWords()) {
      sumUniqueWordCounts += prose.getWordFrequency(word).intValue();
    }
    assertEquals("Sum of Word Frequencies and Defined Word Count: ", ProseTest.WORD_COUNT, sumUniqueWordCounts);
    assertEquals("Sum of Word Frequencies and Discovered Word Count: ", ProseTest.WORD_COUNT, prose.getWordCount().intValue());
  }

}
