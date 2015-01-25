/* Class to encapsulate the state and behavior of the puzzle board

Alden Hart and Spencer Chadinha
1/21/2015
*/

import java.util.Set;
import java.util.HashSet;
import java.util.Random;

public class PuzzleBoard {

    // private static final int EMPTY_SPACE = 0;

    private Tile[][] board;
    private int size;

    /* Initializes the state of the board by making an empty board to be filled
    later.

    @param squareSize the size of the board, typically 3x3
    */
    public PuzzleBoard(int squareSize) {
        this.board = new Tile[squareSize][squareSize];
        this.size = squareSize;
        this.init();
    }

    /* Constructor for testing, so you can input the start state */
    public PuzzleBoard(int squareSize, int a, int b, int c,
                        int d, int e, int f,
                        int g, int h, int i) {
        this.board = new Tile[squareSize][squareSize];
        this.size = squareSize;
        this.board[0][0] = new Tile(0, 0, a);
        this.board[0][1] = new Tile(0, 1, b);
        this.board[0][2] = new Tile(0, 2, c);
        this.board[1][0] = new Tile(1, 0, d);
        this.board[1][1] = new Tile(1, 1, e);
        this.board[1][2] = new Tile(1, 2, f);
        this.board[2][0] = new Tile(2, 0, g);
        this.board[2][1] = new Tile(2, 1, h);
        this.board[2][2] = new Tile(2, 2, i);
    }

    /* Creates a random state of the board. Creates a solved board and then
    makes a random number of random moves, shuffling the board.
    */
    private void init() {

        Random rand = new Random();

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                // Start with a solved board
                // this.board[i][j] = this.size*i + j;
                this.board[i][j] = new Tile(i, j, this.size*i + j);
            }
        }

        // how many random moves to make? Tweak this to get a good
        // distribution of random boards
        int randRange = 100;
        int numMoves = rand.nextInt(randRange);

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


        // Need a move() method here...

        return new int[2][2];
    }

    /* Checks if a move exists from the given number */
    public boolean moveExists(int num) {

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                Tile current = this.board[i][j];
                if (num == current.getVal()) {
                    // You've found the number you're worried about, so check
                    // if the empty space is next to it. Unfortunately you
                    // have to iterate through it, so we lose the efficiency
                    // of checking set containment. There are a max of 4
                    // elements here though, so it shouldn't be that bad
                    Set<Tile> nbd = this.neighborhood(i, j);
                    for (Tile t : nbd) {
                        if (t.isEmptySpace()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /* Helper function for moveExists, returns the numbers to the left, right,
    above, and below the given (row, column), if they exist.
    */
    private Set<Tile> neighborhood(int r, int c) {
        Set<Tile> nbd = new HashSet<Tile>();
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
                if (this.board[i][j].getVal() != this.size*i + j) {
                    if (!this.board[i][j].isEmptySpace()) {
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
                if (!this.board[i][j].isEmptySpace()) {
                    int number = this.board[i][j].getVal();
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
    public Tile[][] getBoard() {
        return this.board;
    }

    /* Public getter for the size */
    public int getSize() {
        return this.size;
    }

}