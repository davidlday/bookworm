package com.prosegrinder.bookworm.util;

import com.prosegrinder.bookworm.enums.PovType;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
// import org.junit.Ignore;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

// @Ignore
public class ProseTest {

  /** Log4j Logger. **/
  private static final Logger logger = LogManager.getLogger(ProseTest.class);
  
  private Prose prose;

  private static final int COMPLEX_WORD_COUNT = 202;
  private static final int LONG_WORD_COUNT = 275;
  private static final int SENTENCE_COUNT = 90;
  private static final int SYLLABLE_COUNT = 2287;
  private static final int UNIQUE_WORD_COUNT = 526;
  private static final int WORD_COUNT = 1528;

  private final Dictionary2 getDictionary(Config config) throws IOException {
    String cmudictFile = config.getString("cmudict.file");
    Long maxWordCacheSize = config.getLong("wordCache.maxentries");
    Long ttlSecondsNonWordCache = config.getLong("nonWordCache.ttlSeconds");
    Boolean cacheNumbers = config.getBoolean("nonWordCache.cacheNumbers");
    Dictionary2 dictionary;
    try {
      dictionary =
          new Dictionary2(cmudictFile, maxWordCacheSize, ttlSecondsNonWordCache, cacheNumbers);
      return dictionary;
    } catch (IOException ioe) {
      logger.error(ioe.getMessage());
      throw ioe;
    }
  }

  private final Dictionary2 getDictionary() throws IOException {
    Config config = ConfigFactory.load().getConfig("com.prosegrinder.bookworm.util.dictionary");
    try {
      return this.getDictionary(config);
    } catch (IOException ioe) {
      throw ioe;
    }
  }
  
  @Before
  public void loadProse() throws IOException, URISyntaxException {
    String prose = "shunn/shortstory.txt";
    ClassLoader classLoader = ProseTest.class.getClassLoader();
    Path prosePath = Paths.get(classLoader.getResource(prose).toURI());
    List<String> lines = Files.readAllLines(prosePath);
    this.prose = new Prose(String.join("\n", lines), this.getDictionary());
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
    assertEquals("Point of View: ", PovType.FIRST, prose.getPov());
  }

  @Test
  public void testSumFragmentWordCount() {
    assertEquals("Dialogue word count + narrative word count = total word count: ",
        ProseTest.WORD_COUNT,
        prose.getDialogueWordCount().intValue() + prose.getNarrativeWordCount().intValue());
  }

}
