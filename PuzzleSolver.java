/* Client class to implement A* and solve the 8-tile puzzle

Alden Hart and Spencer Chadinha
1/21/2015
*/

import java.util.List;
import java.util.ArrayList;
// import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
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

        // PuzzleBoard x = new PuzzleBoard(3, 7, 2, 4,
        //                                 5, 0, 6,
        //                                 8, 3, 1);
        // PuzzleBoard y = new PuzzleBoard(3, 7, 2, 4,
        //                                 5, 0, 6,
        //                                 8, 3, 1);

        // PuzzleBoard x = new PuzzleBoard(3, 1, 2, 0,
        //                                 3, 4, 5,
        //                                 6, 7, 8);
        // PuzzleBoard x = new PuzzleBoard(3, 12);
        // System.out.println(x);

        // int hFunc = DEFAULT_HFUNC;
        // try {
        //     hFunc = Integer.parseInt(args[0]);
        //     if (hFunc != 1 && hFunc != 2) {
        //         System.out.println("Invalid choice of heuristic, defaulting");
        //         System.out.println("to h1: misplaced tiles heuristic.");
        //     }
        // } catch (ArrayIndexOutOfBoundsException e) {
        //     System.out.println("Invalid choice of heuristic, defaulting");
        //     System.out.println("to h1: misplaced tiles heuristic.");
        // }
        // int[] results = solve(x, hFunc);
        // int depth = results[0];
        // int expandedNodes = results[1];
        // System.out.println("Depth: " + depth + " b* " + expandedNodes);
        runTest();

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
                //System.out.println(node);
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
                expandedNodes++;
            }
        }
    }

    // public static void runTest() {

    //     int[] h1depths = new int[12];
    //     int[] h1branch = new int[12];
    //     int[] h2depths = new int[12];
    //     int[] h2branch = new int[12];

    //     int[] h1depthAvg = new int[12];
    //     int[] h1branchAvg = new int[12];
    //     int[] h2depthAvg = new int[12];
    //     int[] h2branchAvg = new int[12];

    //     System.out.println("d\th1 b*\th2 b*");

    //     for (int i = 1; i <= 12; i++) {
    //         int depth = 2*i;
    //         int h1d = 0;
    //         int h1b = 0;
    //         int h2d = 0;
    //         int h2b = 0;
    //         for (int j = 0; j < 100; j++) {
    //             PuzzleBoard b1 = new PuzzleBoard(3, depth);
    //             PuzzleBoard b2 = b1.copy();
    //             int[] r1 = solve(b1, 1);
    //             int[] r2 = solve(b2, 2);
    //             h1d += r1[0];
    //             h1b += r1[1];
    //             h2d += r2[0];
    //             h2b += r2[1];
    //             // h1depths[i] = r1[0];
    //             // h1branch[i] = r1[1];
    //             // h2depths[i] = r2[0];
    //             // h2branch[i] = r2[1];
    //         }
    //         // int h1d = 0;
    //         // int h1b = 0;
    //         // int h2d = 0;
    //         // int h2b = 0;
    //         // for (int j = 0; j < 100; j++) {
    //         //     h1d += h1depths.get(j);
    //         //     h1b += h1branch.get(j);
    //         //     h2d += h2depths.get(j);
    //         //     h2b += h2branch.get(j);
    //         // }
    //         h1depthAvg[i-1] = h1d/100;
    //         h1branchAvg[i-1] = h1b/100;
    //         h2depthAvg[i-1] = h2d/100;
    //         h2branchAvg[i-1] = h2b/100;

    //         System.out.print(depth + "\t");
    //         System.out.print(h1branchAvg[i-1] + "\t");
    //         System.out.println(h2branchAvg[i-1]);

    //         // System.out.println("Depth: " + depth);
    //         // System.out.println("Avg h1 depth: " + h1depthAvg[i-1]);
    //         // System.out.println("Avg h1 b*: " + h1branchAvg[i-1]);
    //         // System.out.println("Avg h2 depth: " + h2depthAvg[i-1]);
    //         // System.out.println("Avg h2 b*: " + h2branchAvg[i-1]);
    //     }
    // }

    /* Randomly generates 1200 boards and solves them, reporting the
    depth and number of nodes generated
    */
    public static void runTest() {

        int numTrials = 1200;

        // int[] h1Depths = new int[numTrials];
        // int[] h1NodesGenerated = new int[numTrials];
        // int[] h2Depths = new int[numTrials];
        // int[] h2NodesGenerated = new int[numTrials];

        Map<Integer, List<Integer>> h1DepthMap = new HashMap<Integer, List<Integer>>();
        Map<Integer, List<Integer>> h2DepthMap = new HashMap<Integer, List<Integer>>();

        System.out.println("Generating and solving boards:");

        for (int i = 0; i < numTrials; i++) {

            PuzzleBoard b1 = new PuzzleBoard(3);
            PuzzleBoard b2 = b1.copy();
            int[] r1 = solve(b1, 1);
            int[] r2 = solve(b2, 2);
            int h1Depth = r1[0];
            int h1Nodes = r1[1];
            int h2Depth = r2[0];
            int h2Nodes = r2[1];
            if (!h1DepthMap.containsKey(h1Depth)) {
                h1DepthMap.put(h1Depth, new ArrayList<Integer>());
            }
            h1DepthMap.get(h1Depth).add(h1Nodes);
            if (!h2DepthMap.containsKey(h2Depth)) {
                h2DepthMap.put(h2Depth, new ArrayList<Integer>());
            }
            h2DepthMap.get(h2Depth).add(h2Nodes);
            if (i % 100 == 0) {
                System.out.println("Finished " + i + " boards.");
            }
        }
        System.out.println("Done generating and solving boards, printing results:");
        System.out.println("\nd\th1 b*\th2 b*");
        // Iterate through the keys and print the average depths
        Set<Integer> h1KeySet = h1DepthMap.keySet();

        for (Integer i : h1KeySet) {
            int h1Avg = 0;
            int h2Avg = 0;
            List<Integer> h1Vals = h1DepthMap.get(i);
            for (Integer n : h1Vals) {
                h1Avg += n;
            }
            h1Avg = h1Avg / h1Vals.size();
            if (h2DepthMap.containsKey(i)) {
                List<Integer> h2Vals = h2DepthMap.get(i);
                for (Integer n : h2Vals) {
                    h2Avg += n;
                }
                h2Avg = h2Avg / h2Vals.size();
            }
            System.out.print(i + "\t" + h1Avg + "\t" + h2Avg + "\n");
        }
    }
}
