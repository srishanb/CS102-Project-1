CS-102 - PROJECT 1 (2026 FALL) - TENNIS DATABASE MANAGER
Student: Srishan Bommidi
Instructor: Prof. Giuseppe Turini - Kettering University


1. HOW TO COMPILE AND RUN

From the folder containing Project1.java (the input file must be in the same folder):

   javac Project1.java TennisDatabase/*.java
   java Project1 example_input_file.txt

The program takes exactly 1 run argument: the name of the input file.
The project was compiled and tested with Java 24 (javac 24).


2. HOW TO USE THE MENU

   1 --> Print All Players            Sorted by player id, with the win/loss record of each player.
   2 --> Print All Matches of a Player  Asks for a player id. Most recent match first.
   3 --> Print All Matches            Most recent match first, with the full names of the players.
   4 --> Insert New Player/Match      Asks for a line in the same format of the input file, examples:
                                         PLAYER/FED81/ROGER/FEDERER/1981/SWITZERLAND
                                         MATCH/20150316/DJO87/FED81/INDIAN WELLS/WINNER1/6-3,6-7,6-2
   5 --> Delete Player/Match          Asks what to delete (1 = player, 2 = match), then asks for the id.
                                      A match id is date+idplayer1+idplayer2, example: 20150316DJO87FED81
   6 --> Exit

Input is not case sensitive: all data is converted to uppercase and stored in uppercase.


3. BEHAVIOR NOTES

- Run argument missing, or input file not found: an error message is printed, and the program
  continues to the menu with an empty database (data can still be inserted with command 4).
- Input file with a PLAYER line after a MATCH line: the whole file is discarded (as required),
  an error message is printed, and the database is left empty.
- Input file with some invalid lines (missing data, invalid data, duplicate player id, duplicate
  match id, match with a player not in the database, unknown line type): the invalid lines are
  skipped, all the valid lines are loaded, and the skipped lines are reported (line number and reason).
- Empty lines in the input file are ignored.
- Data validation: a player id must be 5 characters, a birth year must be a 4-digit number, a match
  date must be an 8-digit number (YYYYMMDD) with month 01-12 and day 01-31, the 2 players of a match
  must be different and both in the database, the winner must be WINNER1 or WINNER2, and the score
  must not be empty and must not include spaces.
- Deleting a player deletes also all the matches of that player (from the container of matches,
  and from the lists of matches of the opponents), because a match needs both its players.
- The win/loss record of a player is computed from the list of matches of that player (in the
  container of players) every time the player data is requested, so it is always up to date.
- Matches played on the same date are all stored and printed together; among them, the match
  inserted last is listed first.


4. NOTES ON THE REQUIREMENTS (DEVIATIONS AND ADDITIONS)

4.1 Fix in a provided class (SortedSinglyLinkedList.java)
    The provided method delete( int i ) removed the node but did not update the number of items
    (numItems), so size() returned a wrong value after any deletion. 1 line has been added at the
    end of delete(): "this.numItems--;". The line is marked with a comment in the code.
    Nothing else has been changed in the provided classes and interfaces.

4.2 Methods not included in the provided interfaces
    The provided interfaces do not include a method to delete a single match from the lists of
    matches of the players, and a method to search a single match by id. These 2 package-private
    methods have been added (they are used by the class TennisDatabase to delete a match):
    - TennisPlayerContainer.deleteMatch( TennisMatch m )
    - TennisMatchContainer.getMatch( String matchId )

4.3 Class TennisPlayerContainerNode
    This package-private class (from the in-class code) implements the node of the circular
    doubly-linked list of players: it stores 1 player and the list of matches of that player.

4.4 Class TennisPlayer
    The methods getWins() and getLosses() have been added (player statistics, used to print the
    win/loss record), with a package-private method setWinLossRecord() used by the container.

4.5 Compilation warnings
    The project compiles with no warnings using "javac". Using "javac -Xlint:all", the only 2
    warnings ("serial") are about the 2 provided exception classes, that have not been modified.


5. AI CITATION BLOCK

5.1 AI tool used
    Claude Code (Anthropic), model Claude Fable 5.2, used on 2026-11-15.
    The AI tool was given the project instructions (PDF), the provided code, and the in-class code.
    The project was completed starting from the in-class code of 2026-11-11.

5.2 Exact prompts used (in order)

    Prompt 1 (with the folders "in-class code" and "instructions + given" attached):
    "analyze these and create a very detailed plan with the strategies used in in class code in
    mind, I want to work and create a plan with you, so ask me questions as you go"

    Answers given to the questions asked by the AI tool (the option selected for each question):
    - How to split the coding work: "Claude writes, I verify"
    - Project deadline: "Tomorrow, Nov 16"
    - Provided SortedSinglyLinkedList.delete() not updating the number of items: "Fix it and document"
    - Hints from class not included in the files: "Nothing beyond files"
    - Win/loss record of a player: "Count on demand"
    - Input file missing or discarded: "Menu, empty database"
    - Invalid line in an input file: "Skip and report"
    - Location of the AI Citation Block: "readme.txt + short headers"

    Prompt 2:
    "you can run and test in stages, if you have a question, a big decision coming up, or an error,
    you should show me, analyze the error, explain the intended function of the code, and explain
    why it broke and how it can be fixed. No Emojis, as I want to keep this project professional.
    This plan is good, but show me all of the changes you took your own definition and vision on
    and I will think if that is what I want to do."

    Prompt 3:
    "keep everything as shown in class, do not depart from in class code. do not depart from
    project instructions. everything else is good"

    Answers given to the questions asked by the AI tool (the option selected for each question):
    - Where to keep the parsing of a PLAYER line: "Leave in place, duplicate"
    - Use of recursion: "Stay iterative"

    Then the final plan written by the AI tool was approved, and the AI tool wrote the code.

5.3 Summary of how the AI-generated output was reviewed/verified/modified/applied

    Applied:
    - The AI-generated code was applied on top of the in-class code. The in-class code was kept as
      written: the only in-class lines changed are the placeholders (TODO and dummy return values)
      and the following fixes, each one requested by the project instructions or needed to fix a bug:
      (1) TennisPlayerContainer.insertPlayer: "this.head = currNodeRef" changed to
          "this.head = newNode" (a new player with the smallest id was not becoming the first node).
      (2) TennisPlayerContainer: "public" removed (the class must be package-private).
      (3) TennisMatch.getDate: month and day zero-padded (format YYYY-MM-DD).
      (4) TennisMatch.getId: date without separators (example: 20150316DJO87FED81).
      (5) Project1.main: run argument and console input checked (main must not throw exceptions).
      (6) The fields left commented in class (container of matches, list of matches) were enabled.
      (7) TennisPlayerContainer.insertPlayer: typo fixed in the error message printed to the user
          ("Player insertion failer" changed to "Player insertion failed").
    - The design decisions were taken by the student, selecting among the alternatives proposed by
      the AI tool (see the answers in 5.2). All the design decisions initially taken by the AI tool
      on its own were listed (34 items) and reviewed; the ones departing from the in-class code
      (shared search helper, binary search, different console reading, node constructor) were
      rejected by the student, and replaced with the solutions shown in class.

    Verified (tests run during development, all passed):
    - Containers: 80 checks with a test driver (not included in the submission): sorted insertion
      of players in scrambled order (including new smallest and new greatest id), duplicate ids,
      search (forward and backward), deletion of first/middle/last/only player, win/loss records,
      insertion and deletion of matches in the lists of the players, array growth and shrinking,
      matches on the same date, duplicate match ids, deletion of first/middle/last match.
    - Example input file: players DJO87, FED81, NAD86 with records 6-2, 3-5, 0-2; the 9 matches
      listed from 2015-11-16 (ATP FINALS) to 2015-02-23 (DUBAI); these values were also computed
      by hand from the input file.
    - Input files with errors: PLAYER line after MATCH line (file discarded), 16 kinds of invalid
      lines (each one skipped and reported), empty file, file not found, run argument missing.
    - Console: invalid menu selections (text, out of range, decimal number), end of input,
      insertions (duplicate player, duplicate match, match with unknown player, invalid lines),
      deletions (match, player with all the matches, ids not found, all data deleted).
    - Larger input file (10 players, 24 matches, 4 matches on the same date): the output of the
      program was compared with the results computed independently from the same file (sorting
      and win/loss records), before and after deleting a player and a match.
    - A comparison (diff) between the submitted files and the in-class code was used to check
      that no in-class line was changed, except the placeholders and the fixes listed above.

    Reviewed by the student:
    - Stage 0 (Setup): Checked the copied 11/11 in-class folder and made sure the baseline code
      compiled before adding anything.
    - Stage 1 (Data classes): Checked TennisPlayer and TennisMatch. Verified that match dates
      zero-pad the month and day, and that getId() formats the string with no dashes.
    - Stage 2 (Player container): Read through TennisPlayerContainer and the node class. Checked
      the head pointer fix in insertPlayer (newNode instead of currNodeRef) and the numItems--
      fix in SortedSinglyLinkedList.delete().
    - Stage 3 (Match container): Reviewed the dynamic array resizing and shifting loops in
      TennisMatchContainer to make sure matches stay sorted from most recent to oldest.
    - Stage 4 (Database): Checked loadInputFile in TennisDatabase to make sure it still uses the
      in-class Scanner style, skips invalid lines, and clears the database if a PLAYER line shows
      up after a MATCH line.
    - Stage 5 (Console UI): Tested the menu options in Project1 by hand (printing, inserting,
      deleting, and typing bad menu inputs) to make sure it doesn't crash.
    - Stage 6 & 7 (Testing & cleanup): Checked the output against example_input_file.txt and the
      test files, and compared the final code with the 11/11 in-class code to make sure the
      professor's style and comments were kept.
