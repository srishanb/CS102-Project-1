


// CS-102: "Computing and Algorithms II"
// Prof. Giuseppe Turini
// Kettering University
// 2022-04-09

package TennisDatabase;

import java.lang.Exception;
import java.lang.String;

// Custom (checked) exception for the TennisDatabase package, representing critical runtime errors (that must be handled).
// Note: Assignment 1 of CS-102 (Spring 2022).
public class TennisDatabaseException extends java.lang.Exception {
   
   // Desc.: Constructor.
   // Input: Description of the runtime error.
   public TennisDatabaseException( String s ) { super(s); }
   
}


