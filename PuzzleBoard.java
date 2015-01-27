/* Class to encapsulate the state and behavior of the puzzle board

Alden Hart and Spencer Chadinha
1/21/2015
*/

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;
import java.util.Iterator;
import java.util.Collections;

public class PuzzleBoard {

    private static final int EMPTY_SPACE = 0;
    private static final int LAST_MOVED = 1;

    private Tile[][] board;
    private int size;
    // private int blankRow; // Unconvinced I want/need these
    // private int blankCol;

    // if parity + d(blank, goalBlank) is odd, board isn't solvable

    /* Initializes the state of the board by making an empty board to be filled
    later.

    @param squareSize the size of the board, typically 3x3
    */
    public PuzzleBoard(int squareSize) {
        this.board = new Tile[squareSize][squareSize];
        this.size = squareSize;
        // this.blankRow = 0;
        // this.blankCol = 0;
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

    /* Creates a random solvable board using the algorithm described at
    http://cseweb.ucsd.edu/~ccalabro/essays/15_puzzle.pdf
    */
    private void init() {
        boolean valid = false;
        while (!valid) {
            this.makeRandomBoard();
            valid = this.isValid();
        }
        System.out.println(this.parity());
        System.out.println(this.zeroDistance());
    }

    /* Checks if the current board is valid */
    private boolean isValid() {
        int parity = this.parity();
        int dist = this.zeroDistance();
        return parity % 2 == dist % 2;
    }

    /* Makes a random board
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
    to get from the starting board to the solved board
    */
    private int parity() {

        // Make lists instead of matrices for ease of computing parity
        int len = this.size * this.size;

        // List of goal state: [0, 1, ..., n^2]
        int[] goal = new int[len];
        int[] state = new int[len];

        int numSwaps = 0;
        int numCorrect = 0;

        for (int i = 0; i < len; i++) {
            goal[i] = i;
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
    position y
    */
    private static int[] swap(int[] a, int x, int y) {
        int temp = a[x];
        a[x] = a[y];
        a[y] = temp;
        return a;
    }

    /* Moves the tile in position (r, c) into the blank space */
    public void move() {

    }

    // /* Creates a random state of the board. Creates a solved board and then
    // makes a random number of random moves, shuffling the board.
    // */
    // private void init() {

    //     Random rand = new Random();

    //     for (int i = 0; i < this.size; i++) {
    //         for (int j = 0; j < this.size; j++) {
    //             // Start with a solved board
    //             // this.board[i][j] = this.size*i + j;
    //             this.board[i][j] = new Tile(i, j, this.size*i + j);
    //         }
    //     }

    //     // how many random moves to make? Tweak this to get a good
    //     // distribution of random boards
    //     int randRange = 100;
    //     int numMoves = rand.nextInt(randRange);
    //     // lastMoved[0] = position of the empty space
    //     // lastMoved[1] = position of the last Tile moved
    //     Tile[] lastMoved = new Tile[2];
    //     lastMoved[EMPTY_SPACE] = this.board[0][0];
    //     // Pick any tile that can't be moved the first time for this
    //     lastMoved[LAST_MOVED] = this.board[2][2];
    //     for (int i = 0; i < numMoves; i++) {
    //         lastMoved = randomMove(lastMoved);
    //     }

    // }

    // /* Helper for init(), makes a random move on the board. As above,
    // lastMoved[0] = the position of the empty space, and
    // lastMoved[1] = the position of the last Tile moved. We track these
    // so that your move cannot undo your previous move, which prevents
    // the space of likely boards from being too small.
    // */
    // private Tile[] randomMove(Tile[] lastMoved) {
    //     /* Notes:
    //         - remember where you just moved so you can't do that
    //         - keep track of where the 0 is so you don't have to loop
    //             through the whole board every time
    //     */

    //     Random rand = new Random();

    //     Tile empty = lastMoved[EMPTY_SPACE];

    //     List<Tile> nbd = this.neighborhood(empty.getRow(), empty.getCol());
    //     Collections.shuffle(nbd);
    //     int index = 0;
    //     Tile toMove = nbd.get(index);   // Check the first thing
    //     if (toMove == lastMoved[LAST_MOVED]) {
    //         // The first thing in the shuffled neighborhood was the last tile
    //         // you moved. Can't use it, so use the next one.
    //         index++;
    //         toMove = nbd.get(index);
    //         // Note that the neighborhood will always have at least two
    //         // elements, so this will never throw an exception
    //     }

    //     // System.out.println(toMove);
    //     System.out.println("toMove: " + toMove + 
    //         toMove.getRow() + toMove.getCol());
    //     System.out.println("blank: " + empty + 
    //         empty.getRow() + empty.getCol());
    //     System.out.println("lastMoved: " + lastMoved[LAST_MOVED] + 
    //         lastMoved[LAST_MOVED].getRow() + lastMoved[LAST_MOVED].getCol());

    //     lastMoved = this.move(toMove.getRow(), toMove.getCol(), 
    //                empty.getRow(), empty.getCol(), lastMoved);

    //     return lastMoved;
    // }

    // /* Moves the tile in position (r, c) into the empty space */
    // public Tile[] move(int r, int c, int blankR, int blankC, Tile[] moved) {
    //     // if (this.moveExists(this.board[r][c].getVal())) {
    //     //     Tile temp = this.board[r][c];
    //     // }

    //     // Not sure you need to check if the move exists, since you're
    //     // only calling the move() method on tiles in the neightborhood
    //     // of the empty space
    //     Tile temp = this.board[r][c];
    //     this.board[r][c] = this.board[blankR][blankC];
    //     moved[EMPTY_SPACE] = this.board[r][c];
    //     this.board[blankR][blankC] = this.board[r][c];
    //     moved[LAST_MOVED] = this.board[blankR][blankC];
    //     return moved;
    // }

    // /* Checks if a move exists from the given number */
    // public boolean moveExists(int num) {

    //     for (int i = 0; i < this.size; i++) {
    //         for (int j = 0; j < this.size; j++) {
    //             Tile current = this.board[i][j];
    //             if (num == current.getVal()) {
    //                 // You've found the number you're worried about, so check
    //                 // if the empty space is next to it. Unfortunately you
    //                 // have to iterate through it, so we lose the efficiency
    //                 // of checking set containment. There are a max of 4
    //                 // elements here though, so it shouldn't be that bad
    //                 List<Tile> nbd = this.neighborhood(i, j);
    //                 for (Tile t : nbd) {
    //                     if (t.isEmptySpace()) {
    //                         return true;
    //                     }
    //                 }
    //             }
    //         }
    //     }

    //     return false;
    // }

    /* Helper function for moveExists, returns the numbers to the left, right,
    above, and below the given (row, column), if they exist.
    */
    private List<Tile> neighborhood(int r, int c) {
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

    /* Public getter for the board */
    public Tile[][] getBoard() {
        return this.board;
    }

    /* Public getter for the size */
    public int getSize() {
        return this.size;
    }

}
