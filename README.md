# Data Masker

A command-line tool which hides sensitive information in a JSON file through leveraging regex patterns as a rule set to determine which data elements should be masked.

## Table of Contents

- [Getting Started](#getting-started)
    - [Usage](#usage)
        - [Example](#example)
- [Development](#development)
    - [Testing](#testing)
    - [Tools](#tools)
    - [Project Structure](#project-structure)
- [Challenges](#challenges)
    - [During Testing](#during-testing)
    - [During Development](#during-development)

## Getting Started

### Usage

Run `DataMasker.jar` from the command-line:

    java -jar DataMasker.jar <path-to-data-json> <path-to-rules-json>

#### Example

    jar -jar DataMasker.jar C:\Masker\people.json C:\Masker\a.rules.json

## Development

### Testing

The task was approached using TDD, utilising Jupiter's JUnit 5 API to write and execute the unit tests, you can find these in `DataMasker/test`.

### Tools

- *Checkstyle* was used in development to adhere to [Google's Java coding standards](https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/google_checks.xml).

- *JavaDoc* was utilised for documentation, some methods in `DataMasker/src` do not contain JavaDoc as Google's Checkstyle guidelines do not require short methods to be documented.

### Project Structure

- `DataMasker/resources` holds the multiple versions of the data and rule json files.
    - `./dummy` holds *unedited* data json files, so they can be copied into the json files which are actually used for testing in each unit test.
    - `./masked` holds permutations of *manually masked* data files with different rules, these are used in unit tests as comparisons. 
- `DataMasker/test` is a directory which holds all the unit tests for this program.

## Challenges

### During Testing

An immediate challenge was trying to make unit tests repeatable, the program masks JSON data by modifying the *original* json file, so you couldn't re-run a test case as the file was already masked.

- I solved this problem by maintaining a directory of [unedited data json files](#project-structure) in `DataMasker/resources/dummy` which gets copied into the *original* data json files in `DataMasker/resources` before each unit test.

I also encountered problem where the *dummy* data json files would be locked as there was another process opening them: `Files.copy`'s interface cannot be closed manually, so you rely on *Java's garbage collector* to automatically close the files.

- To solve this, I manually ran the garbage collector with `System.gc()` in the `setup()` method of `TestDataMasker`.
### During Development

Initially, I tried to handle regex with `String.replaceAll()`, this, however, gave incorrect results when I tried to mask `nuts.json` with `c.rules.json`. `String.replaceAll()` replaces each occurrence of the pattern with a *fixed length* string, without considering the varying lengths of each matching segment e.g. `peanuts` would be masked as `*******` instead of `pea***s`.

- To address this, I switched to using Java's `Pattern` and `Matcher` classes, which provided more control when handling regex.