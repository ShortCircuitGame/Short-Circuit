import java.util.Hashtable;


public class Room {
	private Client	admin;
	private Hashtable<String, Client> members;
	private String roomName;
	
	public Room(String roomName){
		this.members = new Hashtable<String, Client>();
		this.roomName = roomName;
	}
	
	public Room(String roomName, Client admin){
		this(roomName);
		this.admin = admin;
	}
	
	public void addMember(Client client){
		this.members.put(client.getUsername(), client);
	}

	public Client getMember(String key){
		return this.members.get(key);
	}
	
	public void removeMember(Client client){
		this.members.remove(client.getUsername());
	}
	
	public void broadcastMessage(String message) {
		for(String key : members.keySet()){
			members.get(key).sendMessage(message);
		}
	}
	
	public Hashtable<String, Client> getMemberList(){
		return this.members;
	}

	public String getRoomName() {
		return roomName;
	}

	public Client getAdmin() {
		return admin;
	}
	
}
