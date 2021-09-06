package it.polimi.ingsw.client.view.CustomSwing;

import it.polimi.ingsw.server.Model.LeaderAbilities.StorageAbility;
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

public class StorageAbilityPrinter implements SpecialAbilityPrinter {
    private final StorageAbility ability;

    public StorageAbilityPrinter(StorageAbility ability) {
        this.ability = ability;
    }

    @Override
    public void print(JPanel pane) {
        int quantity = ability.getSize();

        GridBagConstraints gbc = new GridBagConstraints();

        JPanel abilityPane = new JPanel(new GridBagLayout());

        Border innerEmptyBorder = new EmptyBorder(10, 15, 10, 15);
        Border lineBorder = new LineBorder(Color.BLACK);

        CompoundBorder innerBorder = new CompoundBorder(lineBorder, innerEmptyBorder);

        Border outerEmptyBorder = new EmptyBorder(5, 0, 5, 0);

        CompoundBorder border = new CompoundBorder(outerEmptyBorder, innerBorder);

        abilityPane.setOpaque(false);
        abilityPane.setBorder(border);

        ImageIcon icon = null;
        JLabel info, iconLabel;

        info = new JLabel("extra storage");
        info.setFont(new Font(Font.DIALOG, Font.BOLD, 11));
        info.setHorizontalAlignment(SwingConstants.CENTER);
        info.setBorder(new EmptyBorder(5,0,5,0));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = quantity;

        abilityPane.add(info, gbc);

        for (int i = 0; i < quantity; i++) {
            gbc = new GridBagConstraints();
            gbc.gridx = i;
            gbc.gridy = 1;
            try {
                icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/" + ability.getResource().toString().toLowerCase() + ".jpg"))));
                icon = new ImageIcon(ImageUtil.getScaledImage(icon.getImage(), 40, 40));
            } catch (IOException e) {
                e.printStackTrace();
            }
            iconLabel = new JLabel(icon);
            abilityPane.add(iconLabel, gbc);
        }

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;

        pane.add(abilityPane, gbc);
    }
}
