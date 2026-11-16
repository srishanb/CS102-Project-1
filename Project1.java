


// CS-102: "Computing and Algorithms I"
// Project 1.
// Prof. Giuseppe Turini
// Kettering University
// 2026-10-14
//
// Student: Srishan Bommidi (project completed on 2026-11-15, starting from the in-class code).
// AI Citation: this file includes AI-generated code (tool: Claude Code, model: Claude Fable 5.2).
// See readme.txt ("AI Citation Block") for the exact prompts and the review/verification summary.

import java.lang.String; // ...
import java.util.Scanner; // To read from console, etc.
import java.util.InputMismatchException; // Thrown by a Scanner if a token is not of the expected type.
import java.util.NoSuchElementException; // Thrown by a Scanner if there is no more input.

import TennisDatabase.TennisDatabase; // package-name.class-name
import TennisDatabase.TennisDatabaseException;
import TennisDatabase.TennisDatabaseRuntimeException;
import TennisDatabase.TennisPlayer;
import TennisDatabase.TennisMatch;

// Define a public class Project1 included in no specific package.
// This class is the only class with a main method which uses a TennisDatabase object
// to store data and perform the operations requested by the user.
// This class is the only class allowed to print/read to/from the console.
public class Project1 {

   // Desc.: Entry point of the program: loads the input file, then runs the console-based menu loop.
   // Input: The run arguments (exactly 1 expected: the name of the input file).
   // NOTE: this method does not throw any exception (all exceptions are caught and handled).
   public static void main( String[] args ) {

      String inputFileName = null; // Input file name (null if the run argument is missing).
      // Check the run arguments (exactly 1 expected).
      if( args.length == 1 ) {
         inputFileName = args[0]; // Store run argument 1 into local var (input file name).
      }
      else {
         System.out.println( "Run arguments invalid, expected exactly 1 argument: the input file name." );
         System.out.println( "Usage: java Project1 input-file-name" );
      }

      TennisDatabase myDatabaseManager = new TennisDatabase(); // Main object to handle tennis DB data/ops.

      try {
         // Asking the DB manager to load data from file.
         // NOTE: Func could throw TennisDatabaseException or TennisDatabaseRuntimeException...
         myDatabaseManager.loadInputFile( inputFileName );
         System.out.println( "Input file loaded successfully." );
      }
      //catch( exc-type var-name ) { /*What to do if that exc-type happens*/ }
      //catch( TennisDatabaseRuntimeException e ) { /* Do nothing */ }
      catch( TennisDatabaseException exc ) { System.out.println( exc.getMessage() ); }
      // Non-critical error: the input file has been loaded, but some invalid lines have been skipped.
      catch( TennisDatabaseRuntimeException exc ) { System.out.println( exc.getMessage() ); }
      // Any other (unexpected) error.
      catch( Exception exc ) { System.out.println( "Load input file failed, unexpected error: " + exc.getMessage() ); }

      System.out.println(); // Newline.

      //...
      //TennisPlayer[] myPlayers = myDatabaseManager.getAllPlayers();

      Scanner readerFromConsole = new Scanner( System.in ); // Scanner object to read from console (System.in).

      int userMenuSelection = -1; // Initialized with invalid selection (-1).

      while( userMenuSelection != 6 ) {
         printConsoleBasedMenu(); // Printing console-based menu.
         // Monitor for exceptions related to console reading and to the operation selected.
         try {
            userMenuSelection = readerFromConsole.nextInt(); // Using Scanner object to read user selection.
            // Discard the rest of the console line (after the user selection).
            if( readerFromConsole.hasNextLine() ) { readerFromConsole.nextLine(); }
            System.out.println(); // Newline.
            // Perform the operation selected by the user.
            if( userMenuSelection == 1 ) { printAllPlayers( myDatabaseManager ); }
            else if( userMenuSelection == 2 ) { printMatchesOfPlayer( myDatabaseManager, readerFromConsole ); }
            else if( userMenuSelection == 3 ) { printAllMatches( myDatabaseManager ); }
            else if( userMenuSelection == 4 ) { insertPlayerOrMatch( myDatabaseManager, readerFromConsole ); }
            else if( userMenuSelection == 5 ) { deletePlayerOrMatch( myDatabaseManager, readerFromConsole ); }
            else if( userMenuSelection == 6 ) { System.out.println( "Exiting..." ); }
            else { System.out.println( "Selection invalid, please enter a number between 1 and 6." ); }
         }
         catch( InputMismatchException exc ) {
            // The user selection is not a number: discard the invalid console line, and show the menu again.
            if( readerFromConsole.hasNextLine() ) { readerFromConsole.nextLine(); }
            System.out.println();
            System.out.println( "Selection invalid, please enter a number between 1 and 6." );
         }
         catch( NoSuchElementException exc ) {
            // No more input available from console (console closed), exit.
            System.out.println();
            System.out.println( "No more input available, exiting..." );
            userMenuSelection = 6;
         }
         catch( Exception exc ) {
            // Any other (unexpected) error: report it, and show the menu again.
            System.out.println( "Operation failed, unexpected error: " + exc.getMessage() );
         }
         System.out.println(); // Newline.
      }

      // ...
      System.out.println("End main");
   }

