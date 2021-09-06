package it.polimi.ingsw.messages;

/**
 * Message used to manage the reconnection of a disconnected client in the initial resource management process
 */
public class ReconnectInitialResources extends Message {

    private final int reconnectedPlayerIndex;

    public ReconnectInitialResources(int reconnectedPlayerIndex) {
        this.reconnectedPlayerIndex = reconnectedPlayerIndex;
    }

    public int getReconnectedPlayerIndex() {
        return reconnectedPlayerIndex;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {

    }

    @Override
    public void read(ClientVisitor clientVisitor) {

    }
}
