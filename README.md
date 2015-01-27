# HW1 - Tile Puzzle

Solves an arbitrarily-sized N-Tile puzzle (though intended to solve
the 8-Tile puzzle). To compile and run from a UNIX shell, run

javac *.java; java PuzzleSolver x

where x is the number of the heuristic function you want to use. The heuristics
are defined as follows:

    1 chooses the misplaced tiles heuristic, which counts the number of tiles
    that are not in their proper position.

    2 chooses the Manhattan distance heuristic, which sums the Manhattan distance
    of each tile on the board from its position in a solved board
