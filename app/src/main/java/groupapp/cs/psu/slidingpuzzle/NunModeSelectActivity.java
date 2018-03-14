package groupapp.cs.psu.slidingpuzzle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class NunModeSelectActivity extends AppCompatActivity {

    public Button button1, button2, button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nun_mode_select);

        button1 = (Button) findViewById(R.id.spbutton);
        button2 = (Button) findViewById(R.id.aibutton);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opennum1();
            }

            private void opennum1() {

                Intent intent = new Intent(NunModeSelectActivity.this, NumberGameActivity.class);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opennum2();
            }

            private void opennum2() {

                Intent intent = new Intent(NunModeSelectActivity.this, NumberGameAIActivity.class);
                startActivity(intent);
            }
        });
    }
}
