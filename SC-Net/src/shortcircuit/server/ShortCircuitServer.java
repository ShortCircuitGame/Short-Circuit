package shortcircuit.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Hashtable;

import shortcircuit.shared.Simulator;

public class ShortCircuitServer {

	private static Authenticator authenticator;
	private static Room lobby;
	private static Hashtable<String, Room> roomList;
	private static boolean listening;
	private static Simulator sim;
/* main method to run the server itself, sets port number, ip and binds the server socket  and listens */
	public static void main(String[] args) throws IOException {

		int portNumber = 8970;
		ShortCircuitServer.listening = true;

		ShortCircuitServer.lobby = new Room("Lobby");
		ShortCircuitServer.roomList = new Hashtable<String, Room>();

		ShortCircuitServer.sim = new Simulator(0, 0, null);
		ShortCircuitServer.authenticator = new Authenticator();
		ShortCircuitServer.authenticator.create();

		try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
			ShortCircuitServerThread incomingClient;
			while (listening) {
				incomingClient = new ShortCircuitServerThread(
						serverSocket.accept());
				incomingClient.start();

			}
		} catch (IOException e) {
			System.err.println("Could not listen on port " + portNumber);
			System.exit(-1);
		}
	}
/* getter method for lobby */
	public static Room getGetLobby() {
		return lobby;
	}
/* hashtable for room */
	public static Hashtable<String, Room> getRoomList() {
		return roomList;
	}
/* getter method for room */
	public static Room getRoom(String key) {
		return roomList.get(key);
	}
/* getter method for authenticator */
	public static Authenticator getAuthenticator() {
		return authenticator;
	}
/* static room creator */
	public static Room createRoom(String roomName, Client admin) {
		Room room = new Room(roomName, admin);
		roomList.put(roomName, room);
		return room;
	}
/* static remove room method */	
	public static void removeRoom(String roomName) {
		roomList.remove(roomName);
	}
/* get simulator method */
	public static Simulator getSim() {
		return sim;
	}
}