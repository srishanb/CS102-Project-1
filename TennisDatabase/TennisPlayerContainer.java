


// CS-102: "Computing and Algorithms I"
// Project 1.
// Prof. Giuseppe Turini
// Kettering University
// 2026-11-06
//
// Student: Srishan Bommidi (project completed on 2026-11-15, starting from the in-class code).
// AI Citation: this file includes AI-generated code (tool: Claude Code, model: Claude Fable 5.2).
// See readme.txt ("AI Citation Block") for the exact prompts and the review/verification summary.

package TennisDatabase;

// Define a package-private class TennisPlayerContainer included in the package TennisDatabase, and 
// implementing the interface InterfaceTennisPlayerContainer.
// This class should be a 2-level list to store tennis players.
// The upper-level list will be a circular doubly-linked list and sorted by player id (alphabetically). 
// The nodes in the upper-level list store tennis players (one player per node), and 
// their list of matches (in a lower-level list, one match list per node).
// The lower-level list will be 
// a sorted linked list implemented by the class SortedSinglyLinkedList<T> (provided), and 
// storing the tennis matches of that player (in the node in the upper-level list), 
// sorted by date (most recent first).
// NOTE: package-private by default (no access modifier), as required by the specifications.
class TennisPlayerContainer implements InterfaceTennisPlayerContainer {
   
   private TennisPlayerContainerNode head; // Link (ref) to first list node (player with smallest id), or null.
   private int size; // Number of nodes/players in this list.
   
   // Default constructor.
   public TennisPlayerContainer() {
      // Init this container as an EMPTY DLL circular sorted of tennis players.
      this.head = null;
      this.size = 0;
   }
   
   // Desc.: Search for a player in this container by input id, and returns a copy of that player (if found).
   // Output: Throws an unchecked (non-critical) exception if there is no player with that input id.
   public TennisPlayer getPlayer( String id ) throws TennisDatabaseRuntimeException {
      // Search for the node storing the player with the input id.
      TennisPlayerContainerNode nodeFound = this.findNode( id );
      // Check search result.
      if( nodeFound == null ) {
         // Search failed, throw non-critical exception.
         throw new TennisDatabaseRuntimeException( "Player search failed, player ID not found in database!" );
      }
      else {
         // Search successful, return a copy of the player (including the win/loss record).
         return this.buildPlayerCopy( nodeFound );
      }
   }

