package it.polimi.ingsw.messages;

/**
 * Message sent only from server to client in three cases:
 * - a player has finished the setup, so the controller tells him to wait for the remaining players
 * - a player left the game before ending his setup, so i tell the players that are ready to wait for him to reconnect
 * - a player reconnected and before he left he already had completed the setup
 */
public class SetupComplete extends Message {

    private final int nReady;

    private final int total;

    public SetupComplete() {
        nReady = -1;
        total = -1;
    }

    public SetupComplete(int nReady, int total) {
        this.nReady = nReady;
        this.total = total;
    }

    public int getnReady() {
        return nReady;
    }

    public int getTotal() {
        return total;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {

    }
}
