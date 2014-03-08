package shortcircuit.shared;

import java.util.ArrayList;

import shortcircuit.server.Client;

public class Simulator {

    private int width, height;
    private Tile[][] map;

    private ArrayList<Client> clients;

    public Simulator(int width, int height, ArrayList<Client> clients) {
	this.width = width;
	this.height = height;
	this.clients = clients;
    }

    public Command executeCommand(Command command) {
	return null;
    }
}
