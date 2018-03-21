package groupapp.cs.psu.slidingpuzzle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    private ListView highScoreList;
    private  List<PlayerScoreInformation> topPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        // Initialize the firebase database instance.
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("player");
        highScoreLayout = (LinearLayout) findViewById(R.id.high_score_cl);
        highScoreList = (ListView) findViewById(R.id.listDisplayScore);


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String email = (String) postSnapshot.child("email").getValue();
                    Long score = (Long) postSnapshot.child("singlePlayerScore").getValue();
                    playerScores.add(new PlayerScoreInformation(score.intValue(), email));
                }

                Collections.sort(playerScores);
                //populateHighScoreList();
                topPlayers = new ArrayList<PlayerScoreInformation>(playerScores.subList(0, Math.min(playerScores.size(), 5)));
                CustomAdapter customAdapter = new CustomAdapter();
                highScoreList.setAdapter(customAdapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void populateHighScoreList() {

        //if playerScores list contains 5 or more than 5 items, display only top 5
        if (playerScores.size() >= 5) {
            for (int i = 0; i < 5; i++) {
                System.out.println("Player Scores:" + playerScores.get(i).getSinglePlayerScore());
                highScore[i] = new TextView(this);
                //highScore[i].setLayoutParams(params);
                highScore[i].setText(playerScores.get(i).getEmail() + "  : " + playerScores.get(i).getSinglePlayerScore());
                highScoreLayout.addView(highScore[i]);
            }
        } else {
            for (int i = 0; i < playerScores.size(); i++) {
                System.out.println("Player Scores:" + playerScores.get(i).getSinglePlayerScore());
                highScore[i] = new TextView(this);
                //highScore[i].setLayoutParams(params);
                highScore[i].setText(playerScores.get(i).getEmail() + "  : " + playerScores.get(i).getSinglePlayerScore());
                highScoreLayout.addView(highScore[i]);
            }
        }
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return topPlayers.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.highscorelayout,null);
            TextView textView_email = (TextView) convertView.findViewById(R.id.textView_email);
            TextView textView_score = (TextView) convertView.findViewById(R.id.textView_score);
            textView_email.setText(topPlayers.get(position).getEmail());
            textView_score.setText(String.valueOf(topPlayers.get(position).getSinglePlayerScore()));

            return convertView;
        }
    }

}
