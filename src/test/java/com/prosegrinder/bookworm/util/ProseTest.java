package com.prosegrinder.bookworm.util;

import com.prosegrinder.bookworm.enums.PovType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
// import org.junit.Rule;
// import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ProseTest {

  /** Log4j Logger. **/
  private static final Logger logger = LogManager.getLogger(SyllableDictionaryTest.class);
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

//   @Test
//   public void testWordsInDictionary() {
//     Set<Word> words = prose.getUniqueWords();
//     words.stream()
//         .forEach( word -> {
//             if (!word.isDictionaryWord()) {
//               logger.info("Not in dictionary: " + word + " (" + word.getSyllableCount() + ")");
//             }
//         });
//   }

  @Test
  public void testSyllableCount() {
    assertEquals("Syllable Count: ", this.SYLLABLE_COUNT, prose.getSyllableCount().intValue());
  }

  @Test
  public void testWordCount() {
    assertEquals("Word Count: ", this.WORD_COUNT, prose.getWordCount().intValue());
  }

  @Test
  public void testLongWordCount() {
    assertEquals("Long Word Count: ", this.LONG_WORD_COUNT, prose.getLongWordCount().intValue());
  }

  @Test
  public void testComplexWordCount() {
    assertEquals("Complex Word Count: ", this.COMPLEX_WORD_COUNT, prose.getComplexWordCount().intValue());
  }

  @Test
  public void testUniqueWordCount() {
    assertEquals("Unique Word Count: ", this.UNIQUE_WORD_COUNT, prose.getUniqueWordCount().intValue());
  }

  @Test
  public void testSentenceCount() {
    assertEquals("Sentence Count: ", this.SENTENCE_COUNT, prose.getSentenceCount().intValue());
  }

  @Test
  public void testPov() {
    assertEquals("Point of View: ", PovType.FIRST, prose.getPov());
  }

  @Test
  public void testSumFragmentWordCount() {
    assertEquals("Dialogue word count + narrative word count = total word count: ", this.WORD_COUNT,
        prose.getDialogueWordCount().intValue() + prose.getNarrativeWordCount().intValue());
  }

}
