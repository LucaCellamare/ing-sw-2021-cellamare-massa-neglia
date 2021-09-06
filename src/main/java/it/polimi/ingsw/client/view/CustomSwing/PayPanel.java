package it.polimi.ingsw.client.view.CustomSwing;

import it.polimi.ingsw.client.SendMessageToServer;
import it.polimi.ingsw.messages.ChooseResourceLocation;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.PersonalBoard.Strongbox;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Warehouse;
import it.polimi.ingsw.server.Model.Requirements.ResourceRequirement;
import it.polimi.ingsw.utils.ImageUtil;
import it.polimi.ingsw.utils.StringUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Objects;

public class PayPanel extends JPanel {
    private final JFrame gameFrame;
    private final SendMessageToServer sendToServer;
    private final Warehouse playerWarehouse;
    private final Strongbox playerStrongbox;
    private final ArrayList<ResourceRequirement> toPay;
    private final EnumMap<ResourceEnum, Integer> payWarehouse = new EnumMap<>(ResourceEnum.class);
    private final EnumMap<ResourceEnum, Integer> payStrongbox = new EnumMap<>(ResourceEnum.class);
    private final JPanel warehouseInfoPane;
    private final JPanel strongboxInfoPane;
    private WarehousePanel warehousePane;
    private StrongboxPanel strongboxPane;
    private JPanel resourcesPane;
    private JButton warehouseButton;
    private JButton strongboxButton = new JButton("take from Strongbox");
    private JPanel fromWarehousePane;
    private JPanel fromStrongboxPane;

    private JTextField fromWarehouseTextField;
    private JTextField fromStrongboxTextField;

    private JDialog askResourceType;

    public PayPanel(JFrame frame, SendMessageToServer sendToServer, ResourceRequirement[] toPay, Warehouse playerWarehouse, Strongbox playerStrongbox) {
        super(new GridBagLayout());

        this.sendToServer = sendToServer;
        gameFrame = frame;

        this.playerWarehouse = playerWarehouse;
        this.playerStrongbox = playerStrongbox;

        GridBagConstraints gbc;
        this.toPay = new ArrayList<>(Arrays.asList(toPay));

        JLabel infoPay = new JLabel("you have to pay the following resources: ");
        infoPay.setFont(new Font(Font.DIALOG, Font.BOLD, 20));

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        add(infoPay, gbc);

        resourcesPane = new JPanel(new GridBagLayout());

        Border innerEmptyBorder = new EmptyBorder(10, 5, 10, 5);
        Border lineBorder = new LineBorder(Color.BLACK);

        CompoundBorder innerBorder = new CompoundBorder(lineBorder, innerEmptyBorder);
        Border outerEmptyBorder = new EmptyBorder(5, 0, 5, 0);

        CompoundBorder border = new CompoundBorder(outerEmptyBorder, innerBorder);

        resourcesPane.setBorder(border);

        updateResourcePane();

        warehouseInfoPane = new JPanel(new GridBagLayout());

        JLabel infoWarehouse;
        infoWarehouse = new JLabel("this is your warehouse:");
        infoWarehouse.setHorizontalAlignment(SwingConstants.CENTER);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        warehouseInfoPane.add(infoWarehouse, gbc);

        warehousePane = new WarehousePanel(playerWarehouse);

        updateWarehousePane();

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;

        add(warehouseInfoPane, gbc);

        strongboxInfoPane = new JPanel(new GridBagLayout());

        JLabel infoStrongbox;
        infoStrongbox = new JLabel("this is your strongbox:");
        infoStrongbox.setHorizontalAlignment(SwingConstants.CENTER);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        strongboxInfoPane.add(infoStrongbox, gbc);

        strongboxPane = new StrongboxPanel(playerStrongbox);

        updateStrongboxPane();


        innerEmptyBorder = new EmptyBorder(5, 5, 5, 5);
        lineBorder = new LineBorder(Color.BLACK);

        innerBorder = new CompoundBorder(lineBorder, innerEmptyBorder);

        outerEmptyBorder = new EmptyBorder(5, 0, 5, 10);

        border = new CompoundBorder(outerEmptyBorder, innerBorder);

        strongboxInfoPane.setBorder(border);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;

        add(strongboxInfoPane, gbc);

        initializeInvisible();
    }

