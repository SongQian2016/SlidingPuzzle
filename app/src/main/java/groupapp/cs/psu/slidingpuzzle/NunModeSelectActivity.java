package groupapp.cs.psu.slidingpuzzle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class NunModeSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nun_mode_select);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            MenuInflater menuInflater = getMenuInflater();

            menuInflater.inflate(R.menu.menu_options, menu);
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.item1:
                    Intent intent1 = new Intent(NunModeSelectActivity.this, NumberGameActivity.class);
                    startActivity(intent1);
                    break;

                case R.id.item2:
                    Intent intent2 = new Intent(NunModeSelectActivity.this, NumberGameAIActivity.class);
                    startActivity(intent2);
                    break;

                default:
                    return super.onOptionsItemSelected(item);

            }
            return true;
    }

}


