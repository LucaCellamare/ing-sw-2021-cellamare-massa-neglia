package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.Enums.ActionEnum;
import it.polimi.ingsw.server.Model.Requirements.Requirement;

import java.util.List;

/**
 * Message that tells the Client that he/she doesn't have enough resources
 * to perform the requested action(buy Development Card, activate Leader Card, activate a production power).
 */
public class NotEnoughResources extends Message {

    private final List<Requirement> missingRequirements;
    private final ActionEnum action;

    public NotEnoughResources(List<Requirement> missingRequirements, ActionEnum action) {
        this.missingRequirements = missingRequirements;
        this.action = action;
    }

    public List<Requirement> getMissingRequirements() {
        return missingRequirements;
    }

    public ActionEnum getAction() {
        return action;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {

    }
}
