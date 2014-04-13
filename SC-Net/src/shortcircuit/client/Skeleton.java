package shortcircuit.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.DefaultFocusManager;
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

    public Skeleton(ShortCircuitClient client, String game) {
	this.client = client;
	this.game = new Game(game);
	initUI();
    }

    private void initUI() {

	JPanel panel = new Surface();

	KeyEventDispatcher myKeyEventDispatcher = new DefaultFocusManager();
	KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {    
	    @Override
	    public boolean dispatchKeyEvent(KeyEvent e) {
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
		return false;
	    }
	});

	GroupLayout groupLayout = new GroupLayout(getContentPane());
	groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(panel, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE));
	groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(panel, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE));
	getContentPane().setLayout(groupLayout);
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	setLocationRelativeTo(null);
	setSize(100, 100);
	pack();
    }

    class Surface extends JPanel {

	private int[][] map = game.map;
	private ArrayList<Player> players = game.getPlayers();

	private void doDrawing(Graphics g) {
	    Graphics2D g2d = (Graphics2D) g;
	    double width = (double) this.getWidth() / (double) map[0].length;
	    double height = (double) this.getHeight() / (double) map.length;
	    int color;
	    for (int i = 0; i < map[0].length; i++) {
		for (int j = 0; j < map.length; j++) {
		    color = (map[j][i]);
		    g2d.setColor(new Color(color, color, color));
		    g2d.fill(new Rectangle2D.Double((double) i * width, (double) j * height, width, height));
		}
	    }
	    for (int i = 0; i < players.size(); i++) {
		Color pColor = null;
		switch (i) {
		case 0:
		    pColor = Color.RED;
		    break;
		case 1:
		    pColor = Color.BLUE;
		    break;
		case 2:
		    pColor = Color.YELLOW;
		    break;
		case 3:
		    pColor = Color.GREEN;
		    break;
		default:
		    break;
		}
		g2d.setColor(pColor);
		g2d.fill(new Ellipse2D.Double((double) players.get(i).x * width, (double) players.get(i).y * height, width, height));
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