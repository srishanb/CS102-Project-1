


// CS-102: "Computing and Algorithms I"
// Project 1.
// Srishan Bommidi
// Kettering University
// 2026-11-15
//
// AI Citation: this file includes AI-generated code (tool: Claude Code, model: Claude Fable 5.2).
// See readme.txt ("AI Citation Block") for the exact prompts and the review/verification summary.

package TennisDatabase;

import java.lang.String;

// Define a package-private class TennisMatchContainer included in the package TennisDatabase, and
// implementing the interface InterfaceTennisMatchContainer.
// This class should be a dynamically-allocated array-based list storing tennis matches,
// sorted by date (most recent first).
// This list should be implemented using an array of TennisMatch objects,
// re-sizing this array whenever appropriate.
// NOTE: package-private by default (no access modifier), as required by the specifications.
class TennisMatchContainer implements InterfaceTennisMatchContainer {

   private static final int INITIAL_CAPACITY = 4; // Initial (and minimum) physical size of the internal array.

   private TennisMatch[] matches; // Internal array storing the matches, sorted by date (most recent first).
   private int numMatches; // Number of matches stored in the internal array (logical size).

   // Default constructor.
   public TennisMatchContainer() {
      // Init this container as an EMPTY array-based list of tennis matches.
      this.matches = new TennisMatch[ INITIAL_CAPACITY ];
      this.numMatches = 0;
   }

   // Desc.: Returns the number of matches in this container.
   // Output: The number of matches in this container as an integer.
   public int getNumMatches() { return this.numMatches; }

   // Desc.: Insert a tennis match into this container.
   // Input: A tennis match.
   // Output: Throws a checked (critical) exception if the container is full.
   //         Throws a checked (critical) exception if match id is already in this container.
   // NOTE: as a dynamically-allocated array-based list, this container will "never" be full.
   // NOTE: sorted insertion (no full sorting): search for point of insertion, then shift.
   public void insertMatch( TennisMatch match ) throws TennisDatabaseException {
      // Check if input is valid.
      if( match == null ) { throw new TennisDatabaseException( "Match insertion failed, new match invalid!" ); }
      // Search for point of insertion.
      // Forward array traversal until we find a match not more recent than input match, or reach end of array.
      int currIndex = 0;
      while( ( currIndex < this.numMatches ) &&
             ( this.matches[ currIndex ].compareTo( match ) > 0 ) ) {
         currIndex++; // Advance to next match in array.
      }
      // Here, currIndex is the point of insertion (first match with same date, or older, or end of array).
      // Check if the input match id is already in this container.
      // NOTE: only the matches with the same date of the input match need to be checked (array sorted by date).
      int currIndexSameDate = currIndex;
      while( ( currIndexSameDate < this.numMatches ) &&
             ( this.matches[ currIndexSameDate ].compareTo( match ) == 0 ) ) {
         if( this.matches[ currIndexSameDate ].getId().equals( match.getId() ) ) {
            // A match with the same id of input match is already stored.
            // Abort insertion, and throw critical exception.
            throw new TennisDatabaseException( "Match insertion failed, new match ID already in database!" );
         }
         currIndexSameDate++;
      }
      // Check if the internal array is full, and if so re-size it (doubling its physical size).
      if( this.numMatches == this.matches.length ) { this.resize( this.matches.length * 2 ); }
      // Shift (by 1 position, to the right) all matches from the point of insertion on (backward traversal).
      for( int i = ( this.numMatches - 1 ); i >= currIndex; i-- ) {
         this.matches[ i + 1 ] = this.matches[i];
      }
      // Perform insertion.
      this.matches[ currIndex ] = match;
      this.numMatches++;
   }

