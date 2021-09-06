package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.Cards.LeaderCard;

import java.util.List;

/**
 * Message that tells the server that the Client wants to activate or discard a Leader Card.
 * It's also used to send to the view the list of available Leader Cards to be shown to the CLient.
 */
public class GetLeaderCard extends Message {


    private List<LeaderCard> availableLeaderCard;

    private final boolean isDiscardAction;
    private final boolean isStartTurn;


    public GetLeaderCard(List<LeaderCard> leaderCards,boolean isDiscardAction,boolean isStartTurn) {
        this.availableLeaderCard = leaderCards;
        this.isDiscardAction = isDiscardAction;
        this.isStartTurn = isStartTurn;
    }

    public GetLeaderCard(boolean isDiscardAction,boolean isStartTurn){
        this.isDiscardAction = isDiscardAction;
        this.isStartTurn = isStartTurn;
    }

    public List<LeaderCard> getAvailableLeaderCard() {
        return availableLeaderCard;
    }

    public boolean isDiscardAction() {
        return isDiscardAction;
    }

    public boolean isStartTurn() {
        return isStartTurn;
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
