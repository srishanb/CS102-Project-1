


// CS-102: "Computing and Algorithms I"
// Project 1.
// Prof. Giuseppe Turini
// Kettering University
// 2026-10-21
//
// Student: Srishan Bommidi (project completed on 2026-11-15, starting from the in-class code).
// AI Citation: this file includes AI-generated code (tool: Claude Code, model: Claude Fable 5.2).
// See readme.txt ("AI Citation Block") for the exact prompts and the review/verification summary.

package TennisDatabase; // This package is implemented by a subfolder with the same name.

import java.io.File; // To use the Java File class.
import java.util.Scanner; // To use the Java Scanner class.
import java.io.FileNotFoundException; // ...
import java.util.InputMismatchException; // Thrown by a Scanner if a token is not of the expected type.
import java.util.NoSuchElementException; // Thrown by a Scanner if there are no more tokens.

// Define a public class TennisDatabase included in a package with the same name (TennisDatabase),
// and implementing the InterfaceTennisDatabase.
// This class contains methods which allow to manipulate a collection of tennis records
// and includes 2 containers:
// (1) a container of type TennisPlayerContainer storing all players, and
// (2) a container of type TennisMatchContainer storing all matches.
public class TennisDatabase implements InterfaceTennisDatabase {

   private TennisPlayerContainer tpc; // Internal container for tennis players.
   private TennisMatchContainer tmc; // Internal container for tennis matches.

   // My own custom "default constructor".
   public TennisDatabase() {
      // Init both internal container for tennis players and matches.
      this.tpc = new TennisPlayerContainer(); // Empty container ready to be used.
      this.tmc = new TennisMatchContainer(); // Empty container ready to be used.
   }

