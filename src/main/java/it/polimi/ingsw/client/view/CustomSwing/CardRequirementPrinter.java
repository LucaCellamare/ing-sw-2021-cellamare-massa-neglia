package it.polimi.ingsw.client.view.CustomSwing;

import it.polimi.ingsw.server.Model.Enums.LevelEnum;
import it.polimi.ingsw.server.Model.Enums.TypeEnum;
import it.polimi.ingsw.server.Model.Requirements.CardRequirement;
import it.polimi.ingsw.utils.ImageUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CardRequirementPrinter implements RequirementPrinter {
    private final CardRequirement cardRequirement;

    public CardRequirementPrinter(CardRequirement requirement) {
        this.cardRequirement = requirement;
    }

    @Override
    public void print(JPanel pane, int position) {
        TypeEnum cardType = cardRequirement.getCardType();
        LevelEnum cardLevel = cardRequirement.getLevel();
        int quantity = cardRequirement.getQuantity();
        JLabel reqLabel, numberLabel;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = position;
        gbc.gridy = 0;

        if (cardType == TypeEnum.NONE) {
            reqLabel = new JLabel("lvl " + cardLevel);
        } else if (cardLevel == LevelEnum.NONE) {
            reqLabel = new JLabel("any lvl");
            reqLabel.setOpaque(true);
            reqLabel.setBackground(ImageUtil.colorMap.get(cardType));
        } else {
            reqLabel = new JLabel("lvl " + cardLevel);
            reqLabel.setOpaque(true);
            reqLabel.setBackground(ImageUtil.colorMap.get(cardType));
        }

        Border innerEmptyBorder = new EmptyBorder(10, 5, 10, 10);
        reqLabel.setBorder(innerEmptyBorder);

        pane.add(reqLabel, gbc);

        position++;

        gbc = new GridBagConstraints();
        gbc.gridx = position;
        gbc.gridy = 0;

        numberLabel = new JLabel(" x" + quantity + " ") {{
            setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        }};

        pane.add(numberLabel, gbc);
    }
}
