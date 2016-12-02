package com.prosegrinder.bookworm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.junit.Test;

public class ParserTest {

  /** Need to find suitable prose for testing. **/
  private String getProse() throws IOException {
    ClassLoader classLoader = this.getClass().getClassLoader();
    String prose = new String(
      Files.readAllBytes(
        Paths.get(
          classLoader.getResource("prose.txt").getFile()
        )
      )
    );
    return prose;
  }

  @Test
  public void testParseWords() throws Exception {
    String prose = this.getProse();
    List<String> words = Parser.parseWords(prose);
  }

  @Test
  public void testParseDialogue() throws Exception {
    String prose = this.getProse();
    List<String> dialogue = Parser.parseDialogue(prose);
  }

}

