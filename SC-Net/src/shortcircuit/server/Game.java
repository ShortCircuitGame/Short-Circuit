package shortcircuit.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Random;

public class Game {

	/* Parameters for the game */
	public static final int WIDTH = 40;
	public static final int HEIGHT = 15;
	public static final int PORT_NUMBER = 8888;
	public static final int MAX_PLAYERS = 2;
	
	/* Lets just ignore this */
	public static final char ESCCODE = 0x1B;
	
	public static final Random rnd = new Random();
	public static char[][] map;			//The map is simply a matrix of Characters
	private ArrayList<Player> players;	//List that holds a reference for each client

	public Game() {
		map = new char[HEIGHT][WIDTH];	//Initialize the matrix
		int mod = 0;
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
			    	if(rnd.nextInt() % 5 == 0 && rnd.nextInt() % 3 == 0){
				    mod = 1;			    	    
			    	}else{
			    	    mod = 0;
			    	}   	
				map[j][i] = (char) (35 + mod);
			}
		}

		this.players = new ArrayList<Player>(MAX_PLAYERS);	//Initialize the player list
	}

	public void addPlayer(Player player) {
		this.players.add(player);
	}

	private void draw() {
	    int color = 34;
	    for (int i = 0; i < WIDTH; i++) {		//Iterate for each column
		for (int j = 0; j < HEIGHT; j++) {	//Iterate for each row
		    if(map[j][i] == 35){		//Check if there is grass or a rock
			color = 34;			//Grass is green
		    }else {
			color = 30;			//Rocks are black
		    }
		    //Set the cursos X and Y position
		    System.out.print(String.format("%c[%d;%df%c[42m%c[%dm", ESCCODE, j, i, ESCCODE,ESCCODE, color));
		    //Print the character
		    System.out.print(map[j][i]);
		}
	    }
	}

	public void render() {
		this.draw();				//Draw the map
		for (Player player : this.players) {	//Iterate over each player
			player.draw();			//Draw each player
		}
	}

	public void execute(int id, char command) {
		this.players.get(id).execute(command);	//Send the character to the correct player
		this.render();				//Render everything again with the new game state
	}

	public static void main(String args[]) throws IOException,
			InterruptedException {
		Game game = new Game();	//Create a new instance of Game
		game.render();				//Display the map

		try (ServerSocket serverSocket = new ServerSocket(PORT_NUMBER)) {	//This line will handle creating a socket, binding it and start listening for connections
			for (int i = 0; i < MAX_PLAYERS; i++) {				//Accept up to MAX_PLAYERS number of players
				new ShortCircuitServerThread(serverSocket.accept()).start();	//Create a thread and pass the new socket, an ID and a reference to the game instance
				game.players.add(new Player(rnd.nextInt(WIDTH), rnd		//Create a new Player instance and add it to the list
						.nextInt(HEIGHT), 'X'));
			}
			System.out.println("All players connected, starting game");
		} catch (IOException e) {
			System.err.println("Could not listen on port " + PORT_NUMBER);
			System.exit(-1);
		}
	}
}