   // Desc.: Loads data from input file following the format described in the specifications.
   // Input: The name of the input file.
   // Output: Throws an unchecked (non-critical) exception if the loading is not fully successfull.
   //         Throws a checked (critical) exception if the file (file name) does not exist.
   //         Throws a checked (critical) exception if the file is discarded (PLAYER line after a MATCH line).
   // NOTE: an invalid file line is skipped (the loading continues), and reported in the unchecked exception.
   // NOTE: if the file is discarded, the database is left empty.
   public void loadInputFile( String fileName ) throws TennisDatabaseException,
                                                       TennisDatabaseRuntimeException {
      // Check if input is valid.
      if(fileName == null) { throw new TennisDatabaseException("Load input file failed, file name invalid!"); }
      String skippedLinesReport = ""; // Description of all file lines skipped because invalid (if any).
      int currFileLineNumber = 0; // Number of current file line (the first file line is line 1).
      boolean matchLineFound = false; // True after the first MATCH line (then no PLAYER line is allowed).
      boolean fileDiscarded = false; // True if the input file format is violated (PLAYER line after MATCH line).
      // Monitor for exceptions related to file I/O and file parsing.
      try {
         File file = new File(fileName); // Create a file object from the file name in input.
         Scanner scannerFile = new Scanner(file); // Scanner to read file data.
         // Read the input file line by line.
         while(scannerFile.hasNextLine()) {
            String currFileLine = scannerFile.nextLine();
            currFileLineNumber++;
            // NOTE: The data loaded from inputs should be stored/processed internally in uppercase.
            currFileLine = currFileLine.trim().toUpperCase();
            // Process current file line only if it is not empty (empty lines are ignored).
            if(currFileLine.length() > 0) {
               // Parsing a file line data using another scanner.
               Scanner scannerLine = new Scanner(currFileLine); // Init scanner to parse current file line.
               scannerLine.useDelimiter("/"); // Set delimiter so scanner can split curren file line.
               // Monitor for exceptions related to the data of current file line (parsing and insertion).
               try {
                  String currFileLineToken = scannerLine.next();
                  // Check if current file line is a PLAYER line or a MATCH line.
                  if(currFileLineToken.equals("PLAYER")) {
                     // Check input file format: all PLAYER lines must be listed before any MATCH line.
                     if(matchLineFound) {
                        fileDiscarded = true; // Critical error, the whole input file must be discarded.
                        throw new TennisDatabaseException("Load input file failed, file discarded: " +
                                                          "PLAYER line after MATCH line (line " +
                                                          currFileLineNumber + ")!");
                     }
                     // Parse a PLAYER line, splitting all player data into individual pieces of info.
                     String newPlayerId = scannerLine.next();
                     String newPlayerFirstName = scannerLine.next();
                     String newPlayerLastName = scannerLine.next();
                     int newPlayerBirthYear = scannerLine.nextInt(); //String newPlayerBirthYear = scannerLine.next();
                     String newPlayerCountry = scannerLine.next();
                     // Check if the PLAYER line has more data than expected.
                     if(scannerLine.hasNext()) { throw new TennisDatabaseException("PLAYER line invalid, too much data!"); }
                     // Check if the PLAYER line data is valid.
                     this.checkPlayerData(newPlayerId,
                                          newPlayerFirstName,
                                          newPlayerLastName,
                                          newPlayerBirthYear,
                                          newPlayerCountry);
                     // Build a TennisPlayer object from the PLAYER line data.
                     TennisPlayer newPlayer = new TennisPlayer(newPlayerId,
                                                               newPlayerFirstName,
                                                               newPlayerLastName,
                                                               newPlayerBirthYear,
                                                               newPlayerCountry);
                     // Insert newly created TennisPlayer object into proper container.
                     this.tpc.insertPlayer(newPlayer);
                  }
                  else if(currFileLineToken.equals("MATCH")) {
                     matchLineFound = true; // From now on, no PLAYER line is allowed in the input file.
                     // Parse a MATCH line, splitting all match data into individual pieces of info.
                     String newMatchDate = scannerLine.next();
                     String newMatchIdPlayer1 = scannerLine.next();
                     String newMatchIdPlayer2 = scannerLine.next();
                     String newMatchTournament = scannerLine.next();
                     String newMatchWinner = scannerLine.next();
                     String newMatchScore = scannerLine.next();
                     // Check if the MATCH line has more data than expected.
                     if(scannerLine.hasNext()) { throw new TennisDatabaseException("MATCH line invalid, too much data!"); }
                     // Check if the MATCH line data is valid.
                     this.checkMatchData(newMatchDate,
                                         newMatchIdPlayer1,
                                         newMatchIdPlayer2,
                                         newMatchTournament,
                                         newMatchWinner,
                                         newMatchScore);
                     // Build a TennisMatch object from the MATCH line data.
                     TennisMatch newMatch = this.buildMatch(newMatchDate,
                                                            newMatchIdPlayer1,
                                                            newMatchIdPlayer2,
                                                            newMatchTournament,
                                                            newMatchWinner,
                                                            newMatchScore);
                     // Insert newly created TennisMatch object into proper containers.
                     this.addMatch(newMatch);
                  }
                  else {
                     // Current file line is neither a PLAYER line nor a MATCH line.
                     throw new TennisDatabaseException("Line invalid, not a PLAYER line and not a MATCH line!");
                  }
               }
               catch(TennisDatabaseException exc) {
                  // Check if the error is critical for the whole input file, or only for current file line.
                  if(fileDiscarded) {
                     // Input file format violated: release the file, discard all data loaded so far
                     // (re-init both containers as empty), and forward the critical exception.
                     scannerLine.close();
                     scannerFile.close();
                     this.tpc = new TennisPlayerContainer();
                     this.tmc = new TennisMatchContainer();
                     throw exc;
                  }
                  else {
                     // Current file line is invalid, skip it (and report it later).
                     skippedLinesReport += "\n   Line " + currFileLineNumber + ": " + exc.getMessage();
                  }
               }
               catch(InputMismatchException exc) {
                  // Current file line has a birth year that is not a number, skip it (and report it later).
                  skippedLinesReport += "\n   Line " + currFileLineNumber + ": PLAYER line invalid, birth year not a number!";
               }
               catch(NoSuchElementException exc) {
                  // Current file line has less data than expected, skip it (and report it later).
                  skippedLinesReport += "\n   Line " + currFileLineNumber + ": Line invalid, missing data!";
               }
               scannerLine.close(); // Closing file line scanner.
            }
         }
         scannerFile.close(); // Closing the scanner to release the file used.
      }
      catch(NullPointerException exc) {
         throw new TennisDatabaseException("Load input file failed, file name invalid!");
      }
      catch(FileNotFoundException exc) {
         throw new TennisDatabaseException("Load input file failed, file not found!");
      }
      // Check if the loading has been fully successful (no file line skipped).
      if(skippedLinesReport.length() > 0) {
         throw new TennisDatabaseRuntimeException("Load input file not fully successful, lines skipped:" +
                                                  skippedLinesReport);
      }
   }

