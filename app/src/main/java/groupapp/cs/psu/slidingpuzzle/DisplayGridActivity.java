package groupapp.cs.psu.slidingpuzzle;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import groupapp.cs.psu.slidingpuzzle.grid.objects.Board;
import groupapp.cs.psu.slidingpuzzle.grid.objects.Coordinates;


public class DisplayGridActivity extends AppCompatActivity {

    /** The main view. */
    private ViewGroup mainView;

    /** The game board. */
    private Board board;

    /** The board view that generates the tiles and lines using 2-D graphics. */
    private DisplayGrid displayGrid;

    /** Text view to show the user the number of movements. */
    private TextView moves;

    /** The board size. Default value is an 5x5 game. */
    private int boardSize = 5;

    /*
     * (non-Javadoc)
     *
     * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_grid);
        mainView = findViewById(R.id.mainLayout);
        moves = (TextView) findViewById(R.id.moves);
        moves.setTextColor(Color.BLACK);
        moves.setTextSize(20);
        this.newGame();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Generates a new game.
     */
    private void newGame() {
        this.board = new Board();
        this.board.addBoardChangeListener(boardChangeListener);
        this.board.rearrange();
        this.mainView.removeView(displayGrid);
        this.displayGrid = new DisplayGrid(this, board);
        this.mainView.addView(displayGrid);
        this.moves.setText("Number of movements: 0");
    }


    /** The board change listener. */
    private Board.BoardChangeListener boardChangeListener = new Board.BoardChangeListener() {
        public void tileSlid(Coordinates from, Coordinates to, int numOfMoves) {
            moves.setText("Number of movements: "
                    + Integer.toString(numOfMoves));
        }

        public void solved(int numOfMoves) {
            moves.setText("Solved in " + Integer.toString(numOfMoves)
                    + " moves!");
            Toast.makeText(getApplicationContext(), "You won!",
                    Toast.LENGTH_LONG).show();
        }
    };
}
