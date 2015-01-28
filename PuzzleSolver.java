/* Client class to implement A* and solve the 8-tile puzzle

Alden Hart and Spencer Chadinha
1/21/2015
*/

import java.util.List;
import java.util.Iterator;
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
        // PuzzleBoard y = new PuzzleBoard(3, 7, 2, 4,
        //                                 5, 0, 6,
        //                                 8, 3, 1);

        // PuzzleBoard x = new PuzzleBoard(3, 1, 2, 0,
        //                                 3, 4, 5,
        //                                 6, 7, 8);
        System.out.println(x);

        int hFunc = DEFAULT_HFUNC;
        try {
            hFunc = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid choice of heuristic, defaulting");
            System.out.println("to h1: misplaced tiles heuristic.");
        }
        int[] results = solve(x, hFunc);
        int depth = results[0];
        int expandedNodes = results[1];
        System.out.println("Depth: " + depth + " b* " + expandedNodes);

    }

    /* Solves the board using A* search.

    @param board the board you're solving
    @param hFunc the heuristic function to use

    @return an array: array[0] = the depth of the solution, 
    array[1] = the effective branching factor needed to find the solution
    */
    public static int[] solve(PuzzleBoard board, int hFunc) {

        return aStar(board, hFunc);
    }

    /* Private helper method for solve(). Implements A* and tracks the depth
    of the search.

    @param board the board to solve
    @param numTries the current depth of your search
    @param fLimit the f-cost limit
    @param hFunc the heuristic function to use

    @return an array: array[0] = the depth of the solution, 
    array[1] = the effective branching factor needed to find the solution
    */
    private static int[] aStar(PuzzleBoard board, int hFunc) {

        int expandedNodes = 0;
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
                int[] out = new int[2];
                out[0] = node.getDepth();
                out[1] = expandedNodes;
                return out;
            }
            explored.add(node);
            List<Tile> possibleMoves = node.neighborhood(node.getBlankRow(),
                                                         node.getBlankCol());

            for (Tile t : possibleMoves) {
                PuzzleBoard child = node.copy();
                child.setDepth(node.getDepth() + 1);
                child.move(t.getRow(), t.getCol());

                if (hFunc == 2) {
                    child.setScore(child.h2() + child.getDepth());
                } else {
                    child.setScore(child.h1() + child.getDepth());
                }

                if (!explored.contains(child) && !frontier.contains(child)) {
                    frontier.add(child);
                } else if (frontier.contains(child)) {
                    // If it's in frontier, check if the board in frontier has
                    // a higher cost than this one.
                    int childScore = child.getScore();
                    int frontierScore = 0;
                    PuzzleBoard next = null;
                    if (frontier.peek() == child) {
                        next = frontier.poll();
                        frontierScore = next.getScore();
                    } else {
                        int i = 1;
                        Object[] frontierArray = frontier.toArray();
                        next = (PuzzleBoard) frontierArray[0];
                        while (!next.equals(child)) {
                            next = (PuzzleBoard) frontierArray[i];
                            i++;
                        }
                        frontierScore = next.getScore();
                    }
                    if (childScore < frontierScore) {
                        frontier.remove(next);
                        frontier.add(child);
                    }
                }
            }
            expandedNodes++;
        }
    }
}