   // Desc.: Search for a player in the database by id, and returns a (deep) copy of that player (if found).
   // Input: The id of a player.
   // Output: Throws an unchecked (non-critical) exception if there is no player with that id.
   public TennisPlayer getPlayer( String playerId ) throws TennisDatabaseRuntimeException {
      // Check if input is valid.
      if(playerId == null) { throw new TennisDatabaseRuntimeException("Player search failed, player ID invalid!"); }
      // NOTE: All internal data is stored in uppercase.
      return this.tpc.getPlayer(playerId.trim().toUpperCase());
   }

   // Desc.: Search for a match in the database by id, and returns a (deep) copy of that match (if found).
   // Input: The id of a match.
   // Output: Throws an unchecked (non-critical) exception if there is no match with that id.
   public TennisMatch getMatch( String matchId ) throws TennisDatabaseRuntimeException {
      // Check if input is valid.
      if(matchId == null) { throw new TennisDatabaseRuntimeException("Match search failed, match ID invalid!"); }
      // NOTE: All internal data is stored in uppercase.
      return this.tmc.getMatch(matchId.trim().toUpperCase());
   }

   // Desc.: Returns (deep) copies of all players in the database arranged in the output array (sorted by id, alphabetically).
   // Output: Throws an unchecked (non-critical) exception if there are no players in the database.
   public TennisPlayer[] getAllPlayers() throws TennisDatabaseRuntimeException {
      return this.tpc.getAllPlayers();
   }

   // Desc.: Returns (deep) copies of all matches of that player id arranged in the output array (sorted by date, most recent first).
   // Input: The id of a player.
   // Output: Throws a checked (critical) exception if the player (id) does not exists.
   //         Throws an unchecked (non-critical) exception if there are no matches (but the player id exists).
   // NOTE: the matches are retrieved from the list of matches of the player (in the container of players).
   public TennisMatch[] getMatchesOfPlayer( String playerId  ) throws TennisDatabaseException,
                                                                      TennisDatabaseRuntimeException {
      // Check if input is valid.
      if(playerId == null) { throw new TennisDatabaseException("Player matches search failed, player ID invalid!"); }
      // NOTE: All internal data is stored in uppercase.
      return this.tpc.getMatchesOfPlayer(playerId.trim().toUpperCase());
   }

   // Desc.: Returns (deep) copies of all matches in the database arranged in the output array (sorted by date, most recent first).
   // Output: Throws an unchecked (non-critical) exception if there are no matches in the database.
   public TennisMatch[] getAllMatches() throws TennisDatabaseRuntimeException {
      return this.tmc.getAllMatches();
   }

