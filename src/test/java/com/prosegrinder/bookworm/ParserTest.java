package com.prosegrinder.bookworm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.File;
import java.io.FileReader;
import org.junit.Test;

public class ParserTest {

  private String getProse() throws IOException {
    // http://javarevisited.blogspot.com/2015/09/how-to-read-file-into-string-in-java-7.html
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
//     words.stream().forEach(System.out::println);
//     System.out.println("Word Count: " + words.size());
  }

  @Test
  public void testParseDialogue() throws Exception {
    String prose = this.getProse();
    List<String> dialogue = Parser.parseDialogue(prose);
//     dialogue.stream().forEach(System.out::println);
  }

}