   // Desc.: Insert a tennis player into this container.
   // Input: A tennis player.
   // Output: Throws a checked (critical) exception if player id is already in this container.
   //         Throws a checked (critical) exception if the container is full.
   // NOTE: as a sorted circular doubly-linked list, this container will "never" be full.
   public void insertPlayer( TennisPlayer newPlayer ) throws TennisDatabaseException {
      // Check if input is valid.
      if( newPlayer == null ) { throw new TennisDatabaseException( "Player insertion failed, new player invalid!" ); }
      // Check special case (empty list)
      if(this.size == 0) {
         // Build a new node storing input player.
         TennisPlayerContainerNode newNode = new TennisPlayerContainerNode();
         newNode.player = newPlayer;
         newNode.matches = new SortedSinglyLinkedList<TennisMatch>(); // Empty list of matches.
         newNode.next = newNode;
         newNode.prev = newNode;
         // Update list linking it to new node.
         this.head = newNode;
         this.size++;
      }
      else {
         // Here, the list is not empty, search for point of insertion.
         // NOTE: perform backward search when appropriate!!!
         TennisPlayerContainerNode currNodeRef = this.head; // Ref to current node being processed.
         int currNodeIndex = 0;
         // Check if a backward search is appropriate (input player closer to the end of this list).
         if( this.isBackwardSearchAppropriate( newPlayer.getId() ) ) {
            // Backward list traversal (from last node) until we find a player not greater than input player,
            // or reach begin of list.
            currNodeRef = this.head.prev; // Last node in list (the list is circular).
            currNodeIndex = this.size - 1;
            while( ( currNodeRef.player.compareTo( newPlayer ) > 0 ) &&
                   ( currNodeIndex > 0 ) ) {
               currNodeRef = currNodeRef.prev; // Go back to previous node in list.
               currNodeIndex--;
            }
            // Here, currNodeRef points at the left-side of the insertion point (or at begin of list).
            // NOTE: the forward traversal below completes the search (with 1 step forward at most).
         }
         // Forward list traversal until we find a player greater than input player, or reach end of list.
         while( ( currNodeRef.player.compareTo( newPlayer ) < 0 ) && 
                ( currNodeIndex < this.size ) ) {
            currNodeRef = currNodeRef.next; // Advance to next node in list.
            currNodeIndex++;
         }
         // Here, currNodeRef points at the right-side of the insertion point.
         // Check if the input player id is already in this list.
         if( currNodeRef.player.compareTo( newPlayer ) == 0 ) {
            // currNodeRef stores a player with same id of input player.
            // Abort insertion, and throw critical exception.
            throw new TennisDatabaseException( "Player insertion failed, new player ID already in database!" );
         }
         else {
            // currNodeRef does NOT store a player with same id of input player.
            // Perform insertion.
            TennisPlayerContainerNode newNode = new TennisPlayerContainerNode();
            newNode.player = newPlayer;
            newNode.matches = new SortedSinglyLinkedList<TennisMatch>(); // Empty list of matches.
            newNode.next = currNodeRef;
            newNode.prev = currNodeRef.prev;
            currNodeRef.prev.next = newNode; // !!!
            currNodeRef.prev = newNode; // !!!
            this.size++;
            // Update list head if insertion happens at beginning.
            // FIX: the new node (not currNodeRef) is the new first node of this list.
            if( currNodeIndex == 0 ) { this.head = newNode; }
         }
      }
   }
   
   // Desc.: Delete a player from this container.
   // Input: A player id.
   // Output: Throws a checked (critical) exception if player id is not found.
   public void deletePlayer( String playerId ) throws TennisDatabaseException {
      // Search for the node storing the player with the input id.
      TennisPlayerContainerNode nodeToDelete = this.findNode( playerId );
      // Check search result.
      if( nodeToDelete == null ) {
         // Search failed, abort deletion, and throw critical exception.
         throw new TennisDatabaseException( "Player deletion failed, player ID not found in database!" );
      }
      else if( this.size == 1 ) {
         // SPECIAL CASE: deletion of the only node in this list, the list becomes empty.
         this.head = null;
         this.size = 0;
      }
      else {
         // Here, the list has at least 2 nodes, perform deletion (bypassing nodeToDelete in both directions).
         nodeToDelete.prev.next = nodeToDelete.next;
         nodeToDelete.next.prev = nodeToDelete.prev;
         // Update list head if deletion happens at beginning.
         if( nodeToDelete == this.head ) { this.head = nodeToDelete.next; }
         this.size--;
      }
   }
   
   // Desc.: Insert a tennis match into the lists of both tennis players of the input match.
   // Input: A tennis match.
   // Output: Throws a checked (critical) exception if the insertion is not fully successful.
   public void insertMatch( TennisMatch m ) throws TennisDatabaseException {
      // Check if input is valid.
      if( m == null ) { throw new TennisDatabaseException( "Match insertion failed, new match invalid!" ); }
      // Search for the nodes storing the 2 players of the input match.
      // NOTE: both searches are performed before any insertion, so a failed insertion changes nothing.
      TennisPlayerContainerNode nodePlayer1 = this.findNode( m.getIdPlayer1() );
      TennisPlayerContainerNode nodePlayer2 = this.findNode( m.getIdPlayer2() );
      // Check search results.
      if( ( nodePlayer1 == null ) || ( nodePlayer2 == null ) ) {
         // At least 1 player of the input match is not in this container, abort insertion.
         throw new TennisDatabaseException( "Match insertion failed, match player not in database!" );
      }
      else if( nodePlayer1 == nodePlayer2 ) {
         // The 2 players of the input match are the same player, abort insertion.
         throw new TennisDatabaseException( "Match insertion failed, match players must be different!" );
      }
      else {
         // Here, both players are in this container, insert the match in both lists of matches.
         // NOTE: the lists keep the matches sorted by date (most recent first).
         try {
            nodePlayer1.matches.insert( m );
            nodePlayer2.matches.insert( m );
         }
         catch( Exception exc ) {
            // The list of matches reported an error, forward it as a critical exception of this package.
            throw new TennisDatabaseException( "Match insertion failed, error inserting in player matches!" );
         }
      }
   }

