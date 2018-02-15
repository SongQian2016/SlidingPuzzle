package groupapp.cs.psu.slidingpuzzle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class NumberGameAIActivity extends AppCompatActivity {

    //int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};

    public Button startBtn, exitBtn;
    public List numArray = new ArrayList<>();
    public TextView[] tvs = new TextView[25];
    public TextView temp;
    public List<String> textList = new ArrayList<String>(numArray.size());

    public ToggleButton pauseBtn;
    private TextView tv_time;

    private boolean ispaused = false;
    private boolean mstarted;

    long starttime=0L;
    long timeSwap = 0L;
    long finalTime = 0L;
    long millis=0L;
    long currenttime=0L;

    private Handler mhandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_game_ai);

        init();

    }

    public void init() {

        for (int i = 1; i <= 24; i++) {
            numArray.add(i);
        }
        startBtn = (Button) findViewById(R.id.startBt);


        tv_time = (TextView) findViewById(R.id.tv_time);
        startBtn = (Button) findViewById(R.id.startBt);
        exitBtn = (Button) findViewById(R.id.exitBt);
        pauseBtn = (ToggleButton) findViewById(R.id.pauseBtn);

        mhandler = new Handler();
        // button visibility
        exitBtn.setEnabled(false);
        pauseBtn.setEnabled(false);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {

                startBtn.setEnabled(false);
                pauseBtn.setEnabled(true);
                exitBtn.setEnabled(true);

                ispaused=false;
                //genNewGame();
                starttimer();

                Collections.shuffle(numArray);

                while (!isSolvable(numArray)) {
                    Collections.shuffle(numArray);
                }
                //Log.d("Number generated.");
                for (int i = 0; i < numArray.size(); ++i) {
                    textList.add(String.valueOf((numArray.get(i))));
                    String textID = "textView" + i;
                    int resID = getResources().getIdentifier(textID, "id", getPackageName());
                    tvs[i] = (TextView) findViewById(resID);
                    tvs[i].setText(textList.get(i));
                  //  tvs[i].setOnTouchListener(onTouchListener);
                    // tvs[i].setOnDragListener(dragListener);

                   /* if (tvs[i].getText() != null &&
                            (tvs[i+1] == null || tvs[i-1] == null || tvs[i+5] == null || tvs[i-5] ==null)){
                        tvs[i].setOnTouchListener(onTouchListener);
                    }*/
                }
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pausetimer();
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openMenu();
            }

            private void openMenu() {
                Intent intent = new Intent(NumberGameAIActivity.this, NunModeSelectActivity.class);
                startActivity(intent);
            }
        });

    }

    int inversions = 0;

    private int inversionCounter(List<Integer> A) {

        for (int i = 0; i < A.size() - 1; ++i) {
            for (int j = i + 1; j < A.size(); ++j) {
                if (A.get(i) > A.get(j)) {
                    inversions += 1;
                }
            }
        }
        return inversions;
    }

    //If the grid width is odd, then the number of inversions in a solvable situation is even.

    private boolean isSolvable(List<Integer> A) {
        if (inversionCounter(A) % 2 != 0) {
            return false;
        }
        return true;
    }

   // View.OnTouchListener onTouchListener = new View.OnTouchListener(){

        public boolean onTouchEvent(MotionEvent event) {
            //if (v.getId() == R.id.textView23) {
            //  tvs[24].setText("00");
            //}

            float x = event.getX();
            float y = event.getY();

            if (event.getAction() == MotionEvent.ACTION_MOVE){
                tvs[24].setX(x);
                tvs[24].setY(y);
            }

            return true;
        }


    private void starttimer() {
        mstarted=true;
        starttime= SystemClock.uptimeMillis();
        mhandler.postDelayed(mrunnable,10L); //10
    }

    private void pausetimer() {
        if(pauseBtn.isChecked()) {
            ispaused=true;
            mstarted=false;
            //timeSwap += millis;
            mhandler.removeCallbacks(mrunnable);
            currenttime = SystemClock.uptimeMillis() - starttime;
            //currenttime = SystemClock.uptimeMillis() - starttime;
        }
        else
        {
            ispaused=false;
            mstarted=true;
            //starttime = System.currentTimeMillis() - currenttime;
            starttime = SystemClock.uptimeMillis() - currenttime;
            mhandler.postAtTime(mrunnable,0L);
            //mhandler.removeCallbacks(mrunnable);
            //mhandler.postDelayed(mrunnable,0L);
        }
    }

    private final Runnable mrunnable = new Runnable() {
        @Override
        public void run() {
            if(mstarted) {
                //millis = System.currentTimeMillis() - starttime;
                millis = (SystemClock.uptimeMillis() - starttime);
                finalTime = timeSwap + millis;
                long seconds = finalTime / 1000;
                //long seconds = millis/1000;
                tv_time.setText(String.format("%02d:%02d:%02d",seconds/60,seconds%60,millis%100));//100
                mhandler.postDelayed(mrunnable,millis/10L);//10
            }
        }
    };

}









