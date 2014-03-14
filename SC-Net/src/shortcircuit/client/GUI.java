package shortcircuit.client;

import java.awt.Color;
import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import shortcircuit.shared.Command;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUI extends JFrame implements ClientEventListener {

	private JPanel contentPane;
	private JTextField roomNameImput;
	private JTextField textInput;
	private ShortCircuitClient client;
	private JTextArea textArea;
	private JList userList;
	private JList roomList;

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 */
	public GUI(final ShortCircuitClient client) throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 516, 358);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		roomList = new JList();
		roomList.setBorder(new LineBorder(new Color(0, 0, 0)));

		JButton createRoomButton = new JButton("Create Room");

		roomNameImput = new JTextField();
		roomNameImput.setColumns(10);

		userList = new JList();
		userList.setBorder(new LineBorder(new Color(0, 0, 0)));

		JButton startButton = new JButton("Start");

		textArea = new JTextArea();
		textArea.setBackground(Color.LIGHT_GRAY);

		textInput = new JTextField();
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
		gl_contentPane
				.setHorizontalGroup(gl_contentPane
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.LEADING,
																false)
														.addComponent(
																roomList,
																GroupLayout.PREFERRED_SIZE,
																125,
																GroupLayout.PREFERRED_SIZE)
														.addGroup(
																gl_contentPane
																		.createParallelGroup(
																				Alignment.TRAILING,
																				false)
																		.addComponent(
																				createRoomButton,
																				Alignment.LEADING,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addComponent(
																				roomNameImput,
																				Alignment.LEADING)))
										.addGap(18)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addComponent(
																				textInput,
																				GroupLayout.DEFAULT_SIZE,
																				161,
																				Short.MAX_VALUE)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				sendButton))
														.addComponent(
																textArea,
																GroupLayout.DEFAULT_SIZE,
																237,
																Short.MAX_VALUE))
										.addGap(18)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.TRAILING,
																false)
														.addComponent(
																userList,
																GroupLayout.PREFERRED_SIZE,
																106,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																startButton))));
		gl_contentPane
				.setVerticalGroup(gl_contentPane
						.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.TRAILING)
														.addComponent(
																textArea,
																GroupLayout.DEFAULT_SIZE,
																291,
																Short.MAX_VALUE)
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addComponent(
																				roomList,
																				GroupLayout.DEFAULT_SIZE,
																				266,
																				Short.MAX_VALUE)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				roomNameImput,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE))
														.addComponent(
																userList,
																GroupLayout.DEFAULT_SIZE,
																291,
																Short.MAX_VALUE))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																createRoomButton)
														.addComponent(
																startButton)
														.addComponent(
																textInput,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																sendButton))));
		contentPane.setLayout(gl_contentPane);

		this.client = client;
		this.client.addListener(this);
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
					break;
				case JOIN:
					break;
				case LEAVE:
					break;
				case DISCONNECT:
					break;
				case ROOMS:
					break;
				case USERS:
					break;
				case START:
					break;
				case STOP:
					break;
				default:
					break;
				}
			}
		});
	}
}
