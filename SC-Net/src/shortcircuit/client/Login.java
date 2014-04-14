package shortcircuit.client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import shortcircuit.shared.Command;

public class Login extends JDialog implements ClientEventListener {

    private final JPanel contentPanel = new JPanel();
    private JTextField textField;
    private JPasswordField passwordField;
    private ShortCircuitClient client;
    private JLabel lblMessage;
    private String serverName;
    
    public static void main(final String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		try {
		    Login dialog;
			if(args.length > 0){
				dialog = new Login(args[0]);
			}else{
				dialog = new Login("localhost");
			}
		    
		    dialog.setModal(true);
		    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		    dialog.setVisible(true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    public Login(String hostname) {
	this.client = new ShortCircuitClient(hostname);
	this.serverName = hostname;
	this.client.start();
	this.client.addListener(this);

	setBounds(100, 100, 399, 186);
	getContentPane().setLayout(new BorderLayout());
	contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
	getContentPane().add(contentPanel, BorderLayout.CENTER);

	JLabel lblUsername = new JLabel("Username");

	textField = new JTextField();
	textField.setColumns(10);

	JLabel lblPassword = new JLabel("Password");

	passwordField = new JPasswordField();

	lblMessage = new JLabel("");
	GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
	gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(
		gl_contentPanel
			.createSequentialGroup()
			.addContainerGap()
			.addGroup(
				gl_contentPanel
					.createParallelGroup(Alignment.LEADING)
					.addGroup(
						gl_contentPanel.createSequentialGroup().addComponent(lblUsername).addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField, GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE))
					.addGroup(
						gl_contentPanel.createSequentialGroup().addComponent(lblPassword).addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)).addComponent(lblMessage))
			.addContainerGap()));
	gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(
		gl_contentPanel
			.createSequentialGroup()
			.addContainerGap()
			.addGroup(
				gl_contentPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblUsername)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
			.addGap(18)
			.addGroup(
				gl_contentPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblPassword)
					.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
			.addPreferredGap(ComponentPlacement.RELATED, 32, Short.MAX_VALUE).addComponent(lblMessage)));
	contentPanel.setLayout(gl_contentPanel);
	{
	    JPanel buttonPane = new JPanel();
	    buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
	    getContentPane().add(buttonPane, BorderLayout.SOUTH);
	    {
		JButton loginButton = new JButton("Sign In");
		loginButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			client.sendMessage(new Command(Command.CommandType.SIGNIN));
			client.sendMessage(new Command(Command.CommandType.USERNAME, textField.getText()));
			client.sendMessage(new Command(Command.CommandType.PASSWORD, new String(passwordField.getPassword())));
		    }
		});
		{
		    JButton cancelButton = new JButton("Cancel");
		    cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			    System.exit(0);
			}
		    });
		    cancelButton.setActionCommand("Cancel");
		    buttonPane.add(cancelButton);
		}
		loginButton.setActionCommand("OK");
		buttonPane.add(loginButton);
		getRootPane().setDefaultButton(loginButton);
	    }

	    JButton btnNewButton = new JButton("Sign Up");
	    btnNewButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    client.sendMessage(new Command(Command.CommandType.SIGNUP));
		    client.sendMessage(new Command(Command.CommandType.USERNAME, textField.getText()));
		    client.sendMessage(new Command(Command.CommandType.PASSWORD, new String(passwordField.getPassword())));
		}
	    });
	    buttonPane.add(btnNewButton);
	}
    }

    @Override
    public void commandRecievedEvent(final Command command) {
	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		try {
		    if (command.command == Command.CommandType.SUCCESS) {			
			client.removeListenet(Login.this);
			client.setUsername(textField.getText());
			GUI frame = new GUI(client);
			frame.setVisible(true);
			dispose();
		    } else {
			if (command.command == Command.CommandType.FAILURE) {
			    lblMessage.setText(command.message);
			} else {
			    lblMessage.setText("ERROR!");
			}
			client.removeListenet(Login.this);
			client = new ShortCircuitClient(serverName);
			client.start();
			client.addListener(Login.this);
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});

    }
}
