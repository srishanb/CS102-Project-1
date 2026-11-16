


// CS-102: "Computing and Algorithms II"
// Prof. Giuseppe Turini
// Kettering University
// 2022-04-09

package TennisDatabase;

import java.lang.String;

// Interface (package-private) providing the specifications for the TennisMatchesContainer class.
// Note: Assignment 1 of CS-102 (Spring 2022).
interface InterfaceTennisMatchContainer {

   // Desc.: Returns the number of matches in this container.
   // Output: The number of matches in this container as an integer.
   public int getNumMatches();

   // Desc.: Insert a tennis match into this container.
   // Input: A tennis match.
   // Output: Throws a checked (critical) exception if the container is full.
   public void insertMatch( TennisMatch match ) throws TennisDatabaseException;
   
   // Desc.: Delete a match from this container.
   // Input: A match id.
   // Output: Throws a checked (critical) exception if match id is not found.
   public void deleteMatch( String matchId ) throws TennisDatabaseException;
   
   // Desc.: Returns all matches in the database arranged in the output array (sorted by date, most recent first).
   // Output: Throws an exception if there are no matches in this container.
   public TennisMatch[] getAllMatches() throws TennisDatabaseRuntimeException;
   
   // Desc.: Returns all matches of input player (id) arranged in the output array (sorted by date, most recent first).
   // Input: The id of the tennis player.
   // Output: Throws an unchecked (non-critical) exception if there are no tennis matches in the list.
   public TennisMatch[] getMatchesOfPlayer( String playerId ) throws TennisDatabaseRuntimeException;
   
   // Desc.: Delete all matches of a player by id (if found).
   // Output: Throws an unchecked (non-critical) exception if there is no match with that input id.
   public void deleteMatchesOfPlayer( String playerId ) throws TennisDatabaseRuntimeException;
   
}


