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

    public void sendCommand(Command command) {
	this.thread.sendMessage(command);
    }

    public ShortCircuitServerThread getThread() {
	return thread;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

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
		if (this.thread.getRoom().isEmpty() || this == this.thread.getRoom().getAdmin()) {
		    this.thread.getRoom().broadcastCommand(new Command(Command.CommandType.KICK));
		    ShortCircuitServer.removeRoom(this.thread.getRoom().getRoomName());
		    ShortCircuitServer.getGetLobby().broadcastCommand(new Command(Command.CommandType.ROOMDESTROY, this.thread.getRoom().getRoomName()));
		}
		this.thread.getRoom().removeMember(this);
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
		System.out.println("You need 4 people to start a game");
		sendCommand(new Command(Command.CommandType.FAILURE, "You need 4 people to start a game"));
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
	case ATTACK:
	    this.thread.getRoom().runCommand(id, command.command);
	    break;
	default:
	    ShortCircuitServer.getSim().executeCommand(command);
	    break;
	}
    }

    public int getId() {
	return this.id;
    }
    
    public void setId(int id){
	this.id = id;
    }

    public Player getPlayer() {
	return player;
    }

    public void setPlayer(Player player) {
	this.player = player;
    }
}
