package groupapp.cs.psu.slidingpuzzle.firebase.objects;

public class PlayerScoreInformation implements Comparable {

    public int singlePlayerScore;
    public String email;

    /**
     * This method stores the player email and score on firebase
     * @param singlePlayerScore
     * @param email
     */
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

    /**
     * This method compares the individual player scores.
     * Displays themn in sorted order.
     * Also used to identify user for a new high score
     * @param comparePlayer
     * @return
     */
    @Override
    public int compareTo(Object comparePlayer) {
        int compareScore=((PlayerScoreInformation)comparePlayer).getSinglePlayerScore();
        return compareScore-this.singlePlayerScore;
    }
}
