package shortcircuit.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;

import shortcircuit.shared.Command;
import shortcircuit.shared.Command.CommandType;
import shortcircuit.shared.Game;
import shortcircuit.shared.Player;

public class Skeleton extends JFrame {

    private ShortCircuitClient client;
    private Game game;

    private BufferedImage dirt;
    private BufferedImage grass;
    private BufferedImage mountain;
    private BufferedImage rock;
    private BufferedImage water;
    
    private BufferedImage bio;
    private BufferedImage mecha;
    private BufferedImage protoss;
    
    private int tileWidth;
    private int tileHeight;
    
    public Skeleton(ShortCircuitClient client, String game) {
	this.client = client;
	this.game = new Game(game);
	initUI();
	
	try {
	    dirt = ImageIO.read(new File("resources/img/terrian/dirt.png"));
	    grass = ImageIO.read(new File("resources/img/terrian/grass.png"));
	    mountain = ImageIO.read(new File("resources/img/terrian/mountains.png"));
	    rock = ImageIO.read(new File("resources/img/terrian/rock.png"));
	    water = ImageIO.read(new File("resources/img/terrian/water.png"));
	    
	    bio = ImageIO.read(new File("resources/img/actors/bio.png"));
	    mecha = ImageIO.read(new File("resources/img/actors/mecha.png"));
	    protoss = ImageIO.read(new File("resources/img/actors/protoss.png"));
	    
	    this.tileHeight = dirt.getHeight();
	    this.tileWidth = dirt.getWidth();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void initUI() {

	JPanel panel = new Surface();

	panel.addKeyListener(new KeyListener() {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
    		switch (e.getKeyCode()) {
    		case KeyEvent.VK_LEFT:
    		    client.sendMessage(new Command(CommandType.LEFT));
    		    break;
    		case KeyEvent.VK_RIGHT:
    		    client.sendMessage(new Command(CommandType.RIGHT));
    		    break;
    		case KeyEvent.VK_UP:
    		    client.sendMessage(new Command(CommandType.UP));
    		    break;
    		case KeyEvent.VK_DOWN:
    		    client.sendMessage(new Command(CommandType.DOWN));
    		    break;
    		default:
    		    break;
    		}

        }
    });

	
	panel.setFocusable(true);
    panel.requestFocusInWindow();
    
	GroupLayout groupLayout = new GroupLayout(getContentPane());
	groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(panel, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE));
	groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(panel, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE));
	getContentPane().setLayout(groupLayout);
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	setLocationRelativeTo(null);
	setSize(100, 100);
	
	this.setTitle(this.client.getUsername());
	
	pack();
    }

    class Surface extends JPanel {

	private int[][] map = game.map;
	private ArrayList<Player> players = game.getPlayers();

	private void doDrawing(Graphics g) {
	    Graphics2D g2d = (Graphics2D) g;
	    int width = this.getWidth() / map[0].length;
	    int height = this.getHeight() / map.length;
	    int color;
	    for (int i = 0; i < map[0].length; i++) {
		for (int j = 0; j < map.length; j++) {
		    switch (map[j][i]) {
		    case 0:
			g2d.drawImage(water, i * width, j * height, i * width + width, j * height + height, 0, 0, tileWidth, tileHeight, null);
			break;
		    case 1:
			g2d.drawImage(dirt, i * width, j * height, i * width + width, j * height + height, 0, 0, tileWidth, tileHeight, null);
			break;
		    case 2:
			g2d.drawImage(grass, i * width, j * height, i * width + width, j * height + height, 0, 0, tileWidth, tileHeight, null);
			break;
		    case 3:
			g2d.drawImage(mountain, i * width, j * height, i * width + width, j * height + height, 0, 0, tileWidth, tileHeight, null);
			break;
		    case 4:
			g2d.drawImage(rock, i * width, j * height, i * width + width, j * height + height, 0, 0, tileWidth, tileHeight, null);
			break;
		    }
		}
	    }
	    for (int i = 0; i < players.size(); i++) {
		Color pColor = null;
		switch (i) {
		case 0:
		    g2d.drawImage(bio, players.get(i).x * width + 2, players.get(i).y * height + 2, players.get(i).x * width + width - 2, players.get(i).y * height + height - 2, 0, 0, tileWidth, tileHeight, null);
		    break;
		case 1:
		    g2d.drawImage(mecha, players.get(i).x * width + 2, players.get(i).y * height + 2, players.get(i).x * width + width - 2, players.get(i).y * height + height - 2, 0, 0, tileWidth, tileHeight, null);
		    break;
		case 2:
		    g2d.drawImage(protoss, players.get(i).x * width + 2, players.get(i).y * height + 2, players.get(i).x * width + width - 2, players.get(i).y * height + height - 2, 0, 0, tileWidth, tileHeight, null);
		    break;
		default:
		    break;
		}
	    }
	}

	@Override
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    doDrawing(g);
	}
    }
    
    public Game getGame(){
	return this.game;
    }
}