package groupapp.cs.psu.slidingpuzzle.firebase.objects;


// This class helps to store data on firebase
public class PlayerScoreInformation implements Comparable {

    public int singlePlayerScore;

    public String email;

    PlayerScoreInformation(){

    }

    public PlayerScoreInformation(int singlePlayerScore, String email) {
        this.singlePlayerScore = singlePlayerScore;
        this.email = email;
    }
    public int getSinglePlayerScore() {
        return singlePlayerScore;
    }
    public String getEmail() {
        return email;
    }

    @Override
    public int compareTo(Object comparePlayer) {
        int compareScore=((PlayerScoreInformation)comparePlayer).getSinglePlayerScore();
        return compareScore-this.singlePlayerScore;
    }
}
