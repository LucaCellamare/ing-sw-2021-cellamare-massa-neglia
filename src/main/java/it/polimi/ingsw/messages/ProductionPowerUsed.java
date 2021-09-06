package it.polimi.ingsw.messages;

/**
 * Bi-directional message,  used to manage the activation of a production power
 */
public class ProductionPowerUsed extends Message{
    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {
        clientVisitor.elaborate(this);
    }
}