   // Desc.: Delete a tennis match from the lists of both tennis players of the input match.
   // Input: A tennis match.
   // Output: Throws a checked (critical) exception if the match is not deleted from both lists.
   // NOTE: this method is not part of the interface, it is needed to delete a single match (by the database).
   void deleteMatch( TennisMatch m ) throws TennisDatabaseException {
      // Check if input is valid.
      if( m == null ) { throw new TennisDatabaseException( "Match deletion failed, match invalid!" ); }
      // Search for the nodes storing the 2 players of the input match.
      TennisPlayerContainerNode nodePlayer1 = this.findNode( m.getIdPlayer1() );
      TennisPlayerContainerNode nodePlayer2 = this.findNode( m.getIdPlayer2() );
      // Delete the match from the list of matches of each player (if found).
      boolean deletedFromPlayer1 = false;
      boolean deletedFromPlayer2 = false;
      if( nodePlayer1 != null ) { deletedFromPlayer1 = this.removeMatchFromNode( nodePlayer1, m ); }
      if( nodePlayer2 != null ) { deletedFromPlayer2 = this.removeMatchFromNode( nodePlayer2, m ); }
      // Check if the deletion is fully successful.
      if( !deletedFromPlayer1 || !deletedFromPlayer2 ) {
         throw new TennisDatabaseException( "Match deletion failed, match ID not found in player matches!" );
      }
   }
   
   // Desc.: Returns all players in the database arranged in the output array (sorted by id, alphabetically).
   // Output: Throws an unchecked (non-critical) exception if there are no players in this container.
   public TennisPlayer[] getAllPlayers() throws TennisDatabaseRuntimeException {
      // TRIVIAL CASE
      if( this.size == 0 ) { throw new TennisDatabaseRuntimeException( "No players in database!" ); }
      else {
         // Here, this list is not empty, traverse it forward (1 full lap), copying each player.
         // NOTE: the list is sorted by player id, so the output array is sorted too.
         TennisPlayer[] allPlayers = new TennisPlayer[ this.size ]; // Output array.
         TennisPlayerContainerNode currNodeRef = this.head; // Ref to current node being processed.
         int currNodeIndex = 0;
         while( currNodeIndex < this.size ) {
            allPlayers[ currNodeIndex ] = this.buildPlayerCopy( currNodeRef );
            currNodeRef = currNodeRef.next; // Advance to next node in list.
            currNodeIndex++;
         }
         return allPlayers;
      }
   }
   
