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

    public Room(String roomName) {
	this.members = new Hashtable<String, Client>();
	this.roomName = roomName;
    }

    public Room(String roomName, Client admin) {
	this(roomName);
	this.admin = admin;
    }

    public void addMember(Client client) {
	this.members.put(client.getUsername(), client);
    }

    public Client getMember(String key) {
	return this.members.get(key);
    }

    public void removeMember(Client client) {
	this.members.remove(client.getUsername());
    }

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

    public String getRoomName() {
	return roomName;
    }

    public Client getAdmin() {
	return admin;
    }

    public boolean isEmpty() {
	return (this.members.size() == 0);
    }

    public boolean startGame(Client client) {
	if (client == admin) {
	    this.game = new Game();
	    this.game.start();
	    return true;
	} else {
	    return false;
	}
    }

    public void setIds() {
	int i = 0;
	ArrayList<Player> players = new ArrayList<Player>();
	this.clients = new ArrayList<Client>();
	
	Random random = new Random();
	for (String key : members.keySet()) {
	    Client client = members.get(key);
	    client.setId(i);
	    this.clients.add(client);
	    players.add(new Player(random.nextInt(Game.WIDTH), random.nextInt(Game.HEIGHT), i, game));
	    i++;
	}
	this.game.setPlayers(players);
    }

    public void runCommand(int id, CommandType command) {
	if(this.game != null && this.game.execute(id, command)){
	    this.broadcastCommand(new Command(command, Integer.toString(id)));
	    notifyTurn(this.game.currentTurn);
	}
    }

    public void notifyTurn(int id) {
	    clients.get(id).sendCommand(new Command(CommandType.TURN));
    }
    
    public Game getGame(){
	return this.game;
    }

    public void stopGame() {
	this.game = null;	
    }
}