    private void updateResourcePane() {
        remove(resourcesPane);
        resourcesPane = new JPanel(new GridBagLayout());

        GridBagConstraints gbc;
        ImageIcon icon = null;
        JLabel iconLabel, numberLabel;
        JButton resourceButton;
        Action payResourceAction;

        ResourceRequirement currRequirement;
        for (int i = 0; i < toPay.size(); i++) {
            currRequirement = toPay.get(i);

            try {
                icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/" + currRequirement.getResourceType().toString().toLowerCase() + ".jpg"))));
                icon = new ImageIcon(ImageUtil.getScaledImage(icon.getImage(), 50, 50));
            } catch (IOException e) {
                e.printStackTrace();
            }

            resourceButton = new JButton();
            resourceButton.setLayout(new GridBagLayout());
            resourceButton.setContentAreaFilled(false);
            payResourceAction = new PayResourceAction(currRequirement);
            resourceButton.addActionListener(payResourceAction);

            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            iconLabel = new JLabel(icon);

            resourceButton.add(iconLabel, gbc);

            gbc = new GridBagConstraints();
            gbc.gridx = 1;
            gbc.gridy = 0;
            numberLabel = new JLabel(" x" + currRequirement.getQuantity() + " ");
            resourceButton.add(numberLabel, gbc);

            gbc = new GridBagConstraints();
            gbc.gridx = i;
            gbc.gridy = 0;

            resourcesPane.add(resourceButton, gbc);
        }

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;

        add(resourcesPane, gbc);
        resourcesPane.revalidate();
        resourcesPane.repaint();
        revalidate();
        repaint();
    }

    private void updateWarehousePane() {
        GridBagConstraints gbc;

        warehouseInfoPane.remove(warehousePane);

        warehousePane = new WarehousePanel(playerWarehouse);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        warehouseInfoPane.add(warehousePane, gbc);

        repaint();
        revalidate();
    }

    private void updateStrongboxPane() {
        GridBagConstraints gbc;

        strongboxInfoPane.remove(strongboxPane);

        strongboxPane = new StrongboxPanel(playerStrongbox);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;

        strongboxInfoPane.add(strongboxPane, gbc);

        repaint();
        revalidate();

    }

