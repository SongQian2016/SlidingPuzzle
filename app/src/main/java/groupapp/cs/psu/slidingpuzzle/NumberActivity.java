package groupapp.cs.psu.slidingpuzzle;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class NumberActivity extends AppCompatActivity {

    private Chronometer math_timer;
    private long lastpause;
    public Button pauseBtn;

    Numbers nums;
    private TextView steps;
    private int numbSteps;
    private TextView history;
    private int numbBestSteps;

    private boolean check;
    private final int N = 5;
    private ImageButton[][] button;
    private final int BUTTONS[][] = {{R.id.b00, R.id.b01, R.id.b02, R.id.b03, R.id.b04},
            {R.id.b10, R.id.b11, R.id.b12, R.id.b13, R.id.b14},
            {R.id.b20, R.id.b21, R.id.b22, R.id.b23, R.id.b24},
            {R.id.b30, R.id.b31, R.id.b32, R.id.b33, R.id.b34},
            {R.id.b40, R.id.b41, R.id.b42, R.id.b43, R.id.b44}};
    private final int NUMS[] = {R.drawable.n00, R.drawable.n01, R.drawable.n02, R.drawable.n03, R.drawable.n04,
            R.drawable.n05, R.drawable.n06, R.drawable.n07, R.drawable.n08, R.drawable.n09,
            R.drawable.n10, R.drawable.n11, R.drawable.n12, R.drawable.n13, R.drawable.n14,
            R.drawable.n15, R.drawable.n16, R.drawable.n17, R.drawable.n18, R.drawable.n19,
            R.drawable.n20, R.drawable.n21, R.drawable.n22, R.drawable.n23, R.drawable.n24};

    /*
    Generate buttons on pause
     */
    private void genbuttonsonpause(){
        button = new ImageButton[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                button[i][j] = (ImageButton) this.findViewById(BUTTONS[i][j]);
                button[i][j].setOnClickListener(null);
            }
        }
    }

    /*
    generate buttons on resume
     */
    private void genbuttonsonresume(){
        button = new ImageButton[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                button[i][j] = (ImageButton) this.findViewById(BUTTONS[i][j]);
                button[i][j].setOnClickListener(onClickListener);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);

        button = new ImageButton[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                button[i][j] = (ImageButton) this.findViewById(BUTTONS[i][j]);
                button[i][j].setOnClickListener(onClickListener);
            }
        }

        // pause button and chronometer
        pauseBtn=(Button)findViewById(R.id.pausebutton);
        math_timer = findViewById(R.id.chronometer);
        math_timer.start();

        /**
         * This method shows the functionality of pause and resume
         */
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pausetext = pauseBtn.getText().toString();
                if(pausetext.equals("P")){
                    pauseBtn.setText("R");
                    lastpause = SystemClock.elapsedRealtime();
                    math_timer.stop();
                    genbuttonsonpause();
                }
                else{
                    pauseBtn.setText("P");
                    math_timer.setBase(math_timer.getBase()+ SystemClock.elapsedRealtime()-lastpause);
                    math_timer.start();
                    genbuttonsonresume();
                }
            }
        });

        Button newGameBtn = (Button) this.findViewById(R.id.newGameBtn);
        newGameBtn.setOnClickListener(onClickListener);

        Typeface digitalFont = Typeface.createFromAsset(this.getAssets(), "myFont.ttf");
        TextView mySteps = (TextView) this.findViewById(R.id.steps);
        mySteps.setTypeface(digitalFont);
        steps = (TextView) this.findViewById(R.id.step);
        steps.setTypeface(digitalFont);

        TextView myHistory = (TextView) this.findViewById(R.id.historys);
        myHistory.setTypeface(digitalFont);
        history = (TextView) this.findViewById(R.id.history);
        history.setTypeface(digitalFont);

        nums = new Numbers(N, N);
        try {
            if(getIntent().getExtras().getInt("keyGame") == 1) {
                continueGame();
                checkResult();
            } else newGame();
        } catch (Exception e) {
            newGame();
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!check) {
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        if (v.getId() == BUTTONS[i][j])
                            butMove(i, j);
                    }
                }
            }
            switch (v.getId()) {
                case R.id.newGameBtn:
                    newGame();
                    break;
                default:
                    break;
            }
        }
    };

    public void butMove(int row, int columb) {
        nums.moveNumbers(row, columb);
        if (nums.resultMove()) {
            numbSteps++;
            displayGame();
            checkResult();
        }
    }

    public void newGame() {
        nums.getNewNumbers();
        numbSteps = 0;
        numbBestSteps = Integer.parseInt(readFile("fbs24"));
        history.setText(Integer.toString(numbBestSteps));
        displayGame();
        check = false;
    }

    private void continueGame() {
        String text = getPreferences(MODE_PRIVATE).getString("savePyatnashki", "");
        int k = 0;
        for(int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                nums.setValueBoard(i, j, Integer.parseInt("" + text.charAt(k) + text.charAt(k + 1)));
                k += 2;
            }
        }
        numbSteps = Integer.parseInt(readFile("fileScore"));
        numbBestSteps = Integer.parseInt(readFile("fbs24"));
        history.setText(Integer.toString(numbBestSteps));

        displayGame();
        check = false;
    }

    private void saveValueBoard() {
        String text = "";
        for(int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (nums.getValueBoard(i, j) < 10)
                    text += "0" + nums.getValueBoard(i, j);
                else text += nums.getValueBoard(i, j);
            }
        }
        getPreferences(MODE_PRIVATE).edit().putString("saveThis", text).commit();
        writeFile(Integer.toString(numbSteps), "fileScore");
        writeFile(Integer.toString(N), "fileN");
    }

    public void displayGame() {
        steps.setText(Integer.toString(numbSteps));
        for (int i = 0; i < N; i++){
            for (int j = 0; j < N; j++) {
                button[i][j].setImageResource(NUMS[nums.getValueBoard(i, j)]);
            }
        }
    }

    public void checkResult(){
        if(nums.finished(N, N)){
            displayGame();
            Toast.makeText(NumberActivity.this, R.string.you_won, Toast.LENGTH_SHORT).show();
            if ((numbSteps < numbBestSteps) || (numbBestSteps == 0)) {
                writeFile(Integer.toString(numbSteps), "fbs24");
                history.setText(Integer.toString(numbSteps));
            }
            check = true;
        }
    }

    public void writeFile(String text, String FILENAME) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput(FILENAME, MODE_PRIVATE)));
            bw.write(text);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String readFile(String FILENAME) {
        String text;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(FILENAME)));
            text = br.readLine();
        } catch(IOException e) {
            text = "0";
        }
        return text;
    }

}
