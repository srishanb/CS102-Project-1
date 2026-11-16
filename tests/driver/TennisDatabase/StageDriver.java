// Throwaway test driver for the two containers (NOT part of the submission).
// It lives in package TennisDatabase so it can reach the package-private containers.
// Compile it together with the project sources, then run: java TennisDatabase.StageDriver

package TennisDatabase;

public class StageDriver {

   private static int numPassed = 0;
   private static int numFailed = 0;

   private static void check( String name, boolean condition ) {
      if( condition ) { numPassed++; }
      else { numFailed++; System.out.println( "FAIL: " + name ); }
   }

   private static String ids( TennisPlayer[] players ) {
      String s = "";
      for( int i = 0; i < players.length; i++ ) { s += players[i].getId() + " "; }
      return s.trim();
   }

   private static String ids( TennisMatch[] matches ) {
      String s = "";
      for( int i = 0; i < matches.length; i++ ) { s += matches[i].getId() + " "; }
      return s.trim();
   }

   private static TennisPlayer p( String id ) { return new TennisPlayer( id, "F" + id, "L" + id, 1990, "USA" ); }

   private static TennisMatch m( int date, String id1, String id2, int winner ) {
      return new TennisMatch( date / 10000, ( date / 100 ) % 100, date % 100, id1, id2, "T", winner, "6-0" );
   }

   private static String allPlayers( TennisPlayerContainer c ) {
      try { return ids( c.getAllPlayers() ); }
      catch( TennisDatabaseRuntimeException e ) { return "<empty>"; }
   }

   private static boolean insertFails( TennisPlayerContainer c, TennisPlayer pl ) {
      try { c.insertPlayer( pl ); return false; } catch( TennisDatabaseException e ) { return true; }
   }

   private static boolean found( TennisPlayerContainer c, String id ) {
      try { return c.getPlayer( id ).getId().equals( id ); }
      catch( TennisDatabaseRuntimeException e ) { return false; }
   }

   private static String record( TennisPlayerContainer c, String id ) {
      TennisPlayer pl = c.getPlayer( id );
      return pl.getWins() + "-" + pl.getLosses();
   }

   private static String matchesOf( TennisPlayerContainer c, String id ) {
      try { return ids( c.getMatchesOfPlayer( id ) ); }
      catch( TennisDatabaseException e ) { return "<unknown>"; }
      catch( TennisDatabaseRuntimeException e ) { return "<none>"; }
   }

   private static String allMatches( TennisMatchContainer c ) {
      try { return ids( c.getAllMatches() ); }
      catch( TennisDatabaseRuntimeException e ) { return "<empty>"; }
   }

   private static void testDataClasses() {
      TennisMatch a = m( 20150316, "DJO87", "FED81", 1 );
      check( "match id format", a.getId().equals( "20150316DJO87FED81" ) );
      check( "match date format", a.getDate().equals( "2015-03-16" ) );
      check( "match compare newer", m( 20151116, "A", "B", 1 ).compareTo( a ) > 0 );
      check( "match compare same date", m( 20150316, "X", "Y", 1 ).compareTo( a ) == 0 );
      TennisMatch copy = new TennisMatch( a );
      check( "match copy equal id", copy.getId().equals( a.getId() ) && ( copy != a ) );
      TennisPlayer pl = p( "DJO87" );
      TennisPlayer plCopy = new TennisPlayer( pl );
      check( "player copy", plCopy.getId().equals( "DJO87" ) && ( plCopy != pl ) && plCopy.getBirthYear() == 1990 );
   }

