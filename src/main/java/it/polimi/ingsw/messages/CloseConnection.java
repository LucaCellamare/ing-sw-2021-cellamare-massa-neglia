package it.polimi.ingsw.messages;

/**
 * Message that tells the server that the clients wants to close the connection
 *
 * @author Roberto Neglia
 */
public class CloseConnection extends Message {
    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {

    }
}
