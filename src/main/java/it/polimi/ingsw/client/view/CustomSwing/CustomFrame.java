package it.polimi.ingsw.client.view.CustomSwing;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.utils.ImageUtil;
import it.polimi.ingsw.utils.StringUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class CustomFrame extends JFrame {
    private final JTextField ipAddressField;
    private final JTextField portField;
    private final int WIDTH = 1288;
    private final int HEIGHT = 800;

    public CustomFrame() {
        super("Maestri del Rinascimento");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(100,70);
        setSize(WIDTH, HEIGHT);
        setResizable(false);


        try {
            ImageIcon backgroundImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/sfondo.jpg"))));
            backgroundImage = new ImageIcon(ImageUtil.getScaledImage(backgroundImage.getImage(), WIDTH, HEIGHT));
            setContentPane(new JLabel(backgroundImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        JLabel lblLogin = new JLabel("Login");
        lblLogin.setForeground(Color.WHITE);
        lblLogin.setFont(new Font("Times New Roman", Font.PLAIN, 46));

        JPanel panelLogin = new JPanel();
        panelLogin.setOpaque(false);
        panelLogin.setLayout(new GridBagLayout());


        ipAddressField = new JTextField(15);
        ipAddressField.setFont(new Font("Tahoma", Font.PLAIN, 32));

        portField = new JTextField(15);
        portField.setFont(new Font("Tahoma", Font.PLAIN, 32));

        JLabel lblIpAddress = new JLabel("Ip Address:");
        lblIpAddress.setBackground(Color.WHITE);
        lblIpAddress.setForeground(Color.WHITE);
        lblIpAddress.setFont(new Font("Tahoma", Font.PLAIN, 32));

        JLabel lblPort = new JLabel("Port Number:");
        lblPort.setForeground(Color.WHITE);
        lblPort.setBackground(Color.WHITE);
        lblPort.setFont(new Font("Tahoma", Font.PLAIN, 32));

        JButton btnLogin = new JButton("Connect to server");
        btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 26));
        btnLogin.addActionListener(e -> {
            if (StringUtils.isNumeric(portField.getText())) {
                if (checkIpAndPortCorrectness(ipAddressField.getText(), Integer.parseInt(portField.getText()))) {
                    Client.setIpAddress(String.valueOf(ipAddressField.getText()));
                    Client.setPortNumber(Integer.parseInt(String.valueOf(portField.getText())));
                    synchronized (Client.WAIT_FOR_IP_AND_PORT) {
                        Client.WAIT_FOR_IP_AND_PORT.notify();
                    }
                } else {
                    ipAddressField.setText("");
                    portField.setText("");
                    JOptionPane.showMessageDialog(this,
                            "Ip Address or Port Number are invalid. Retry",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                }
            } else {
                ipAddressField.setText("");
                portField.setText("");
                JOptionPane.showMessageDialog(this,
                        "Ip Address or Port Number are invalid. Retry",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }

        });

        GridBagConstraints gbc = new GridBagConstraints();


        //lblIpAddress GBC
        gbc.gridx = 0;
        gbc.gridy = 1;

        gbc.weightx = 0.2; // peso
        gbc.weighty = 0.2; // peso

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 0, 0, 6);
        panelLogin.add(lblIpAddress, gbc);
        //FINE GBC labeldepot1

        //ipAddressField GBC
        gbc.gridx = 1;
        gbc.gridy = 1;

        gbc.weightx = 0.2; // peso
        gbc.weighty = 0.2; // peso

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);

        panelLogin.add(ipAddressField, gbc);
        //FINE GBC ipAddressField

        //lblPOrt GBC
        gbc.gridx = 0;
        gbc.gridy = 2;

        gbc.weightx = 0.2; // peso
        gbc.weighty = 0.2; // peso

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 0, 0, 6);

        panelLogin.add(lblPort, gbc);
        //FINE GBC lblPOrt

        //portField GBC
        gbc.gridx = 1;
        gbc.gridy = 2;

        gbc.weightx = 0.2; // peso
        gbc.weighty = 0.2; // peso

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);

        panelLogin.add(portField, gbc);
        //FINE GBC portField

        //btnLogin GBC
        gbc.gridx = 0;
        gbc.gridy = 3;

        gbc.weightx = 1.0; // peso
        gbc.weighty = 1.0; // peso

        gbc.gridwidth = 2;
        gbc.gridheight = 1;

        gbc.anchor = GridBagConstraints.CENTER; // di default center

        gbc.ipadx = 20; //paddding interno del bottone
        gbc.ipady = 10;
        panelLogin.add(btnLogin, gbc);
        //fine btnLogin

        //lblLogin GBC
        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.weightx = 0.2; // peso
        gbc.weighty = 0.2; // peso

        gbc.gridwidth = 2;
        gbc.gridheight = 1;

        gbc.anchor = GridBagConstraints.CENTER; // di default center
        panelLogin.add(lblLogin, gbc);
        //FINE GBC lblLogin
        contentPane.add(panelLogin);
    }

    public void setIPAddressFieldVisible(String defaultIp) {
        ipAddressField.setText(defaultIp);
        ipAddressField.setVisible(true);
    }

    public void setPortNumberFieldVisible(int defaultPort) {
        portField.setText(String.valueOf(defaultPort));
        portField.setVisible(true);
    }

    private boolean checkIpAndPortCorrectness(String ip, int port) {
        return StringUtils.isIpAddress(ipAddressField.getText()) && StringUtils.isNumeric(String.valueOf(port)) && (port >= 1024 && port <= 65535);
    }

}
