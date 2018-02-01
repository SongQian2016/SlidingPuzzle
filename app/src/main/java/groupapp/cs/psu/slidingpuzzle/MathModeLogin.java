package groupapp.cs.psu.slidingpuzzle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

public class MathModeLogin extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_mode_login);

        Button login = (Button) findViewById(R.id.loginBut);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNumGame();
            }

            private void openNumGame() {
                Intent intent = new Intent(MathModeLogin.this, MathModeSelectActivity.class);
                EditText username= (EditText) findViewById(R.id.logUser);
                String message = username.getText().toString();
                intent.putExtra(EXTRA_MESSAGE, message);

                startActivity(intent);
            }
        });
    }
}
