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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class ReadabilityScoresTest {

  /** Log4j Logger. **/
  private static final Logger logger = LogManager.getLogger(SyllableDictionaryTest.class);
  private String text;
  private Prose prose;
  private static final NumberFormat formatter = new DecimalFormat("#0.000");

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
  public void testReadabilityScores() {
//     Integer characterCount = 6965;
//     Integer complexWordCount = 188;
//     Integer longWordCount = 284;
//     Integer sentenceCount = 90;
//     Integer syllableCount = 2301;
//     Integer wordCount = 1535;

    /** https://www.online-utility.org/english/readability_test_and_improve.jsp **/
//     Integer characterCount = 7009;
//     Integer complexWordCount = 188;
//     Integer longWordCount = 284;
//     Integer sentenceCount = 90;
//     Integer syllableCount = 2370;
//     Integer wordCount = 1529;
//
//     ReadabilityScores scores = new ReadabilityScores(characterCount, complexWordCount,
//         longWordCount, sentenceCount, syllableCount, wordCount);
//
    ReadabilityScores scores = new ReadabilityScores(prose);

    logger.info("Automated Readability Index: " + formatter.format(scores.getAutomatedReadabilityIndex()));
    logger.info("Coleman Liau Index: " + formatter.format(scores.getColemanLiauIndex()));
    logger.info("Flesch Reading Ease: " + formatter.format(scores.getFleschReadingEase()));
    logger.info("Flesch Kincaid Grade Level: " + formatter.format(scores.getFleschKincaidGradeLevel()));
    logger.info("Gunning Fox Index: " + formatter.format(scores.getGunningFogIndex()));
    logger.info("LIX: " + formatter.format(scores.getLix()));
    logger.info("SMOG Index: " + formatter.format(scores.getSmog()));
//     logger.info(prose.getInitialText());
  }

}
