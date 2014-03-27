package shortcircuit.server;
public class Player {
    private int x; // X position on the map
    private int y; // Y position on the map
    private char symbol; // Symbol to display

    public Player(int x, int y, char symbol) {
	this.x = x;
	this.y = y;
	this.symbol = symbol;
    }

    public void draw() {
	// Set the cursor to the X and Y location
	System.out.print(String.format("%c[%d;%df%c[31m", Game.ESCCODE, this.y, this.x, Game.ESCCODE));
	// Print the character
	System.out.print(symbol);
    }

    public void execute(char command) {
	if (command == 'w') {
	    // If the player is not on the top border or is in front of a rock
	    if (y > 0 && Game.map[y - 1][x] != 36) {
		y--;
	    }
	} else if (command == 'a' ) {
	    // If the player is not on the left border or is in front of a rock
	    if (x > 0 && Game.map[y][x - 1] != 36) {
		x--;
	    }
	} else if (command == 's') {
	    // If the player is not on the bottom or is in front of a rock
	    // border
	    if (y < Game.HEIGHT - 1 && Game.map[y + 1][x] != 36) {
		y++;
	    }
	} else if (command == 'd') {
	    // If the player is not on the right border or is in front of a rock
	    if (x < Game.WIDTH - 1 && Game.map[y][x + 1] != 36) {
		x++;
	    }
	} else {
	    // Ignore everything else
	}
    }
}
