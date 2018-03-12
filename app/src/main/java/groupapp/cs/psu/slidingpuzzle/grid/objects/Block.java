package groupapp.cs.psu.slidingpuzzle.grid.objects;



public class Block {

    /** Block Number */
    private final int number;
    //private final int operation;

    private final char operation;

    public Block () {
        this.number = -1;
        this.operation = ' ';
    }


    public Block(int number) {
        this.number = number;
        this.operation = ' ';
    }

    public Block(char operation) {
        this.number = 0;
        this.operation = operation;
    }

    public int number() {
        return number;
    }

    public char operation(){
        return operation;
    }
}
