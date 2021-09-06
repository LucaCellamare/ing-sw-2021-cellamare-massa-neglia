package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.Cards.LeaderCard;

/**
 * Message used to handle the act of discarding a LeaderCard.
 */
public class DiscardLeaderCard extends Message {

    private LeaderCard leaderCard;

    private final boolean isDiscardAction;
    private final boolean isStartTurn;
    private boolean succcessDiscard;


    public DiscardLeaderCard(LeaderCard leaderCard, boolean isDiscardAction, boolean isStartTurn) {
        this.leaderCard = leaderCard;
        this.isDiscardAction = isDiscardAction;
        this.isStartTurn = isStartTurn;
    }

    public DiscardLeaderCard(boolean isDiscardAction, boolean isStartTurn,boolean succcessDiscard){
        this.isDiscardAction = isDiscardAction;
        this.isStartTurn = isStartTurn;
        this.succcessDiscard = succcessDiscard;
    }

    public LeaderCard getLeaderCard() {
        return leaderCard;
    }

    public boolean getIsDiscardAction() {
        return isDiscardAction;
    }

    public boolean getIsStartTurn() {
        return isStartTurn;
    }

    public boolean getSucccessDiscard(){return  succcessDiscard; }

    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {
        clientVisitor.elaborate(this);
    }
}
