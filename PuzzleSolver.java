/* Client class to implement A* and solve the 8-tile puzzle

Alden Hart and Spencer Chadinha
1/21/2015
*/

import java.util.List;
import java.util.Iterator;
// import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Queue;
import java.util.PriorityQueue;
import java.lang.RuntimeException;
import java.lang.ArrayIndexOutOfBoundsException;

public class PuzzleSolver {

    private static final int DEFAULT_HFUNC = 1;

    public static void main(String[] args) {
        // PuzzleBoard x = new PuzzleBoard(3);
        // System.out.println(x);

        PuzzleBoard x = new PuzzleBoard(3, 7, 2, 4,
                                        5, 0, 6,
                                        8, 3, 1);
        // System.out.println(x);
        // System.out.println(x.getBlankRow() + " " + x.getBlankCol());
        // System.out.println(x.neighborhood(x.getBlankRow(), x.getBlankCol()));

        // x.move(2, 1);
        // System.out.println(x);
        // System.out.println(x.getBlankRow() + " " + x.getBlankCol());
        // System.out.println(x.neighborhood(x.getBlankRow(), x.getBlankCol()));
        // PuzzleBoard x = new PuzzleBoard(3, 1, 0, 2,
        //                                 3, 4, 5,
        //                                 6, 7, 8);
        // System.out.println(x);
        // x.move(1, 0);
        // System.out.println(x);
        // x.move(2, 0);
        // System.out.println(x);
        // System.out.println(x.neighborhood(2, 0));
        // x.move(2, 1);
        // System.out.println(x);

        int hFunc = DEFAULT_HFUNC;
        try {
            hFunc = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid choice of heuristic, defaulting");
            System.out.println("to h1, misplaced tiles heuristic.");
        }
        int depth = solve(x, hFunc);
        // System.out.println(x);
        System.out.println(depth);

        // PuzzleBoard y = x.copy();
        // System.out.println(y);
        // x.move(1, 0);
        // System.out.println(x);
        // System.out.println(y);

        // x.move(1, 0);
        // System.out.println(x);
        // x.move(0, 0);
        // System.out.println(x);
        // try {
        //     System.out.println(args[0]);
        // } catch (ArrayIndexOutOfBoundsException e) {
        //     System.out.println("Invalid choice of heuristic, defaulting");
        //     System.out.println("to h1, misplaced tiles heuristic.");
        // }
        // System.out.println(x.getBoard()[1][0]);
        // System.out.println(x.getBoard()[1][0].getRow());
        // System.out.println(x.getBoard()[1][0].getCol());
        // x.move(1, 0);
        // System.out.println(x);
        // System.out.println(x.getBoard()[1][0]);
        // System.out.println(x.getBoard()[1][0].getRow());
        // System.out.println(x.getBoard()[1][0].getCol());
        // System.out.println(x.getBlankRow());
        // System.out.println(x.getBlankCol());
        // System.out.println(x.h1());
        // System.out.println(x.h2());
        // System.out.println(x.moveExists(6));

    }

    /* Solves the board using A* search.

    @param board the board you're solving
    @param hFunc the heuristic function to use

    @return the number of steps needed to find the solution
    */
    public static int solve(PuzzleBoard board, int hFunc) {
        /* Needs:
            - Heuristic function (check)
            - g(n) - cost to reach the node from the start 
                - This should just go up by 1 every time you make a move, right?
        */
        return aStar(board, hFunc);
    }

