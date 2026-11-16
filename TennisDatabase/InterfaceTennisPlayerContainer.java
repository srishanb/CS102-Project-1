


// CS-102: "Computing and Algorithms II"
// Prof. Giuseppe Turini
// Kettering University
// 2022-04-09

package TennisDatabase;

import java.lang.String;

// Interface (package-private) providing the specifications for the TennisPlayerContainer class.
// Note: Assignment 1 of CS-102 (Spring 2022).
interface InterfaceTennisPlayerContainer {

   // Desc.: Search for a player in this container by input id, and returns a copy of that player (if found).
   // Output: Throws an unchecked (non-critical) exception if there is no player with that input id.
   public TennisPlayer getPlayer( String id ) throws TennisDatabaseRuntimeException;

   // Desc.: Insert a tennis player into this container.
   // Input: A tennis player.
   // Output: Throws a checked (critical) exception if player id is already in this container.
   //         Throws a checked (critical) exception if the container is full.
   public void insertPlayer( TennisPlayer p ) throws TennisDatabaseException;
   
   // Desc.: Delete a player from this container.
   // Input: A player id.
   // Output: Throws a checked (critical) exception if player id is not found.
   public void deletePlayer( String playerId ) throws TennisDatabaseException;
   
   // Desc.: Insert a tennis match into the lists of both tennis players of the input match.
   // Input: A tennis match.
   // Output: Throws a checked (critical) exception if the insertion is not fully successful.
   public void insertMatch( TennisMatch m ) throws TennisDatabaseException;
   
   // Desc.: Returns all players in the database arranged in the output array (sorted by id, alphabetically).
   // Output: Throws an unchecked (non-critical) exception if there are no players in this container.
   public TennisPlayer[] getAllPlayers() throws TennisDatabaseRuntimeException;
   
   // Desc.: Returns copies (deep copies) of all matches of input player (id) arranged in the output array (sorted by date, most recent first).
   // Input: The id of a player.
   // Output: Throws a checked (critical) exception if the player (id) does not exists.
   //         Throws an unchecked (non-critical) exception if there are no matches (but the player id exists).
   public TennisMatch[] getMatchesOfPlayer( String playerId  ) throws TennisDatabaseException, TennisDatabaseRuntimeException;
   
   // Desc.: Delete all matches of input player (id) from this container.
   // Input: A player id.
   // Output: Throws an unchecked (non-critical) exception if no match is deleted.
   public void deleteMatchesOfPlayer( String playerId  ) throws TennisDatabaseRuntimeException;
   
}


