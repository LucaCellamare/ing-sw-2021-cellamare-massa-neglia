package it.polimi.ingsw.client.view.CustomSwing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class NumberOfPlayersPanel extends JPanel {
    private final JTextField numberOfPlayers;

    public NumberOfPlayersPanel() {
        super(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        JLabel welcome = new JLabel("Welcome to Maestri del Rinascimento") {{
            setFont(new Font(Font.DIALOG, Font.BOLD, 36));
            setBorder(new EmptyBorder(0, 0, 35, 0));
        }};
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;

        add(welcome, gbc);

        JLabel info = new JLabel("Please insert the number of players in this game") {{
            setFont(new Font(Font.DIALOG, Font.BOLD, 20));
            setBorder(new EmptyBorder(15, 0, 25, 0));
        }};

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.1;
        gbc.gridwidth = 2;

        add(info, gbc);

        JLabel askNumberOfPlayers = new JLabel("How many players?");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.LINE_START;

        add(askNumberOfPlayers, gbc);

        numberOfPlayers = new JTextField(15);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.LINE_END;

        add(numberOfPlayers, gbc);

    }

    public String getNumberOfPlayer() {
        return numberOfPlayers.getText();
    }

    public void removeListenerFromTextField() {
        for(ActionListener al : numberOfPlayers.getActionListeners())
            numberOfPlayers.removeActionListener(al);
    }

    public void setNumberOfPlayers(String numberOfPlayers) {
        this.numberOfPlayers.setText(numberOfPlayers);
    }

    public void setTextFieldListener(ActionListener e) {
        numberOfPlayers.addActionListener(e);
    }

}
