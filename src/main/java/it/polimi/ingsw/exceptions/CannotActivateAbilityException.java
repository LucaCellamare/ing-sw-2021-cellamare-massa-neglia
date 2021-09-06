package it.polimi.ingsw.exceptions;

import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.Requirements.Requirement;

public class CannotActivateAbilityException extends MissingRequirementsException {
    boolean alreadyActivated;

    public CannotActivateAbilityException(boolean alreadyActivated) {
        super(null, null);
        this.alreadyActivated = alreadyActivated;
    }

    public CannotActivateAbilityException(Player p, Requirement[] requirements) {
        super(p, requirements);
        this.alreadyActivated = false;
    }

    public boolean isAlreadyActivated() {
        return alreadyActivated;
    }
}