    /* Private helper method for solve(). Implements A* and tracks the depth
    of the search.

    @param board the board to solve
    @param numTries the current depth of your search
    @param fLimit the f-cost limit
    @param hFunc the heuristic function to use

    @return the number of states it took to find a solution
    */
    private static int aStar(PuzzleBoard board, int hFunc) {

        int pathCost = 0;
        Queue<PuzzleBoard> frontier = new PriorityQueue<PuzzleBoard>();
        Set<PuzzleBoard> explored = new HashSet<PuzzleBoard>();
        PuzzleBoard node = board;
        if (hFunc == 2) {
            node.setScore(node.h2());
        } else {
            node.setScore(node.h1());
        }
        frontier.add(node);

        while (true) {
            if (frontier.isEmpty()) {
                // This should never ever happen ever
                throw new RuntimeException("Failed to solve board.");
            }
            // Choose the lowest-cost node in frontier
            node = frontier.poll();
            if (node.isSolved()) {
                System.out.println(node);
                return pathCost;
            }
            explored.add(node);
            List<Tile> possibleMoves = node.neighborhood(node.getBlankRow(),
                                                         node.getBlankCol());
            System.out.println("Node:");
            System.out.println(node);
            System.out.println(possibleMoves);
            for (Tile t : possibleMoves) {
                PuzzleBoard child = node.copy();
                child.move(t.getRow(), t.getCol());
                System.out.println("Possible move");
                System.out.println(child);
                if (hFunc == 2) {
                    child.setScore(child.h2() + pathCost);
                } else {
                    child.setScore(child.h1() + pathCost);
                }
                System.out.println(child.getScore());
                if (!explored.contains(child) && !frontier.contains(child)) {
                    frontier.add(child);
                } else if (frontier.contains(child)) {
                    // If it's in frontier, check if the board in frontier has
                    // a higher cost than this one. This should never happen,
                    // right? Because it'll have the same heuristic score and
                    // a lower path cost...
                    int childScore = child.getScore();
                    int frontierScore = 0;
                    PuzzleBoard next = null;
                    if (frontier.peek() == child) {
                        next = frontier.poll();
                        frontierScore = next.getScore();
                    } else {
                        // It's not the first thing in the queue so you have
                        // to iterate through the whole thing
                        Iterator it = frontier.iterator();
                        next = frontier.poll();
                        while (next != child) {
                            // Don't need to check for it.hasNext() because
                            // you already know it's in the queue, so it'll
                            // find it eventually
                            Object n = it.next();
                            next = (PuzzleBoard) n;
                        }
                        frontierScore = next.getScore();
                    }
                    if (childScore < frontierScore) {
                        frontier.add(child);
                    } else {
                        frontier.add(next);
                    }
                }
            }
            pathCost++;
        }
    }

    /* Private helper method for solve(). Implements A* and tracks the depth
    of the search.

    @param board the board to solve
    @param numTries the current depth of your search
    @param fLimit the f-cost limit
    @param hFunc the heuristic function to use

    @return the number of states it took to find a solution
    */
    // private static int aStar(PuzzleBoard board, int numTries, 
    //                          int fLimit, int hFunc) {

    //     // Base case
    //     if (board.isSolved()) {
    //         return numTries;
    //     }

    //     Queue<PuzzleBoard> successors = new PriorityQueue<PuzzleBoard>();
    //     List<Tile> possibleMoves = board.neighborhood(board.getBlankRow(), 
    //                                                   board.getBlankCol());
    //     for (Tile t : possibleMoves) {
    //         // Make the move
    //         board.move(t.getRow(), t.getCol());
    //         // System.out.println("Move made:");
    //         // System.out.println(board);
    //         successors.add(board.copy());
    //         // Undo the move
    //         board.move(t.getRow(), t.getCol());
    //         // System.out.println("Move undone:");
    //         // System.out.println(board);
    //     }
    //     if (successors.isEmpty()) {
    //         throw new RuntimeException("No successors: Board is unsolvable.");
    //     }
    //     for (PuzzleBoard b : successors) {
    //         // f(n) = g(n) + h(n) => score = number of steps already taken
    //         // plus heuristic function
    //         if (hFunc == 2) {
    //             b.setScore(b.h2() + numTries);
    //         } else {    // Because we initialize h1 as 1 in main(), the else
    //                     // case is safe
    //             b.setScore(b.h1() + numTries);
    //         }
    //     }
    //     while (true) {
    //         PuzzleBoard best = successors.poll();
    //         if (best.getScore() > fLimit) {
    //             throw new RuntimeException("Failed to solve the board. Fatal error.");
    //         }
    //         PuzzleBoard alternative = successors.poll();
    //         // Recurse
    //         best.setScore(aStar(best, numTries + 1, 
    //             Math.min(fLimit, alternative.getScore()), hFunc));
    //     }
    // }
}