   private static void testPlayerContainer() throws Exception {
      TennisPlayerContainer c = new TennisPlayerContainer();
      check( "empty getAllPlayers throws", allPlayers( c ).equals( "<empty>" ) );
      check( "empty getPlayer fails", !found( c, "AAA11" ) );
      String[] order = { "MMM11", "ZZZ11", "AAA11", "FFF11", "TTT11", "BBB11", "MMA11", "ZZZ99", "AAA00" };
      for( int i = 0; i < order.length; i++ ) { c.insertPlayer( p( order[i] ) ); }
      check( "sorted after scrambled inserts: " + allPlayers( c ),
             allPlayers( c ).equals( "AAA00 AAA11 BBB11 FFF11 MMA11 MMM11 TTT11 ZZZ11 ZZZ99" ) );
      for( int i = 0; i < order.length; i++ ) {
         check( "found " + order[i], found( c, order[i] ) );
         check( "duplicate rejected " + order[i], insertFails( c, p( order[i] ) ) );
      }
      check( "not found before head", !found( c, "AAA" ) );
      check( "not found middle", !found( c, "GGG11" ) );
      check( "not found near tail", !found( c, "YYY11" ) );
      check( "not found after tail", !found( c, "ZZZZZ" ) );
      check( "still sorted after rejected duplicates",
             allPlayers( c ).equals( "AAA00 AAA11 BBB11 FFF11 MMA11 MMM11 TTT11 ZZZ11 ZZZ99" ) );

      // Matches: AAA11 beats ZZZ11 twice, loses to MMM11 once.
      TennisMatch m1 = m( 20200101, "AAA11", "ZZZ11", 1 );
      TennisMatch m2 = m( 20210101, "ZZZ11", "AAA11", 2 );
      TennisMatch m3 = m( 20200615, "AAA11", "MMM11", 2 );
      c.insertMatch( m1 ); c.insertMatch( m2 ); c.insertMatch( m3 );
      check( "matches of AAA11 newest first: " + matchesOf( c, "AAA11" ),
             matchesOf( c, "AAA11" ).equals( "20210101ZZZ11AAA11 20200615AAA11MMM11 20200101AAA11ZZZ11" ) );
      check( "record AAA11 " + record( c, "AAA11" ), record( c, "AAA11" ).equals( "2-1" ) );
      check( "record ZZZ11 " + record( c, "ZZZ11" ), record( c, "ZZZ11" ).equals( "0-2" ) );
      check( "record MMM11 " + record( c, "MMM11" ), record( c, "MMM11" ).equals( "1-0" ) );
      check( "matches of unknown player", matchesOf( c, "QQQ11" ).equals( "<unknown>" ) );
      check( "matches of player without matches", matchesOf( c, "BBB11" ).equals( "<none>" ) );
      boolean threw = false;
      try { c.insertMatch( m( 20220101, "AAA11", "QQQ11", 1 ) ); } catch( TennisDatabaseException e ) { threw = true; }
      check( "insertMatch unknown player throws", threw );
      check( "nothing half-inserted", matchesOf( c, "AAA11" ).split( " " ).length == 3 );

      // Delete a single match (uses the fixed SortedSinglyLinkedList.delete).
      c.deleteMatch( new TennisMatch( m3 ) );
      check( "after deleteMatch AAA11: " + matchesOf( c, "AAA11" ),
             matchesOf( c, "AAA11" ).equals( "20210101ZZZ11AAA11 20200101AAA11ZZZ11" ) );
      check( "after deleteMatch MMM11", matchesOf( c, "MMM11" ).equals( "<none>" ) );
      check( "record AAA11 after delete", record( c, "AAA11" ).equals( "2-0" ) );
      threw = false;
      try { c.deleteMatch( new TennisMatch( m3 ) ); } catch( TennisDatabaseException e ) { threw = true; }
      check( "deleteMatch twice throws", threw );

      // Delete all matches of a player (must also clean the opponent lists).
      c.insertMatch( m3 );
      c.deleteMatchesOfPlayer( "AAA11" );
      check( "deleteMatchesOfPlayer own list", matchesOf( c, "AAA11" ).equals( "<none>" ) );
      check( "deleteMatchesOfPlayer opponent ZZZ11", matchesOf( c, "ZZZ11" ).equals( "<none>" ) );
      check( "deleteMatchesOfPlayer opponent MMM11", matchesOf( c, "MMM11" ).equals( "<none>" ) );
      threw = false;
      try { c.deleteMatchesOfPlayer( "AAA11" ); } catch( TennisDatabaseRuntimeException e ) { threw = true; }
      check( "deleteMatchesOfPlayer with none throws runtime", threw );

      // Delete players: head, tail, middle, unknown.
      c.deletePlayer( "AAA00" );
      check( "delete head: " + allPlayers( c ), allPlayers( c ).equals( "AAA11 BBB11 FFF11 MMA11 MMM11 TTT11 ZZZ11 ZZZ99" ) );
      c.deletePlayer( "ZZZ99" );
      check( "delete tail: " + allPlayers( c ), allPlayers( c ).equals( "AAA11 BBB11 FFF11 MMA11 MMM11 TTT11 ZZZ11" ) );
      c.deletePlayer( "MMA11" );
      check( "delete middle: " + allPlayers( c ), allPlayers( c ).equals( "AAA11 BBB11 FFF11 MMM11 TTT11 ZZZ11" ) );
      threw = false;
      try { c.deletePlayer( "QQQ11" ); } catch( TennisDatabaseException e ) { threw = true; }
      check( "delete unknown throws", threw );
      String[] left = { "AAA11", "BBB11", "FFF11", "MMM11", "TTT11", "ZZZ11" };
      for( int i = 0; i < left.length; i++ ) { check( "still found " + left[i], found( c, left[i] ) ); }
      // New smallest and new greatest after deletions (checks links in both directions).
      c.insertPlayer( p( "AAA00" ) ); c.insertPlayer( p( "ZZZ99" ) );
      check( "reinsert ends: " + allPlayers( c ), allPlayers( c ).equals( "AAA00 AAA11 BBB11 FFF11 MMM11 TTT11 ZZZ11 ZZZ99" ) );
      // Delete everything, then reuse the container.
      String[] rest = { "FFF11", "AAA00", "ZZZ99", "TTT11", "AAA11", "ZZZ11", "BBB11", "MMM11" };
      for( int i = 0; i < rest.length; i++ ) { c.deletePlayer( rest[i] ); }
      check( "empty after deleting all", allPlayers( c ).equals( "<empty>" ) );
      c.insertPlayer( p( "KKK11" ) ); c.insertPlayer( p( "CCC11" ) );
      check( "reuse after empty: " + allPlayers( c ), allPlayers( c ).equals( "CCC11 KKK11" ) );
   }

