package it.polimi.ingsw.messages;

/**
 * Message only for the first client that asks for the number of players.
 * It's also used as a response to be sent to the server.
 *
 * @author Roberto Neglia
 */
public class AskNPlayer extends Message {

    /**
     * Number of player set by the client.
     * Used only in the response message.
     */
    int nPlayer;

    public AskNPlayer() {
    }

    public AskNPlayer(int nPlayer) {
        this.nPlayer = nPlayer;
    }

    /**
     * @return the number of player set by the first client
     */
    public int getNPlayer() {
        return nPlayer;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {
        clientVisitor.elaborate(this);
    }
}
