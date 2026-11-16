


// CS-102: "Computing and Algorithms II"
// Prof. Giuseppe Turini
// Kettering University
// 2022-04-09

package TennisDatabase;

import java.lang.RuntimeException;
import java.lang.String;

// Custom (unchecked) exception for the TennisDatabase package, representing non critical runtime errors (handling is optional).
// Note: Assignment 1 of CS-102 (Spring 2022).
public class TennisDatabaseRuntimeException extends java.lang.RuntimeException {
   
   // Desc.: Constructor.
   // Input: Description of the runtime error.
   public TennisDatabaseRuntimeException( String s ) { super(s); }
   
}