   // Desc.: Returns copies (deep copies) of all matches of input player (id) arranged in the output array (sorted by date, most recent first).
   // Input: The id of a player.
   // Output: Throws a checked (critical) exception if the player (id) does not exists.
   //         Throws an unchecked (non-critical) exception if there are no matches (but the player id exists).
   public TennisMatch[] getMatchesOfPlayer( String playerId  ) throws TennisDatabaseException, TennisDatabaseRuntimeException {
      // Search for the node storing the player with the input id.
      TennisPlayerContainerNode nodeFound = this.findNode( playerId );
      // Check search result.
      if( nodeFound == null ) {
         // Search failed, throw critical exception.
         throw new TennisDatabaseException( "Player matches search failed, player ID not found in database!" );
      }
      else if( nodeFound.matches.size() == 0 ) {
         // Player found, but with no matches, throw non-critical exception.
         throw new TennisDatabaseRuntimeException( "No matches in database for this player!" );
      }
      else {
         // Here, the player has at least 1 match, copy all matches into the output array.
         // NOTE: the list of matches is sorted by date (most recent first), so the output array is sorted too.
         TennisMatch[] matchesOfPlayer = new TennisMatch[ nodeFound.matches.size() ]; // Output array.
         for( int i = 0; i < matchesOfPlayer.length; i++ ) {
            matchesOfPlayer[i] = new TennisMatch( nodeFound.matches.get(i) ); // Deep copy.
         }
         return matchesOfPlayer;
      }
   }
   
   // Desc.: Delete all matches of input player (id) from this container.
   // Input: A player id.
   // Output: Throws an unchecked (non-critical) exception if no match is deleted.
   public void deleteMatchesOfPlayer( String playerId  ) throws TennisDatabaseRuntimeException {
      // Search for the node storing the player with the input id.
      TennisPlayerContainerNode nodeFound = this.findNode( playerId );
      // Check search result.
      if( nodeFound == null ) {
         // Search failed, no match deleted, throw non-critical exception.
         throw new TennisDatabaseRuntimeException( "No match deleted, player ID not found in database!" );
      }
      else if( nodeFound.matches.size() == 0 ) {
         // Player found, but with no matches, no match deleted, throw non-critical exception.
         throw new TennisDatabaseRuntimeException( "No match deleted, no matches in database for this player!" );
      }
      else {
         // Here, the player has at least 1 match.
         // Each match is stored also in the list of the opponent, delete it from there first.
         for( int i = 0; i < nodeFound.matches.size(); i++ ) {
            TennisMatch currMatch = nodeFound.matches.get(i);
            // Find the id of the opponent in the current match.
            String opponentId = currMatch.getIdPlayer1();
            if( opponentId.equals( playerId ) ) { opponentId = currMatch.getIdPlayer2(); }
            // Delete the current match from the list of matches of the opponent (if found).
            TennisPlayerContainerNode nodeOpponent = this.findNode( opponentId );
            if( nodeOpponent != null ) { this.removeMatchFromNode( nodeOpponent, currMatch ); }
         }
         // Then, delete all matches of the player (replacing the list with a new empty list).
         nodeFound.matches = new SortedSinglyLinkedList<TennisMatch>();
      }
   }

   // Internal method to check if a backward search (from last node) is appropriate for the input player id.
   // NOTE: the list is sorted by id and circular, so first node (smallest id) and last node (greatest id)
   //       are both directly available: the search starts from the one closer to the input id.
   // NOTE: returns true if the first character of the input id is closer to the id of the last node.
   private boolean isBackwardSearchAppropriate( String playerId ) {
      // TRIVIAL CASE
      if( ( this.size == 0 ) || ( playerId == null ) ) { return false; }
      else {
         String idFirstNode = this.head.player.getId(); // Smallest id in this list.
         String idLastNode = this.head.prev.player.getId(); // Greatest id in this list.
         // Check if the ids have at least 1 character to compare.
         if( ( playerId.length() == 0 ) || ( idFirstNode.length() == 0 ) || ( idLastNode.length() == 0 ) ) {
            return false; // Forward search by default.
         }
         else {
            // Compare the distance (alphabetical) of input id from first node and from last node.
            int distanceFromFirstNode = playerId.charAt(0) - idFirstNode.charAt(0);
            int distanceFromLastNode = idLastNode.charAt(0) - playerId.charAt(0);
            return ( distanceFromLastNode < distanceFromFirstNode );
         }
      }
   }

