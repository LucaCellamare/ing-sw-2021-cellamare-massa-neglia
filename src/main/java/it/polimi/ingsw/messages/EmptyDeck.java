package it.polimi.ingsw.messages;

/**
 * Message used to tell the client that he tried to buy a Development Card from a deck that is empty.
 */
public class EmptyDeck extends Message {
    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {

    }
}
