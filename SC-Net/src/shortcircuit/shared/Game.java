package shortcircuit.shared;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import shortcircuit.shared.Command.CommandType;

public class Game {

    /* Parameters for the game */
    public static final int WIDTH = 40;
    public static final int HEIGHT = 15;
    public static final int MAX_PLAYERS = 4;

    public static final char COMA = ',';
    public int seed;
    public int currentTurn = -1;
    public Random rnd;
    public char[][] map; // The map is simply a matrix of Characters
    private ArrayList<Player> players; // List that holds a reference for each
				       // client

    public Game(String content) {
	Scanner scan = new Scanner(content);
	scan.useDelimiter(",");
	seed = scan.nextInt();
	players = new ArrayList<Player>();
	for(int i = 0; i < 4; i++){
	    players.add(new Player(scan.nextInt(), scan.nextInt(), 0, this));
	}	
    }

    private void generateMap() {
	map = new char[HEIGHT][WIDTH]; // Initialize the matrix
	rnd = new Random(seed);
	int mod = 0;
	for (int i = 0; i < WIDTH; i++) {
	    for (int j = 0; j < HEIGHT; j++) {
		if (rnd.nextInt() % 5 == 0 && rnd.nextInt() % 3 == 0) {
		    mod = 1;
		} else {
		    mod = 0;
		}
		map[j][i] = (char) (35 + mod);
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
    
    public void start(){
	this.currentTurn = 0;
    }
    
    public String toString(){
	StringBuilder builder = new StringBuilder();
	builder.append(seed + COMA);
	for(int i = 0; i < 4; i++){
	    builder.append(players.get(i).x + COMA);
	    builder.append(players.get(i).y + COMA);
	}
	return builder.toString();
    }
}
