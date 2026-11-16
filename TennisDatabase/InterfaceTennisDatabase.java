


// CS-102: "Computing and Algorithms II"
// Prof. Giuseppe Turini
// Kettering University
// 2022-04-09

package TennisDatabase;

import java.lang.String;

// Public interface providing the specifications for the TennisDatabase class.
// Note: Assignment 1 of CS-102 (Spring 2022).
public interface InterfaceTennisDatabase {

   // Desc.: Loads data from input file following the format described in the specifications.
   // Input: The name of the input file.
   // Output: Throws an unchecked (non-critical) exception if the loading is not fully successfull.
   //         Throws a checked (critical) exception if the file (file name) does not exist.
   public void loadInputFile( String fileName ) throws TennisDatabaseException, TennisDatabaseRuntimeException;
   
   // Desc.: Search for a player in the database by id, and returns a (deep) copy of that player (if found).
   // Input: The id of a player.
   // Output: Throws an unchecked (non-critical) exception if there is no player with that id.
   public TennisPlayer getPlayer( String playerId ) throws TennisDatabaseRuntimeException;
   
   // Desc.: Search for a match in the database by id, and returns a (deep) copy of that match (if found).
   // Input: The id of a match.
   // Output: Throws an unchecked (non-critical) exception if there is no match with that id.
   public TennisMatch getMatch( String matchId ) throws TennisDatabaseRuntimeException;
   
   // Desc.: Returns (deep) copies of all players in the database arranged in the output array (sorted by id, alphabetically).
   // Output: Throws an unchecked (non-critical) exception if there are no players in the database.
   public TennisPlayer[] getAllPlayers() throws TennisDatabaseRuntimeException;
   
   // Desc.: Returns (deep) copies of all matches of that player id arranged in the output array (sorted by date, most recent first).
   // Input: The id of a player.
   // Output: Throws a checked (critical) exception if the player (id) does not exists.
   //         Throws an unchecked (non-critical) exception if there are no matches (but the player id exists).
   public TennisMatch[] getMatchesOfPlayer( String playerId  ) throws TennisDatabaseException, TennisDatabaseRuntimeException;
   
   // Desc.: Returns (deep) copies of all matches in the database arranged in the output array (sorted by date, most recent first).
   // Output: Throws an unchecked (non-critical) exception if there are no matches in the database.
   public TennisMatch[] getAllMatches() throws TennisDatabaseRuntimeException;
   
   // Desc.: Insert a player into the database.
   // Input: A player line (same as input file).
   // Output: Throws a checked (critical) exception if player id is already in the database.
   public void insertPlayer( String playerLine ) throws TennisDatabaseException;

   // Desc.: Insert a match into the database.
   // Input: A match line (same as input file).
   // Output: Throws a checked (critical) exception if a player does not exist in the database.
   public void insertMatch( String matchLine ) throws TennisDatabaseException;
   
   // Desc.: Delete a player from the database.
   // Input: A player id.
   // Output: Throws a checked (critical) exception if player id is not found.
   public void deletePlayer( String playerId ) throws TennisDatabaseException;

   // Desc.: Delete a match from the database.
   // Input: A match id.
   // Output: Throws a checked (critical) exception if match id is not found.
   public void deleteMatch( String matchId ) throws TennisDatabaseException;
   
}


