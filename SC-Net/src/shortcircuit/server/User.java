package shortcircuit.server;

public class User {
    private String username;
    private int wins;
    private int losses;
 /* constructor for User, sets username, wins and losses. */   
    public User(String username, int wins, int losses){
	this.username = username;
	this.wins = wins;
	this.losses = losses;
    }
 /* getter method for username */  
    public String getUsername() {
        return username;
    }
 /* setter method for username */   
    public void setUsername(String username) {
        this.username = username;
    }
 /* getter method for wins */   
    public int getWins() {
        return wins;
    }
 /* setter method for wins */
    public void setWins(int wins) {
        this.wins = wins;
    }
 /* getter method for losses */
    public int getLosses() {
        return losses;
    }
 /* setter method for losses */
    public void setLosses(int losses) {
        this.losses = losses;
    }
}
