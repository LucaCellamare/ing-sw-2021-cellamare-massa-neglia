package it.polimi.ingsw.messages;

/**
 * Message that tells the client that the lobby is full
 *
 * @author Roberto Neglia
 */
public class AlreadyFull extends Message {
    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {

    }
}
