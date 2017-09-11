package com.prosegrinder.bookworm.util;

import java.util.List;
import java.util.Set;

/**
 * It's a container of other containers. Calling it ContainerContainer felt, well, just silly.
 * This provides a base abstract class for things like Paragraphs, Sections, Chapters, etc., 
 * which have meaning beyond just a string of words (like WordContainer).
 * 
 */
public abstract class AggregateContainer extends Container {

  protected void aggregateContainers(List<Container> containers) {
    this.setWordCharacterCount(containers.stream()
        .mapToInt( container -> container.getWordCharacterCount())
        .sum());
    this.setSyllableCount(containers.stream()
        .mapToInt( container -> container.getSyllableCount())
        .sum());
    this.setWordCount(containers.stream()
        .mapToInt( container -> container.getWordCount())
        .sum());
    this.setComplexWordCount(containers.stream()
        .mapToInt( container -> container.getComplexWordCount())
        .sum());
    this.setLongWordCount(containers.stream()
        .mapToInt( container -> container.getLongWordCount())
        .sum());
    this.setPovWordCount(containers.stream()
        .mapToInt( container -> container.getPovWordCount())
        .sum());
    this.setFirstPersonWordCount(containers.stream()
        .mapToInt( container -> container.getFirstPersonWordCount())
        .sum());
    this.setSecondPersonWordCount(containers.stream()
        .mapToInt( container -> container.getSecondPersonWordCount())
        .sum());
    this.setThirdPersonWordCount(containers.stream()
        .mapToInt( container -> container.getThirdPersonWordCount())
        .sum());
  
    containers.stream().forEach( container -> {
      Set<Word> uniqueWords = container.getUniqueWords();
      uniqueWords.stream().forEach( word -> {
        this.setWordFrequency(word, this.getWordFrequency(word) + container.getWordFrequency(word));
      });
    });    
  }
  
}
