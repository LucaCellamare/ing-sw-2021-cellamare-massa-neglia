package it.polimi.ingsw.client.view.CustomSwing;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Objects;

public class ShowNewResourcesPanel extends JPanel {

    public ShowNewResourcesPanel(EnumMap<ResourceEnum, Integer> resources) {
        super(new GridBagLayout());
        JLabel title = new JLabel("Resources");
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 0.05;
        gbc.weighty = 0.05;

        gbc.insets = new Insets(3, 0, 5, 0);
        add(title, gbc);


        ImageIcon imageShield = null;
        ImageIcon imageServants = null;
        ImageIcon imageCoin = null;
        ImageIcon imageStone = null;
        try {
            imageShield = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/shield.jpg"))));
            imageShield = new ImageIcon(getScaledImage(imageShield.getImage(), 70, 70));

            imageServants = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/servant.jpg"))));
            imageServants = new ImageIcon(getScaledImage(imageServants.getImage(), 70, 70));

            imageCoin = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/coin.jpg"))));
            imageCoin = new ImageIcon(getScaledImage(imageCoin.getImage(), 70, 70));

            imageStone = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/stone.jpg"))));
            imageStone = new ImageIcon(getScaledImage(imageStone.getImage(), 70, 70));

        } catch (Exception e) {
            System.out.println("missing a image");
        }

        JLabel shieldLabel = new JLabel(imageShield);
        JLabel servantLabel = new JLabel(imageServants);
        JLabel coinLabel = new JLabel(imageCoin);
        JLabel stoneLabel = new JLabel(imageStone);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;

        add(shieldLabel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.05;
        gbc.weighty = 0.05;

        int countShield = resources.get(ResourceEnum.SHIELD) != null ? resources.get(ResourceEnum.SHIELD) : 0;
        add(new JLabel("x" + countShield), gbc);


        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;

        add(servantLabel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.05;
        gbc.weighty = 0.05;

        int countServant = resources.get(ResourceEnum.SERVANT) != null ? resources.get(ResourceEnum.SERVANT) : 0;

        add(new JLabel("x" + countServant), gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;

        add(coinLabel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0.05;
        gbc.weighty = 0.05;

        int countCoin = resources.get(ResourceEnum.COIN) != null ? resources.get(ResourceEnum.COIN) : 0;
        add(new JLabel("x" + countCoin), gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;

        add(stoneLabel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 0.05;
        gbc.weighty = 0.05;

        int countStone = resources.get(ResourceEnum.STONE) != null ? resources.get(ResourceEnum.STONE) : 0;
        add(new JLabel("x" + countStone), gbc);
        repaint();
        validate();
    }

    private Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }
}
