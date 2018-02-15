package groupapp.cs.psu.slidingpuzzle.grid.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Board {

    /* Board Dimensions */
    private final int size = 5;

    /** Number of tile moves made so far. */
    private int numOfMoves;

    /** Places of this board. */
    private final List<Coordinates> coordinatesList;

    /** Listeners listening to board changes such as sliding of tiles. */
    private final List<BoardChangeListener> listeners;

    /** To arrange tiles randomly. */
    private final static Random random = new Random();

    /** Create a new board of the given dimension. Initially, the tiles
     * are ordered with the blank tile as the last tile. */
    public Board() {
        listeners = new ArrayList<BoardChangeListener>();
        coordinatesList = new ArrayList<Coordinates>((size) * (size));

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                coordinatesList.add(x == 1 && y == 4 ?
                        new Coordinates(x, y, this)
                        : new Coordinates(x, y,  y + (size * x), this));
            }
        }

       /* for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                coordinatesList.add(new Coordinates(x, y,  y + (size * x), this));
            }
        }

        for (int x = 2; x < size; x++) {
            for (int y = 2; y < size; y++) {
                coordinatesList.add(new Coordinates(x, y,  '+', this));
            }
        }*/

        numOfMoves = 0;
    }

    /** Rearrange the tiles to create a new, solvable puzzle. */
    public void rearrange() {
        numOfMoves = 0;
        //replace();
        for (int i = 0; i < size*size; i++) {
            swapBlocks();
        }
        do {
            swapBlocks();
        } while (!solvable() || solved());
    }

    /** Swap two tiles randomly. */
    private void swapBlocks() {
        Coordinates p1 = at(random.nextInt(size), random.nextInt(size));
        Coordinates p2 = at(random.nextInt(size), random.nextInt(size));
        if (p1 != p2) {
            Block t = p1.getBlock();
            p1.setBlock(p2.getBlock());
            p2.setBlock(t);
        }
    }

    private void replace() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Coordinates p1 = at(x,y);
                if (p1.getBlock().number() > 9){
                    Block t = new Block('+');
                    p1.setBlock(t);
                }
            }
        }
    }

    /** Is the puzzle (current arrangement of tiles) solvable? */
    private boolean solvable() {
        // alg. from: http://www.cs.bham.ac.uk/~mdr/teaching/modules04/
        //                 java2/TilesSolvability.html
        //
        // count the number of inversions, where an inversion is when
        // a tile precedes another tile with a lower number on it.
       /* int inversion = 0;
        for (Coordinates p: coordinatesList) {
            Block pt = p.getBlock();
            for (Coordinates q: coordinatesList) {
                Block qt = q.getBlock();
                if (p != q && pt != null && qt != null &&
                        indexOf(p) < indexOf(q) &&
                        pt.number() > qt.number()) {
                    inversion++;
                }
            }
        }
        final boolean isEvenSize = size % 2 == 0;
        final boolean isEvenInversion = inversion % 2 == 0;
        boolean isBlankOnOddRow = blank().getY() % 2 == 1;
        // from the bottom
        isBlankOnOddRow = isEvenSize ? !isBlankOnOddRow : isBlankOnOddRow;
        return (!isEvenSize && isEvenInversion) ||
                (isEvenSize && isBlankOnOddRow == isEvenInversion);*/
       return true;
    }

    /** Return the 1-based index of the given place when all the places
     * are arranged in row-major order. */
    private int indexOf(Coordinates p) {
        return (p.getY() - 1) * size + p.getX();

    }

    /** Is this puzzle solved? */
    public boolean solved() {
        boolean result = true;
        for (Coordinates p: coordinatesList) {
            result = result &&
                    ((p.getX() == size && p.getY() == size) ||
                            (p.getBlock() != null &&
                                    p.getBlock().number() == indexOf(p)));
        }
        return result;
    }

    /** Slide the given tile, which is assumed to be slidable, and
     * notify the change to registered board change listeners, if any.
     *
     */
    public void slide(Block block) {
        for (Coordinates p: coordinatesList) {
            if (p.getBlock() == block) {
                final Coordinates to = blank();
                to.setBlock(block);
                p.setBlock(null);
                numOfMoves++;
                notifyTileSliding(p, to, numOfMoves);
                if (solved()) {
                    notifyPuzzleSolved(numOfMoves);
                }
                return;
            }
        }
    }

    /** Is the tile in the given place slidable? */
    public boolean slidable(Coordinates coords) {
        int x = coords.getX();
        int y = coords.getY();
        return isBlank(x - 1, y) || isBlank(x + 1, y)
                || isBlank(x, y - 1) || isBlank(x, y + 1);
    }

    /** Is the place at the given indices empty? */
    private boolean isBlank(int x, int y) {
        return (0 <= x && x < size) && (0 <= y && y < size)
                && at(x,y).getBlock() == null;
    }

    /** Return the blank place. */
    public Coordinates blank() {
        for (Coordinates p: coordinatesList) {
            if (p.getBlock() == null) {
                return p;
            }
        }
        //assert false : "should never reach here!";
        return null;
    }

    /** Return all the places of this board. */
    public Iterable<Coordinates> coordinates() {
        return coordinatesList;
    }

    /** Return the place at the given indices. */
    public Coordinates at(int x, int y) {
        for (Coordinates p: coordinatesList) {
            if (p.getX() == x && p.getY() == y) {
                return p;
            }
        }
        //assert false : "precondition violation!";
        return null;
    }

    /** Return the dimension of this board. */
    public int size() {
        return size;
    }

    /** Return the number of tile moves made so far. */
    public int numOfMoves() {
        return numOfMoves;
    }

    /** Register the given listener to listen to board changes. */
    public void addBoardChangeListener(BoardChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /** Unregister the given listener from listening to board changes. */
    public void removeBoardChangeListener(BoardChangeListener listener) {
        listeners.remove(listener);
    }

    /** Notify a tile sliding to registered board change listeners. */
    private void notifyTileSliding(Coordinates from, Coordinates to, int numOfMove) {
        for (BoardChangeListener listener: listeners) {
            listener.tileSlid(from, to, numOfMoves);
        }
    }

    /** Notify solving of the puzzle to registered board change listeners. */
    private void notifyPuzzleSolved(int numOfMoves) {
        for (BoardChangeListener listener: listeners) {
            listener.solved(numOfMoves);
        }
    }

    /** To listen to board changes such as tile sliding. */
    public interface BoardChangeListener {

        /** Called when the tile was slid to the empty place. Both places
         * will be provided in new states; */
        void tileSlid(Coordinates from, Coordinates to, int numOfMoves);

        /** Called when the puzzle is solved. The number of tile moves
         * is provided as the argument. */
        void solved(int numOfMoves);
    }

}
