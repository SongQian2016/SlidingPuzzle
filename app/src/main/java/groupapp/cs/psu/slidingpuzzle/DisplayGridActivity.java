package groupapp.cs.psu.slidingpuzzle;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import groupapp.cs.psu.slidingpuzzle.firebase.objects.PlayerScoreInformation;
import groupapp.cs.psu.slidingpuzzle.grid.objects.Board;
import groupapp.cs.psu.slidingpuzzle.grid.objects.Coordinates;


public class DisplayGridActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    /**
     * The main view.
     */
    private ViewGroup mainView;

    /**
     * The game board.
     */
    private Board board;

    /**
     * The board view that generates the tiles and lines using 2-D graphics.
     */
    private DisplayGrid displayGrid;

    /**
     * Text view to show the user the number of movements.
     */
    //private TextView moves;

    /**
     * The board size. Default value is an 5x5 game.
     */
    private int boardSize = 5;

    private Coordinates startCoordinates;
    private Coordinates endCoordinates;


    //newly added

    private GestureDetector mGestureDetector = null;
    private TextView mGestureText;
    String TAG = "MyEquationActivity";
    private MotionEvent mDownEvent = null;
    private TextView playerScoreEditText;

    //Database insert score related
    private DatabaseReference playerReference;
    private FirebaseAuth firebaseAuth;

    /*
     * (non-Javadoc)
     *
     * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_grid);
        mainView = findViewById(R.id.mainLayout);
        //moves = (TextView) findViewById(R.id.moves);
        //moves.setTextColor(Color.BLACK);
        //moves.setTextSize(20);

        //Regarding gesture detection start
        //To print swipe activity
        mGestureText = (TextView) findViewById(R.id.swipe_activity);
        playerScoreEditText = (TextView) findViewById(R.id.player_score);

        // Create an object of our Custom Gesture Detector Class
        Log.d("myOnCreate", "Creating CustomGesturDetector>>>>>>>>>>>>>");

        //Create a GestureDetector
        mGestureDetector = new GestureDetector(this, this);

        //Attach listeners that'll be called for double-tap and related gestures
        mGestureDetector.setOnDoubleTapListener(this);


        //Get Instance for FireBase real time database
        firebaseAuth = FirebaseAuth.getInstance();
        playerReference = FirebaseDatabase.getInstance().getReference("player");

        //Regarding gesture detection end
        Log.d("mynewGame", "Creating new Game()>>>>>>>>>>>>>");

        this.newGame();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return mGestureDetector.onTouchEvent(ev);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void newGame() {
        this.board = new Board();
        //this.board.addBoardChangeListener(boardChangeListener);
        this.board.rearrange();
        this.mainView.removeView(displayGrid);
        this.displayGrid = new DisplayGrid(this, board);
        this.mainView.addView(displayGrid);
        this.playerScoreEditText.setText("Player Score: 0");
    }


    /**
     * The board change listener.
     */
   /* private Board.BoardChangeListener boardChangeListener = new Board.BoardChangeListener() {
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
    };*/


    private int solveEquation(MotionEvent e1, MotionEvent e2){
        MathModeProcessEquation processSubmittedEquation = new MathModeProcessEquation();
        int processEquation = 0;
        if (e1.getX() < e2.getX() && e1.getY() > e2.getY()) {
            Log.d(TAG, "Left to Right swipe performed");
            startCoordinates = displayGrid.locateCoordinates1(e1.getRawX(), e1.getRawY());
            endCoordinates = displayGrid.locateCoordinates1(e2.getRawX(), e2.getRawY());
            processEquation = processSubmittedEquation.processEquation(startCoordinates, endCoordinates, 0, this.board);
        } else if (e1.getX() > e2.getX() && e1.getY() < e2.getY()) {
            Log.d(TAG, "Right to Left swipe performed");
            startCoordinates = displayGrid.locateCoordinates1(e1.getRawX(), e1.getRawY());
            endCoordinates = displayGrid.locateCoordinates1(e2.getRawX(), e2.getRawY());
            processEquation = processSubmittedEquation.processEquation(startCoordinates, endCoordinates, 1, this.board);
        } else if (e1.getX() < e2.getX() && e1.getY() < e2.getY()) {
            Log.d(TAG, "Up to Down swipe performed");
            startCoordinates = displayGrid.locateCoordinates1(e1.getRawX(), e1.getRawY());
            endCoordinates = displayGrid.locateCoordinates1(e2.getRawX(), e2.getRawY());
            processEquation = processSubmittedEquation.processEquation(startCoordinates, endCoordinates, 2, this.board);
        } else if (e1.getX() > e2.getX() && e1.getY() > e2.getY()) {
            Log.d(TAG, "Down to Up swipe performed");
            startCoordinates = displayGrid.locateCoordinates1(e1.getRawX(), e1.getRawY());
            endCoordinates = displayGrid.locateCoordinates1(e2.getRawX(), e2.getRawY());
            processEquation = processSubmittedEquation.processEquation(startCoordinates, endCoordinates, 3, this.board);
        }

        return processEquation;
    }

    private void updatePlayerScore(int playerScore){
        //After successful registration of the user, initialize the score of the player to 0
        int initialScore = 0;
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Map<String, Object> scoreUpdates = new HashMap<>();
        scoreUpdates.put("singlePlayerScore", playerScore);
        PlayerScoreInformation playerScoreUpdate = new PlayerScoreInformation(playerScore,user.getEmail());
        playerReference.child(user.getUid()).setValue(playerScoreUpdate);

       /* if(playerScoreEditText.getText()!= "") {
            initialScore = Integer.parseInt(playerScoreEditText.getText().toString());
        }*/
        //playerScoreEditText.setText("Player Score: " +initialScore+playerScore);
    }

    private boolean handleEquationResult(int processEquation){
            if (processEquation >= 0){
                mGestureText.setText("Good job");
                playerScoreEditText.setText("Player Score: "+processEquation);
                updatePlayerScore(processEquation);
                //playerScore.setText();

            }else if (processEquation == -3){
                mGestureText.setText("Try again!");
            } else if(processEquation == -2){
                mGestureText.setText("Submit a well-formed equation.");
            } else if(processEquation == -1){
                mGestureText.setText("Submit a well-formed equation.");
            }
            return true;
    }

    //Code for swipe
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        String TAG = "foo tag";
        Log.d("myOnTouchEvent", "OnTouchEvent method invoked>>>>>>>>>>>>>>>");

        int action = event.getActionMasked();

        switch (action) {

            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "Action was DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "Action was MOVE");
                break;

            case MotionEvent.ACTION_UP:

                Log.d(TAG, "Action was UP");
                break;

            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "Action was CANCEL");
                break;

            case MotionEvent.ACTION_OUTSIDE:
                Log.d(TAG, "Movement occurred outside bounds of current screen element");
                break;
        }
        return this.mGestureDetector.onTouchEvent(event);
    }



    @Override
    public boolean onDown(MotionEvent motionEvent) {
        Log.d("event", "got motionEvent>>>>>>>>>>>>>: " + motionEvent);
        mDownEvent = motionEvent;
        //mGestureText.setText("onDown");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        //TODO
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        //TODO
    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean isOnFlingSuccess = false;
        //Coordinates startCoordinates = displayGrid.locateCoordinates1(e1.getRawX(),e1.getRawY());
        //endCoordinates = displayGrid.locateCoordinates1(e2.getRawX(),e2.getRawY());

        int processEquation = 0;

        Log.d("myOnFling", "INSIDE ON FLING!!!!!!!!!!!");
        /*if (null == e1) {
            e1 = mDownEvent;
        }*/
        if (e1 == null || e2 == null) {
            mGestureText.setText("Submit the equation properly");
            return isOnFlingSuccess;
        }

        processEquation = solveEquation(e1, e2);
        isOnFlingSuccess = handleEquationResult(processEquation);

        return isOnFlingSuccess;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return true;
    }

   /* @Override
    protected void onStart(){
        super.onStart();

        playerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PlayerScoreInformation playerScore = dataSnapshot.getValue(PlayerScoreInformation.class);
                playerScoreEditText.setText("Player Score:" + playerScore.singlePlayerScore);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }*/

}
