package it.polimi.ingsw.client.view.CustomSwing;

import it.polimi.ingsw.server.Model.Cards.DevelopmentCard;
import it.polimi.ingsw.server.Model.Cards.LeaderCard;
import it.polimi.ingsw.server.Model.Requirements.Requirement;
import it.polimi.ingsw.server.Model.Requirements.ResourceRequirement;
import it.polimi.ingsw.utils.ImageUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class CardPanel extends JPanel {
    private DevelopmentCard developmentCard;
    private LeaderCard leaderCard;

    public CardPanel() {
        super(new GridBagLayout());
        setOpaque(false);

        JLabel empty = new JLabel("EMPTY") {{
            setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        }};
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;

        add(empty, gbc);
    }

    public CardPanel(DevelopmentCard developmentCard) {
        super(new GridBagLayout());
        setOpaque(false);
        this.developmentCard = developmentCard;

        addLevelAndColor();

        addPrice();

        addProductionPower();

        addVictoryPoints(developmentCard.getVictoryPoints());
    }

    public CardPanel(LeaderCard leaderCard) {
        super(new GridBagLayout());
        setOpaque(false);
        this.leaderCard = leaderCard;

        addRequirements();

        addVictoryPoints(leaderCard.getVictoryPoints());

        addAbility();
    }

    private void addLevelAndColor() {
        GridBagConstraints gbc;

        Border innerEmptyBorder = new EmptyBorder(8, 5, 8, 8);

        JPanel labelPane = new JPanel() {{
            setOpaque(false);
        }};
        JLabel levelAndColor = new JLabel("lvl " + developmentCard.getLevel());
        levelAndColor.setBorder(innerEmptyBorder);
        levelAndColor.setOpaque(true);
        levelAndColor.setBackground(ImageUtil.colorMap.get(developmentCard.getType()));

        labelPane.add(levelAndColor);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        add(labelPane, gbc);

        innerEmptyBorder = new EmptyBorder(8, 8, 8, 5);

        labelPane = new JPanel() {{
            setOpaque(false);
        }};
        levelAndColor = new JLabel("lvl " + developmentCard.getLevel());
        levelAndColor.setBorder(innerEmptyBorder);
        levelAndColor.setOpaque(true);
        levelAndColor.setBackground(ImageUtil.colorMap.get(developmentCard.getType()));

        labelPane.add(levelAndColor);

        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;

        add(labelPane, gbc);

        labelPane = new JPanel() {{
            setOpaque(false);
        }};
        levelAndColor = new JLabel("lvl " + developmentCard.getLevel());
        levelAndColor.setBorder(innerEmptyBorder);
        levelAndColor.setOpaque(true);
        levelAndColor.setBackground(ImageUtil.colorMap.get(developmentCard.getType()));

        labelPane.add(levelAndColor);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;

        add(labelPane, gbc);

        labelPane = new JPanel() {{
            setOpaque(false);
        }};
        levelAndColor = new JLabel("lvl " + developmentCard.getLevel());
        levelAndColor.setBorder(innerEmptyBorder);
        levelAndColor.setOpaque(true);
        levelAndColor.setBackground(ImageUtil.colorMap.get(developmentCard.getType()));

        labelPane.add(levelAndColor);

        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;

        add(labelPane, gbc);
    }

    private void addPrice() {
        Border emptyBorder = new EmptyBorder(5, 5, 5, 5);
        Border lineBorder = new LineBorder(Color.BLACK);
        CompoundBorder border = new CompoundBorder(lineBorder, emptyBorder);

        GridBagConstraints gbc;
        JPanel pricePanel = new JPanel(new GridBagLayout()) {{
            setOpaque(false);
        }};
        ResourceRequirement[] price = developmentCard.getPrice();

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = price.length * 2;
        pricePanel.setBorder(border);

        int j = 0;
        for (ResourceRequirement requirement : price) {
            ResourceRequirementPrinter printer = new ResourceRequirementPrinter(requirement);
            printer.print(pricePanel, j);
            j += 2;
        }
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;

        add(pricePanel, gbc);
    }

    private void addProductionPower() {
        GridBagConstraints gbc = new GridBagConstraints();

        ProductionPowerPanel ppp = new ProductionPowerPanel(developmentCard.getProductionPower());

        Border innerEmptyBorder = new EmptyBorder(5, 15, 2, 15);
        Border lineBorder = new LineBorder(Color.BLACK);

        CompoundBorder innerBorder = new CompoundBorder(lineBorder, innerEmptyBorder);

        Border outerEmptyBorder = new EmptyBorder(5, 0, 5, 0);

        CompoundBorder border = new CompoundBorder(outerEmptyBorder, innerBorder);

        ppp.setBorder(border);

        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.gridy = 1;
        add(ppp, gbc);
    }

    private void addVictoryPoints(int victoryPoints) {
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel victoryPane = new JPanel(new GridLayout(0, 1)) {{
            setOpaque(false);
        }};
        JLabel info = new JLabel("Victory Points");
        victoryPane.setAlignmentY(CENTER_ALIGNMENT);
        JLabel victoryLabel = new JLabel("" + victoryPoints) {{
            setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
            setHorizontalAlignment(CENTER);
        }};
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.SOUTH;

        victoryPane.add(info);
        victoryPane.add(victoryLabel);

        add(victoryPane, gbc);
    }

    private void addRequirements() {
        Border emptyBorder = new EmptyBorder(5, 5, 5, 5);
        Border lineBorder = new LineBorder(Color.BLACK);
        CompoundBorder border = new CompoundBorder(lineBorder, emptyBorder);

        GridBagConstraints gbc;
        JPanel requirementPanel = new JPanel(new GridBagLayout()) {{
            setOpaque(false);
        }};
        Requirement[] requirements = leaderCard.getRequirements();


        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = requirements.length * 2;
        requirementPanel.setBorder(border);

        RequirementDecorator decorator;

        int j = 0;
        for (Requirement requirement : requirements) {
            decorator = new RequirementDecorator(requirement, requirementPanel, j);
            j += 2;
        }
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;

        add(requirementPanel, gbc);
    }

    private void addAbility() {
        SpecialAbilityDecorator decorator = new SpecialAbilityDecorator(leaderCard.getAbility(), this);
    }

}
