package shortcircuit.server;

import shortcircuit.shared.Command;

public class Client {
    private ShortCircuitServerThread thread;
    private String username;

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

    public void setUsername(String username){
	this.username = username;
    }
    
    public void executeCommand(Command command) {

	switch (command.command) {
	case CHAT:
	    this.thread.getRoom().broadcastCommand(command);
	    break;
	case CREATE:
	    if (this.thread.getRoom() == ShortCircuitServer.getGetLobby()) {
		this.thread.getRoom().removeMember(this);
		this.thread.setRoom(ShortCircuitServer.createRoom(command.message, this));
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
		this.thread.getRoom().removeMember(this);
		this.thread.setRoom(ShortCircuitServer.getRoom(command.message));
		this.thread.getRoom().addMember(this);
		sendCommand(new Command(Command.CommandType.JOIN, command.message));
		this.thread.getRoom().broadcastCommand(new Command(Command.CommandType.OTHERJOIN, command.message), this);
	    } else {
		System.out.println("You are already in a room");
		sendCommand(new Command(Command.CommandType.FAILURE, "You are already in a room"));
	    }
	    break;
	case LEAVE:
	    if (this.thread.getRoom() != ShortCircuitServer.getGetLobby()) {
		this.thread.getRoom().removeMember(this);
		this.thread.setRoom(ShortCircuitServer.getGetLobby());
		this.thread.getRoom().addMember(this);
		this.thread.getRoom().broadcastCommand(command);
	    } else {
		System.out.println("You are already in the lobby");
		sendCommand(new Command(Command.CommandType.FAILURE, "You are already in the lobby"));
	    }
	    break;
	case DISCONNECT:
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
	    break;
	case STOP:
	    break;
	default:
	    ShortCircuitServer.getSim().executeCommand(command);
	    break;
	}
    }
}
