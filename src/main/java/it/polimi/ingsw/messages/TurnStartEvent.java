package it.polimi.ingsw.messages;

/**
 * Message sent from client to server to tell him that its turn has started
 */
public class TurnStartEvent extends Message {

    private final int position;

    private final String nickname;

    public TurnStartEvent(int position, String nickname) {
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
