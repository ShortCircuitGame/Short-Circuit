import java.util.UUID;

public class Client {
	private ShortCircuitServerThread thread;
	private String username;

	public Client(ShortCircuitServerThread thread) {
		this.thread = thread;
		this.username = UUID.randomUUID().toString();
	}

	public void sendMessage(String message) {
		this.thread.sendMessage(message);
	}

	public ShortCircuitServerThread getThread() {
		return thread;
	}

	public String getUsername() {
		return username;
	}

	public Command executeCommand(Command command) {

		switch (command.command) {
		case CHAT:
			this.thread.getRoom().broadcastMessage(command.message);
			return new Command(Command.CommandType.SUCCESS, "DELIVERED");
		case CREATE:
			if (this.thread.getRoom() == ShortCircuitServer.getGetLobby()) {
				this.thread.getRoom().removeMember(this);
				this.thread.setRoom(ShortCircuitServer.createRoom(command.message, this));
				this.thread.getRoom().addMember(this);
				return new Command(Command.CommandType.SUCCESS, "CREATED");
			} else {
				System.out.println("You are already in a room");
				return new Command(Command.CommandType.FAILURE, "FAILED");
			}
		case JOIN:
			if (this.thread.getRoom() == ShortCircuitServer.getGetLobby()) {
				this.thread.getRoom().removeMember(this);
				this.thread.setRoom(ShortCircuitServer.getRoom(command.message));
				this.thread.getRoom().addMember(this);
				return new Command(Command.CommandType.SUCCESS, "JOINED");
			} else {
				System.out.println("You are already in a room");
				return new Command(Command.CommandType.FAILURE, "FAILED");
			}
		case LEAVE:
			if (this.thread.getRoom() != ShortCircuitServer.getGetLobby()) {
				this.thread.getRoom().removeMember(this);
				this.thread.setRoom(ShortCircuitServer.getGetLobby());
				this.thread.getRoom().addMember(this);
				return new Command(Command.CommandType.SUCCESS, "LEFT");
			} else {
				System.out.println("You are already in the lobby");
				return new Command(Command.CommandType.FAILURE, "FAILED");
			}
		case DISCONNECT:
			this.thread.getRoom().removeMember(this);
			this.thread.stopRunning();
			return new Command(Command.CommandType.SUCCESS, "SAVED");
		case ROOMS:
			return new Command(Command.CommandType.SUCCESS, this.thread.generateRoomList());
		case USERS:
			return new Command(Command.CommandType.SUCCESS, this.thread.generateMembersList());
		case START:
			break;
		case STOP:
			break;
		default:
			break;
		}

		return ShortCircuitServer.getSim().executeCommand(command);
	}
}
