package it.polimi.ingsw.messages;

/**
 * Message that tells the server that the Client has finished the setup of the game, and that he/she's ready to play.
 */
public class PlayerReady extends Message{
    @Override
    public void read(ServerVisitor serverVisitor) {

    }

    @Override
    public void read(ClientVisitor clientVisitor) {
        clientVisitor.elaborate(this);
    }
}
