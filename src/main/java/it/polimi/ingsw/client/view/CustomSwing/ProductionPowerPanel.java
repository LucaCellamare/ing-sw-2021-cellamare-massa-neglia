package it.polimi.ingsw.client.view.CustomSwing;

import it.polimi.ingsw.server.Model.Cards.ProductionPower;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Requirements.ResourceRequirement;
import it.polimi.ingsw.utils.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Objects;

public class ProductionPowerPanel extends JPanel {
    private final int ICON_SIZE = 30;

    private final ResourceRequirement[] required;

    private final EnumMap<ResourceEnum, Integer> obtained;

    public ProductionPowerPanel(ProductionPower p) {
        super(new GridBagLayout());
        setOpaque(false);
        required = p.getResourceRequested();
        obtained = p.getResourceObtained();
        GridBagConstraints gbc;
        ImageIcon icon = null;
        JLabel numberLabel, iconLabel;
        JPanel requiredPane = new JPanel(new GridBagLayout());
        JPanel obtainedPane = new JPanel(new GridBagLayout());

        for (int i = 0; i < required.length; i++) {
            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = i;
            try {
                icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/" + required[i].getResourceType().toString().toLowerCase() + ".jpg"))));
                icon = new ImageIcon(ImageUtil.getScaledImage(icon.getImage(), ICON_SIZE, ICON_SIZE));

            } catch (IOException e) {
                e.printStackTrace();
            }
            iconLabel = new JLabel(icon);
            requiredPane.add(iconLabel, gbc);

            gbc = new GridBagConstraints();
            gbc.gridx = 1;
            gbc.gridy = i;

            numberLabel = new JLabel("x" + required[i].getQuantity()) {{
                setFont(new Font(Font.DIALOG, Font.BOLD, 14));
            }};

            requiredPane.add(numberLabel, gbc);
        }
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        add(requiredPane, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = Math.max(required.length, obtained.keySet().size());
        gbc.gridwidth = 1;

        try {
            icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/graffa.jpg"))));
            icon = new ImageIcon(ImageUtil.getScaledImage(icon.getImage(), 30, ICON_SIZE * Math.max(required.length, obtained.keySet().size())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        iconLabel = new JLabel(icon);
        add(iconLabel, gbc);

        int i = 0;
        for (ResourceEnum r : obtained.keySet()) {
            gbc = new GridBagConstraints();
            gbc.gridx = 3;
            gbc.gridy = i;

            try {
                icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/" + r.toString().toLowerCase() + ".jpg"))));
                icon = new ImageIcon(ImageUtil.getScaledImage(icon.getImage(), ICON_SIZE, ICON_SIZE));
            } catch (IOException e) {
                e.printStackTrace();
            }
            iconLabel = new JLabel(icon);
            obtainedPane.add(iconLabel, gbc);

            gbc = new GridBagConstraints();
            gbc.gridx = 4;
            gbc.gridy = i;

            numberLabel = new JLabel("x" + obtained.get(r)) {{
                setFont(new Font(Font.DIALOG, Font.BOLD, 14));
            }};

            obtainedPane.add(numberLabel, gbc);

            i++;
        }

        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;

        add(obtainedPane, gbc);

    }
}
