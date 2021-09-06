package it.polimi.ingsw.client.view.CustomSwing;

import it.polimi.ingsw.server.Model.Requirements.CardRequirement;
import it.polimi.ingsw.server.Model.Requirements.Requirement;
import it.polimi.ingsw.server.Model.Requirements.ResourceRequirement;

import javax.swing.*;

public class RequirementDecorator {
    private final RequirementPrinter printer;

    public RequirementDecorator(Requirement requirement, JPanel pane, int position) {
        if (requirement instanceof ResourceRequirement)
            printer = new ResourceRequirementPrinter((ResourceRequirement) requirement);
        else printer = new CardRequirementPrinter((CardRequirement) requirement);
        printer.print(pane, position);
    }
}
