


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

// Define a public class TennisPlayer included in the package TennisDatabase, 
// and implementing the InterfaceTennisPlayer.
// This class contains fields corresponding to a given player, 
// and methods to deal with the player details and statistics.
// NOTE: A TennisPlayer object should store the data of a tennis player line.
// EXAMPLE: PLAYER/DJO87/NOVAK/DJOKOVIC/1987/SERBIA
// NOTE: All internal data will be stored in uppercase!
public class TennisPlayer implements InterfaceTennisPlayer {

   private String id; // Player unique id (unique!).
   private String nameFirst; // First name.
   private String nameLast; // Last name.
   private int yearBirth; // Birth year.
   private String country; // Country of origin.

   private int wins; // Number of matches won (statistics, see setWinLossRecord).
   private int losses; // Number of matches lost (statistics, see setWinLossRecord).

   //private SortedSinglyLinkedList<TennisMatch> matches; // List of matches this player has played.
   
   // Non-default constructor.
   public TennisPlayer(String i, String fn, String ln, int by, String c) {
      this.id = i;
      this.nameFirst = fn;
      this.nameLast = ln;
      this.yearBirth = by;
      this.country = c;
      this.wins = 0;
      this.losses = 0;
   }

   // Copy constructor (used to build deep copies of tennis players).
   // Input: The tennis player to copy.
   public TennisPlayer(TennisPlayer other) {
      this.id = other.id;
      this.nameFirst = other.nameFirst;
      this.nameLast = other.nameLast;
      this.yearBirth = other.yearBirth;
      this.country = other.country;
      this.wins = other.wins;
      this.losses = other.losses;
   }

   // Accessors (getters).
   public String getId() { return this.id; }
   public String getFirstName() { return this.nameFirst; }
   public String getLastName() { return this.nameLast; }
   public int getBirthYear() { return this.yearBirth; }
   public String getCountry() { return this.country; }

   // Accessors (getters) for the player statistics (win/loss record).
   public int getWins() { return this.wins; }
   public int getLosses() { return this.losses; }

   // Desc.: Set the player statistics (win/loss record).
   // Input: The number of matches won, and the number of matches lost.
   // NOTE: package-private, only the classes of this package (the containers) can update the statistics.
   // NOTE: The statistics are computed by the player container from the list of matches of this player.
   void setWinLossRecord(int w, int l) {
      this.wins = w;
      this.losses = l;
   }
   
   // See Comparable interface: https://docs.oracle.com/javase/8/docs/api/java/lang/Comparable.html
   public int compareTo(TennisPlayer other) { 
      // Compare tennis players: "this" against "other".
      // Rely completely on the String vs String comparison (id vs id) by the String class.
      return this.id.compareTo(other.id);
      // NOTE: Can I access id of "other" without using the accessor/getter? (YES)
   }
   
   // Overloading toString method inherited from our superclass (Object).
   public String toString() {
      return "P-" + this.id;
   }

}


