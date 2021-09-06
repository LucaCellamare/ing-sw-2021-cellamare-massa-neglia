package it.polimi.ingsw.client.view.CustomSwing;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Requirements.ResourceRequirement;
import it.polimi.ingsw.utils.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class ResourceRequirementPrinter implements RequirementPrinter {
    private final ResourceRequirement resourceRequirement;

    public ResourceRequirementPrinter(ResourceRequirement requirement) {
        this.resourceRequirement = requirement;
    }

    @Override
    public void print(JPanel pane, int position) {
        ResourceEnum resource = resourceRequirement.getResourceType();
        int quantity = resourceRequirement.getQuantity();
        JLabel iconLabel, numberLabel;
        ImageIcon icon = null;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = position;
        gbc.gridy = 0;
        try {
            icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/" + resource.toString().toLowerCase() + ".jpg"))));
            icon = new ImageIcon(ImageUtil.getScaledImage(icon.getImage(), 30, 30));
        } catch (IOException e) {
            e.printStackTrace();
        }
        iconLabel = new JLabel(icon);
        pane.add(iconLabel, gbc);

        position++;

        gbc = new GridBagConstraints();
        gbc.gridx = position;
        gbc.gridy = 0;

        numberLabel = new JLabel(" x" + quantity) {{
            setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        }};

        pane.add(numberLabel, gbc);
    }
}
