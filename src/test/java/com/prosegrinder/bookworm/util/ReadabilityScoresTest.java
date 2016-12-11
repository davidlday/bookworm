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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ReadabilityScoresTest {

  /** Log4j Logger. **/
  private static final Logger logger = LogManager.getLogger(SyllableDictionaryTest.class);
  private String text;

  @Before
  public void loadProse() throws IOException {
    String cmudict = "shunn/shortstory.txt";
    ClassLoader classLoader = ProseTest.class.getClassLoader();
    Path prosePath = Paths.get(classLoader.getResource(cmudict).getFile());
    List<String> lines = Files.readAllLines(prosePath);
    this.text = String.join("\n",lines);
  }

  @Test
  public void testReadabilityScores() {
  Integer characterCount = 6965;
  Integer complexWordCount = 188;
  Integer longWordCount = 284;
  Integer sentenceCount = 90;
  Integer syllableCount = 2301;
  Integer wordCount = 1535;

  ReadabilityScores scores = new ReadabilityScores(characterCount, complexWordCount,
      longWordCount, sentenceCount, syllableCount, wordCount);

//     Prose prose = new Prose(this.text);
//     ReadabilityScores scores = new ReadabilityScores(prose);
    logger.info("Automated Readability Index: " + scores.getAutomatedReadabilityIndex());
    logger.info("Coleman Liau Index: " + scores.getColemanLiauIndex());
    logger.info("Flesch Reading Ease: " + scores.getFleschReadingEase());
    logger.info("Flesch Kincaid Grade Level: " + scores.getFleschKincaidGradeLevel());
    logger.info("Gunning Fox Index: " + scores.getGunningFogIndex());
    logger.info("LIX: " + scores.getLix());
    logger.info("SMOG Index: " + scores.getSmog());
//     logger.info(prose.getInitialText());
  }

}