   // Internal method to search for the node storing the player with the input id.
   // NOTE: returns a ref to the node if the search is successful, null otherwise.
   // NOTE: the search exploits the sorting (it ends as soon as the input id is passed),
   //       and the list structure (backward search from last node when appropriate).
   private TennisPlayerContainerNode findNode( String playerId ) {
      // TRIVIAL CASE
      if( ( this.size == 0 ) || ( playerId == null ) ) { return null; }
      else {
         // Here, this list is not empty, perform a sequential search.
         TennisPlayerContainerNode currNodeRef = this.head; // Ref to current node being processed.
         int currNodeIndex = 0;
         if( this.isBackwardSearchAppropriate( playerId ) ) {
            // Backward list traversal (from last node) until we find an id not greater than input id,
            // or reach begin of list.
            currNodeRef = this.head.prev; // Last node in list (the list is circular).
            currNodeIndex = this.size - 1;
            while( ( currNodeRef.player.getId().compareTo( playerId ) > 0 ) &&
                   ( currNodeIndex > 0 ) ) {
               currNodeRef = currNodeRef.prev; // Go back to previous node in list.
               currNodeIndex--;
            }
         }
         else {
            // Forward list traversal (from first node) until we find an id not smaller than input id,
            // or reach end of list.
            while( ( currNodeRef.player.getId().compareTo( playerId ) < 0 ) &&
                   ( currNodeIndex < ( this.size - 1 ) ) ) {
               currNodeRef = currNodeRef.next; // Advance to next node in list.
               currNodeIndex++;
            }
         }
         // Here, check why the loop ended.
         if( currNodeRef.player.getId().equals( playerId ) ) {
            // Loop ended because input id is stored at node pointed by currNodeRef, search successful.
            return currNodeRef;
         }
         else {
            // Loop passed the input id (or reached the end of the traversal), search failed.
            return null;
         }
      }
   }

   // Internal method to build a copy (deep copy) of the player stored in the input node.
   // NOTE: the copy includes the win/loss record, computed from the list of matches in the input node.
   private TennisPlayer buildPlayerCopy( TennisPlayerContainerNode node ) {
      TennisPlayer playerCopy = new TennisPlayer( node.player ); // Deep copy of the player.
      String playerId = node.player.getId();
      int wins = 0; // Number of matches won by the player.
      int losses = 0; // Number of matches lost by the player.
      // Traverse the list of matches of the player, checking the winner of each match.
      for( int i = 0; i < node.matches.size(); i++ ) {
         TennisMatch currMatch = node.matches.get(i);
         // Check if the player is player 1 or player 2 in the current match.
         boolean isPlayer1 = currMatch.getIdPlayer1().equals( playerId );
         // Check if the player won the current match.
         if( ( isPlayer1 && ( currMatch.getWinner() == 1 ) ) ||
             ( !isPlayer1 && ( currMatch.getWinner() == 2 ) ) ) { wins++; }
         else { losses++; }
      }
      playerCopy.setWinLossRecord( wins, losses );
      return playerCopy;
   }

   // Internal method to delete the input match from the list of matches of the input node.
   // NOTE: returns true if the deletion is successful, false otherwise (match not found).
   // NOTE: the search exploits the sorting (by date, most recent first), it ends at the first older match.
   private boolean removeMatchFromNode( TennisPlayerContainerNode node, TennisMatch m ) {
      int currMatchIndex = 0;
      // Traverse the list of matches until we find a match older than input match, or reach end of list.
      while( ( currMatchIndex < node.matches.size() ) &&
             ( node.matches.get( currMatchIndex ).compareTo( m ) >= 0 ) ) {
         // Check if the current match is the input match (same id).
         if( node.matches.get( currMatchIndex ).getId().equals( m.getId() ) ) {
            node.matches.delete( currMatchIndex ); // Match found, perform deletion.
            return true; // Early return.
         }
         currMatchIndex++;
      }
      return false; // No early return means match not found.
   }
      
}


