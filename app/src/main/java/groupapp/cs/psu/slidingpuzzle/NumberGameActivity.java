package groupapp.cs.psu.slidingpuzzle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NumberGameActivity extends AppCompatActivity {
    //store 1-24 numbers in the button array whose index is from 0-23
    //int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
    public List<Integer> nums = new ArrayList<>();
    public Button tiles[] = new Button[25];
    public Button startB;
    public List<String> tileList = new ArrayList<String>(nums.size());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_game);


        for (int i = 1; i <= 24; i++) {
            nums.add(i);
        }

        startB = (Button) findViewById(R.id.startBt2);

        startB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Collections.shuffle(nums);

                while (!isSolvable(nums)) {
                    Collections.shuffle(nums);
                }
                //Log.d("Number generated.");
                for (int i = 0; i < nums.size(); ++i) {
                    tileList.add(String.valueOf((nums.get(i))));
                    String btnID = "button" + i;
                    int resID = getResources().getIdentifier(btnID, "id", getPackageName());
                    tiles[i] = (Button) findViewById(resID);
                    tiles[i].setText(tileList.get(i));

                }
                //tiles[24].setText("");
            }
        });
        // playNumGame();
    }

    private int inversions = 0;

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


}



