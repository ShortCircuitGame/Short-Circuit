package shortcircuit.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ShortCircuitServerThread extends Thread {
    private Socket socket;
    private Client client;
    private Room room;

    private boolean isRunning;

    private PrintWriter out;

    public ShortCircuitServerThread(Socket socket) {
	super("KKMultiServerThread");
	this.socket = socket;
	this.client = new Client(this);
    }

    public void run() {
	this.setRoom(ShortCircuitServer.getGetLobby());
	this.getRoom().addMember(this.client);
	this.isRunning = true;
	System.out.println("New client thread started");
	try {

	    this.out = new PrintWriter(socket.getOutputStream(), true);
	    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

	    String inputLine;
	    while (this.isRunning && (inputLine = in.readLine()) != null) {
		Command command = new Command(inputLine);
		client.executeCommand(command);
	    }
	    System.out.println("Client thread stopping");
	    socket.close();
	    this.out.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void sendMessage(Command command) {
	out.println(command.toString());
    }

    public String generateRoomList() {
	StringBuilder builder = new StringBuilder();
	for (String key : ShortCircuitServer.getRoomList().keySet()) {
	    builder.append(ShortCircuitServer.getRoom(key).getRoomName() + ",");
	}
	return builder.toString();
    }

    public String generateMembersList() {
	StringBuilder builder = new StringBuilder();
	for (String key : this.room.getMemberList().keySet()) {
	    builder.append(this.room.getMember(key).getUsername() + ",");
	}
	return builder.toString();
    }

    public Room getRoom() {
	return room;
    }

    public void setRoom(Room room) {
	this.room = room;
    }

    public void stopRunning() {
	this.isRunning = false;
    }
}