package groupapp.cs.psu.slidingpuzzle;

import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Math1playerActivity extends AppCompatActivity implements View.OnClickListener,GestureDetector.OnGestureListener {

    private Button[][] buttons = new Button[5][5];
    Chronometer GameTimer;
    private SharedPreferences shared_pref;
    private List<Object> gridValues = new ArrayList<>();
    private GestureDetector gDetector;
    private LinearLayout page;
    private Object[] submittedValues = new Object[5];
    private MathEquationProcessor processor = new MathEquationProcessor();
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private TextView Score;
    private int score;
    private Button displayHighScore;
    private List<String> submittedEquations = new ArrayList<>();

    private Button pausebutton;
    private long lastPause;

    /**
     *
     *This method populates the list with grid values
     */
    private void createGridValues(){
        //Add numbers
        int count = 0;
        for (int i = 0; i < 15; i++) {
            if (count > 9) {
                count = 0;
            }
            gridValues.add((Integer)count++);
        }

        //Add operators
        gridValues.add('+');
        gridValues.add('*');
        gridValues.add('-');
        gridValues.add('/');
        gridValues.add('=');
        gridValues.add('=');
        gridValues.add('=');
        gridValues.add('=');
        gridValues.add('=');

        //Shuffle the list to scatter the values
        Collections.shuffle(gridValues);

        // add the value 10 to replace it with blank tile
        gridValues.add(10);

    }

    /**
     *
     * @param params
     * This method populates the grid values in the buttons
     */

    private void populateGrids(LinearLayout.LayoutParams params){
        int k = 0;
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++) {
                buttons[i][j] = new Button(this);
                buttons[i][j].setTag(i + " " + j);
                String val = shared_pref.getString(i + " " + j,gridValues.get(k++).toString() );
                //either i+j or k if former not available
                buttons[i][j].setText(val);
                buttons[i][j].setLayoutParams(params);
                buttons[i][j].setOnClickListener(this); //main activity listens when clicked
            }
    }

    /**
     * This method is called on click of
     * pause button to disable the grid buttons
     * @param params
     */
    private void populateGridsOnPause(LinearLayout.LayoutParams params){
        int k = 0;
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++) {
                buttons[i][j].setOnClickListener(null);
            }
    }

    /**
     * This method is called on click of resume button
     * to activate the grid buttons
     * @param params
     */
    private void populateGridsOnResume(LinearLayout.LayoutParams params){
        int k = 0;
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++) {
                buttons[i][j].setOnClickListener(this);
            }
    }

    /**
     * This method renders the UI for Math mode
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shared_pref = getPreferences(MODE_PRIVATE);



        //LinearLayout page;
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        createGridValues();
        populateGrids(params);

        if (buttons[4][4].getText().toString().equals("10"))  {
            buttons[4][4].setText("");
        }

        page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);

        LinearLayout rows = new LinearLayout(this);
        rows.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < 5; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            rows.addView(row);
            for (int j = 0; j < 5; j++)
                row.addView(buttons[i][j]);
        }
        page.addView(rows);


        LinearLayout NewLine = new LinearLayout(this);
        NewLine.setOrientation(LinearLayout.HORIZONTAL);


        //Score Display
        Score = new TextView(this);
        Score.setLayoutParams(params);


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("player");
        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                score = ((Long)dataSnapshot.child("singlePlayerScore").getValue()).intValue();
                Score.setText("Score: " + score);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        NewLine.addView(Score);

        //Timer
        TextView Time = new TextView(this);
        Time.setLayoutParams(params);
        Time.setText("Timer:");
        NewLine.addView(Time);
        GameTimer = new Chronometer(this);
        if (shared_pref.getLong("chrono", -1) != -1)
            GameTimer.setBase(shared_pref.getLong("chrono", 0));
        GameTimer.start();
        GameTimer.setLayoutParams(params);
        NewLine.addView(GameTimer);
        page.addView(NewLine);

        LinearLayout NewLine2 = new LinearLayout(this);
        NewLine2.setOrientation(LinearLayout.HORIZONTAL);

        // Display Pause Button
        pausebutton = new Button(this);
        pausebutton.setText("Pause");
        pausebutton.setLayoutParams(params);
        NewLine2.addView(pausebutton);
        page.addView(NewLine2);

        setContentView(page);
        gDetector = new GestureDetector(this,this);

        pausebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pausetext = pausebutton.getText().toString();
                if(pausetext.equals("Pause")){
                    pausebutton.setText("Resume");
                    lastPause = SystemClock.elapsedRealtime();
                    GameTimer.stop();
                    populateGridsOnPause(params);
                }
                else{
                    pausebutton.setText("Pause");
                    GameTimer.setBase(GameTimer.getBase()+ SystemClock.elapsedRealtime()-lastPause);
                    GameTimer.start();
                    populateGridsOnResume(params);
                    setContentView(page);
                }
            }
        });

        // Set it up for use:
        page.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gDetector.onTouchEvent(event);
            }
        });


    }


    /**
     * This method is called when the user makes
     * a move on the grids
     * @param v
     */
    @Override
    public void onClick(View v) {
        try{
            Button button = (Button)v;
            String[] s = button.getTag().toString().split(" ");
            int x = Integer.parseInt(s[0]);
            int y = Integer.parseInt(s[1]);

            int[] xx = {x - 1, x, x + 1, x};
            int[] yy = {y, y - 1, y, y + 1};

            for (int k = 0; k < 5; k++) {

                int i = xx[k];
                int j = yy[k];
                if (i >= 0 && i < 5 && j >= 0 && j < 5 && buttons[i][j].getText().equals("")) {
                    buttons[i][j].setText(button.getText());
                    button.setText("");
                    break;
                }
            }}catch (Exception e){
            Toast.makeText(this, "Make a valid move!", Toast.LENGTH_SHORT).show();
        }
    }




    /**
     * Stores the player score in the fire base
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference scoreDataReference = dataSnapshot.getRef().child("singlePlayerScore");
                scoreDataReference.setValue(score);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return gDetector.onTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent event) {

        return this.gDetector.onTouchEvent(event);
    }

    /**
     * Used to detect swipe movements by the user
     * @param start
     * @param finish
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onFling(MotionEvent start, MotionEvent finish, float velocityX, float velocityY) {
        int equationResult = -1;
        if (start == null ||  finish == null) {
            Toast.makeText(this, "Submit proper equation", Toast.LENGTH_SHORT).show();
            return false;
        }


        int xStart = (int) start.getX();
        int yStart = (int) start.getY();
        Button startButton = (Button) findViewAt(page, xStart, yStart);
        String[] startTag = startButton.getTag().toString().split(" ");
        int xs = Integer.parseInt(startTag[0]);
        int ys = Integer.parseInt(startTag[1]);


        int xFinish = (int) finish.getX();
        int yFinish = (int) finish.getY();
        Button finishButton = (Button) findViewAt(page, xFinish, yFinish);
        String[] finishTag = finishButton.getTag().toString().split(" ");
        int xf = Integer.parseInt(finishTag[0]);
        int yf = Integer.parseInt(finishTag[1]);

        if (xs == xf && ys < yf) {
            System.out.println("Left to right");
            int validEquation =  validateEquation(xs, ys, xf, yf, 0);
            if (validEquation == 1) {
                getSubmittedButtons(xs, ys,0);
                equationResult = processor.solveEquation(submittedValues);
                System.out.println("equationResult: "+ equationResult);
                handleResult(equationResult);
            }
            return true;
        } else if (xs == xf && ys > yf) {
            System.out.println("Right to Left");
            int validEquation = validateEquation(xs, ys, xf, yf, 0);
            if (validEquation == 1) {
                getSubmittedButtons(xs, ys,1);
                equationResult = processor.solveEquation(submittedValues);
                System.out.println("equationResult: "+ equationResult);
                handleResult(equationResult);
            }
            return true;
        }else if (xs < xf && ys == yf) {
            System.out.println("Up to Down");
            int validEquation = validateEquation(xs, ys, xf, yf, 1);
            if (validEquation == 1) {
                getSubmittedButtons(xs, ys,2);
                equationResult = processor.solveEquation(submittedValues);
                System.out.println("equationResult: "+ equationResult);
                handleResult(equationResult);
            }
            return true;
        } else if (xs > xf && ys == yf) {
            System.out.println("Down to Up");
            int validEquation = validateEquation(xs, ys, xf, yf, 1);
            if (validEquation == 1) {
                getSubmittedButtons(xs, ys,3);
                equationResult = processor.solveEquation(submittedValues);
                System.out.println("equationResult: "+ equationResult);
                handleResult(equationResult);
            }
            return true;
        }

        return true;
    }

    /**
     * Displays success message if the equation result is
     * correct else gives error message
     * @param equationResult
     * @return
     */
    private boolean handleResult(int equationResult){
        if (equationResult >= 0){
            String equationString = formEquationString();
            if(!checkRepeatedSubmission(equationString)) {
                submittedEquations.add(equationString);
                score = score + equationResult;
                Score.setText("Score: " + score);
                Toast.makeText(this, "Valid Equation Submitted!!", Toast.LENGTH_SHORT).show();
                return true;
            }else {
                Toast.makeText(this, "Equation repeated!", Toast.LENGTH_SHORT).show();
                return false;
            }

        }else{
            Toast.makeText(this, "Please submit a valid equation!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    /**
     * Validates the submitted equation to check
     * if 5 grids are submitted
     * @param xs
     * @param ys
     * @param xf
     * @param yf
     * @param direction
     * @return
     */
    private int validateEquation(int xs, int ys, int xf, int yf, int direction){
        int returnValue = 1;
        switch(direction) {

            //Horizontal
            case 0:
                if (Math.abs((yf - ys))< 4) {
                    Toast.makeText(this, "Please submit a valid equation!", Toast.LENGTH_SHORT).show();
                    returnValue = -1;
                }
                break;
            // Vertical
            case 1:
                if (Math.abs((xf - xs))< 4) {
                    Toast.makeText(this, "Please submit a valid equation!", Toast.LENGTH_SHORT).show();
                    returnValue = -1;
                }
                break;
        }
        return returnValue;
    }


    /**
     *
     * @param viewGroup
     * @param x
     * @param y
     * @return
     */
    private View findViewAt(LinearLayout viewGroup, int x, int y) {
        for(int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof LinearLayout) {
                View foundView = findViewAt((LinearLayout) child, x, y);
                if (foundView != null && foundView.isShown()) {
                    return foundView;
                }
            } else {
                int[] location = new int[2];
                child.getLocationOnScreen(location);
                Rect rect = new Rect(location[0], location[1], location[0] + child.getWidth(), location[1] + child.getHeight());
                if (rect.contains(x, y)) {
                    return child;
                }
            }
        }
        return null;
    }

    /**
     * Fetch values from the submitted grids
     * @param xs
     * @param ys
     * @param direction
     */
    private void getSubmittedButtons(int xs, int ys, int direction){
        int index = 0;
        submittedValues = new Object[5];

        switch(direction){

            //left to right
            case 0:
                for(int column = 0;column < 5; column++) {
                    submittedValues[index] = (Object) buttons[xs][column].getText();
                    System.out.println("submittedValues: " +submittedValues[index]);
                    index++;
                }
                break;

            //right to left
            case 1:
                for(int column = 4;column >= 0; column--) {
                    submittedValues[index] = (Object) buttons[xs][column].getText();
                    System.out.println("submittedValues: " + submittedValues[index]);
                    index++;
                }
                break;

            //up to down
            case 2:
                for(int rows = 0;rows < 5 ; rows++) {
                    submittedValues[index] = (Object) buttons[rows][ys].getText();
                    System.out.println("submittedValues: " + submittedValues[index]);
                    index++;
                }
                break;

            //down to up
            case 3:
                for(int rows = 4;rows >= 0; rows--) {
                    submittedValues[index] = (Object) buttons[rows][ys].getText();
                    System.out.println("submittedValues: " +submittedValues[index]);
                    index++;
                }
                break;
        }


    }

    /**
     * Form an equation in the string format
     * @return
     */
    private String formEquationString(){
        String submittedEquation = "";
        for (int i = 0; i < 5; i++){
            submittedEquation = submittedEquation + submittedValues[i].toString();
        }
        return submittedEquation;
    }

    /**
     * Check if the same equation is submitted more than once
     * @param submittedEquation
     * @return
     */
    private boolean checkRepeatedSubmission(String submittedEquation){
        boolean isRepeated = false;
        if(submittedEquation !=null && !submittedEquation.isEmpty() && submittedEquations.size()!=0) {
            for (int i = 0; i < submittedEquations.size(); i++) {
                if(submittedEquations.get(i).equals(submittedEquation)){
                    isRepeated = true;
                }
            }
        }
        return isRepeated;
    }




}