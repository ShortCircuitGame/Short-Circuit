package shortcircuit.client;

import shortcircuit.shared.Command;

public interface ClientEventListener {
    public void commandRecievedEvent(Command command);
}
