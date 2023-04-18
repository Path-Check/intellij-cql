<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# intellij-cql Changelog

## Unreleased

## 0.1.3 - 2023-04-18
Upgrading CQL+Evaluator to 2.7 and 2.6 respectively
Enabling all IDEA versions

## 0.1.2
Upgrading version compatibility to account for new IDEA versions.

## 0.1.1
Enhanced Model-based code completion

## 0.1.0
Semantic code completion

## 0.0.6
- Contextual Find Usages of a function or variable
- Refactoring: renames now rename all the referenced entities.

## 0.0.5
- Adding tooltip for errors.
- Bugfix for TextRanges: End char is inclusive.
- Bugfix for FindUsages: must return a leaf element.
- Bugfix for Commenters: prefixes and suffixes can't have spaces.
- Bugfix for lexer exceptions (define " EOF)
- Bugfix for empty folding groups.

## 0.0.4

### Added
- Added folding support
- Filename check. It should use the format <libraryName>-<version>.cql
- Reference Finder when clicking in the Identifier.

## 0.0.3

### Added
- Adds a Code Completion for keywords in the language
- Bugfix: Only shows errors in the current file.
- Imports <name>-<version>.cql files in the same directory
- Adds warnings and messages from the CQL compiler as Annotations

## 0.0.2

### Added
- Fixes NullPointerExceptions in concurrent object creation
- Fixes QName duplication by removing UCUM's XPP3 dependency.

## 0.0.1

### Added
- Adds syntax highlighting, semantic highlighting, compilation, and local execution for the HL7 Clinical Quality Language (CQL).
