package groupapp.cs.psu.slidingpuzzle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
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

import groupapp.cs.psu.slidingpuzzle.firebase.objects.PlayerScoreInformation;

public class Math1playerActivity extends AppCompatActivity implements View.OnClickListener,GestureDetector.OnGestureListener {

    private Button[][] buttons = new Button[5][5];
    Chronometer chronometer;
    private SharedPreferences shared_pref;
    private List<Object> gridValues = new ArrayList<>();
    private GestureDetector gDetector;
    private LinearLayout page;
    private Object[] submittedValues = new Object[5];
    private MathEquationProcessor processor = new MathEquationProcessor();
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private PlayerScoreInformation playerScoreInformation;
    private TextView Score;
    private int score;
    private Button displayHighScore;


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

        // add the value 10 to replace it with blanl tile
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shared_pref = getPreferences(MODE_PRIVATE);




        //Firebase to fetch the score from backend
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("player");
        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(

                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        playerScoreInformation = dataSnapshot.getValue(PlayerScoreInformation.class);
                        score = playerScoreInformation.getSinglePlayerScore();
                        System.out.println("Score:" + score );
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        //LinearLayout page;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
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


        // Display High Score Button
        displayHighScore = new Button(this);
        displayHighScore.setText("Display High Score");
        displayHighScore.setLayoutParams(params);
        displayHighScore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Math1playerActivity.this, HighScoreActivity.class));
            }
        });
        NewLine.addView(displayHighScore);


       //Score Display
        Score = new TextView(this);
        Score.setLayoutParams(params);
        Score.setText("Score: " + score);
        NewLine.addView(Score);





        //Timer
        TextView Time = new TextView(this);
        Time.setLayoutParams(params);
        Time.setText("Timer:");
        NewLine.addView(Time);
        chronometer = new Chronometer(this);
        if (shared_pref.getLong("chrono", -1) != -1)
            chronometer.setBase(shared_pref.getLong("chrono", 0));
        chronometer.start();
        chronometer.setLayoutParams(params);
        NewLine.addView(chronometer);
        page.addView(NewLine);

        setContentView(page);
        gDetector = new GestureDetector(this,this);


        // Set it up for use:
        page.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gDetector.onTouchEvent(event);
            }
        });


        }


    @Override
    public void onClick(View v) {
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
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //PlayerScoreInformation playerScoreInformation = dataSnapshot.getValue(PlayerScoreInformation.class);
                DatabaseReference scoreDataReference = dataSnapshot.getRef().child("singlePlayerScore");
                scoreDataReference.setValue(score);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       /* new AlertDialog.Builder(this)
                .setTitle("Save game")
                .setMessage("Would you like to save the game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() { //anonymous class
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = shared_pref.edit();
                        for (int i = 0; i < 4; i++)
                            for (int j = 0; j < 4; j++)
                                editor.putString(i + " " + j, buttons[i][j].getText().toString());
                        editor.putInt("moves", moves);
                        editor.putLong("chrono", chronometer.getBase());
                        editor.apply(); //save to shared pref
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() { //anonymous class
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shared_pref.edit().clear().apply();
                        finish();
                    }
                })
                .show();
*/
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

    private boolean handleResult(int equationResult){
        if (equationResult >= 0){
            score = score + equationResult;
            Score.setText("Score: " + score);
            Toast.makeText(this, "Valid Equation Submitted!!", Toast.LENGTH_SHORT).show();
            return true;
        }else{
            Toast.makeText(this, "Please submit a valid equation!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

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

}
