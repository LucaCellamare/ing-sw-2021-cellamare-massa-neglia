package it.polimi.ingsw.client.view.CustomSwing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class NamePlayerPanel extends JPanel {
    private final JTextField namePlayer;

    public NamePlayerPanel() {
        super(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        JLabel welcome = new JLabel("Welcome to Maestri del Rinascimento") {{
            setFont(new Font(Font.DIALOG, Font.BOLD, 36));
            setBorder(new EmptyBorder(15, 0, 25, 0));
        }};
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;

        add(welcome, gbc);

        JLabel info = new JLabel("Please insert your nickname for this game") {{
            setFont(new Font(Font.DIALOG, Font.BOLD, 20));
            setBorder(new EmptyBorder(15, 0, 25, 0));
        }};

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.1;
        gbc.gridwidth = 2;

        add(info, gbc);

        JLabel askName = new JLabel("What's your nickname?");
        gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.LINE_START;

        add(askName, gbc);

        namePlayer = new JTextField(15);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.LINE_END;

        add(namePlayer, gbc);

    }

    public String getNamePlayerText() {
        return namePlayer.getText();
    }

    public void setNamePlayerText(String namePlayer) {
        this.namePlayer.setText(namePlayer);
    }

    public void setTextFieldListener(ActionListener e) {
        namePlayer.addActionListener(e);
    }
}
