package shortcircuit.shared;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import shortcircuit.shared.Command.CommandType;

public class Game {

    /* Parameters for the game */
    public static final int WIDTH = 160;
    public static final int HEIGHT = 90;
    public static final int MAX_PLAYERS = 2;

    public static final String COMA = ",";
    public int seed;
    public int currentTurn = -1;
    public Random rnd;
    public int[][] map; // The map is simply a matrix of Characters
    private ArrayList<Player> players; // List that holds a reference for each
				       // client    
    public Game() {
	seed = 100;
	players = new ArrayList<Player>();
	generateMap();
    }
    
    public Game(String content) {
	Scanner scan = new Scanner(content);
	scan.useDelimiter(",");
	seed = scan.nextInt();
	players = new ArrayList<Player>();
	for(int i = 0; i < MAX_PLAYERS; i++){
	    players.add(new Player(scan.nextInt(), scan.nextInt(), 0, this));
	}
	generateMap();
    }

    private void generateMap() {
	map = new int[HEIGHT][WIDTH]; // Initialize the matrix
	rnd = new Random(seed);
	int mod = 0;
	for (int i = 0; i < WIDTH; i++) {
	    for (int j = 0; j < HEIGHT; j++) {
		map[j][i] = (int) (rnd.nextInt(255));
	    }
	}
    }

    public boolean execute(int id, CommandType command) {
	if (id == currentTurn) {
	    return false;
	} else {
	    this.players.get(id).execute(command);
	    nextTurn();
	    return true;
	}
    }

    private void nextTurn() {
	if (currentTurn >= MAX_PLAYERS - 1) {
	    currentTurn = 0;
	} else {
	    currentTurn++;
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
	}
	return builder.toString();
    }
}
