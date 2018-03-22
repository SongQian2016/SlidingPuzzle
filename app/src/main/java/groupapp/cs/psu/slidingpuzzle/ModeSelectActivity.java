package groupapp.cs.psu.slidingpuzzle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

public class ModeSelectActivity  extends AppCompatActivity{

    public Button numBtn, mathBtn, backBtn;

    /**
     *This method allows the user to select math mode or number mode game
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modeselection);

        numBtn = (Button) findViewById(R.id.numButton);

        mathBtn = (Button) findViewById(R.id.mathButton);

        mathBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNum1();
            }
            private void openNum1() {

                Intent intent = new Intent(ModeSelectActivity.this, MathModeLoginActivity.class);
                startActivity(intent);
            }
        });

        numBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNum();
            }
            private void openNum() {
                Intent intent = new Intent(ModeSelectActivity.this, NunModeSelectActivity.class);
                startActivity(intent);
            }
        });

    }
}


