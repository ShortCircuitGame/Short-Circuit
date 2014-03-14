package shortcircuit.server;

import java.util.Hashtable;

import shortcircuit.shared.Command;

public class Room {
    private Client admin;
    private Hashtable<String, Client> members;
    private String roomName;

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
		members.get(key).sendCommand(new Command(command.command, members.get(key).getUsername() + ":" + command.message));
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

}
