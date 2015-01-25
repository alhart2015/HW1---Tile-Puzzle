/* Class to encapsulate the state and behavior of the puzzle board

Alden Hart and Spencer Chadinha
1/21/2015
*/

import java.util.Set;
import java.util.HashSet;
import java.util.Random;

public class PuzzleBoard {

    private static final int EMPTY_SPACE = 0;

    private int[][] board;
    private int size;

    /* Initializes the state of the board by making an empty board to be filled
    later.

    @param squareSize the size of the board, typically 3x3
    */
    public PuzzleBoard(int squareSize) {
        this.board = new int[squareSize][squareSize];
        this.size = squareSize;
        this.init();
    }

    /* Constructor for testing, so you can input the start state */
    public PuzzleBoard(int squareSize, int a, int b, int c,
                        int d, int e, int f,
                        int g, int h, int i) {
        this.board = new int[squareSize][squareSize];
        this.size = squareSize;
        this.board[0][0] = a;
        this.board[0][1] = b;
        this.board[0][2] = c;
        this.board[1][0] = d;
        this.board[1][1] = e;
        this.board[1][2] = f;
        this.board[2][0] = g;
        this.board[2][1] = h;
        this.board[2][2] = i;
    }

    /* Creates a random state of the board. Creates a solved board and then
    makes a random number of random moves, shuffling the board.
    */
    private void init() {

        Random rand = new Random();

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                // Start with a solvec board
                this.board[i][j] = this.size*i + j;
            }
        }

        // how many random moves to make? Tweak this to get a good
        // distribution of random boards
        int randRange = 100;
        int numMoves = rand.nextInt(randRange);
        int[][] lastMoves = new int[2][2];
        // lastMoves[0] = position of the empty space
        // ie. lastMoves[0][0] = row of the empty space, etc
        // lastMoves[1] = position of the tile just moved

        // Initialize lastMoves to be all 0's
        for (int i = 0; i < 2; i++) {
            lastMoves[i][i] = 0;
        }

        for (int i = 0; i < numMoves; i++) {
            lastMoves = this.randomMove(lastMoves);
        }
    }

    /* Helper for init(), makes a random move on the board
    */
    private int[][] randomMove(int[][] lastMoves) {
        /* Notes:
            - remember where you just moved so you can't do that
            - keep track of where the 0 is so you don't have to loop
                through the whole board every time
        */

        Random rand = new Random();

        int blankR = lastMoves[0][0];
        int blankC = lastMoves[0][1];
        Set<Integer> nbd = neighborhood(blankR, blankC);

        // Need to add one because you don't want to access 0 elements of
        // the set and you want to be able to reach the last element
        int choice = rand.nextInt(nbd.size() + 1);
        Iterator it = nbd.iterator();
        int next;
        for (int i = 0; i < choice; i++) {
            next = it.next();
        }

        // Need a move() method here...

        return new int[2][2];
    }

    /* Checks if a move exists from the given number */
    public boolean moveExists(int num) {

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                int current = this.board[i][j];
                if (num == current) {
                    // You've found the number you're worried about, so check
                    // if the empty space is next to it
                    if (this.neighborhood(i, j).contains(EMPTY_SPACE)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /* Helper function for moveExists, returns the numbers to the left, right,
    above, and below the given (row, column), if they exist.
    */
    private Set<Integer> neighborhood(int r, int c) {
        Set<Integer> nbd = new HashSet<Integer>();
        if (r > 0) {
            nbd.add(this.board[r-1][c]);
        }
        if (r < this.size-1) {
            nbd.add(this.board[r+1][c]);
        }
        if (c > 0) {
            nbd.add(this.board[r][c-1]);
        }
        if (c < this.size-1) {
            nbd.add(this.board[r][c+1]);
        }
        return nbd;
    }

    /* @Override */
    public String toString() {
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                out.append(this.board[i][j]);
                if (j < this.size-1) {
                    out.append("|");
                }
            }
            if (i < this.size-1) {
                out.append("\n");
                for (int k = 0; k < this.size * 2 - 1; k++) {
                       out.append("-");
                }
                out.append("\n");
            }
        }
        out.append("\n");
        return out.toString();
    }

    /* Heuristic function following the h1 described in the text on page 103.
    Counts the number of misplaced tiles on the board.
    */
    public int h1() {

        int wrongTiles = 0;

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (this.board[i][j] != this.size*i + j) {
                    if (this.board[i][j] != EMPTY_SPACE) {
                        wrongTiles++;   
                    }
                }
            }
        }

        return wrongTiles;
    }

    /* Heuristic function following the h2 described in the text on page 103.
    Computes the Manhattan between each tile and its position in the goal
    state.
    */
    public int h2() {

        int totalDistance = 0;

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (this.board[i][j] != EMPTY_SPACE) {
                    int number = this.board[i][j];
                    int currentRow = i;
                    int currentCol = j;
                    int goalRow = number / this.size;
                    int goalCol = number % this.size;
                    totalDistance += Math.abs(goalRow - currentRow);
                    totalDistance += Math.abs(goalCol - currentCol);
                }
            }
        }

        return totalDistance;
    }

    /* Public getter for the board */
    public int[][] getBoard() {
        return this.board;
    }

    /* Public getter for the size */
    public int getSize() {
        return this.size;
    }

}