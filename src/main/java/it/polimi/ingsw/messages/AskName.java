package it.polimi.ingsw.messages;

/**
 * Message that asks the client for a nickname.
 * It's also used as a response to be sent to the server.
 *
 * @author Roberto Neglia
 */
public class AskName extends Message {

    private boolean again;

    private int reconnectedPlayerIndex;

    /**
     * Name of the player.
     * Used only in the response message.
     */
    private String playerName;

    public AskName() {
        reconnectedPlayerIndex = -1;
    }

    public AskName(boolean again) {
        this();
        this.again = again;
    }

    public AskName(int reconnectedPlayerIndex) {
        this.reconnectedPlayerIndex = reconnectedPlayerIndex;
    }

    public AskName(String playerName) {
        this.playerName = playerName;
    }

    public AskName(String playerName, int reconnectedPlayerIndex) {
        this.playerName = playerName;
        this.reconnectedPlayerIndex = reconnectedPlayerIndex;
    }

    public boolean isAgain() {
        return again;
    }

    /**
     * @return the nickname of the player
     */
    public String getPlayerName() {
        return playerName;
    }

    public int getReconnectedPlayerIndex() {
        return reconnectedPlayerIndex;
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
