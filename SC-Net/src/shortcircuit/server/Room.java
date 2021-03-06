package shortcircuit.server;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import shortcircuit.shared.Command;
import shortcircuit.shared.Command.CommandType;
import shortcircuit.shared.Game;
import shortcircuit.shared.Player;

public class Room {
    private Client admin;
    private Hashtable<String, Client> members;
    private ArrayList<Client> clients;
    private String roomName;
    private Game game;

 /* room constructor for users who are not the admin */
    public Room(String roomName) {
	this.members = new Hashtable<String, Client>();
	this.roomName = roomName;
    }
/* room constructor for the admin of a room */ 
    public Room(String roomName, Client admin) {
	this(roomName);
	this.admin = admin;
    }
/* method to add users to a room */
    public void addMember(Client client) {
	this.members.put(client.getUsername(), client);
    }
/* method to get members of a room */
    public Client getMember(String key) {
	return this.members.get(key);
    }
/* method to remove a member from a room */
    public void removeMember(Client client) {
	this.members.remove(client.getUsername());
    }
/* method to broadcast a chat */
    public void broadcastCommand(Command command) {
	broadcastCommand(command, null);
    }

    public void broadcastCommand(Command command, Client client) {
	for (String key : members.keySet()) {
	    if (members.get(key) != client) {
		members.get(key).sendCommand(command);
	    }
	}
    }

    public Hashtable<String, Client> getMemberList() {
	return this.members;
    }
/* getter method for room name */
    public String getRoomName() {
	return roomName;
    }
/* getter method for the admin */
    public Client getAdmin() {
	return admin;
    }

    public boolean isEmpty() {
	return (this.members.size() == 0);
    }
/* method to start the game */
    public boolean startGame(Client client) {
	if (client == admin) {
	    this.game = new Game();
	    this.game.start();
	    return true;
	} else {
	    return false;
	}
    }
/* method to set the sprite and id of each player */
    public void setIds() {
	int i = 0;
	ArrayList<Player> players = new ArrayList<Player>();
	this.clients = new ArrayList<Client>();

	Random random = new Random();
	for (String key : members.keySet()) {
	    Client client = members.get(key);
	    client.setId(i);
	    this.clients.add(client);
	    players.add(new Player(random.nextInt(Game.WIDTH), random.nextInt(Game.HEIGHT), i, game, client.getUsername()));
	    i++;
	}
	this.game.setPlayers(players);
    }
/* method used to run through the game itself */
    public void runCommand(int id, CommandType command) {
	if (this.game != null) {
	    int result = this.game.execute(id, command);
	    if (result == 1) {
		return;
	    }
	    this.broadcastCommand(new Command(command, Integer.toString(id)));
	    if(result == 2){
		notifyTurn(this.game.currentTurn);
	    }
	}
    }
/* notification method for whose turn it is */
    public void notifyTurn(int id) {
	clients.get(id).sendCommand(new Command(CommandType.TURN));
    }
/* getter method for game */
    public Game getGame() {
	return this.game;
    }
/* method to stop the game */
    public void stopGame() {
	this.game = null;
    }
}
