package shortcircuit.server;

import shortcircuit.shared.Command;
import shortcircuit.shared.Game;
import shortcircuit.shared.Player;
import shortcircuit.shared.Command.CommandType;

public class Client {
    private ShortCircuitServerThread thread;
    private String username;
    private Player player;
    private int id;

    public Client(ShortCircuitServerThread thread, String username) {
	this.thread = thread;
	this.username = username;
    }
/* sends message to the clients */
    public void sendCommand(Command command) {
	this.thread.sendMessage(command);
    }
/* returns the thread for the server */
    public ShortCircuitServerThread getThread() {
	return thread;
    }
/* getter method for username */
    public String getUsername() {
	return username;
    }
/* setter method for user name */
    public void setUsername(String username) {
	this.username = username;
    }

/* Method used to execute all commands including chat, creating a chatroom/gameroom, joining a chatroom,
 * leaving a room, disconnecting from the client, starting the game, stopping the game, as well as all
 * of the in game commands (move up,down,left,right, ranged attack and melee attack).  Also recieves
 * who has won the game itself.   */ 
    public void executeCommand(Command command) {

	switch (command.command) {
	case CHAT:
	    command.message = this.username + ":" + command.message;
	    this.thread.getRoom().broadcastCommand(command);
	    break;
	case CREATE:
	    if (this.thread.getRoom() == ShortCircuitServer.getGetLobby()) {
		this.thread.getRoom().removeMember(this);
		this.thread.setRoom(ShortCircuitServer.createRoom(command.message, this));
		ShortCircuitServer.getGetLobby().broadcastCommand(new Command(Command.CommandType.CREATE, command.message));
		ShortCircuitServer.getGetLobby().broadcastCommand(new Command(Command.CommandType.LEAVE, this.username));
		this.thread.getRoom().addMember(this);
		this.thread.getRoom().broadcastCommand(command);
		sendCommand(new Command(Command.CommandType.JOIN, command.message));
		this.thread.getRoom().broadcastCommand(new Command(Command.CommandType.OTHERJOIN, command.message), this);
	    } else {
		System.out.println("You are already in a room");
		sendCommand(new Command(Command.CommandType.FAILURE, "You are already in a room"));
	    }
	    break;
	case JOIN:
	    if (this.thread.getRoom() == ShortCircuitServer.getGetLobby()) {
		if(ShortCircuitServer.getRoom(command.message).getMemberList().size() >= Game.MAX_PLAYERS){
		    System.out.println("Room is full");
		    sendCommand(new Command(Command.CommandType.FAILURE, "Room is full"));
		}
		this.thread.getRoom().removeMember(this);
		this.thread.getRoom().broadcastCommand(new Command(CommandType.LEAVE, this.username), this);
		this.thread.setRoom(ShortCircuitServer.getRoom(command.message));
		this.thread.getRoom().addMember(this);
		sendCommand(new Command(Command.CommandType.JOIN, command.message));
		this.thread.getRoom().broadcastCommand(new Command(Command.CommandType.OTHERJOIN, this.username), this);
	    } else {
		System.out.println("You are already in a room");
		sendCommand(new Command(Command.CommandType.FAILURE, "You are already in a room"));
	    }
	    break;
	case LEAVE:
	    if (this.thread.getRoom() != ShortCircuitServer.getGetLobby()) {
		command.message = this.username;
		this.thread.getRoom().broadcastCommand(command);
		this.thread.getRoom().removeMember(this);
		if (this.thread.getRoom().isEmpty() || this == this.thread.getRoom().getAdmin()) {
		    this.thread.getRoom().broadcastCommand(new Command(Command.CommandType.KICK));
		    ShortCircuitServer.removeRoom(this.thread.getRoom().getRoomName());
		    ShortCircuitServer.getGetLobby().broadcastCommand(new Command(Command.CommandType.ROOMDESTROY, this.thread.getRoom().getRoomName()));
		}
		this.thread.setRoom(ShortCircuitServer.getGetLobby());
		this.thread.getRoom().addMember(this);
		this.thread.getRoom().broadcastCommand(new Command(Command.CommandType.OTHERJOIN, this.username), this);
		sendCommand(new Command(Command.CommandType.JOINLOBBY));
	    } else {
		System.out.println("You are already in the lobby");
		sendCommand(new Command(Command.CommandType.FAILURE, "You are already in the lobby"));
	    }
	    break;

	case DISCONNECT:
	    command.message = this.username;
	    this.thread.getRoom().removeMember(this);
	    this.thread.stopRunning();
	    this.thread.getRoom().broadcastCommand(command);
	    break;
	case ROOMS:
	    sendCommand(new Command(Command.CommandType.ROOMS, this.thread.generateRoomList()));
	    break;
	case USERS:
	    sendCommand(new Command(Command.CommandType.USERS, this.thread.generateMembersList()));
	    break;
	case START:
	    if (this.thread.getRoom() == ShortCircuitServer.getGetLobby()) {
		System.out.println("You cannot start a game in the lobby");
		sendCommand(new Command(Command.CommandType.FAILURE, "You cannot start a game in the lobby"));
		break;
	    }
	    if (this.thread.getRoom().getMemberList().size() != Game.MAX_PLAYERS) {
		System.out.println("You need " + Game.MAX_PLAYERS + " people to start a game");
		sendCommand(new Command(Command.CommandType.FAILURE, "You need " + Game.MAX_PLAYERS + " people to start a game"));
		break;
	    }
	    if (this.thread.getRoom().startGame(this)) {
		this.thread.getRoom().setIds();
		command.message = this.thread.getRoom().getGame().toString();
		this.thread.getRoom().broadcastCommand(command);
		this.thread.getRoom().notifyTurn(0);
	    }else{
		System.out.println("Only the admin can start a game");
		sendCommand(new Command(Command.CommandType.FAILURE, "Only the admin can start a game"));		
	    }
	    break;
	case STOP:
	    this.thread.getRoom().broadcastCommand(command);
	    this.thread.getRoom().stopGame();
	    break;
	case TURN:
	    break;
	case UP:
	case DOWN:
	case LEFT:
	case RIGHT:
	case MATTACK:
	case RATTACK:
	    this.thread.getRoom().runCommand(id, command.command);
	    int winner = this.thread.getRoom().getGame().getWinner();
	    if(winner != -1){
	    	this.thread.getRoom().broadcastCommand(new Command(Command.CommandType.CHAT, "Player " +
	    				this.thread.getRoom().getGame().getPlayers().get(winner).name + " won"));
	    }
	    break;
	default:
	    ShortCircuitServer.getSim().executeCommand(command);
	    break;
	}
    }
/* getter method for id */
    public int getId() {
	return this.id;
    }
/*setter method for id */    
    public void setId(int id){
	this.id = id;
    }
/* getter method for player */
    public Player getPlayer() {
	return player;
    }
/* setter method for player */
    public void setPlayer(Player player) {
	this.player = player;
    }
    
    public void killPlayer(){
	    this.thread.getRoom().getGame().killPlayer(id);
    }

}