   // Desc.: Insert a player into the database.
   // Input: A player line (same as input file).
   // Output: Throws a checked (critical) exception if player id is already in the database.
   //         Throws a checked (critical) exception if the player line is invalid.
   public void insertPlayer( String playerLine ) throws TennisDatabaseException {
      // Check if input is valid.
      if(playerLine == null) { throw new TennisDatabaseException("Player insertion failed, PLAYER line invalid!"); }
      // Parsing the player line data using a scanner (same parsing used for the lines of the input file).
      // NOTE: The data loaded from inputs should be stored/processed internally in uppercase.
      Scanner scannerLine = new Scanner(playerLine.trim().toUpperCase()); // Init scanner to parse player line.
      scannerLine.useDelimiter("/"); // Set delimiter so scanner can split player line.
      // Monitor for exceptions related to the parsing of the player line.
      try {
         // Check if the input line is a PLAYER line.
         String lineToken = scannerLine.next();
         if(!lineToken.equals("PLAYER")) { throw new TennisDatabaseException("Player insertion failed, not a PLAYER line!"); }
         // Parse a PLAYER line, splitting all player data into individual pieces of info.
         String newPlayerId = scannerLine.next();
         String newPlayerFirstName = scannerLine.next();
         String newPlayerLastName = scannerLine.next();
         int newPlayerBirthYear = scannerLine.nextInt();
         String newPlayerCountry = scannerLine.next();
         // Check if the PLAYER line has more data than expected.
         if(scannerLine.hasNext()) { throw new TennisDatabaseException("PLAYER line invalid, too much data!"); }
         // Check if the PLAYER line data is valid.
         this.checkPlayerData(newPlayerId,
                              newPlayerFirstName,
                              newPlayerLastName,
                              newPlayerBirthYear,
                              newPlayerCountry);
         // Build a TennisPlayer object from the PLAYER line data.
         TennisPlayer newPlayer = new TennisPlayer(newPlayerId,
                                                   newPlayerFirstName,
                                                   newPlayerLastName,
                                                   newPlayerBirthYear,
                                                   newPlayerCountry);
         // Insert newly created TennisPlayer object into proper container.
         this.tpc.insertPlayer(newPlayer);
      }
      catch(InputMismatchException exc) {
         throw new TennisDatabaseException("PLAYER line invalid, birth year not a number!");
      }
      catch(NoSuchElementException exc) {
         throw new TennisDatabaseException("PLAYER line invalid, missing data!");
      }
      finally {
         scannerLine.close(); // Closing line scanner (in any case).
      }
   }

   // Desc.: Insert a match into the database.
   // Input: A match line (same as input file).
   // Output: Throws a checked (critical) exception if a player does not exist in the database.
   //         Throws a checked (critical) exception if match id is already in the database.
   //         Throws a checked (critical) exception if the match line is invalid.
   public void insertMatch( String matchLine ) throws TennisDatabaseException {
      // Check if input is valid.
      if(matchLine == null) { throw new TennisDatabaseException("Match insertion failed, MATCH line invalid!"); }
      // Parsing the match line data using a scanner (same parsing used for the lines of the input file).
      // NOTE: The data loaded from inputs should be stored/processed internally in uppercase.
      Scanner scannerLine = new Scanner(matchLine.trim().toUpperCase()); // Init scanner to parse match line.
      scannerLine.useDelimiter("/"); // Set delimiter so scanner can split match line.
      // Monitor for exceptions related to the parsing of the match line.
      try {
         // Check if the input line is a MATCH line.
         String lineToken = scannerLine.next();
         if(!lineToken.equals("MATCH")) { throw new TennisDatabaseException("Match insertion failed, not a MATCH line!"); }
         // Parse a MATCH line, splitting all match data into individual pieces of info.
         String newMatchDate = scannerLine.next();
         String newMatchIdPlayer1 = scannerLine.next();
         String newMatchIdPlayer2 = scannerLine.next();
         String newMatchTournament = scannerLine.next();
         String newMatchWinner = scannerLine.next();
         String newMatchScore = scannerLine.next();
         // Check if the MATCH line has more data than expected.
         if(scannerLine.hasNext()) { throw new TennisDatabaseException("MATCH line invalid, too much data!"); }
         // Check if the MATCH line data is valid.
         this.checkMatchData(newMatchDate,
                             newMatchIdPlayer1,
                             newMatchIdPlayer2,
                             newMatchTournament,
                             newMatchWinner,
                             newMatchScore);
         // Build a TennisMatch object from the MATCH line data.
         TennisMatch newMatch = this.buildMatch(newMatchDate,
                                                newMatchIdPlayer1,
                                                newMatchIdPlayer2,
                                                newMatchTournament,
                                                newMatchWinner,
                                                newMatchScore);
         // Insert newly created TennisMatch object into proper containers.
         this.addMatch(newMatch);
      }
      catch(NoSuchElementException exc) {
         throw new TennisDatabaseException("MATCH line invalid, missing data!");
      }
      finally {
         scannerLine.close(); // Closing line scanner (in any case).
      }
   }

