package groupapp.cs.psu.slidingpuzzle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MathModeLoginActivity extends AppCompatActivity implements View.OnClickListener{


    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_mode_login);

        firebaseAuth = FirebaseAuth.getInstance();

      /*  if(firebaseAuth.getCurrentUser() != null){
            //profile activity
            finish();
            startActivity(new Intent(getApplicationContext(),DisplayGridActivity.class));
        }
*/
        progressDialog = new ProgressDialog(this);
        editTextEmail = (EditText) findViewById(R.id.logUser);
        editTextPassword = (EditText) findViewById(R.id.logPassword);
        buttonSignIn = (Button) findViewById(R.id.signInBut);
        textViewSignup = (TextView) findViewById(R.id.textViewSignIp);

        buttonSignIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);


    }

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this,"Please enter email", Toast.LENGTH_SHORT).show();

            //Stops any further execution
            return;
        }

        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();

            //Stops further execution
            return;
        }

        //if validations are good so far
        //We will show a progress dialogue

        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MathModeLoginActivity.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    //finish();
                    startActivity(new Intent(getApplicationContext(),MathModeSelectActivity.class));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == buttonSignIn){
            userLogin();
        }
        if(view == textViewSignup){
            finish();
            startActivity(new Intent(this, MathModeRegisterActivity.class));
        }
    }
}