   private static void testMatchContainer() throws Exception {
      TennisMatchContainer c = new TennisMatchContainer();
      check( "empty getAllMatches throws", allMatches( c ).equals( "<empty>" ) );
      check( "empty count", c.getNumMatches() == 0 );
      int[] dates = { 20150316, 20151026, 20150511, 20150712, 20150817, 20150913, 20151116, 20150223, 20151005 };
      for( int i = 0; i < dates.length; i++ ) { c.insertMatch( m( dates[i], "AAA11", "BBB11", 1 ) ); }
      check( "count after 9 inserts (array grew past 4 and 8)", c.getNumMatches() == 9 );
      TennisMatch[] all = c.getAllMatches();
      boolean sorted = true;
      for( int i = 1; i < all.length; i++ ) { if( all[i - 1].compareTo( all[i] ) < 0 ) { sorted = false; } }
      check( "most recent first: " + allMatches( c ), sorted && all[0].getId().startsWith( "20151116" ) && all[8].getId().startsWith( "20150223" ) );
      boolean threw = false;
      try { c.insertMatch( m( 20150712, "AAA11", "BBB11", 2 ) ); } catch( TennisDatabaseException e ) { threw = true; }
      check( "duplicate id rejected", threw && c.getNumMatches() == 9 );
      // Same date, different players: allowed, and placed in front of the existing one on that date.
      c.insertMatch( m( 20150712, "CCC11", "AAA11", 1 ) );
      all = c.getAllMatches();
      int pos = -1;
      for( int i = 0; i < all.length; i++ ) { if( all[i].getId().equals( "20150712CCC11AAA11" ) ) { pos = i; } }
      check( "same date goes in front", pos >= 0 && all[pos + 1].getId().equals( "20150712AAA11BBB11" ) );
      check( "getMatch found", c.getMatch( "20150712CCC11AAA11" ).getIdPlayer1().equals( "CCC11" ) );
      threw = false;
      try { c.getMatch( "20150712CCC11ZZZ11" ); } catch( TennisDatabaseRuntimeException e ) { threw = true; }
      check( "getMatch unknown throws runtime", threw );
      threw = false;
      try { c.getMatch( "garbage" ); } catch( TennisDatabaseRuntimeException e ) { threw = true; }
      check( "getMatch malformed id throws runtime", threw );
      check( "matches of CCC11", ids( c.getMatchesOfPlayer( "CCC11" ) ).equals( "20150712CCC11AAA11" ) );
      check( "matches of AAA11 count", c.getMatchesOfPlayer( "AAA11" ).length == 10 );
      threw = false;
      try { c.getMatchesOfPlayer( "QQQ11" ); } catch( TennisDatabaseRuntimeException e ) { threw = true; }
      check( "matches of unknown throws runtime", threw );
      // Delete first, last, middle, unknown.
      c.deleteMatch( "20151116AAA11BBB11" );
      c.deleteMatch( "20150223AAA11BBB11" );
      c.deleteMatch( "20150712AAA11BBB11" );
      threw = false;
      try { c.deleteMatch( "20150712AAA11BBB11" ); } catch( TennisDatabaseException e ) { threw = true; }
      check( "delete unknown throws", threw );
      all = c.getAllMatches();
      sorted = true;
      for( int i = 1; i < all.length; i++ ) { if( all[i - 1].compareTo( all[i] ) < 0 ) { sorted = false; } }
      check( "after deletes: " + allMatches( c ), sorted && c.getNumMatches() == 7 && all[0].getId().startsWith( "20151026" ) );
      // Delete all matches of BBB11 (6 of the 7), CCC11 match survives.
      c.deleteMatchesOfPlayer( "BBB11" );
      check( "deleteMatchesOfPlayer: " + allMatches( c ), allMatches( c ).equals( "20150712CCC11AAA11" ) );
      threw = false;
      try { c.deleteMatchesOfPlayer( "BBB11" ); } catch( TennisDatabaseRuntimeException e ) { threw = true; }
      check( "deleteMatchesOfPlayer none throws runtime", threw );
      c.deleteMatch( "20150712CCC11AAA11" );
      check( "empty again", allMatches( c ).equals( "<empty>" ) && c.getNumMatches() == 0 );
      // Grow, shrink and grow again with many matches.
      for( int i = 1; i <= 28; i++ ) { c.insertMatch( m( 20200100 + i, "AAA11", "BBB11", 1 ) ); }
      for( int i = 1; i <= 27; i++ ) { c.deleteMatch( "202001" + ( i < 10 ? "0" + i : "" + i ) + "AAA11BBB11" ); }
      check( "shrunk to one: " + allMatches( c ), allMatches( c ).equals( "20200128AAA11BBB11" ) );
      for( int i = 1; i <= 27; i++ ) { c.insertMatch( m( 20200100 + i, "AAA11", "BBB11", 1 ) ); }
      all = c.getAllMatches();
      sorted = true;
      for( int i = 1; i < all.length; i++ ) { if( all[i - 1].compareTo( all[i] ) <= 0 ) { sorted = false; } }
      check( "regrown and sorted", sorted && c.getNumMatches() == 28 );
   }

   public static void main( String[] args ) {
      try {
         testDataClasses();
         testPlayerContainer();
         testMatchContainer();
      }
      catch( Exception e ) {
         numFailed++;
         System.out.println( "UNEXPECTED EXCEPTION: " + e );
         e.printStackTrace( System.out );
      }
      System.out.println( "Passed: " + numPassed + ", Failed: " + numFailed );
   }

}
