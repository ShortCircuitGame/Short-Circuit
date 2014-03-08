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

		try {

			this.out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			String inputLine, outputLine;
			while (this.isRunning && (inputLine = in.readLine()) != null) {
				Command command = new Command(inputLine);
				outputLine = client.executeCommand(command).toString();
				out.println(outputLine);
			}
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String message) {
		out.println(message);
	}

	public String generateRoomList() {
		StringBuilder builder = new StringBuilder();
		for (String key : ShortCircuitServer.getRoomList().keySet()) {
			builder.append(ShortCircuitServer.getRoom(key).getRoomName());
		}
		return builder.toString();
	}

	public String generateMembersList() {
		StringBuilder builder = new StringBuilder();
		for (String key : this.room.getMemberList().keySet()) {
			builder.append(this.room.getMember(key));
		}
		return builder.toString();
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public void stopRunning(){
		this.isRunning = false;
	}
}