    private void initializeInvisible() {
        GridBagConstraints gbc;

        warehouseButton = new JButton("take from Warehouse");
        warehouseButton.setVisible(false);
        strongboxButton = new JButton("take from Strongbox");
        strongboxButton.setVisible(false);

        fromWarehousePane = new JPanel(new GridBagLayout());
        fromWarehousePane.setVisible(false);
        JLabel howMany = new JLabel("How many?");
        fromWarehouseTextField = new JTextField(7);

        Border emptyBorder = new EmptyBorder(0, 10, 0, 0);

        fromWarehousePane.setBorder(emptyBorder);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        fromWarehousePane.add(howMany, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        fromWarehousePane.add(fromWarehouseTextField, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        warehouseInfoPane.add(fromWarehousePane, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2
        ;
        warehouseInfoPane.add(warehouseButton, gbc);

        fromStrongboxPane = new JPanel(new GridBagLayout());
        fromStrongboxPane.setVisible(false);
        howMany = new JLabel("How many?");
        fromStrongboxTextField = new JTextField(7);

        emptyBorder = new EmptyBorder(0, 0, 0, 10);

        fromStrongboxPane.setBorder(emptyBorder);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        fromStrongboxPane.add(howMany, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;

        fromStrongboxPane.add(fromStrongboxTextField, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        strongboxInfoPane.add(fromStrongboxPane, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        strongboxInfoPane.add(strongboxButton, gbc);
    }

    private void selectWhere(ResourceRequirement resourceSelected) {
        if (resourceSelected.getResourceType() == ResourceEnum.JOLLY)
            handleJollyResource(resourceSelected);
        else {
            int inWarehouse, inStrongbox;

            inWarehouse = playerWarehouse.getResourceAmount(resourceSelected.getResourceType());
            inStrongbox = playerStrongbox.getResourceCount(resourceSelected.getResourceType());

            Action takeFromWarehouseAction;
            Action takeFromStrongboxAction;

            if (inWarehouse != 0 && inStrongbox != 0) {
                for (ActionListener al : warehouseButton.getActionListeners())
                    warehouseButton.removeActionListener(al);
                takeFromWarehouseAction = new TakeFromWarehouseAction(resourceSelected, inWarehouse);
                warehouseButton.addActionListener(takeFromWarehouseAction);

                for (ActionListener al : strongboxButton.getActionListeners())
                    strongboxButton.removeActionListener(al);
                takeFromStrongboxAction = new TakeFromStrongboxAction(resourceSelected, inStrongbox);
                strongboxButton.addActionListener(takeFromStrongboxAction);

                fromWarehousePane.setVisible(true);
                warehouseButton.setVisible(true);
                fromStrongboxPane.setVisible(true);
                strongboxButton.setVisible(true);
            } else if (inWarehouse != 0) {
                for (ActionListener al : warehouseButton.getActionListeners())
                    warehouseButton.removeActionListener(al);
                takeFromWarehouseAction = new TakeFromWarehouseAction(resourceSelected, inWarehouse);
                warehouseButton.addActionListener(takeFromWarehouseAction);

                fromWarehousePane.setVisible(true);
                warehouseButton.setVisible(true);
                fromStrongboxPane.setVisible(false);
                strongboxButton.setVisible(false);
            } else if (inStrongbox != 0) {
                for (ActionListener al : strongboxButton.getActionListeners())
                    strongboxButton.removeActionListener(al);
                takeFromStrongboxAction = new TakeFromStrongboxAction(resourceSelected, inStrongbox);
                strongboxButton.addActionListener(takeFromStrongboxAction);

                fromWarehousePane.setVisible(false);
                warehouseButton.setVisible(false);
                fromStrongboxPane.setVisible(true);
                strongboxButton.setVisible(true);
            } else {
                fromWarehousePane.setVisible(false);
                warehouseButton.setVisible(false);
                fromStrongboxPane.setVisible(false);
                strongboxButton.setVisible(false);
            }
        }

        validate();
    }

    private void handleJollyResource(ResourceRequirement resourceSelected) {
        askResourceType = new JDialog(gameFrame, "Choose the jolly resource", Dialog.ModalityType.APPLICATION_MODAL);
        askResourceType.setLayout(new GridBagLayout());

        GridBagConstraints gbc;
        JPanel chooseResourcePanel = new JPanel(new GridBagLayout());
        ImageIcon icon = null;
        JLabel iconLabel, chooseResourceInfo;
        JButton chosenResourceButton;

        ResourceEnum[] resources = Arrays.copyOfRange(ResourceEnum.values(), 0, 4);

        chooseResourceInfo = new JLabel("select which resource you want to take as a jolly");

        gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = resources.length;

        chooseResourcePanel.add(chooseResourceInfo, gbc);

        Action chooseResourceAction;

        for (int i = 0; i < resources.length; i++) {

            try {
                icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/" + resources[i].toString().toLowerCase() + ".jpg"))));
                icon = new ImageIcon(ImageUtil.getScaledImage(icon.getImage(), 55, 55));
            } catch (IOException e) {
                e.printStackTrace();
            }

            chosenResourceButton = new JButton();
            chosenResourceButton.setLayout(new GridBagLayout());
            chosenResourceButton.setContentAreaFilled(false);

            chooseResourceAction = new ChooseJollyResourceAction(resources[i], resourceSelected.getQuantity());
            chosenResourceButton.addActionListener(chooseResourceAction);

            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            iconLabel = new JLabel(icon);

            chosenResourceButton.add(iconLabel, gbc);

            gbc = new GridBagConstraints();
            gbc.gridx = i;
            gbc.gridy = 1;

            chooseResourcePanel.add(chosenResourceButton, gbc);
        }

        askResourceType.add(chooseResourcePanel);

        askResourceType.pack();

        askResourceType.setVisible(true);
    }

    class TakeFromStrongboxAction extends AbstractAction {
        private final ResourceRequirement resourceSelected;
        private final int inStrongbox;

        public TakeFromStrongboxAction(ResourceRequirement resourceSelected, int inStrongbox) {
            this.resourceSelected = resourceSelected;
            this.inStrongbox = inStrongbox;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ResourceRequirement updatedResourceSelected;
            String answer = fromStrongboxTextField.getText();
            if (StringUtils.isNumeric(answer)) {
                int qty = Integer.parseInt(answer);
                if(qty == 0){
                    JOptionPane.showMessageDialog(gameFrame,
                            "You need to insert a number > 0",
                            "Alt",
                            JOptionPane.WARNING_MESSAGE);
                }
                else if (qty > 0 && qty <= inStrongbox) {
                    if (qty <= resourceSelected.getQuantity()) {
                        playerStrongbox.remove(resourceSelected.getResourceType(), qty);
                        if (payStrongbox.containsKey(resourceSelected.getResourceType()))
                            payStrongbox.put(resourceSelected.getResourceType(), payStrongbox.get(resourceSelected.getResourceType()) + qty);
                        else payStrongbox.put(resourceSelected.getResourceType(), qty);

                        if (qty < resourceSelected.getQuantity()) {
                            updatedResourceSelected = new ResourceRequirement(resourceSelected.getResourceType(), resourceSelected.getQuantity() - qty);
                            toPay.set(toPay.indexOf(resourceSelected), updatedResourceSelected);
                        } else toPay.remove(resourceSelected);

                        updateResourcePane();
                        updateStrongboxPane();

                        fromWarehousePane.setVisible(false);
                        warehouseButton.setVisible(false);
                        fromStrongboxPane.setVisible(false);
                        strongboxButton.setVisible(false);

                        if (toPay.isEmpty()) {
                            JOptionPane.showMessageDialog(gameFrame, "all resources have been paid");
                            sendToServer.sendChosenResourceLocation(new ChooseResourceLocation(payWarehouse, payStrongbox));
                        }

                    } else JOptionPane.showMessageDialog(gameFrame,
                            "you are paying more than it needs, retry",
                            "Alt",
                            JOptionPane.WARNING_MESSAGE);
                } else JOptionPane.showMessageDialog(gameFrame,
                        "you don't have that much in your warehouse, retry",
                        "Alt",
                        JOptionPane.WARNING_MESSAGE);
            } else JOptionPane.showMessageDialog(gameFrame,
                    "that's not a number, retry",
                    "Alt",
                    JOptionPane.WARNING_MESSAGE);

        }
    }

    class TakeFromWarehouseAction extends AbstractAction {
        private final ResourceRequirement resourceSelected;
        private final int inWarehouse;

        public TakeFromWarehouseAction(ResourceRequirement resourceSelected, int inWarehouse) {
            this.resourceSelected = resourceSelected;
            this.inWarehouse = inWarehouse;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ResourceRequirement updatedResourceSelected;
            String answer = fromWarehouseTextField.getText();
            if (StringUtils.isNumeric(answer)) {
                int qty = Integer.parseInt(answer);
                if(qty == 0){
                    JOptionPane.showMessageDialog(gameFrame,
                            "You need to insert a number > 0",
                            "Alt",
                            JOptionPane.WARNING_MESSAGE);
                }
                else if (qty > 0 && qty <= inWarehouse) {
                    if (qty <= resourceSelected.getQuantity()) {
                        playerWarehouse.remove(resourceSelected.getResourceType(), qty);
                        if (payWarehouse.containsKey(resourceSelected.getResourceType()))
                            payWarehouse.put(resourceSelected.getResourceType(), payWarehouse.get(resourceSelected.getResourceType()) + qty);
                        else payWarehouse.put(resourceSelected.getResourceType(), qty);

                        if (qty < resourceSelected.getQuantity()) {
                            updatedResourceSelected = new ResourceRequirement(resourceSelected.getResourceType(), resourceSelected.getQuantity() - qty);
                            toPay.set(toPay.indexOf(resourceSelected), updatedResourceSelected);
                        } else toPay.remove(resourceSelected);

                        updateResourcePane();
                        updateWarehousePane();

                        fromWarehousePane.setVisible(false);
                        warehouseButton.setVisible(false);
                        fromStrongboxPane.setVisible(false);
                        strongboxButton.setVisible(false);

                        if (toPay.isEmpty()) {
                            JOptionPane.showMessageDialog(gameFrame, "all resources have been paid");
                            sendToServer.sendChosenResourceLocation(new ChooseResourceLocation(payWarehouse, payStrongbox));
                        }

                    } else JOptionPane.showMessageDialog(gameFrame,
                            "you are paying more than it needs, retry",
                            "Alt",
                            JOptionPane.WARNING_MESSAGE);
                } else JOptionPane.showMessageDialog(gameFrame,
                        "you don't have that much in your warehouse, retry",
                        "Alt",
                        JOptionPane.WARNING_MESSAGE);
            } else JOptionPane.showMessageDialog(gameFrame,
                    "that's not a number, retry",
                    "Alt",
                    JOptionPane.WARNING_MESSAGE);

        }
    }

    class PayResourceAction extends AbstractAction {
        private final ResourceRequirement resourceSelected;

        public PayResourceAction(ResourceRequirement resourceSelected) {
            this.resourceSelected = resourceSelected;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            selectWhere(resourceSelected);
        }
    }

    class ChooseJollyResourceAction extends AbstractAction {
        private final ResourceEnum resourceSelected;
        private final int quantity;

        public ChooseJollyResourceAction(ResourceEnum resourceSelected, int quantity) {
            this.resourceSelected = resourceSelected;
            this.quantity = quantity;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            ResourceRequirement req, oldReq;
            oldReq = toPay.stream().filter(r -> r.getResourceType() == resourceSelected).findFirst().orElse(new ResourceRequirement(resourceSelected, 0));
            if (playerWarehouse.getResourceAmount(resourceSelected) + playerStrongbox.getResourceCount(resourceSelected) >= quantity + oldReq.getQuantity()) {

                if (toPay.stream().anyMatch(r -> r.getResourceType() == resourceSelected)) {
                    req = new ResourceRequirement(resourceSelected, oldReq.getQuantity() + quantity);
                    toPay.set(toPay.indexOf(oldReq), req);
                } else {
                    req = new ResourceRequirement(resourceSelected, quantity);
                    toPay.add(req);
                }


                ResourceRequirement toRemove = new ResourceRequirement(ResourceEnum.JOLLY, quantity);

                toPay.remove(toRemove);

                updateResourcePane();
                askResourceType.setVisible(false);
            } else
                JOptionPane.showMessageDialog(gameFrame, "you don't have enough resources in your warehouse and strongbox for this resource, select another please");
        }
    }
}
