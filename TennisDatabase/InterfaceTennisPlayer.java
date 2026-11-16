


// CS-102: "Computing and Algorithms II"
// Prof. Giuseppe Turini
// Kettering University
// 2022-04-09

package TennisDatabase;

import java.lang.Comparable;
import java.lang.String;

// Public interface providing the specifications for the TennisPlayer class.
// Note: Assignment 1 of CS-102 (Spring 2022).
public interface InterfaceTennisPlayer extends Comparable<TennisPlayer> {
   
   // Accessors (getters).
   public String getId();
   public String getFirstName();
   public String getLastName();
   public int getBirthYear();
   public String getCountry();
   
}


