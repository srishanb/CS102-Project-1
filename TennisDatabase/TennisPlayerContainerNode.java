


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

// Internal (to the package) class implementing a node for a DLL circular sorted of tennis players...
// NOTE: package-private by default.
class TennisPlayerContainerNode {
   
   TennisPlayer player; // Tennis player stored in this node.
   SortedSinglyLinkedList<TennisMatch> matches; // Matches of the player (sorted by date, most recent first).
   
   TennisPlayerContainerNode prev; // Link for DLL implementation.
   TennisPlayerContainerNode next; // Link for DLL implementation.
   
}


