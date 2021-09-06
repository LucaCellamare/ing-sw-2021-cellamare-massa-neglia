package it.polimi.ingsw.client.view.CustomSwing;

import it.polimi.ingsw.server.Model.LeaderAbilities.*;

import javax.swing.*;

public class SpecialAbilityDecorator {
    SpecialAbilityPrinter printer;

    public SpecialAbilityDecorator(SpecialAbility ability, JPanel pane) {
        if (ability instanceof DiscountAbility)
            printer = new DiscountAbilityPrinter((DiscountAbility) ability);
        else if (ability instanceof StorageAbility)
            printer = new StorageAbilityPrinter((StorageAbility) ability);
        else if (ability instanceof WhiteMarbleAbility)
            printer = new WhiteMarbleAbilityPrinter((WhiteMarbleAbility) ability);
        else printer = new ProductionAbilityPrinter((ProductionAbility) ability);

        printer.print(pane);
    }
}
