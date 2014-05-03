package shortcircuit.shared;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import shortcircuit.shared.Command.CommandType;

public class Game {

    /* Parameters for the game */
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    public static final int MAX_PLAYERS = 2;

    public static final String COMA = ",";
    public int seed;
    public int currentTurn = -1;
    public Random rnd;
    public int[][] map; // The map is simply a matrix of Characters
    private ArrayList<Player> players; // List that holds a reference for each
				       // client    
    public Game() {
    rnd = new Random();
	seed = rnd.nextInt();
	players = new ArrayList<Player>();
	generateMap();
    }
    
    public Game(String content) {
	Scanner scan = new Scanner(content);
	scan.useDelimiter(",");
	seed = scan.nextInt();
	players = new ArrayList<Player>();
	for(int i = 0; i < MAX_PLAYERS; i++){
	    players.add(new Player(scan.nextInt(), scan.nextInt(), 0, this, scan.next()));
	}
	generateMap();
    }

    private void generateMap() {
	map = new int[HEIGHT][WIDTH]; // Initialize the matrix
	rnd = new Random(seed);
	int mod = 0;
	for (int i = 0; i < WIDTH; i++) {
	    for (int j = 0; j < HEIGHT; j++) {
		map[j][i] = (int) (rnd.nextInt(5));
	    }
	}
    }

    public int execute(int id, CommandType command) {
	if (id != currentTurn) {
	    System.out.println(id + ": Illegal action");
	    return 1;
	} else {
		boolean action = this.players.get(id).execute(command);
		for (int i=0; i < this.players.size(); i++){
			if(this.players.get(i).health <= 0){
				this.players.get(i).alive = false;
			}
		}
	    if(action){
		System.out.println(id + ": Player turn end");
		nextTurn();
		return 2;
	    }
	    System.out.println(id + ": Player action");
	    return 3;
	}	
    }

    private void nextTurn() {
	if (currentTurn >= players.size() - 1) {
	    currentTurn = 0;
	} else {
	    currentTurn++;
	}
	
	int deaths = 0;
	for(int i = 0; i < players.size(); i++){
		if(!players.get(i).alive){
			deaths++;
		}
	}
	if(deaths == players.size()){
		return;
	}else if(!players.get(currentTurn).alive){
		nextTurn();
	}
    }
    
    public int getWinner(){
    	int alive = 0;
    	int winnerIndex = -1;
    	for(int i = 0; i < players.size(); i++){
    		if(players.get(i).alive){
    			alive++;
    			winnerIndex = i;
    		}
    	}
    	if(alive == 1){
    		return winnerIndex;
    	}else{
    		return -1;
    	}
    }
    
    public void setPlayers(ArrayList<Player> players){
	this.players = players;
    }
    
    public ArrayList<Player> getPlayers(){
	return this.players;
    }
    
    public void start(){
	this.currentTurn = 0;
    }
    
    public String toString(){
	StringBuilder builder = new StringBuilder();
	builder.append(seed + COMA);
	for(int i = 0; i < MAX_PLAYERS; i++){
	    builder.append(players.get(i).x + COMA);
	    builder.append(players.get(i).y + COMA);
	    builder.append(players.get(i).name + COMA);
	}
	return builder.toString();
    }
}
