/* Class to create the graph of possible tile states 

Alden Hart and Spencer Chadinha
1/21/2015
*/

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class PuzzleGraph {

    private Map<PuzzleBoard, ArrayList<PuzzleBoard>> graph;

    /* Implements the graph as an adjacency list*/
    public PuzzleGraph() {
        this.graph = new HashMap<PuzzleBoard, ArrayList<PuzzleBoard>>();
    }

    /* Gonna have to wait to do this one until we figure out how to do the
    random generation...
    */
    public void fill() {

    }
}
