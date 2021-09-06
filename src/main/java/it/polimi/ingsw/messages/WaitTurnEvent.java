package it.polimi.ingsw.messages;

/**
 * Message only sent from server to client when a turn starts, but not his
 */
public class WaitTurnEvent extends Message {
    private final int position;

    private final String nickname;

    public WaitTurnEvent(int position, String nickname) {
        this.position = position;
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {

    }
}
