


// CS-102: "Computing and Algorithms II"
// Prof. Giuseppe Turini
// Kettering University
// 2022-04-09

package TennisDatabase;

import java.lang.Comparable;
import java.lang.String;

// Public interface providing the specifications for the TennisMatch class.
// Note: Assignment 1 of CS-102 (Spring 2022).
public interface InterfaceTennisMatch extends Comparable<TennisMatch> {      
   
   // Accessors (getters).
   public String getId();
   public String getDate();
   public int getDateYear();
   public int getDateMonth();
   public int getDateDay();
   public String getIdPlayer1();
   public String getIdPlayer2();
   public String getTournament();
   public String getMatchScore();
   public int getWinner();
   
}


