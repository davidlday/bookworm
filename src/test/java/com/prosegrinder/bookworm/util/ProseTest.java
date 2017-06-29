package com.prosegrinder.bookworm.util;

import com.prosegrinder.bookworm.enums.PovType;
//import org.junit.Ignore;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

//@Ignore
public class ProseTest {

  private Prose prose;

  private static final int COMPLEX_WORD_COUNT = 202;
  private static final int LONG_WORD_COUNT = 275;
  private static final int SENTENCE_COUNT = 90;
  private static final int SYLLABLE_COUNT = 2287;
  private static final int UNIQUE_WORD_COUNT = 526;
  private static final int WORD_COUNT = 1528;
  
  @Before
  public void loadProse() throws IOException, URISyntaxException {
    String prose = "shunn/shortstory.txt";
    ClassLoader classLoader = ProseTest.class.getClassLoader();
    Path prosePath = Paths.get(classLoader.getResource(prose).toURI());
    List<String> lines = Files.readAllLines(prosePath);
    this.prose = new Prose(String.join("\n",lines));
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
    assertEquals("Long Word Count: ", ProseTest.LONG_WORD_COUNT, prose.getLongWordCount().intValue());
  }

  @Test
  public void testComplexWordCount() {
    assertEquals("Complex Word Count: ", ProseTest.COMPLEX_WORD_COUNT, prose.getComplexWordCount().intValue());
  }

  @Test
  public void testUniqueWordCount() {
    assertEquals("Unique Word Count: ", ProseTest.UNIQUE_WORD_COUNT, prose.getUniqueWordCount().intValue());
  }

  @Test
  public void testSentenceCount() {
    assertEquals("Sentence Count: ", ProseTest.SENTENCE_COUNT, prose.getSentenceCount().intValue());
  }

  @Test
  public void testPov() {
    assertEquals("Point of View: ", PovType.FIRST, prose.getPov());
  }

  @Test
  public void testSumFragmentWordCount() {
    assertEquals("Dialogue word count + narrative word count = total word count: ", ProseTest.WORD_COUNT,
        prose.getDialogueWordCount().intValue() + prose.getNarrativeWordCount().intValue());
  }

}
