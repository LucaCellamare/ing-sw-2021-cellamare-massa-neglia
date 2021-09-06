package it.polimi.ingsw.messages;

/**
 * Message sent from server to client to tell to the player the total amount victory points has received in the game
 */
public class TotalVictoryPoints extends Message {
    private final int totalVictoryPoints;

    public TotalVictoryPoints(int totalVictoryPoints) {
        this.totalVictoryPoints = totalVictoryPoints;
    }

    public int getTotalVictoryPoints() {
        return totalVictoryPoints;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {

    }
}
