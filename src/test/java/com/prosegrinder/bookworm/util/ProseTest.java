package com.prosegrinder.bookworm.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class ProseTest {

  /** Log4j Logger. **/
  private static final Logger logger = LogManager.getLogger(SyllableDictionaryTest.class);
  private String text;
  private Prose prose;

  @Before
  public void loadProse() throws IOException, URISyntaxException {
    String prose = "shunn/shortstory.txt";
    ClassLoader classLoader = ProseTest.class.getClassLoader();
    Path prosePath = Paths.get(classLoader.getResource(prose).toURI());
    List<String> lines = Files.readAllLines(prosePath);
    this.text = String.join("\n",lines);
    this.prose = new Prose(this.text);
  }

  @Test
  public void testProse() {
    assertEquals("Word Count: ", 1528, prose.getWordCount().intValue());
    assertEquals("Sentence Count: ", 90, prose.getSentenceCount().intValue());

    assertEquals("Dialogue word count + narrative word count = total word count: ", 1528,
        prose.getDialogueWordCount().intValue() + prose.getNarrativeWordCount().intValue());

    logger.info("Syllable Count: " + prose.getSyllableCount());
//     logger.info("Word Count: " + prose.getWordCount());
//     logger.info("Sentence Count: " + prose.getSentenceCount());
    logger.info("Complex Word Count: " + prose.getComplexWordCount());
    logger.info("Long Word Count: " + prose.getLongWordCount());
    logger.info("Average Syllables per Word: " + prose.getAverageSyllablesPerWord());
    logger.info("Average Words per Sentence: " + prose.getAverageWordsPerSentence());
    logger.info("Dialogue Fragment Word Count: " + prose.getDialogueWordCount());
    logger.info("Narrative Fragment Word Count: " + prose.getNarrativeWordCount());
    logger.info("Point of View: " + prose.getPov());
    logger.info("Unique Words: " + prose.getUniqueWords().size());
//     int scount = 1;
//     for (Paragraph paragraph: prose.getParagraphs()) {
//       for (Sentence sentence: paragraph.getSentences()) {
//         logger.info(scount + ":\t" + sentence);
//         scount++;
//       }
//     }
  }

}