   // Desc.: Delete a player from the database.
   // Input: A player id.
   // Output: Throws a checked (critical) exception if player id is not found.
   // NOTE: all the matches of the player are deleted too (a match needs both its players in the database).
   public void deletePlayer( String playerId ) throws TennisDatabaseException {
      // Check if input is valid.
      if(playerId == null) { throw new TennisDatabaseException("Player deletion failed, player ID invalid!"); }
      String id = playerId.trim().toUpperCase(); // NOTE: All internal data is stored in uppercase.
      // Delete all matches of the player from the lists of matches in the container of players
      // (the list of the player, and the lists of his opponents).
      try { this.tpc.deleteMatchesOfPlayer(id); }
      catch(TennisDatabaseRuntimeException exc) { /* Do nothing: the player has no matches to delete. */ }
      // Delete all matches of the player from the container of matches.
      try { this.tmc.deleteMatchesOfPlayer(id); }
      catch(TennisDatabaseRuntimeException exc) { /* Do nothing: the player has no matches to delete. */ }
      // Delete the player from the container of players.
      // NOTE: this throws a critical exception if the player id is not found (and nothing has been deleted).
      this.tpc.deletePlayer(id);
   }

   // Desc.: Delete a match from the database.
   // Input: A match id.
   // Output: Throws a checked (critical) exception if match id is not found.
   public void deleteMatch( String matchId ) throws TennisDatabaseException {
      // Check if input is valid.
      if(matchId == null) { throw new TennisDatabaseException("Match deletion failed, match ID invalid!"); }
      String id = matchId.trim().toUpperCase(); // NOTE: All internal data is stored in uppercase.
      // Search for the match to delete (its players are needed to delete it from their lists of matches).
      TennisMatch matchToDelete = null;
      try { matchToDelete = this.tmc.getMatch(id); }
      catch(TennisDatabaseRuntimeException exc) {
         // Search failed, abort deletion, and throw critical exception.
         throw new TennisDatabaseException("Match deletion failed, match ID not found in database!");
      }
      // Delete the match from the container of matches.
      this.tmc.deleteMatch(id);
      // Delete the match from the lists of matches of both players (in the container of players).
      this.tpc.deleteMatch(matchToDelete);
   }

   // Internal method to check if the data of a PLAYER line is valid.
   // Input: All the individual pieces of info of a PLAYER line.
   // Output: Throws a checked (critical) exception (describing the problem) if the data is not valid.
   private void checkPlayerData( String id, String firstName, String lastName, int birthYear, String country )
                                                                                 throws TennisDatabaseException {
      if(id.length() != 5) {
         throw new TennisDatabaseException("PLAYER line invalid, player ID must be 5 characters!");
      }
      else if(firstName.trim().length() == 0) {
         throw new TennisDatabaseException("PLAYER line invalid, first name missing!");
      }
      else if(lastName.trim().length() == 0) {
         throw new TennisDatabaseException("PLAYER line invalid, last name missing!");
      }
      else if((birthYear < 1000) || (birthYear > 9999)) {
         throw new TennisDatabaseException("PLAYER line invalid, birth year must be a 4-digit number!");
      }
      else if(country.trim().length() == 0) {
         throw new TennisDatabaseException("PLAYER line invalid, country missing!");
      }
   }

