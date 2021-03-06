package shortcircuit.shared;

import shortcircuit.shared.Command.CommandType;


public class Player {
	public static final int MAX_HEALTH = 10;
	public static final int MAX_STAMINA = 10;
	
    public int x; // X position on the map
    public int y; // Y position on the map
    private int id;
    private int direction;
    public String name;
    public  int stamina = MAX_STAMINA;
    public int health = MAX_HEALTH;
    public boolean alive = true;
    
    private Game game;
  
    /* player constructor, it retrieves their x and y coordinates, their id, name and game that they are in */
    public Player(int x, int y, int id, Game game, String name) {
	this.x = x;
	this.y = y;
	this.id = id;
	this.game = game;
	this.name = name;
    }

    /* Method used to execute player commands. Each command has separate checks for what is done 
     * if that key is pressed, for example, if up is pressed the stamina of the player will decrement by one.
     * This method also contains the attack commands which decrement health (ranged decrements health by 1 and 
     * melee decrements health by 3) which is the determining factor upon who wins the game based on who is the last
     * to live.  There are also special cases for movement in which a player cannot move through rock terrain, 
     * and a player takes twice as much stamina to move through water.  After the user has used up all their
     * stamina it ends their turn and resets their stamina to max for the next turn. */
    public boolean execute(CommandType command) {
	System.out.println(id + " did action " + command.toString());
	if (command == CommandType.UP) {
	    if (y > 0 && this.game.map[y - 1][x] <= 3) {
			if(this.game.map[y - 1][x] == 0){
				stamina--;
			}
			y--;
	    }
	} else if (command == CommandType.LEFT ) {
	    if (x > 0 && this.game.map[y][x - 1] <= 3) {
			if(this.game.map[y][x - 1] == 0){
				stamina--;
			}
		x--;
	    }
	} else if (command == CommandType.DOWN) {
	    if (y < Game.HEIGHT - 1 && this.game.map[y + 1][x] <= 3) {
			if(this.game.map[y + 1][x] == 0){
				stamina--;
			}
		y++;
	    }
	} else if (command == CommandType.RIGHT) {
	    if (x < Game.WIDTH - 1 && this.game.map[y][x + 1] <= 3) {
			if(this.game.map[y][x + 1] == 0){
				stamina--;
			}
			x++;
	    }
	} else if (command == CommandType.MATTACK) {
	    for(int i = 0; i < this.game.getPlayers().size(); i++)
	    {
	    	Player player = this.game.getPlayers().get(i);
	    	
	    	if(player == this)
	    		continue;
	    	
	    	if(Math.abs(this.x - player.x) < 2 && Math.abs(this.y - player.y) < 2 
	    			&& (this.x ==  player.x  || this.y == player.y))
	    	{
	    		
	    		  if(player.health > 0)
	  		    {
	  		    	player.health -= 3;	
	  		    }
	  	    
	    	}
	    }	    
	}else if (command == CommandType.RATTACK){
	    for(int i = 0; i < this.game.getPlayers().size(); i++)
	    {
	    	Player player = this.game.getPlayers().get(i);
	    	if(player == this)
	    		continue;
	    	
	    	if(Math.abs(this.x - player.x) < 5 && Math.abs(this.y - player.y) < 5 
	    			&& (this.x ==  player.x  || this.y == player.y))
	    	{
	    		if(player.health > 0)
	  		    {
	  		    	player.health --;	
	  		    }
	  	    
	    	}
	    }
    }

	if(this.stamina > 1){
	    this.stamina--;
	    return false;
	}else{
	    this.stamina = MAX_STAMINA;
	    return true;
	}
    }
}
