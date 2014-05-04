package shortcircuit.shared;

public class Command {

	/* list of commands that are usable within the game itself */
    public static enum CommandType {
	CREATE, JOIN, OTHERJOIN, JOINLOBBY, KICK, ROOMS, ROOMDESTROY, USERS, LEAVE, DISCONNECT, CHAT, START, STOP, NONE, SUCCESS, FAILURE, USERNAME, PASSWORD, SIGNUP, SIGNIN, TURN, UP, DOWN, LEFT, RIGHT, MATTACK,RATTACK
    };

    public static final String delimiter = "/";
    public CommandType command;
    public String message;

    /* Constructor for command.  Checks if the commands used are valid or invalid commands by the user. */
    public Command(String input) {
	try {
	    this.command = CommandType.valueOf(input.split(Command.delimiter)[0].toUpperCase());
	    this.message = input.split(Command.delimiter)[1];
	} catch (IllegalArgumentException ex) {
	    this.command = CommandType.NONE;
	    this.message = "COMMANDNOTVALID";
	    System.err.println("Command recieved is not valid");
	} catch (ArrayIndexOutOfBoundsException ex) {
	    this.command = CommandType.NONE;
	    this.message = "DELIMITERNOTFOUND";
	    System.err.println("Command recieved is not correctly formatted");
	}
    }
/* Constructor that converts the client's ,essage to a string. */
    public Command(CommandType type, String message) {
	this.command = type;
	if (message.isEmpty()) {
	    this.message = "-";
	} else {
	    this.message = message;
	}
    }
/* Simplified version of constructor above */
    public Command(CommandType type) {
	this.command = type;
	this.message = "-";
    }
/* to string method for command */
    public String toString() {
	return command.toString() + delimiter + message;
    }
/* setter method for message */
    public void setMessage(String message) {
	this.message = message;
    }
}
