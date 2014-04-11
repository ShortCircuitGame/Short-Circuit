package shortcircuit.shared;

public class Command {

    public static enum CommandType {
	CREATE, JOIN, OTHERJOIN, JOINLOBBY, KICK, ROOMS, ROOMDESTROY, USERS, LEAVE, DISCONNECT, CHAT, START, STOP, NONE, SUCCESS, FAILURE, USERNAME, PASSWORD, SIGNUP, SIGNIN, TURN, UP, DOWN, LEFT, RIGHT, ATTACK
    };

    public static final String delimiter = "/";
    public CommandType command;
    public String message;

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

    public Command(CommandType type, String message) {
	this.command = type;
	if (message.isEmpty()) {
	    this.message = "-";
	} else {
	    this.message = message;
	}
    }

    public Command(CommandType type) {
	this.command = type;
	this.message = "-";
    }

    public String toString() {
	return command.toString() + delimiter + message;
    }

    public void setMessage(String message) {
	this.message = message;
    }
}
