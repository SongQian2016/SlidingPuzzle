package groupapp.cs.psu.slidingpuzzle.grid.objects;


public class Coordinates {

    private  int x;


    private  int y;


    private Block block;


    private Board board;


    private String UID;




    public Coordinates(int x, int y, Board board) {
        this.x = x;
        this.y = y;
        this.board = board;

    }

    /** Create a new place with the given indices and a tile marked with
     * the given number for the given board. */
    public Coordinates(int x, int y, int number, Board board) {
        this(x, y, board);
        block = new Block(number);
    }

    /** Create a new place with the given indices and a tile marked with
     * the given number for the given board. */
    public Coordinates(int x, int y, char operation, Board board) {
        this(x, y, board);
        block = new Block(operation);
    }


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }


    public void setY(int y) {
       this.y = y;
    }


    /** Check if any block is present */
    public boolean hasBlock() {
        return block != null;
    }


    public Block getBlock() {
        return block;
    }


    public void setBlock(Block block) {
        this.block = block;
    }

    /** Is the tile in this place slidable? Return false if this place
     * is empty, i.e., no tile is placed. */
    public boolean slidable() {
        return hasBlock() && board.slidable(this);
    }

    public void slide() {
        board.slide(getBlock());
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
