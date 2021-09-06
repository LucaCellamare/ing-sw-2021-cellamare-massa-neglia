package it.polimi.ingsw.messages;

/**
 * Message that tells the client that he has correctly connected
 *
 * @author Roberto Neglia
 */
public class Connected extends Message {

    private final int connectedIndex;

    public Connected(int connectedIndex) {
        this.connectedIndex = connectedIndex;
    }

    public int getConnectedIndex() {
        return connectedIndex;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {
    }
}
