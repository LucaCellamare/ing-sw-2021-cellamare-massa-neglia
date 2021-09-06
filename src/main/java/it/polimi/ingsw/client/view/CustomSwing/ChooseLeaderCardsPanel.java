package it.polimi.ingsw.client.view.CustomSwing;

import it.polimi.ingsw.server.Model.Cards.LeaderCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ChooseLeaderCardsPanel extends JPanel {

    private final List<JButton> buttons;


    private final List<Integer> chosenCards = new ArrayList<>();

//    public ChooseLeaderCardsPanel(List<LeaderCard> leaderCards){
//        super(new GridBagLayout());
//        buttonCard1 = new JButton();
//        buttonCard2 = new JButton();
//        buttonCard3 = new JButton();
//        buttonCard4 = new JButton();
//
//        Vector<String> leaderPaths = new Vector<>();
//        for (LeaderCard i : leaderCards) {
//            leaderPaths.add(i.getPath().strip());
//        }
//        try {
//            ImageIcon image = new ImageIcon(ImageIO.read(new File("src/main/resources/jpg/" + leaderPaths.get(0) + ".jpg")));
//            image = new ImageIcon(getScaledImage(image.getImage(), 200, 250));
//            buttonCard1.setIcon(image);
//            buttonCard1.setBorderPainted(false);
//            buttonCard1.setContentAreaFilled(false);
//            buttonCard1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//
//            image = new ImageIcon(ImageIO.read(new File("src/main/resources/jpg/" + leaderPaths.get(1) + ".jpg")));
//            image = new ImageIcon(getScaledImage(image.getImage(), 200, 250));
//            buttonCard2.setIcon(image);
//            buttonCard2.setBorderPainted(false);
//            buttonCard2.setContentAreaFilled(false);
//            buttonCard2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//
//            image = new ImageIcon(ImageIO.read(new File("src/main/resources/jpg/" + leaderPaths.get(2) + ".jpg")));
//            image = new ImageIcon(getScaledImage(image.getImage(), 200, 250));
//            buttonCard3.setIcon(image);
//            buttonCard3.setBorderPainted(false);
//            buttonCard3.setContentAreaFilled(false);
//            buttonCard3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//
//            image = new ImageIcon(ImageIO.read(new File("src/main/resources/jpg/" + leaderPaths.get(3) + ".jpg")));
//            image = new ImageIcon(getScaledImage(image.getImage(), 200, 250));
//            buttonCard4.setIcon(image);
//            buttonCard4.setBorderPainted(false);
//            buttonCard4.setContentAreaFilled(false);
//            buttonCard4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        buttonCard1.addActionListener(e -> {
//            if(chosenCards.size() == 0 || !chosenCards.contains(0))
//            {
//                chosenCards.add(0);
//                buttonCard1.setContentAreaFilled(true);
//                buttonCard1.setBackground(Color.YELLOW);
//            }else{
//                chosenCards.remove((Integer) 0);
//                buttonCard1.setContentAreaFilled(false);
//            }
//        });
//
//        buttonCard2.addActionListener(e -> {
//            if(chosenCards.size() == 0 || !chosenCards.contains(1))
//            {
//                chosenCards.add(1);
//                buttonCard2.setContentAreaFilled(true);
//                buttonCard2.setBackground(Color.YELLOW);
//            }else{
//                chosenCards.remove((Integer) 1);
//                buttonCard2.setContentAreaFilled(false);
//            }
//        });
//
//        buttonCard3.addActionListener(e -> {
//            if(chosenCards.size() == 0 || !chosenCards.contains(2))
//            {
//                chosenCards.add(2);
//                buttonCard3.setContentAreaFilled(true);
//                buttonCard3.setBackground(Color.YELLOW);
//            }else{
//                chosenCards.remove((Integer) 2);
//                buttonCard3.setContentAreaFilled(false);
//            }
//        });
//
//        buttonCard4.addActionListener(e -> {
//            if(chosenCards.size() == 0 || !chosenCards.contains(3))
//            {
//                chosenCards.add(3);
//                buttonCard4.setContentAreaFilled(true);
//                buttonCard4.setBackground(Color.YELLOW);
//            }else{
//                chosenCards.remove((Integer) 3);
//                buttonCard4.setContentAreaFilled(false);
//            }
//        });
//
//
//        JLabel lblNewLabel = new JLabel("YOU PICKED THE FOLLOWING LEADERCARDS");
//        lblNewLabel.setForeground(Color.BLACK);
//        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 36));
//        Insets insets = new Insets(-5,0,10,0);
//        GridBagConstraints gbc = new GridBagConstraints();
//
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//
//        gbc.weightx = 0.1;
//        gbc.weighty = 0.1;
//
//        gbc.gridwidth = 4;
//        gbc.gridheight = 1;
//
//        gbc.insets = insets;
//        add(lblNewLabel,gbc);
//
//        gbc = new GridBagConstraints();
//        insets = new Insets(5,0,5,0);
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//
//        gbc.weightx = 0.3;
//        gbc.weighty = 0.3;
//        gbc.insets = insets;
//
//        add(buttonCard1,gbc);
//
//        gbc = new GridBagConstraints();
//        gbc.gridx = 1;
//        gbc.gridy = 1;
//
//        gbc.weightx = 0.3;
//        gbc.weighty = 0.3;
//        gbc.insets = insets;
//
//        add(buttonCard2,gbc);
//
//        gbc = new GridBagConstraints();
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//
//        gbc.weightx = 0.3;
//        gbc.weighty = 0.3;
//        gbc.insets = insets;
//
//        add(buttonCard3,gbc);
//
//        gbc = new GridBagConstraints();
//        gbc.gridx = 1;
//        gbc.gridy = 2;
//
//        gbc.weightx = 0.3;
//        gbc.weighty = 0.3;
//        gbc.insets = insets;
//
//        add(buttonCard4,gbc);
//    }

    class SelectLeaderCardAction extends AbstractAction {
        private final int leaderPosition;

        public SelectLeaderCardAction(int leaderPosition) {
            this.leaderPosition = leaderPosition;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (chosenCards.size() == 0 || !chosenCards.contains(leaderPosition)) {
                chosenCards.add(leaderPosition);
                buttons.get(leaderPosition).setContentAreaFilled(true);
                buttons.get(leaderPosition).setBackground(Color.YELLOW);
            } else {
                chosenCards.remove((Integer) leaderPosition);
                buttons.get(leaderPosition).setContentAreaFilled(false);
            }
        }
    }

    public ChooseLeaderCardsPanel(List<LeaderCard> leaderCards) {
        super(new GridBagLayout());

        buttons = new ArrayList<>();

        GridBagConstraints gbc;

        JLabel lblNewLabel = new JLabel("YOU PICKED THE FOLLOWING LEADERCARDS");
        lblNewLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 36));
        Insets insets = new Insets(-5, 0, 10, 0);
        gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.weightx = 0.1;
        gbc.weighty = 0.1;

        gbc.gridwidth = 4;

        gbc.insets = insets;
        add(lblNewLabel, gbc);

        JButton temp;

        int k = 0;
        for (int i = 0; i < leaderCards.size() / 2; i++) {
            for (int j = 0; j < leaderCards.size() / 2; j++) {
                temp = new JButton();

                buttons.add(temp);

                temp.add(new CardPanel(leaderCards.get(k)));
                temp.setBorderPainted(false);
                temp.setContentAreaFilled(false);
                temp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                Action action = new SelectLeaderCardAction(k);
                temp.addActionListener(action);

                insets = new Insets(5, 0, 5, 0);
                gbc = new GridBagConstraints();
                gbc.gridx = i;
                gbc.gridy = j + 1;

                gbc.weightx = 0.3;
                gbc.weighty = 0.3;

                gbc.insets = insets;


                add(temp, gbc);
                k++;
            }
        }
    }

    public List<Integer> getChosenCards() {
        return chosenCards;
    }

}
