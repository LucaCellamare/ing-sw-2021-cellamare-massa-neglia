package it.polimi.ingsw.client.view.CustomSwing;

import it.polimi.ingsw.server.Model.LeaderAbilities.ProductionAbility;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ProductionAbilityPrinter implements SpecialAbilityPrinter {
    private final ProductionAbility ability;

    public ProductionAbilityPrinter(ProductionAbility ability) {
        this.ability = ability;
    }

    @Override
    public void print(JPanel pane) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;

        ProductionPowerPanel ppp = new ProductionPowerPanel(ability.getProductionPower());

        Border innerEmptyBorder = new EmptyBorder(15, 20, 15, 20);
        Border lineBorder = new LineBorder(Color.BLACK);

        CompoundBorder innerBorder = new CompoundBorder(lineBorder, innerEmptyBorder);

        Border outerEmptyBorder = new EmptyBorder(5, 0, 5, 0);

        CompoundBorder border = new CompoundBorder(outerEmptyBorder, innerBorder);

        ppp.setBorder(border);

        pane.add(ppp, gbc);
    }
}
