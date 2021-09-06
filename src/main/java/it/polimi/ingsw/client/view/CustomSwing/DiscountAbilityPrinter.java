package it.polimi.ingsw.client.view.CustomSwing;

import it.polimi.ingsw.server.Model.LeaderAbilities.DiscountAbility;
import it.polimi.ingsw.utils.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class DiscountAbilityPrinter implements SpecialAbilityPrinter {
    private final DiscountAbility ability;

    public DiscountAbilityPrinter(DiscountAbility ability) {
        this.ability = ability;
    }

    @Override
    public void print(JPanel pane) {
        GridBagConstraints gbc;

        JPanel abilityPane = new JPanel(new GridBagLayout());

        Border innerEmptyBorder = new EmptyBorder(10,15,10,15);
        Border lineBorder = new LineBorder(Color.BLACK);

        CompoundBorder innerBorder = new CompoundBorder(lineBorder, innerEmptyBorder);

        Border outerEmptyBorder = new EmptyBorder(5,0,5,0);

        CompoundBorder border = new CompoundBorder(outerEmptyBorder, innerBorder);

        abilityPane.setOpaque(false);
        abilityPane.setBorder(border);

        ImageIcon icon = null;
        JLabel iconLabel, numberLabel, discount;

        discount = new JLabel("discount of  ");

        abilityPane.add(discount);

        try {
            icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/" + ability.getResource().toString().toLowerCase() + ".jpg"))));
            icon = new ImageIcon(ImageUtil.getScaledImage(icon.getImage(), 40, 40));
        } catch (IOException e) {
            e.printStackTrace();
        }

        iconLabel = new JLabel(icon);
        abilityPane.add(iconLabel);

        numberLabel = new JLabel(" x" + ability.getQuantity() + " ");

        abilityPane.add(numberLabel);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;

        pane.add(abilityPane, gbc);
    }
}
