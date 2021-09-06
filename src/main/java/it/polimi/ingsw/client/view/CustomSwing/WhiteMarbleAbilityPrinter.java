package it.polimi.ingsw.client.view.CustomSwing;

import it.polimi.ingsw.server.Model.LeaderAbilities.WhiteMarbleAbility;
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

public class WhiteMarbleAbilityPrinter implements SpecialAbilityPrinter {
    private final WhiteMarbleAbility ability;

    public WhiteMarbleAbilityPrinter(WhiteMarbleAbility ability) {
        this.ability = ability;
    }

    @Override
    public void print(JPanel pane) {
        GridBagConstraints gbc = new GridBagConstraints();


        JPanel abilityPane = new JPanel(new GridBagLayout());
        abilityPane.setOpaque(false);

        Border innerEmptyBorder = new EmptyBorder(10, 15, 10, 15);
        Border lineBorder = new LineBorder(Color.BLACK);

        CompoundBorder innerBorder = new CompoundBorder(lineBorder, innerEmptyBorder);

        Border outerEmptyBorder = new EmptyBorder(5, 0, 5, 0);

        CompoundBorder border = new CompoundBorder(outerEmptyBorder, innerBorder);

        abilityPane.setOpaque(false);
        abilityPane.setBorder(border);

        ImageIcon marbleIcon = null, resourceIcon = null;
        JLabel marbleIconLabel, resourceIconLabel, equalLabel;

        equalLabel = new JLabel(" = ");
        equalLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 30));

        try {
            marbleIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/white_marble.jpg"))));
            marbleIcon = new ImageIcon(ImageUtil.getScaledImage(marbleIcon.getImage(), 40, 40));
            resourceIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/" + ability.getResource().toString().toLowerCase() + ".jpg"))));
            resourceIcon = new ImageIcon(ImageUtil.getScaledImage(resourceIcon.getImage(), 40, 40));
        } catch (IOException e) {
            e.printStackTrace();
        }

        marbleIconLabel = new JLabel(marbleIcon);
        marbleIconLabel.setOpaque(true);
        abilityPane.add(marbleIconLabel);

        abilityPane.add(equalLabel);

        resourceIconLabel = new JLabel(resourceIcon);
        abilityPane.add(resourceIconLabel);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;

        pane.add(abilityPane, gbc);
    }
}
