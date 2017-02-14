[![Build Status](https://travis-ci.org/davidlday/bookworm.svg?branch=develop)](https://travis-ci.org/davidlday/bookworm)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/094675d4098d41c0a78adaaa87912259)](https://www.codacy.com/app/davidlday/bookworm?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=davidlday/bookworm&amp;utm_campaign=Badge_Grade)

# bookworm

A free, lightweight text analytics library for prose fiction.

Bookworm is an attempt to discover relevant statics in fiction. An initial experiment, developed in Python and NLTK, is hosted at http://bookworm.davidlday.com, and demonstrates some initial findings at discovering basic editor preference across 10 relatively popular and well-respected SF/F/H magazines.

The goal of this library is to encourage the application of practical, meaningful statistical analysis in the submission process, a critical part of the publishing value chain, and to provide a foundation for creating an open platform for analysing prose fiction.

This library is written in Java, uses Maven as its build mechanism, and has minimal dependencies. It is constructed from the ground up to target specific elements of fiction, such as words instead of general tokens, structure, point of view, and dialogue.

The main class is com.prosegrinder.bookworm.util.Prose, which takes a single block of text (short story, novel, etc) as the argument to its constructor, and provides methods for getting at all of the underlying elements and statistics.

Also included is a com.prosegrinder.bookworm.util.ReadabilityScores class, which provides a consistent set of readability statitics. In my research, I found variation among publicly accessible formulas which could lead to inconsistent results.

A sister project, [bookworm-restserver](https://github.com/davidlday/bookworm-restserver), wraps this library in a self-contained, deployable REST API for easy use across various languages. See that project for more details on plans around the REST API.

## Download

When ready, binary downloads will be available at: https://github.com/davidlday/bookworm/releases

## Documentation

WIKI: https://github.com/davidlday/bookworm/wiki
Issue tracker: https://github.com/davidlday/bookworm/issues

## Building

#### Prerequisites:
 1. Java (JDK) 1.8+
 2. Apache Maven 3+
 3. Internet access
 
 #### Build
```sh
git clone https://github.com/davidlday/bookworm.git bookworm
cd bookworm
mvn install
```
This will put the library in your local Maven repository.

## Notes

- Bookworm is a free library, but still considered experimental.
- I'm not currently accepting pull requests, but will entertain feature suggestions. Pull requests will be welcome once the core library is relatively stable.
