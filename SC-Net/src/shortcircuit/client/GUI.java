package shortcircuit.client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import shortcircuit.shared.Command;

public class GUI extends JFrame implements ClientEventListener {

	private JPanel contentPane;
	private JTextField roomNameInput;
	private JTextField textInput;
	private ShortCircuitClient client;
	private JTextArea textArea;
	private JList userList;
	private DefaultListModel userModel;
	private JList roomList;
	private DefaultListModel roomModel;
	private JToggleButton createRoomButton;
	private JMenu mnRace;
	private JRadioButtonMenuItem rdbtnmntmBio;
	private JRadioButtonMenuItem rdbtnmntmMecha;
	private JRadioButtonMenuItem rdbtnmntmProtoss;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JMenu mnMap;
	private JRadioButtonMenuItem rdbtnmntmTest;
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private JToggleButton startButton;	
	private Skeleton window;
	
	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 */
	public GUI(final ShortCircuitClient client) throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 516, 358);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnProgram = new JMenu("Program");
		menuBar.add(mnProgram);

		JMenuItem mntmDisconnect = new JMenuItem("Disconnect");
		mntmDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				client.sendMessage(new Command(Command.CommandType.DISCONNECT));
				dispose();
			}
		});
		mnProgram.add(mntmDisconnect);
		
		mnRace = new JMenu("Race");
		menuBar.add(mnRace);
		
		rdbtnmntmBio = new JRadioButtonMenuItem("Bio");
		rdbtnmntmBio.setSelected(true);
		buttonGroup.add(rdbtnmntmBio);
		mnRace.add(rdbtnmntmBio);
		
		rdbtnmntmMecha = new JRadioButtonMenuItem("Mecha");
		buttonGroup.add(rdbtnmntmMecha);
		mnRace.add(rdbtnmntmMecha);
		
		rdbtnmntmProtoss = new JRadioButtonMenuItem("Protoss");
		buttonGroup.add(rdbtnmntmProtoss);
		mnRace.add(rdbtnmntmProtoss);
		
		mnMap = new JMenu("Map");
		menuBar.add(mnMap);
		
		rdbtnmntmTest = new JRadioButtonMenuItem("Test");
		rdbtnmntmTest.setSelected(true);
		buttonGroup_1.add(rdbtnmntmTest);
		mnMap.add(rdbtnmntmTest);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		roomModel = new DefaultListModel();
		roomList = new JList(roomModel);
		roomList.setBorder(new LineBorder(new Color(0, 0, 0)));
		roomList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList) evt.getSource();
				if (evt.getClickCount() == 2) {
					client.sendMessage(new Command(Command.CommandType.JOIN,
							(String) roomList.getSelectedValue()));
				}
			}
		});

		createRoomButton = new JToggleButton("Create Room");
		createRoomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (createRoomButton.isSelected()) {
					client.sendMessage(new Command(Command.CommandType.CREATE,
							roomNameInput.getText()));
					createRoomButton.setSelected(false);
				} else {
					client.sendMessage(new Command(Command.CommandType.LEAVE));
					createRoomButton.setSelected(true);
				}
			}
		});

		roomNameInput = new JTextField();
		roomNameInput.setColumns(10);

		userModel = new DefaultListModel();
		userList = new JList(userModel);
		userList.setBorder(new LineBorder(new Color(0, 0, 0)));

		startButton = new JToggleButton("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			    if(startButton.isSelected()){
				client.sendMessage(new Command(Command.CommandType.START));	
				startButton.setSelected(false);
			    }else{
				client.sendMessage(new Command(Command.CommandType.STOP));
				startButton.setSelected(true);
			    }
			}
		});

		textArea = new JTextArea();
		textArea.setBackground(Color.LIGHT_GRAY);

		textInput = new JTextField();
		textInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String message = textInput.getText();
				if (!message.isEmpty()) {
					client.sendMessage(new Command(Command.CommandType.CHAT,
							message));
					textInput.setText("");
				}			    
			}
		});
		textInput.setColumns(10);

		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = textInput.getText();
				if (!message.isEmpty()) {
					client.sendMessage(new Command(Command.CommandType.CHAT,
							message));
					textInput.setText("");
				}
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(roomList, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(createRoomButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(roomNameInput, Alignment.LEADING)))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(textInput, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(sendButton))
						.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(startButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(userList, GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(roomList, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(roomNameInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(userList, GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(createRoomButton)
						.addComponent(startButton)
						.addComponent(textInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(sendButton)))
		);
		contentPane.setLayout(gl_contentPane);

		this.client = client;
		this.client.addListener(this);

		client.sendMessage(new Command(Command.CommandType.ROOMS));
		client.sendMessage(new Command(Command.CommandType.USERS));
		
		this.setTitle(this.client.getUsername());
	}

	@Override
	public void commandRecievedEvent(final Command command) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				switch (command.command) {
				case CHAT:
					textArea.append(command.message + "\n");
					break;
				case CREATE:
					roomModel.addElement(command.message);
					break;
				case JOIN:
					userModel.clear();
					roomModel.clear();
					textArea.setText("");
					roomNameInput.setText("");
					createRoomButton.setSelected(true);
					createRoomButton.setText("Leave");
					client.sendMessage(new Command(Command.CommandType.USERS));
					break;
				case OTHERJOIN:
					userModel.addElement(command.message);
					break;
				case LEAVE:
					userModel.removeElement(command.message);
					break;
				case JOINLOBBY:
					userModel.clear();
					createRoomButton.setSelected(false);
					textArea.setText("");
					createRoomButton.setText("Create Room");
					client.sendMessage(new Command(Command.CommandType.USERS));
					client.sendMessage(new Command(Command.CommandType.ROOMS));
					break;
				case KICK:
					userModel.clear();
					textArea.setText("");
					client.sendMessage(new Command(Command.CommandType.LEAVE));
					createRoomButton.setSelected(true);
					break;
				case DISCONNECT:
				    	if(client.getUsername().equals(command.message)){
				    	    dispose();
				    	}else{
				    	    userModel.removeElement(command.message);
				    	}
					break;
				case ROOMDESTROY:
					roomModel.removeElement(command.message);
					break;
				case ROOMS:
					roomModel.clear();
					String[] rooms = command.message.split(",");
					for (int i = 0; i < rooms.length; i++) {
					    if(!"-".equals(rooms[i])){
						roomModel.addElement(rooms[i]);
					    }
					}
					break;
				case USERS:
					userModel.clear();
					String[] users = command.message.split(",");
					for (int i = 0; i < users.length; i++) {
						userModel.addElement(users[i]);
					}
					break;
				case START:
				    	startButton.setSelected(true);
				    	startButton.setText("Stop");
				    	window = new Skeleton(client, command.message);
				    	window.setVisible(true);
					break;
				case STOP:
				    	startButton.setSelected(false);
				    	startButton.setText("Start");
				    	window.dispose();
					break;
				case UP:
				case DOWN:
				case LEFT:
				case RIGHT:
				    window.getGame().getPlayers().get(Integer.parseInt(command.message)).execute(command.command);
				    window.repaint();
				    break;
				case TURN:
					JOptionPane.showMessageDialog(GUI.this, "It is your turn!");
				default:
					break;
				}
			}
		});
	}
}
