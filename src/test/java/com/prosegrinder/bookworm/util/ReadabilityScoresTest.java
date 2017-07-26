package com.prosegrinder.bookworm.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
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
  private static final Logger logger = LogManager.getLogger(ReadabilityScoresTest.class);
  private Prose prose;
  private static final NumberFormat formatter = new DecimalFormat("#0.000");

  private static final int COMPLEX_WORD_COUNT = 202;
  private static final int LONG_WORD_COUNT = 275;
  private static final int SENTENCE_COUNT = 90;
  private static final int SYLLABLE_COUNT = 2287;
  private static final int WORD_COUNT = 1528;
  private static final int CHARACTER_COUNT = 7008;

  @Before
  public void loadProse() throws IOException, URISyntaxException {
    String prose = "shunn/shortstory.txt";
    ClassLoader classLoader = ReadabilityScoresTest.class.getClassLoader();
    Path prosePath = Paths.get(classLoader.getResource(prose).toURI());
    List<String> lines = Files.readAllLines(prosePath);
    this.prose = new Prose(String.join("\n", lines), Dictionary2.getDictionary());
  }

  @Test
  public void testReadabilityScores() {

    ReadabilityScores expectedScores = new ReadabilityScores(CHARACTER_COUNT, COMPLEX_WORD_COUNT,
        LONG_WORD_COUNT, SENTENCE_COUNT, SYLLABLE_COUNT, WORD_COUNT);

    ReadabilityScores scores = new ReadabilityScores(prose);
    assertEquals("Automated Readability Index: ", expectedScores.getAutomatedReadabilityIndex(),
        scores.getAutomatedReadabilityIndex());
    assertEquals("Coleman Liau Index: ", expectedScores.getColemanLiauIndex(),
        scores.getColemanLiauIndex());
    assertEquals("Flesch Kincaid Grade Level: ", expectedScores.getFleschKincaidGradeLevel(),
        scores.getFleschKincaidGradeLevel());
    assertEquals("Flesch Reading Ease: ", expectedScores.getFleschReadingEase(),
        scores.getFleschReadingEase());
    assertEquals("Gunning Fox Index: ", expectedScores.getGunningFogIndex(),
        scores.getGunningFogIndex());
    assertEquals("LIX: ", expectedScores.getLix(), scores.getLix());
    assertEquals("RIX: ", expectedScores.getRix(), scores.getRix());
    assertEquals("SMOG Index: ", expectedScores.getSmog(), scores.getSmog());

    logger.info(
        "Automated Readability Index: " + formatter.format(scores.getAutomatedReadabilityIndex()));
    logger.info("Coleman Liau Index: " + formatter.format(scores.getColemanLiauIndex()));
    logger.info("Flesch Reading Ease: " + formatter.format(scores.getFleschReadingEase()));
    logger.info(
        "Flesch Kincaid Grade Level: " + formatter.format(scores.getFleschKincaidGradeLevel()));
    logger.info("Gunning Fox Index: " + formatter.format(scores.getGunningFogIndex()));
    logger.info("LIX: " + formatter.format(scores.getLix()));
    logger.info("RIX: " + formatter.format(scores.getRix()));
    logger.info("SMOG Index: " + formatter.format(scores.getSmog()));
  }

}
