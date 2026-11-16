


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

// Define a public class TennisMatch included in the package TennisDatabase, 
// and implementing the InterfaceTennisMatch.
// This class contains members corresponding to a given match, 
// and methods to deal with the match details.
// NOTE: A TennisMatch object should store the data of a tennis match line.
// EXAMPLE: MATCH/20150316/DJO87/FED81/INDIAN WELLS/WINNER1/6-3,6-7,6-2
// NOTE: All internal data will be stored in uppercase!
// NOTE: The concatenated sequence matchdate+idplayer1+idplayer2 is the unique id of a match.
public class TennisMatch implements InterfaceTennisMatch {

   private int dateYear; // ...
   private int dateMonth; // ...
   private int dateDay; // ...
   private String idPlayer1; // NOTE: Unique id!
   private String idPlayer2; // NOTE: Unique id!
   private String tournament; // NOTE: Could include white spaces and special characters!
   private int winner; // NOTE: 1 if player 1 won, 2 if player 2 won, 0 if invalid.
   private String score; // NOTE: single string.

   // Non-default constructor.
   // Input: The match date (year, month, day), the ids of player 1 and player 2, the tournament,
   //        the winner (1 if player 1 won, 2 if player 2 won), and the match score.
   public TennisMatch(int dy, int dm, int dd, String ip1, String ip2, String t, int w, String s) {
      this.dateYear = dy;
      this.dateMonth = dm;
      this.dateDay = dd;
      this.idPlayer1 = ip1;
      this.idPlayer2 = ip2;
      this.tournament = t;
      this.winner = w;
      this.score = s;
   }

   // Copy constructor (used to build deep copies of tennis matches).
   // Input: The tennis match to copy.
   public TennisMatch(TennisMatch other) {
      this.dateYear = other.dateYear;
      this.dateMonth = other.dateMonth;
      this.dateDay = other.dateDay;
      this.idPlayer1 = other.idPlayer1;
      this.idPlayer2 = other.idPlayer2;
      this.tournament = other.tournament;
      this.winner = other.winner;
      this.score = other.score;
   }

   // Accessors (getters).

   // NOTE: The concatenated sequence matchdate+idplayer1+idplayer2 is the unique id of a match.
   // NOTE: The match date in the id has the same format used in the input file (YYYYMMDD, no separators).
   // EXAMPLE: 20150316DJO87FED81
   public String getId() {
      return this.getDate().replace("-", "") + this.idPlayer1 + this.idPlayer2;
   }

   // Build a string-representation for the internal int-based date data (format is YYYY-MM-DD).
   public String getDate() {
      String dateYearString = String.valueOf(this.dateYear);
      String dateMonthString = String.valueOf(this.dateMonth);
      String dateDayString = String.valueOf(this.dateDay);
      // Zero-padding (e.g., month 3 becomes 03), so the format is always YYYY-MM-DD.
      while(dateYearString.length() < 4) { dateYearString = "0" + dateYearString; }
      if(this.dateMonth < 10) { dateMonthString = "0" + dateMonthString; }
      if(this.dateDay < 10) { dateDayString = "0" + dateDayString; }
      return dateYearString + "-" + dateMonthString + "-" + dateDayString;
   }
   
   public int getDateYear() { return this.dateYear; }
   public int getDateMonth() { return this.dateMonth; }
   public int getDateDay() { return this.dateDay; }
   public String getIdPlayer1() { return this.idPlayer1; }
   public String getIdPlayer2() { return this.idPlayer2; }
   public String getTournament() { return this.tournament; }
   public String getMatchScore() { return this.score; }
   public int getWinner() { return this.winner; }
   
   // See Comparable interface: https://docs.oracle.com/javase/8/docs/api/java/lang/Comparable.html
   public int compareTo(TennisMatch other) { 
      // Compare tennis matches: "this" against "other".
      // Date-based comparison: greater than means more recent.
      if(this.dateYear > other.dateYear) { return 1; } // "this" match is more recent.
      else if(this.dateYear < other.dateYear) { return -1; } // "this" match is less recent.
      else {
         // Here "this" and "other" matches have the same year, compare their months.
         if(this.dateMonth > other.dateMonth) { return 1; } // "this" match is more recent.
         else if(this.dateMonth < other.dateMonth) { return -1; } // "this" match is less recent.
         else {
            // Here "this" and "other" matches have the same year AND the same month, compare their days.
            if(this.dateDay > other.dateDay) { return 1; } // "this" match is more recent.
            else if(this.dateDay < other.dateDay) { return -1; } // "this" match is less recent.
            else {
               // Here "this" and "other" matches have exactly the same date.
               return 0;
            }
         }
      }
   } // End compareTo.
   
}


