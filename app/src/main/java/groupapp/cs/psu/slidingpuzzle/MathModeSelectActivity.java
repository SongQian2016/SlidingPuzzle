package groupapp.cs.psu.slidingpuzzle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

public class MathModeSelectActivity extends AppCompatActivity {

    public Button button1, button2, button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_mode_select);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MathModeLogin.EXTRA_MESSAGE);

        TextView textView = findViewById(R.id.userGreeting);
        textView.setText("Welcome " + message +"!");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.play_options, menu);
        return true;
    }

}
