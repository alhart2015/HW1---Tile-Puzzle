/* Class to encapsulate the state and behavior of the puzzle board

Alden Hart and Spencer Chadinha
1/21/2015
*/

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;

public class PuzzleBoard implements Comparable<PuzzleBoard> {

    private Tile[][] board;
    private int size;
    private int blankRow;
    private int blankCol;
    private int score;

    // if parity + d(blank, goalBlank) is odd, board isn't solvable

    /* Initializes the state of the board by making an empty board to be filled
    later.

    @param squareSize the size of the board, typically 3x3
    */
    public PuzzleBoard(int squareSize) {
        this.board = new Tile[squareSize][squareSize];
        this.size = squareSize;
        this.blankRow = 0;  // Just to initialize them before actually finding
        this.blankCol = 0;  // the empty tile in init()
        this.score = Integer.MAX_VALUE;
        this.init();
    }

    /* Constructor for testing, so you can input the start state */
    public PuzzleBoard(int squareSize, int a, int b, int c,
                        int d, int e, int f,
                        int g, int h, int i) {

        this.board = new Tile[squareSize][squareSize];
        this.size = squareSize;
        this.blankRow = 0;
        this.blankCol = 0;
        this.score = Integer.MAX_VALUE;

        this.board[0][0] = new Tile(0, 0, a);
        this.board[0][1] = new Tile(0, 1, b);
        this.board[0][2] = new Tile(0, 2, c);
        this.board[1][0] = new Tile(1, 0, d);
        this.board[1][1] = new Tile(1, 1, e);
        this.board[1][2] = new Tile(1, 2, f);
        this.board[2][0] = new Tile(2, 0, g);
        this.board[2][1] = new Tile(2, 1, h);
        this.board[2][2] = new Tile(2, 2, i);

        for (int r = 0; r < this.size; r++) {
            for (int col = 0; col < this.size; col++) {
                if (this.board[r][col].getVal() == 0) {
                    this.blankRow = r;
                    this.blankCol = col;
                }
            }
        }
    }

    /* Creates a random solvable board using the parity sign check.
    */
    private void init() {
        boolean valid = false;
        while (!valid) {
            this.makeRandomBoard();
            valid = this.isValid();
        }

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (this.board[i][j].getVal() == 0) {
                    this.blankRow = i;
                    this.blankCol = j;
                }
            }
        }
    }

    /* Checks if the current board is valid.
    */
    private boolean isValid() {
        int parity = this.parity();
        int dist = this.zeroDistance();
        return parity % 2 == dist % 2;
    }

    /* Makes a random board, may not be solvable.
    */
    private void makeRandomBoard() {
        Random rand = new Random();
        Set<Integer> used = new HashSet<Integer>();
        int r = 0;
        int c = 0;
        int numAdded = 0;
        while (numAdded < this.size * this.size) {
            int next = rand.nextInt(this.size * this.size);
            if (!used.contains(next)) {
                this.board[r][c] = new Tile(r, c, next);
                if (c == 2) {
                    r++;
                    c = 0;
                } else {
                    c++;
                }
                used.add(next);
                numAdded++;
            }
        }
    }

    /* Calculates the parity of the board, ie the number of swaps needed
    to get from the starting board to the solved board.
    */
    private int parity() {

        // Make lists instead of matrices for ease of computing parity
        int len = this.size * this.size;
        int[] state = new int[len];

        int numSwaps = 0;
        int numCorrect = 0;

        for (int i = 0; i < len; i++) {
            state[i] = this.board[i/this.size][i%this.size].getVal();
            if (i == state[i]) {
                numCorrect++;
            }
        }

        while (numCorrect < len) {
            numCorrect = 0;
            for (int i = 0; i < len; i++) {
                if (i != state[i]) {
                    state = swap(state, i, state[i]);
                    numSwaps++;
                }
            }
            for (int j = 0; j < len; j++) {
                if (state[j] == j) {
                    numCorrect++;
                }
            }
        }

        return numSwaps;
    }

    /* Calculates the manhattan distance between 0 and its position in the
    goal state.
    */
    private int zeroDistance() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (this.board[i][j].getVal() == 0) {
                    return i + j;
                }
            }
        }
        // This should never be executed
        return -1;
    }

    /* Swap the element of array a in position x with the element in
    position y.
    */
    private static int[] swap(int[] a, int x, int y) {
        int temp = a[x];
        a[x] = a[y];
        a[y] = temp;
        return a;
    }

    /* Moves the tile in position (r, c) into the blank space.

    @param r the row of the tile to move into the empty space
    @param c the column of the tile to move into the empty space
    */
    public void move(int r, int c) {
        // Update the tiles
        // int rowDiff = this.blankRow - r;
        // int colDiff = this.blankCol - c;
        this.board[r][c].setRow(this.blankRow);
        this.board[r][c].setCol(this.blankCol);
        this.board[blankRow][blankRow].setRow(r);
        this.board[blankRow][blankCol].setCol(c);

        // Make the move
        Tile temp = this.board[r][c];
        this.board[r][c] = this.board[blankRow][blankCol];
        this.board[blankRow][blankCol] = temp;
        this.blankRow = r;
        this.blankCol = c;
    }

    /* Helper function for moveExists, returns the numbers to the left, right,
    above, and below the given (row, column), if they exist.
    */
    public List<Tile> neighborhood(int r, int c) {
        List<Tile> nbd = new ArrayList<Tile>();
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

    /* Solves the board using A* search.

    @return the number of steps needed to find the solution
    */
    // public int solve() {
        /* Needs:
            - Heuristic function (check)
            - g(n) - cost to reach the node from the start 
                - This should just go up by 1 every time you make a move, right?
        */
    //    return this.aStar(0, Integer.MAX_VALUE);
    // }

    /* Private helper method for solve(). Implements A* and tracks the depth
    of the search.

    @param numTries the current depth of your search
    @param fLimit the f-cost limit

    @return the number of states it took to find a solution
    */
    // private int aStar(int numTries, int fLimit) {

    //     // Base case
    //     if (this.isSolved()) {
    //         return numTries;
    //     }

    //     List<Tile> successors = new ArrayList<Tile>();
    //     List<Tile> possibleMoves = this.neighborhood(this.blankRow, this.blankCol);
    //     for (Tile t : possibleMoves) {
    //         // Make the move
    //         this.move(t.getRow(), t.getCol());
    //         // Add the board to successors...
    //         // This should be in PuzzleSolver, because successors needs to be
    //         // a List<PuzzleBoard> not a List<Tile>
    //     }
    // }

    /* Helper for A*. Checks if the current board is a solved board.

    @return true if the board is in its goal state, false otherwise
    */
    public boolean isSolved() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                int val = this.board[i][j].getVal();
                if (val != i*this.size + j) {
                    return false;
                }
            }
        }
        return true;
    }

    /* @Override */
    public int compareTo(PuzzleBoard b) {
        return this.score - b.getScore();
    }

    /* Public getter for the board */
    public Tile[][] getBoard() {
        return this.board;
    }

    /* Public getter for the size */
    public int getSize() {
        return this.size;
    }

    /* Public getter for the row of the empty space */
    public int getBlankRow() {
        return this.blankRow;
    }

    /* Public getter for the column of the empty space */
    public int getBlankCol() {
        return this.blankCol;
    }

    /* Public getter for the score of the board */
    public int getScore() {
        return this.score;
    }

    /* Public setter for the score of the board */
    public void setScore(int s) {
        this.score = s;
    }

}
