package it.polimi.ingsw.exceptions;

import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.Requirements.Requirement;

import java.util.ArrayList;
import java.util.List;

public class MissingRequirementsException extends Exception {
    Player p;
    Requirement[] requirements;

    public MissingRequirementsException(Player p, Requirement[] requirements) {
        this.p = p;
        this.requirements = requirements;
    }

    public List<Requirement> getMissingRequirements() {
        List<Requirement> notFulfilled = new ArrayList<>();
        for (Requirement r : requirements) {
            if (!r.checkRequirement(p))
                notFulfilled.add(r);
        }
        return notFulfilled;
    }
}
