package groupapp.cs.psu.slidingpuzzle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class NumberGameActivity extends AppCompatActivity {
    //store 1-24 numbers in the button array whose index is from 0-23
    int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
    public Button tiles[];
    public Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_game);

        startBtn = (Button) findViewById(R.id.startButton);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                genNewGame();
            }

            String btnText[];

            private void genNewGame() {
                randShuffle(nums);

                if (!isSolvable(nums)) {
                    randShuffle(nums);
                } else {

                    for (int i = 0; i < nums.length; ++i) {
                        btnText[i] = Integer.toString(nums[i]);
                        String buttonID = "button" + i;
                        int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                        tiles[i] = findViewById(resID);
                        tiles[i].setText(btnText[i]);
                    }

                }
            }
        });

    }
    //to generate a solvable board, we first randomly shuffle the array, then check if it is solvable,
    //if no, reshuffle. if yes, assign them to button text.

    public void randShuffle(int[] A) {
        for (int i = 0; i < A.length; ++i) {
            int index = (int) (Math.random() * A.length);
            int temp = A[i];
            A[i] = A[index];
            A[index] = temp;
        }
    }

    int inversions = 0;

    public int inversionCounter(int[] A) {

        for (int i = 0; i < A.length - 1; ++i) {
            for (int j = i + 1; j < A.length; ++j) {
                if (A[i] > A[j]) {
                    inversions += 1;
                }
            }
        }
        return inversions;
    }
    //If the grid width is odd, then the number of inversions in a solvable situation is even.

    public boolean isSolvable(int[] A) {

        if (inversionCounter(A) % 2 != 0) {
            return false;
        }
        return true;
    }

}



