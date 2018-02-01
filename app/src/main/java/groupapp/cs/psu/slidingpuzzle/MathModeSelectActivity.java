package groupapp.cs.psu.slidingpuzzle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

         switch (item.getItemId()) {
             case R.id.mathItem1:
                 Intent intent3 = new Intent(MathModeSelectActivity.this, Math1playerActivity.class);
                 this.startActivity(intent3);
                 break;

             case R.id.mathItem2:
                 Intent intent4 = new Intent(MathModeSelectActivity.this, Math2playersActivity.class);
                 this.startActivity(intent4);
                 break;

             case R.id.mathItem3:
                 Intent intent5 = new Intent(MathModeSelectActivity.this, MathCutthroughActivity.class);
                 this.startActivity(intent5);
                 break;
             default:
                 return super.onOptionsItemSelected(item);
         }
         return true;

    }
}
