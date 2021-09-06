package it.polimi.ingsw.messages;

/**
 * Message received by the client when the insertion of a resource in a depot fails.
 */
public class AbortInsert extends Message {

    @Override
    public void read(ServerVisitor serverVisitor) {

    }

    @Override
    public void read(ClientVisitor clientVisitor) {
        clientVisitor.elaborate(this);
    }
}
