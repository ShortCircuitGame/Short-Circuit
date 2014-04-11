package shortcircuit.shared;

import shortcircuit.shared.Command.CommandType;


public class Player {
    public int x; // X position on the map
    public int y; // Y position on the map
    private int id;
    private int direction;
    
    private Game game;
    
    public Player(int x, int y, int id, Game game) {
	this.x = x;
	this.y = y;
	this.id = id;
	this.game = game;
    }

    public void execute(CommandType command) {
	System.out.println(id + " did action " + command.toString());
	if (command == CommandType.UP) {
	    if (y > 0 && this.game.map[y - 1][x] != 36) {
		y--;
	    }
	} else if (command == CommandType.LEFT ) {
	    if (x > 0 && this.game.map[y][x - 1] != 36) {
		x--;
	    }
	} else if (command == CommandType.DOWN) {
	    if (y < Game.HEIGHT - 1 && this.game.map[y + 1][x] != 36) {
		y++;
	    }
	} else if (command == CommandType.RIGHT) {
	    if (x < Game.WIDTH - 1 && this.game.map[y][x + 1] != 36) {
		x++;
	    }
	} else if (command == CommandType.ATTACK) {

	}
    }
}
