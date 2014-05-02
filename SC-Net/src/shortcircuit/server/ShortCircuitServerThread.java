package shortcircuit.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import shortcircuit.shared.Command;
import shortcircuit.shared.Command.CommandType;

public class ShortCircuitServerThread extends Thread {
	private Socket socket;
	private Client client;
	private Room room;

	private boolean isRunning;

	private PrintWriter out;

	public ShortCircuitServerThread(Socket socket) {
		super("MultiServerThread");
		this.socket = socket;
	}

	public void run() {
		this.isRunning = true;
		System.out.println("New client thread started");
		try {

			this.out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			String inputLine;

			Command initial = new Command(in.readLine());
			Command username = new Command(in.readLine());
			Command password = new Command(in.readLine());

			if (username.command == Command.CommandType.USERNAME
					&& password.command == CommandType.PASSWORD) {
				boolean allowed;
				if (initial.command == Command.CommandType.SIGNUP) {
					allowed = ShortCircuitServer.getAuthenticator().addUser(
							username.message, password.message);
				} else if (initial.command == Command.CommandType.SIGNIN) {
					allowed = ShortCircuitServer.getAuthenticator().authorize(
							username.message, password.message);
				} else {
					allowed = false;
				}
				if (allowed) {
					this.client = new Client(this, username.message);
					this.setRoom(ShortCircuitServer.getGetLobby());
					this.getRoom().addMember(this.client);

					this.client.setUsername(username.message);
					this.getRoom().broadcastCommand(new Command(CommandType.OTHERJOIN, this.client.getUsername()), this.client);
					sendMessage(new Command(Command.CommandType.SUCCESS));
					while (this.isRunning
							&& (inputLine = in.readLine()) != null) {
						Command command = new Command(inputLine);
						client.executeCommand(command);
					}
				} else {
					System.out.println("Not authorized");
					sendMessage(new Command(Command.CommandType.FAILURE,
							"Not authorized"));
				}
			} else {
				System.out.println("Authentication information not provided");
				sendMessage(new Command(Command.CommandType.FAILURE,
						"Authentication information not provided"));
			}
			System.out.println("Client thread stopping");
			this.getRoom().removeMember(this.client);
			if(this.getRoom().isEmpty()){
				ShortCircuitServer.removeRoom(this.getRoom().getRoomName());
				ShortCircuitServer.getGetLobby().broadcastCommand(new Command(Command.CommandType.ROOMDESTROY, this.getRoom().getRoomName()));
			}else{
				this.getRoom().broadcastCommand(new Command(CommandType.DISCONNECT, this.client.getUsername()));
			}
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
		Object[] keys = ShortCircuitServer.getRoomList().keySet().toArray();
		int size = ShortCircuitServer.getRoomList().keySet().size();
		for (int i = 0; i < size; i++) {
			builder.append(ShortCircuitServer.getRoom((String) keys[i])
					.getRoomName());
			if (i != size - 1) {
				builder.append(",");
			}
		}
		return builder.toString();
	}

	public String generateMembersList() {
		StringBuilder builder = new StringBuilder();
		Object[] keys = this.room.getMemberList().keySet().toArray();
		int size = this.room.getMemberList().keySet().size();
		for (int i = 0; i < size; i++) {
			builder.append(this.room.getMember((String) keys[i]).getUsername());
			if (i != size - 1) {
				builder.append(",");
			}
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