   // Desc.: Delete a match from this container.
   // Input: A match id.
   // Output: Throws a checked (critical) exception if match id is not found.
   public void deleteMatch( String matchId ) throws TennisDatabaseException {
      // Search for the match with the input id.
      int matchIndex = this.findMatchIndex( matchId );
      // Check search result.
      if( matchIndex == -1 ) {
         // Search failed, abort deletion, and throw critical exception.
         throw new TennisDatabaseException( "Match deletion failed, match ID not found in database!" );
      }
      else {
         // Search successful, perform deletion.
         // Shift (by 1 position, to the left) all matches after the match to delete (forward traversal).
         for( int i = matchIndex; i < ( this.numMatches - 1 ); i++ ) {
            this.matches[i] = this.matches[ i + 1 ];
         }
         this.matches[ this.numMatches - 1 ] = null; // Clear the array element not used anymore.
         this.numMatches--;
         // Check if the internal array is mostly empty, and if so re-size it.
         this.shrinkIfAppropriate();
      }
   }

   // Desc.: Returns all matches in the database arranged in the output array (sorted by date, most recent first).
   // Output: Throws an exception if there are no matches in this container.
   // NOTE: the output array stores copies (deep copies) of the matches.
   public TennisMatch[] getAllMatches() throws TennisDatabaseRuntimeException {
      // TRIVIAL CASE
      if( this.numMatches == 0 ) { throw new TennisDatabaseRuntimeException( "No matches in database!" ); }
      else {
         // Here, this container is not empty, copy all matches into the output array.
         // NOTE: the internal array is sorted by date, so the output array is sorted too.
         TennisMatch[] allMatches = new TennisMatch[ this.numMatches ]; // Output array.
         for( int i = 0; i < this.numMatches; i++ ) {
            allMatches[i] = new TennisMatch( this.matches[i] ); // Deep copy.
         }
         return allMatches;
      }
   }

   // Desc.: Returns all matches of input player (id) arranged in the output array (sorted by date, most recent first).
   // Input: The id of the tennis player.
   // Output: Throws an unchecked (non-critical) exception if there are no tennis matches in the list.
   // NOTE: the output array stores copies (deep copies) of the matches.
   public TennisMatch[] getMatchesOfPlayer( String playerId ) throws TennisDatabaseRuntimeException {
      // Count the matches of the input player (to allocate the output array with the proper size).
      int numMatchesOfPlayer = 0;
      for( int i = 0; i < this.numMatches; i++ ) {
         if( this.isMatchOfPlayer( this.matches[i], playerId ) ) { numMatchesOfPlayer++; }
      }
      // Check if the input player has at least 1 match.
      if( numMatchesOfPlayer == 0 ) {
         throw new TennisDatabaseRuntimeException( "No matches in database for this player!" );
      }
      else {
         // Here, the input player has at least 1 match, copy all his matches into the output array.
         TennisMatch[] matchesOfPlayer = new TennisMatch[ numMatchesOfPlayer ]; // Output array.
         int currIndexOutput = 0; // Index of next element to write in output array.
         for( int i = 0; i < this.numMatches; i++ ) {
            if( this.isMatchOfPlayer( this.matches[i], playerId ) ) {
               matchesOfPlayer[ currIndexOutput ] = new TennisMatch( this.matches[i] ); // Deep copy.
               currIndexOutput++;
            }
         }
         return matchesOfPlayer;
      }
   }

   // Desc.: Delete all matches of a player by id (if found).
   // Output: Throws an unchecked (non-critical) exception if there is no match with that input id.
   public void deleteMatchesOfPlayer( String playerId ) throws TennisDatabaseRuntimeException {
      // Traverse the internal array once (forward), keeping only the matches not involving the input player.
      // NOTE: the matches to keep are moved to the left (compacting the array), so sorting is preserved.
      int currIndexWrite = 0; // Index of next element to write (number of matches kept so far).
      for( int i = 0; i < this.numMatches; i++ ) {
         if( !this.isMatchOfPlayer( this.matches[i], playerId ) ) {
            this.matches[ currIndexWrite ] = this.matches[i]; // Match to keep.
            currIndexWrite++;
         }
      }
      // Here, currIndexWrite is the number of matches kept.
      int numMatchesDeleted = this.numMatches - currIndexWrite;
      // Clear all array elements not used anymore.
      for( int i = currIndexWrite; i < this.numMatches; i++ ) {
         this.matches[i] = null;
      }
      this.numMatches = currIndexWrite;
      // Check if the internal array is mostly empty, and if so re-size it.
      this.shrinkIfAppropriate();
      // Check if at least 1 match has been deleted.
      if( numMatchesDeleted == 0 ) {
         throw new TennisDatabaseRuntimeException( "No match deleted, no matches in database for this player!" );
      }
   }

