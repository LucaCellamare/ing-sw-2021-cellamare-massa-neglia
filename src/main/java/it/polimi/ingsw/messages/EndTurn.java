package it.polimi.ingsw.messages;

/**
 * Message used to show to the Client the moves he can perform before ending his/her turn.
 * It's also used to notify the serve when the Client has ended his/her turn.
 */
public class EndTurn extends Message {
    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {
        clientVisitor.elaborate(this);
    }
}
