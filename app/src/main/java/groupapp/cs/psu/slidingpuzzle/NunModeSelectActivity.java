package groupapp.cs.psu.slidingpuzzle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class NunModeSelectActivity extends AppCompatActivity {

    public Button button3, button4;

    /**
     * This method allows the user to select single player or AI mode
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nun_mode_select);

        button3 = (Button) findViewById(R.id.spbutton);
        button4 = (Button) findViewById(R.id.aibutton);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open3();
            }
            private void open3() {
                Intent intent = new Intent(NunModeSelectActivity.this, NumberActivity.class);
                startActivity(intent);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open4();
            }
            private void open4() {
                Intent intent = new Intent(NunModeSelectActivity.this, NumberGameAIActivity.class);
                startActivity(intent);
            }
        });

    }
}
