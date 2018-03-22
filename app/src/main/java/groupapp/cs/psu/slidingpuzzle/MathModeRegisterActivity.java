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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import groupapp.cs.psu.slidingpuzzle.firebase.objects.PlayerScoreInformation;

public class MathModeRegisterActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_mode_register);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        //Initializing views
        buttonRegister = (Button) findViewById(R.id.loginBut);
        editTextEmail = (EditText) findViewById(R.id.logUser);
        editTextPassword = (EditText) findViewById(R.id.logPassword);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        //Changed
        databaseReference = FirebaseDatabase.getInstance().getReference("player");
        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }

    /**
     * This method checks if the email or the password is empty and valid and registers the user
     */
    private void registerUser(){
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }

        //if validations are good so far, display a progress dialog
        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        /**
         * This method registers the user successfully
         */
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                   Toast.makeText(MathModeRegisterActivity.this,"Registered successfully",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                   addPlayer();

                } else{
                    Toast.makeText(MathModeRegisterActivity.this,"Could not register, please try again",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * This method adds the user to firebase
     */
    private void addPlayer(){
        String email = editTextEmail.getText().toString();
        int initialPlayerScore = 0;

        if((!TextUtils.isEmpty(email))){

            PlayerScoreInformation initialScore = new PlayerScoreInformation(initialPlayerScore, email);
            FirebaseUser user = firebaseAuth.getCurrentUser();

            databaseReference.child(user.getUid()).setValue(initialScore);
            finish();
            progressDialog.dismiss();
            startActivity(new Intent(getApplicationContext(),MathModeSelectActivity.class));

        }

    }

    @Override
    public void onClick(View view) {
        if(view == buttonRegister){
            registerUser();
        }
        if(view == textViewSignin){
            //will open login activity
            finish();
            startActivity(new Intent(this, MathModeLoginActivity.class));
        }
    }
}