package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.Cards.LeaderCard;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Requirements.Requirement;

import java.util.List;

/**
 * Message used to handle the activation of a LeaderCard.
 */
public class ActivateLeaderCard extends Message {


    private LeaderCard leaderCard;

    private final boolean isDiscardAction;
    private final boolean isStartTurn;
    private boolean successActivate;
    private String message; // messaggio utilizzabile se successActivate = false -> motivazione
    private final List<Requirement> missingRequirements;
    private List<ResourceEnum> whiteMarbleLeaderCardAbilities;
    private List<ResourceEnum> discountLeaderCardAbilities;


    public ActivateLeaderCard(LeaderCard leaderCard, boolean isDiscardAction, boolean isStartTurn) {
        this.leaderCard = leaderCard;
        this.isDiscardAction = isDiscardAction;
        this.isStartTurn = isStartTurn;
        this.missingRequirements = null;
    }

    public ActivateLeaderCard(boolean isDiscardAction, boolean isStartTurn, boolean successActivate,List<ResourceEnum> whiteMarbleAbilities,List<ResourceEnum> discountAbilities){
        this.isDiscardAction = isDiscardAction;
        this.isStartTurn = isStartTurn;
        this.successActivate = successActivate;
        this.missingRequirements = null;
        this.whiteMarbleLeaderCardAbilities = whiteMarbleAbilities;
        this.discountLeaderCardAbilities = discountAbilities;
    }
    public ActivateLeaderCard(boolean isDiscardAction, boolean isStartTurn, boolean successActivate,List<Requirement> missingRequirements){
        this.isDiscardAction = isDiscardAction;
        this.isStartTurn = isStartTurn;
        this.successActivate = successActivate;
        this.missingRequirements = missingRequirements;
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

    public boolean getSuccessActivate(){return  successActivate; }

    public List<Requirement> getMissingRequirements() {
        return missingRequirements;
    }


    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {
        clientVisitor.elaborate(this);
    }

    public List<ResourceEnum> getDiscountLeaderCardAbilities() {
        return discountLeaderCardAbilities;
    }

    public List<ResourceEnum> getWhiteMarbleLeaderCardAbilities() {
        return whiteMarbleLeaderCardAbilities;
    }
}