   // Internal static method to print the console-based menu.
   private static void printConsoleBasedMenu() {
      System.out.println("--- CS-102 - PROJECT 1 - TENNIS DATABASE MANAGER ---");
      System.out.println("1 --> Print All Players");
      System.out.println("2 --> Print All Matches of a Player");
      System.out.println("3 --> Print All Matches");
      System.out.println("4 --> Insert New Player/Match");
      System.out.println("5 --> Delete Player/Match");
      System.out.println("6 --> Exit");
      System.out.println(); // Newline.
      System.out.println("Your Choice? ");
   }

   // Internal static method to perform menu command 1: print all players (sorted by id, alphabetically).
   // Input: The tennis database.
   private static void printAllPlayers( TennisDatabase database ) {
      try {
         TennisPlayer[] allPlayers = database.getAllPlayers(); // Sorted by id (alphabetically).
         System.out.println( "All players (" + allPlayers.length + "):" );
         for( int i = 0; i < allPlayers.length; i++ ) {
            System.out.println( formatPlayer( allPlayers[i] ) );
         }
      }
      catch( TennisDatabaseRuntimeException exc ) {
         // No players in the database.
         System.out.println( exc.getMessage() );
      }
   }

   // Internal static method to perform menu command 2: print all matches of a player (most recent first).
   // Input: The tennis database, and the Scanner object to read from console.
   private static void printMatchesOfPlayer( TennisDatabase database, Scanner readerFromConsole ) {
      System.out.println( "Player id? " );
      String playerId = readConsoleLine( readerFromConsole );
      System.out.println(); // Newline.
      try {
         TennisMatch[] matchesOfPlayer = database.getMatchesOfPlayer( playerId ); // Sorted by date (most recent first).
         System.out.println( "All matches of player " + playerId.toUpperCase() + " (" + matchesOfPlayer.length + "):" );
         for( int i = 0; i < matchesOfPlayer.length; i++ ) {
            System.out.println( formatMatch( database, matchesOfPlayer[i] ) );
         }
      }
      catch( TennisDatabaseException exc ) {
         // Player id not found in the database.
         System.out.println( exc.getMessage() );
      }
      catch( TennisDatabaseRuntimeException exc ) {
         // Player found, but with no matches.
         System.out.println( exc.getMessage() );
      }
   }

   // Internal static method to perform menu command 3: print all matches (most recent first).
   // Input: The tennis database.
   private static void printAllMatches( TennisDatabase database ) {
      try {
         TennisMatch[] allMatches = database.getAllMatches(); // Sorted by date (most recent first).
         System.out.println( "All matches (" + allMatches.length + "):" );
         for( int i = 0; i < allMatches.length; i++ ) {
            System.out.println( formatMatch( database, allMatches[i] ) );
         }
      }
      catch( TennisDatabaseRuntimeException exc ) {
         // No matches in the database.
         System.out.println( exc.getMessage() );
      }
   }

   // Internal static method to perform menu command 4: insert a new player or a new match.
   // Input: The tennis database, and the Scanner object to read from console.
   // NOTE: the user inputs a player/match line (same format as input file).
   private static void insertPlayerOrMatch( TennisDatabase database, Scanner readerFromConsole ) {
      System.out.println( "Player/match line to insert (same format as input file)? " );
      System.out.println( "Example: PLAYER/FED81/ROGER/FEDERER/1981/SWITZERLAND" );
      System.out.println( "Example: MATCH/20150316/DJO87/FED81/INDIAN WELLS/WINNER1/6-3,6-7,6-2" );
      String newLine = readConsoleLine( readerFromConsole );
      System.out.println(); // Newline.
      try {
         // Check if the input line is a PLAYER line or a MATCH line.
         if( newLine.toUpperCase().startsWith( "PLAYER" ) ) {
            database.insertPlayer( newLine );
            System.out.println( "Player inserted successfully." );
         }
         else if( newLine.toUpperCase().startsWith( "MATCH" ) ) {
            database.insertMatch( newLine );
            System.out.println( "Match inserted successfully." );
         }
         else {
            System.out.println( "Insertion failed, line invalid: not a PLAYER line and not a MATCH line!" );
         }
      }
      catch( TennisDatabaseException exc ) {
         // Insertion failed (line invalid, duplicate player id, duplicate match id, or match player not in database).
         System.out.println( exc.getMessage() );
      }
      catch( TennisDatabaseRuntimeException exc ) {
         System.out.println( exc.getMessage() );
      }
   }

