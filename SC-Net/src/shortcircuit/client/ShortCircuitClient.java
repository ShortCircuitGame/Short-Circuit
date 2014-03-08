package shortcircuit.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import shortcircuit.server.Command;
import shortcircuit.shared.Simulator;

public class ShortCircuitClient extends Thread {

    private static Simulator sim;
    private PrintWriter out;

    public ShortCircuitClient() {
	ShortCircuitClient.sim = new Simulator(0, 0, null);

    }

    public void run() {
	String hostName = "localhost";
	int portNumber = 8970;

	try (Socket socket = new Socket(hostName, portNumber);
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
	    String fromServer;

	    this.out = out;
	    while ((fromServer = in.readLine()) != null) {
		System.out.println(fromServer);
	    }
	} catch (UnknownHostException e) {
	    System.err.println("Don't know about host " + hostName);
	    System.exit(1);
	} catch (IOException e) {
	    System.err.println("Couldn't get I/O for the connection to " + hostName);
	    System.exit(1);
	}
    }

    public void sendMessage(Command command) {
	this.out.println(command.toString());
    }

    public static void main(String[] args) throws IOException {
	BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	String input;
	ShortCircuitClient client = new ShortCircuitClient();
	client.start();
	while ((input = stdIn.readLine()) != null) {
	    client.sendMessage(new Command(input));
	}
	stdIn.close();
    }
}