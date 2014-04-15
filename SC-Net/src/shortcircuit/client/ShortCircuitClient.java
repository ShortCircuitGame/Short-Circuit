package shortcircuit.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import shortcircuit.shared.Command;
import shortcircuit.shared.Simulator;

public class ShortCircuitClient extends Thread {

	private Simulator sim;
	private PrintWriter out;
	private ArrayList<ClientEventListener> listeners;
	private String username;
	private String serverAddress;
	public ShortCircuitClient(String hostname) {
		this.sim = new Simulator(0, 0, null);
		this.listeners = new ArrayList<ClientEventListener>();
		
		serverAddress = "localhost";
	}

	public void run() {
		
		int portNumber = 8970;
		try (Socket socket = new Socket(serverAddress, portNumber);
				PrintWriter out = new PrintWriter(socket.getOutputStream(),
						true);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));) {
			String fromServer;

			this.out = out;
			Command command;
			while ((fromServer = in.readLine()) != null) {
				command = new Command(fromServer);
				System.out.println(fromServer);
				notifyListeners(command);

			}
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + serverAddress);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to "
					+ serverAddress);
		}
	}

	public void sendMessage(Command command) {
		this.out.println(command.toString());
	}

	private void notifyListeners(Command command) {
		for (ClientEventListener listener : listeners) {
			listener.commandRecievedEvent(command);
		}
	}

	public void addListener(ClientEventListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public void removeListener(ClientEventListener listener) {
		listeners.remove(listener);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}