   // Internal static method to perform menu command 5: delete a player or a match.
   // Input: The tennis database, and the Scanner object to read from console.
   // NOTE: the user selects what to delete (player or match), then inputs the appropriate id.
   private static void deletePlayerOrMatch( TennisDatabase database, Scanner readerFromConsole ) {
      System.out.println( "1 --> Delete Player" );
      System.out.println( "2 --> Delete Match" );
      System.out.println(); // Newline.
      System.out.println( "Your Choice? " );
      String userDeleteSelection = readConsoleLine( readerFromConsole );
      System.out.println(); // Newline.
      try {
         if( userDeleteSelection.equals( "1" ) ) {
            System.out.println( "Player id? " );
            String playerId = readConsoleLine( readerFromConsole );
            System.out.println(); // Newline.
            database.deletePlayer( playerId );
            System.out.println( "Player deleted successfully (including all matches of the player)." );
         }
         else if( userDeleteSelection.equals( "2" ) ) {
            System.out.println( "Match id (date+idplayer1+idplayer2, example: 20150316DJO87FED81)? " );
            String matchId = readConsoleLine( readerFromConsole );
            System.out.println(); // Newline.
            database.deleteMatch( matchId );
            System.out.println( "Match deleted successfully." );
         }
         else {
            System.out.println( "Selection invalid, please enter 1 or 2." );
         }
      }
      catch( TennisDatabaseException exc ) {
         // Deletion failed (id not found).
         System.out.println( exc.getMessage() );
      }
      catch( TennisDatabaseRuntimeException exc ) {
         System.out.println( exc.getMessage() );
      }
   }

   // Internal static method to read a line from console (without leading/trailing white spaces).
   // Input: The Scanner object to read from console.
   // Output: The line read, or an empty string if there is no more input available.
   private static String readConsoleLine( Scanner readerFromConsole ) {
      if( readerFromConsole.hasNextLine() ) { return readerFromConsole.nextLine().trim(); }
      else { return ""; }
   }

   // Internal static method to build the string (to print) describing a player.
   // Input: A tennis player.
   // Output: A string with id, full name, birth year, country, and win/loss record of the player.
   // EXAMPLE: DJO87: NOVAK DJOKOVIC, born 1987, SERBIA, record 6-2 (W-L)
   private static String formatPlayer( TennisPlayer player ) {
      return player.getId() + ": " +
             player.getFirstName() + " " + player.getLastName() +
             ", born " + player.getBirthYear() +
             ", " + player.getCountry() +
             ", record " + player.getWins() + "-" + player.getLosses() + " (W-L)";
   }

   // Internal static method to build the string (to print) describing a match.
   // Input: The tennis database (to get the full names of the players), and a tennis match.
   // Output: A string with date, tournament, full names of the players, winner, and score of the match.
   // EXAMPLE: 2015-11-16, ATP FINALS, NOVAK DJOKOVIC vs ROGER FEDERER, winner NOVAK DJOKOVIC, 6-3,6-4
   private static String formatMatch( TennisDatabase database, TennisMatch match ) {
      String namePlayer1 = getPlayerFullName( database, match.getIdPlayer1() );
      String namePlayer2 = getPlayerFullName( database, match.getIdPlayer2() );
      // Find the full name of the winner (1 if player 1 won, 2 if player 2 won).
      String nameWinner = namePlayer1;
      if( match.getWinner() == 2 ) { nameWinner = namePlayer2; }
      return match.getDate() + ", " +
             match.getTournament() + ", " +
             namePlayer1 + " vs " + namePlayer2 +
             ", winner " + nameWinner +
             ", " + match.getMatchScore();
   }

   // Internal static method to get the full name (first name and last name) of a player.
   // Input: The tennis database, and the id of a player.
   // Output: The full name of the player, or the id of the player if not found in the database.
   private static String getPlayerFullName( TennisDatabase database, String playerId ) {
      try {
         TennisPlayer player = database.getPlayer( playerId );
         return player.getFirstName() + " " + player.getLastName();
      }
      catch( TennisDatabaseRuntimeException exc ) {
         return playerId; // Player not found, use the id instead of the full name.
      }
   }

}