   // Internal method to check if the data of a MATCH line is valid.
   // Input: All the individual pieces of info of a MATCH line (as strings).
   // Output: Throws a checked (critical) exception (describing the problem) if the data is not valid.
   private void checkMatchData( String date, String idPlayer1, String idPlayer2,
                                String tournament, String winner, String score ) throws TennisDatabaseException {
      // Check the match date (8-digit number in format YYYYMMDD).
      if(date.length() != 8) {
         throw new TennisDatabaseException("MATCH line invalid, date must be an 8-digit number (YYYYMMDD)!");
      }
      for(int i = 0; i < date.length(); i++) {
         if(!Character.isDigit(date.charAt(i))) {
            throw new TennisDatabaseException("MATCH line invalid, date must be an 8-digit number (YYYYMMDD)!");
         }
      }
      int dateMonth = Integer.parseInt(date.substring(4, 6));
      int dateDay = Integer.parseInt(date.substring(6, 8));
      if((dateMonth < 1) || (dateMonth > 12)) {
         throw new TennisDatabaseException("MATCH line invalid, date month must be between 01 and 12!");
      }
      else if((dateDay < 1) || (dateDay > 31)) {
         throw new TennisDatabaseException("MATCH line invalid, date day must be between 01 and 31!");
      }
      // Check the ids of the 2 players.
      else if((idPlayer1.length() != 5) || (idPlayer2.length() != 5)) {
         throw new TennisDatabaseException("MATCH line invalid, player IDs must be 5 characters!");
      }
      else if(idPlayer1.equals(idPlayer2)) {
         throw new TennisDatabaseException("MATCH line invalid, the 2 players must be different!");
      }
      // Check tournament, winner, and score.
      else if(tournament.trim().length() == 0) {
         throw new TennisDatabaseException("MATCH line invalid, tournament missing!");
      }
      else if(!winner.equals("WINNER1") && !winner.equals("WINNER2")) {
         throw new TennisDatabaseException("MATCH line invalid, winner must be WINNER1 or WINNER2!");
      }
      else if((score.length() == 0) || (score.indexOf(' ') >= 0)) {
         throw new TennisDatabaseException("MATCH line invalid, score missing or with spaces!");
      }
   }

   // Internal method to build a TennisMatch object from the data of a MATCH line.
   // Input: All the individual pieces of info of a MATCH line (as strings).
   // Output: The TennisMatch object storing the input data.
   // NOTE: Assuming input data valid (already checked by checkMatchData).
   private TennisMatch buildMatch( String date, String idPlayer1, String idPlayer2,
                                   String tournament, String winner, String score ) {
      // Split the match date (YYYYMMDD) into year, month, and day.
      int dateYear = Integer.parseInt(date.substring(0, 4));
      int dateMonth = Integer.parseInt(date.substring(4, 6));
      int dateDay = Integer.parseInt(date.substring(6, 8));
      // Convert the match winner into a number (1 if player 1 won, 2 if player 2 won).
      int winnerNumber = 1;
      if(winner.equals("WINNER2")) { winnerNumber = 2; }
      return new TennisMatch(dateYear, dateMonth, dateDay, idPlayer1, idPlayer2, tournament, winnerNumber, score);
   }

   // Internal method to insert a match into both containers (container of matches, and container of players).
   // Input: A tennis match.
   // Output: Throws a checked (critical) exception if a player of the match does not exist in the database.
   //         Throws a checked (critical) exception if match id is already in the database.
   // NOTE: all the checks are performed before any insertion, so a failed insertion changes nothing.
   private void addMatch( TennisMatch newMatch ) throws TennisDatabaseException {
      // Check if both players of the match are in the database.
      try {
         this.tpc.getPlayer(newMatch.getIdPlayer1());
         this.tpc.getPlayer(newMatch.getIdPlayer2());
      }
      catch(TennisDatabaseRuntimeException exc) {
         throw new TennisDatabaseException("Match insertion failed, match player not in database!");
      }
      // Insert the match into the container of matches (this fails if the match id is already in the database).
      this.tmc.insertMatch(newMatch);
      // Insert the match into the lists of matches of both players (in the container of players).
      this.tpc.insertMatch(newMatch);
   }

}


