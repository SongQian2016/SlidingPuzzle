package groupapp.cs.psu.slidingpuzzle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public Button numBtn, mathBtn,gridBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numBtn = (Button) findViewById(R.id.numButton);

        mathBtn = (Button) findViewById(R.id.mathButton);

        numBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNumGame();
            }

            private void openNumGame() {
                Intent intent = new Intent(MainActivity.this, NunModeSelectActivity.class);
                startActivity(intent);
            }
        });
        mathBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMathGameSelection();
            }

            private void openMathGameSelection() {
                Intent intent = new Intent(MainActivity.this, MathModeRegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
