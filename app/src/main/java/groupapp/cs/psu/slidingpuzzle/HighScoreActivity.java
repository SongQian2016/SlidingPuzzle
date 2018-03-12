package groupapp.cs.psu.slidingpuzzle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import groupapp.cs.psu.slidingpuzzle.firebase.objects.PlayerScoreInformation;

public class HighScoreActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private List<PlayerScoreInformation> playerScores = new ArrayList<>();
    private TextView[] highScore = new TextView[5];
    private LinearLayout highScoreLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Initialize the firebase database instance.
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("player");
        highScoreLayout = (LinearLayout) findViewById(R.id.high_score_cl);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String email = (String) postSnapshot.child("email").getValue();
                    Long score = (Long) postSnapshot.child("singlePlayerScore").getValue();
                    playerScores.add(new PlayerScoreInformation(score.intValue(), email));
                    populateHighScoreList();
                }

                Collections.sort(playerScores);

                for(int i = 0; i < playerScores.size();i++ ){
                    System.out.println("Player Scores:" + playerScores.get(i).getSinglePlayerScore());
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        setContentView(R.layout.activity_high_score);

    }

    private void populateHighScoreList(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        for(int i = 0; i < 5;i++ ){
            System.out.println("Player Scores:" + playerScores.get(i).getSinglePlayerScore());
            highScore[i]=new TextView(this);
            highScore[i].setLayoutParams(params);
            highScore[i].setText(playerScores.get(i).getEmail()+"  : "+playerScores.get(i).getSinglePlayerScore());
            highScoreLayout.addView(highScore[i]);
        }
    }


}
