package shortcircuit.server;

public class User {
    private String username;
    private int wins;
    private int losses;
    
    public User(String username, int wins, int losses){
	this.username = username;
	this.wins = wins;
	this.losses = losses;
    }
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getWins() {
        return wins;
    }
    public void setWins(int wins) {
        this.wins = wins;
    }
    public int getLosses() {
        return losses;
    }
    public void setLosses(int losses) {
        this.losses = losses;
    }
}
