/* Class representing a tile in the puzzle. Keeps track of its position and
value.

Alden Hart and Spencer Chadinha
*/

public class Tile {

    private static final int EMPTY_SPACE = 0;

    private int r;
    private int c;
    private int val;

    /* Creates a tile with the given (row, column) position and value
    */
    public Tile(int row, int column, int value) {
        this.r = row;
        this.c = column;
        this.val = value;
    }

    /* @Override */
    public String toString() {
        return Integer.toString(this.val);
    }

    public boolean isEmptySpace() {
        return this.val == EMPTY_SPACE;
    }

    // Getters
    public int getRow() {
        return this.r;
    }

    public int getCol() {
        return this.c;
    }

    public int getVal() {
        return this.val;
    }

    // Setters
    public void setRow(int move) {
        this.r = move;
    }

    public void setCol(int move) {
        this.c = move;
    }

}
