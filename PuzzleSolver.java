/* Client class to implement A* and solve the 8-tile puzzle

Alden Hart and Spencer Chadinha
1/21/2015
*/

import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.PriorityQueue;
import java.lang.RuntimeException;

public class PuzzleSolver {

    public static void main(String[] args) {
        // PuzzleBoard x = new PuzzleBoard(3);
        // System.out.println(x);

        PuzzleBoard x = new PuzzleBoard(3, 7, 2, 4,
                                        5, 0, 6,
                                        8, 3, 1);
        System.out.println(x);
        x.move(1, 0);
        System.out.println(x);
        x.move(0, 0);
        System.out.println(x);
        System.out.println(args[0]);
        System.out.println(x.getBoard()[1][0]);
        System.out.println(x.getBoard()[1][0].getRow());
        System.out.println(x.getBoard()[1][0].getCol());
        x.move(1, 0);
        System.out.println(x);
        System.out.println(x.getBoard()[1][0]);
        System.out.println(x.getBoard()[1][0].getRow());
        System.out.println(x.getBoard()[1][0].getCol());
        System.out.println(x.getBlankRow());
        System.out.println(x.getBlankCol());
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
        return aStar(board, 0, Integer.MAX_VALUE, hFunc);
    }

    /* Private helper method for solve(). Implements A* and tracks the depth
    of the search.

    @param board the board to solve
    @param numTries the current depth of your search
    @param fLimit the f-cost limit
    @param hFunc the heuristic function to use

    @return the number of states it took to find a solution
    */
    private static int aStar(PuzzleBoard board, int numTries, 
                             int fLimit, int hFunc) {

        // Base case
        if (board.isSolved()) {
            return numTries;
        }

        Queue<PuzzleBoard> successors = new PriorityQueue<PuzzleBoard>();
        List<Tile> possibleMoves = board.neighborhood(board.getBlankRow(), 
                                                      board.getBlankCol());
        for (Tile t : possibleMoves) {
            // Make the move
            board.move(t.getRow(), t.getCol());
            successors.add(board);
            // Undo the move
            board.move(t.getRow(), t.getCol());
        }
        if (successors.isEmpty()) {
            throw new RuntimeException("No successors: Board is unsolvable.");
        }
        for (PuzzleBoard b : successors) {
            // f(n) = g(n) + h(n) => score = number of steps already taken
            // plus heuristic function
            if (hFunc == 1) {
                b.setScore(b.h1() + numTries);
            } else if (hFunc == 2) {
                b.setScore(b.h2() + numTries);
            } else {
                throw new RuntimeException("Invalid heuristic function chosen.");
            }
        }

        return 0;
    }

    // Moving these guys to the puzzleBoard class

    // /* Implements A* search on a board. */
    // public static void aStar(PuzzleBoard board) {
    //     /* Needs:
    //         - Heuristic function (check)
    //         - g(n) - cost to reach the node from the start 
    //             - This should just go up by 1 every time you make a move, right?
    //     */
    // }

    //  This is basically the same as A*, just with adding g(n) to h(n).
    // I'm doing this so I can get the algorithm written without screwing
    // with A* code.
    
    // public static void uniformCostSearch(PuzzleBoard board) {

    // }

    // /* Helper for uniform cost search.

    // @param board the board to be messed with*/
    // private static void UCS(PuzzleBoard board, int r, int c, int fLimit) {

    // }
}
