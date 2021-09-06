package it.polimi.ingsw.client.view.CustomSwing;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class PanelWhiteMarbleAndCount extends JPanel {

    private final JLabel imageWhiteMarble;


    public PanelWhiteMarbleAndCount(){
        super();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        imageWhiteMarble = new JLabel();
        try {
            imageWhiteMarble.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/white_marble.jpg")))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,0,0,10);
        add(imageWhiteMarble,gbc);
    }
}
