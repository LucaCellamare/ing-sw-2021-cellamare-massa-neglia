package it.polimi.ingsw.messages;

/**
 * Message that tells the client to wait for other players to connect the game.
 *
 * @author Roberto Neglia
 */
public class WaitForPlayers extends Message {
    /**
     * Number of players already connected to the game.
     */
    private final int playersConnected;

    /**
     * Total number of players of the game the user is trying to connect (the one chosen by the first player connected)
     */
    private final int totalPlayers;

    public WaitForPlayers() {
        playersConnected = -1;
        totalPlayers = -1;
    }

    public WaitForPlayers(int playersConnected, int totalPlayers) {
        this.playersConnected = playersConnected;
        this.totalPlayers = totalPlayers;
    }

    /**
     * @return the number of players already connected
     */
    public int getPlayersConnected() {
        return playersConnected;
    }

    /**
     * @return the total number of players of a game
     */
    public int getTotalPlayers() {
        return totalPlayers;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {
    }
}
