/* Client class to implement A* and solve the 8-tile puzzle

Alden Hart and Spencer Chadinha
1/21/2015
*/

public class PuzzleSolver {

    public static void main(String[] args) {
        PuzzleBoard x = new PuzzleBoard(3);
        System.out.println(x);

        // PuzzleBoard x = new PuzzleBoard(3, 7, 2, 4,
        //                                 5, 0, 6,
        //                                 8, 3, 1);
        // System.out.println(x);

        // System.out.println(x.h1());
        // System.out.println(x.h2());
        // System.out.println(x.moveExists(6));

    }

    /* Implements A* search on a board. */
    public static void aStar(PuzzleBoard board) {
        /* Needs:
            - Heuristic function (check)
            - g(n) - cost to reach the node from the start 
                - This should just go up by 1 every time you make a move, right?
        */
    }

    /* This is basically the same as A*, just with adding g(n) to h(n).
    I'm doing this so I can get the algorithm written without screwing
    with A* code.
    */
    public static void uniformCostSearch(PuzzleBoard board) {

    }
}
