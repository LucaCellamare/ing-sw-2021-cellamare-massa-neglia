package it.polimi.ingsw.client.view.CustomSwing;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Depot;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Warehouse;
import it.polimi.ingsw.utils.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WarehousePanel extends JPanel {



    public WarehousePanel(Warehouse warehouse) {
        super(new GridBagLayout());

        List<Depot> depots = warehouse.getDepots();

        GridBagConstraints gbc;

        List<JPanel> depotsPanels = new ArrayList<>();

        ImageIcon icon = null;
        JLabel iconLabel;

        int j = 0;
        for (Depot d : depots) {
            depotsPanels.add(new JPanel(new GridBagLayout()));
            for (int i = 0; i < d.getSize(); i++) {
                String iconToRead;

                if (i < d.getAmountStored())
                    iconToRead = d.getResource().toString().toLowerCase();
                else iconToRead = ResourceEnum.NONE.toString().toLowerCase();

                try {
                    icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/" + iconToRead + ".jpg"))));
                    icon = new ImageIcon(ImageUtil.getScaledImage(icon.getImage(), 70, 70));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                iconLabel = new JLabel(icon);
                gbc = new GridBagConstraints();
                gbc.gridx = i;
                gbc.gridy = 0;
                depotsPanels.get(depotsPanels.size() - 1).add(iconLabel, gbc);
            }
            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = j;

            add(depotsPanels.get(j), gbc);
            j++;
        }

        Border innerEmptyBorder = new EmptyBorder(10,10,10,10);
        Border lineBorder = new LineBorder(Color.BLACK);
        CompoundBorder innerBorder = new CompoundBorder(lineBorder,innerEmptyBorder);

        Border outerEmptyBorder = new EmptyBorder(10,10,10,10);

        CompoundBorder border = new CompoundBorder(outerEmptyBorder, innerBorder);

        setBorder(border);


        repaint();
        validate();
    }
}