   // Desc.: Search for a match in this container by input id, and returns a copy of that match (if found).
   // Input: A match id.
   // Output: Throws an unchecked (non-critical) exception if there is no match with that input id.
   // NOTE: this method is not part of the interface, it is needed to search/delete a single match (by the database).
   TennisMatch getMatch( String matchId ) throws TennisDatabaseRuntimeException {
      // Search for the match with the input id.
      int matchIndex = this.findMatchIndex( matchId );
      // Check search result.
      if( matchIndex == -1 ) {
         // Search failed, throw non-critical exception.
         throw new TennisDatabaseRuntimeException( "Match search failed, match ID not found in database!" );
      }
      else {
         // Search successful, return a copy of the match.
         return new TennisMatch( this.matches[ matchIndex ] ); // Deep copy.
      }
   }

   // Internal method to search for the match with the input id.
   // NOTE: returns the array index of the match if the search is successful, -1 otherwise.
   // NOTE: a match id is the sequence matchdate+idplayer1+idplayer2 (8+5+5 characters).
   // NOTE: the search exploits the sorting (by date, most recent first), it ends at the first older match.
   private int findMatchIndex( String matchId ) {
      // TRIVIAL CASES
      if( matchId == null ) { return -1; } // Input id invalid, search failed.
      else if( matchId.length() != 18 ) { return -1; } // Input id invalid, search failed.
      else if( this.numMatches == 0 ) { return -1; } // Container empty, search failed.
      else {
         // Here, this container is not empty, perform a sequential forward search.
         // NOTE: dates in format YYYYMMDD can be compared as strings (same result as comparing dates).
         String matchDate = matchId.substring( 0, 8 ); // Date of the match to find (YYYYMMDD).
         int currIndex = 0;
         // Traverse the array until we find a match with a date not more recent than the date to find,
         // or we reach the end of the array.
         while( ( currIndex < this.numMatches ) &&
                ( this.matches[ currIndex ].getId().substring( 0, 8 ).compareTo( matchDate ) > 0 ) ) {
            currIndex++; // Advance to next match in array.
         }
         // Here, currIndex is at the first match with the date to find (if any).
         // Traverse all the matches with the date to find, checking their ids.
         while( ( currIndex < this.numMatches ) &&
                ( this.matches[ currIndex ].getId().substring( 0, 8 ).equals( matchDate ) ) ) {
            if( this.matches[ currIndex ].getId().equals( matchId ) ) { return currIndex; } // Early return.
            currIndex++; // Advance to next match in array.
         }
         return -1; // No early return means search unsuccessful.
      }
   }

   // Internal method to check if the input match involves the input player (as player 1 or player 2).
   private boolean isMatchOfPlayer( TennisMatch match, String playerId ) {
      return ( match.getIdPlayer1().equals( playerId ) || match.getIdPlayer2().equals( playerId ) );
   }

   // Internal method to re-size the internal array (changing its physical size).
   // NOTE: Assuming new capacity is enough to store all matches (newCapacity >= numMatches).
   private void resize( int newCapacity ) {
      TennisMatch[] newMatches = new TennisMatch[ newCapacity ]; // New (re-sized) internal array.
      // Copy all matches from old array into new array (from and to indices equal).
      for( int i = 0; i < this.numMatches; i++ ) {
         newMatches[i] = this.matches[i];
      }
      this.matches = newMatches;
   }

   // Internal method to shrink the internal array (halving its physical size) when it is mostly empty.
   // NOTE: the array is halved while at most 1/4 of it is used, and never below its initial size.
   private void shrinkIfAppropriate() {
      while( ( this.matches.length > INITIAL_CAPACITY ) &&
             ( this.numMatches <= ( this.matches.length / 4 ) ) ) {
         this.resize( this.matches.length / 2 );
      }
   }

}


