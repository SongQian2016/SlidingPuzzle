package groupapp.cs.psu.slidingpuzzle;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class NumberGameAIActivity extends AppCompatActivity {

    public final static int N = 25;
    public Button startBtn, nextBtn;
    public List numArray = new ArrayList<>();
    public TextView[] tvs = new TextView[N];
    public List<String> textList = new ArrayList<String>();
    private List<List<String>> childrenStates = new ArrayList<>();

    private List<List<String>> visited = new ArrayList<>();

    private int count = 0, preSteps = 0;

    public List<String> tvStrs = new ArrayList<String>(N);
    public ToggleButton pauseBtn;
    private TextView tv_time;
    //private float dX, dY;

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

        startBtn = (Button) findViewById(R.id.startBt);
        tv_time = (TextView) findViewById(R.id.tv_time);
        startBtn = (Button) findViewById(R.id.startBt);
        nextBtn = (Button) findViewById(R.id.nextBt);
        pauseBtn = (ToggleButton) findViewById(R.id.pauseBtn);

        mhandler = new Handler();
        // button visibility
        nextBtn.setEnabled(false);
        pauseBtn.setEnabled(false);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {

                // startBtn.setEnabled(false);
                pauseBtn.setEnabled(true);
                nextBtn.setEnabled(true);

                ispaused=false;

                starttimer();

                List<String> visit = new ArrayList<>();
                for (int i = 0; i < N; i++) {
                    visit.add(" ");
                }
                visited.add(visit);
                init();
                //initiate();

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                aiSearch();
                preSteps++;
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pausetimer();
            }
        });
    }

    public void init() {

        for (int i = 0; i <= 24; i++) {
            numArray.add(i);
        }
        //generate solvable board

        Collections.shuffle(numArray);

        while (!isSolvable(numArray)) {
            Collections.shuffle(numArray);
        }
        for (int i = 0; i < N; i++) {
            textList.add(String.valueOf((numArray.get(i))));
            String textID = "textView" + i;
            int resID = getResources().getIdentifier(textID, "id", getPackageName());
            tvs[i] = (TextView) findViewById(resID);
            tvStrs.add("0");
            if (textList.get(i).equals("0")) {
                tvs[i].setText(" ");
            } else  {
                tvs[i].setText(textList.get(i));
            }
        }
    }

    private void initiate() {
        for (int i = 0; i < N; i++) {
            textList.add(String.valueOf(i+1));
            String textID = "textView" + i;
            int resID = getResources().getIdentifier(textID, "id", getPackageName());
            tvs[i] = (TextView) findViewById(resID);
            tvStrs.add("0");
            tvs[i].setText(textList.get(i));
        }
        //textList.add(String.valueOf((numArray.get(i))));
        tvs[8].setText(" ");
        tvs[13].setText("9");
        tvs[14].setText("14");
        tvs[19].setText("15");
        tvs[18].setText("20");
        tvs[21].setText("17");
        tvs[16].setText("18");
        tvs[17].setText("19");
        tvs[24].setText("24");
        tvs[23].setText("23");
        tvs[22].setText("22");

    }

    private List<List<String>> getAllChildren(List<String> currentState) {
        String temp;
        List<String> upDateCurrentState;
        if (!childrenStates.isEmpty()) {
            childrenStates.clear();
        }
        for(int i = 0; i < N; i++) {
            if(currentState.get(i).equals(" ")) {
                if(i == 6 || i==7 || i ==8 || i==11 ||i==12 || i==13 || i==16
                        || i==17|| i==18) {

                    temp = currentState.get(i + 1);
                    currentState.set(i, currentState.get(i + 1));
                    currentState.set(i + 1, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    currentState.set(i + 1, temp);
                    currentState.set(i, " ");
                    temp = currentState.get(i - 1);
                    currentState.set(i, currentState.get(i - 1));
                    currentState.set(i - 1, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    currentState.set(i - 1, temp);
                    currentState.set(i, " ");
                    temp = currentState.get(i + 5);
                    currentState.set(i, currentState.get(i + 5));
                    currentState.set(i + 5, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    currentState.set(i + 5, temp);
                    currentState.set(i, " ");
                    temp = currentState.get(i - 5);
                    currentState.set(i, currentState.get(i - 5));
                    currentState.set(i - 5, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    break;

                } else if(i == 1 || i== 2 || i==3) {

                    temp = currentState.get(i + 1);
                    currentState.set(i, currentState.get(i + 1));
                    currentState.set(i + 1, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    currentState.set(i + 1, temp);
                    currentState.set(i, " ");
                    temp = currentState.get(i - 1);
                    currentState.set(i, currentState.get(i - 1));
                    currentState.set(i - 1, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    currentState.set(i - 1, temp);
                    currentState.set(i, " ");
                    temp = currentState.get(i + 5);
                    currentState.set(i, currentState.get(i + 5));
                    currentState.set(i + 5, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    break;

                } else if( i== 21 || i==22 || i==23) {

                    temp = currentState.get(i + 1);
                    currentState.set(i, currentState.get(i + 1));
                    currentState.set(i + 1, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    currentState.set(i + 1, temp);
                    currentState.set(i, " ");
                    temp = currentState.get(i - 1);
                    currentState.set(i, currentState.get(i - 1));
                    currentState.set(i - 1, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    currentState.set(i - 1, temp);
                    currentState.set(i, " ");
                    temp = currentState.get(i - 5);
                    currentState.set(i, currentState.get(i - 5));
                    currentState.set(i - 5, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    break;

                } else if (i== 5 || i == 10 || i==15) {

                    temp = currentState.get(i + 1);
                    currentState.set(i, currentState.get(i + 1));
                    currentState.set(i + 1, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    currentState.set(i + 1, temp);
                    currentState.set(i, " ");
                    temp = currentState.get(i + 5);
                    currentState.set(i, currentState.get(i + 5));
                    currentState.set(i + 5, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    currentState.set(i + 5, temp);
                    currentState.set(i, " ");
                    temp = currentState.get(i - 5);
                    currentState.set(i, currentState.get(i - 5));
                    currentState.set(i - 5, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    break;

                } else if(i == 9|| i == 14 || i ==19) {

                    temp = currentState.get(i - 1);
                    currentState.set(i, currentState.get(i - 1));
                    currentState.set(i - 1, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    currentState.set(i - 1, temp);
                    currentState.set(i, " ");
                    temp = currentState.get(i + 5);
                    currentState.set(i, currentState.get(i + 5));
                    currentState.set(i + 5, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    currentState.set(i + 5, temp);
                    currentState.set(i, " ");
                    temp = currentState.get(i - 5);
                    currentState.set(i, currentState.get(i - 5));
                    currentState.set(i - 5, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    break;

                } else if (i == 0) {
                    temp = currentState.get(i + 1);
                    currentState.set(i, currentState.get(i + 1));
                    currentState.set(i + 1, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    currentState.set(i + 1, temp);
                    currentState.set(i, " ");
                    temp = currentState.get(i + 5);
                    currentState.set(i, currentState.get(i + 5));
                    currentState.set(i + 5, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    break;

                }else if ( i == 4) {

                    temp = currentState.get(i - 1);
                    currentState.set(i, currentState.get(i - 1));
                    currentState.set(i - 1, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    currentState.set(i - 1, temp);
                    currentState.set(i, " ");
                    temp = currentState.get(i + 5);
                    currentState.set(i, currentState.get(i + 5));
                    currentState.set(i + 5, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    break;

                } else if ( i == 20) {

                    temp = currentState.get(i + 1);
                    currentState.set(i, currentState.get(i + 1));
                    currentState.set(i + 1, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    currentState.set(i + 1, temp);
                    currentState.set(i, " ");
                    temp = currentState.get(i - 5);
                    currentState.set(i, currentState.get(i - 5));
                    currentState.set(i - 5, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    break;

                } else if (i == 24) {

                    temp = currentState.get(i - 1);
                    currentState.set(i, currentState.get(i - 1));
                    currentState.set(i - 1, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    currentState.set(i - 1, temp);
                    currentState.set(i, " ");
                    temp = currentState.get(i - 5);
                    currentState.set(i, currentState.get(i - 5));
                    currentState.set(i - 5, " ");
                    upDateCurrentState = new ArrayList<String>(currentState);
                    childrenStates.add(upDateCurrentState);
                    break;

                }
            }
        }
        return childrenStates;
    }

    private int numOfWrongPosition(List<String> currentStateList) {
        count = 0;
        for(int i = 0; i < N;i++){
            if (!currentStateList.get(i).equals(String.valueOf(i+1))
                    && (!currentStateList.get(i).equals(" "))) {
                count += 1;
            }
        }
        return count;
    }

    int minCount, index;
    private List<String> getOptimal (List<String> gTvs) {

        List<List<String>> tempChildren = new ArrayList<List<String>>();

        tempChildren = getAllChildren(gTvs);

        minCount = numOfWrongPosition(tempChildren.get(0));
        index = 0;
        for(int i = 1; i < tempChildren.size(); i++) {
            if ((!visited.contains(tempChildren.get(i))) && numOfWrongPosition(tempChildren.get(i)) < minCount ) {
                minCount = numOfWrongPosition(tempChildren.get(i));
                index = i;

            }
        }
        //visited.add(tempChildren.get(index));
        return tempChildren.get(index);
    }

    private void aiSearch() {
        for (int i = 0; i < N; i++) {
            String textID = "textView" + i;
            int resID = getResources().getIdentifier(textID, "id", getPackageName());
            tvs[i] = (TextView) findViewById(resID);
        }
        for (int i = 0; i < N; i++) {
            if (tvs[i].getText().toString().equals(" ")) {
                tvStrs.set(i, " ");
            } else {
                tvStrs.set(i, tvs[i].getText().toString());
            }
        }
        List<String> tempString = new ArrayList<>(getOptimal(tvStrs));
        visited.add(tempString);
        for(int i = 0; i < N; i++) {
            tvs[i].setText(tempString.get(i));
        }
    }

 /*   View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dX = v.getX() - event.getRawX();
                    dY = v.getY() - event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    v.animate()
                            .x(event.getRawX() + dX - (v.getWidth()/2))
                            .y(event.getRawY() + dY - (v.getHeight()/2))
                            .setDuration(0)
                            .start();
                    break;
                default:
                    return false;
            }
            return true;

        }

    }; */

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

    private void starttimer() {
        mstarted=true;
        starttime= SystemClock.uptimeMillis();
        mhandler.postDelayed(mrunnable,10L); //10
    }

    private void pausetimer() {
        if(pauseBtn.isChecked()) {
            ispaused=true;
            mstarted=false;
            mhandler.removeCallbacks(mrunnable);
            currenttime = SystemClock.uptimeMillis() - starttime;
        }
        else
        {
            ispaused=false;
            mstarted=true;
            starttime = SystemClock.uptimeMillis() - currenttime;
            mhandler.postAtTime(mrunnable,0L);
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





















