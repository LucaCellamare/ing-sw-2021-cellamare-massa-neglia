package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.CLIView;
import it.polimi.ingsw.client.view.GUIView;
import it.polimi.ingsw.client.view.View;

import java.io.IOException;
import java.net.Socket;

/*
    TODO
        create a thread for the network handler, or the user cannot insert commands in the cli
 */

/**
 * Executable class that starts a new client that connects to the server to play Maestri del Rinascimento
 *
 * @author Roberto Neglia
 */
public class Client {

    /**
     * Object used to handle synchronization for Ip and port
     */
    public static final Object WAIT_FOR_IP_AND_PORT = new Object();

    /**
     * Object used to handle synchronization for retry
     */
    public static final Object WAIT_FOR_RETRY = new Object();
    /**
     * Ip address of the server to connect to
     */
    private static String ipAddress;

    /**
     * Port number where the server is listening for the game
     */
    private static int portNumber;

    /**
     * View part of the MVC pattern, used to interact with the player
     */
    private static View clientView;

    /**
     * Boolean used to retry to establish connection
     */
    private static boolean retry = true;

    /**
     * Method to set the IpAddress
     */
    public static void setIpAddress(String ipAddress) {
        Client.ipAddress = ipAddress;
    }

    /**
     * Method to set the PortNumber
     */
    public static void setPortNumber(int portNumber) {
        Client.portNumber = portNumber;
    }

    /**
     * Method to retry to establish the connection to the server
     */
    public static void setRetry() {
        retry = true;
    }


    public static void main(String[] args) {
        // based on the args hew instantiates the CLIView or the GUIView:
        // I will just instantiates a CLIView for now

        if (args.length > 0) {
            if (args[0].equals("--CLI")) {
                clientView = new CLIView();

                while (retry) {
                    // asks the user where to connect
                    ipAddress = clientView.askIpAddress();
                    portNumber = clientView.askPortNumber();

                    Socket server;

                    try {

                        // tries to connect with the specified ip address and port number
                        server = new Socket(ipAddress, portNumber);
                        // if connects, creates a network handler
                        NetworkHandler net = new NetworkHandler(server, clientView);
                        net.receiveMessage(); // receives a message from the server
                        retry = false;
                    } catch (IOException e) {
                        System.out.println("cannot connect to server :\\");
                        System.exit(0);
                    }
                }
                System.out.println("ok, bye!");
                System.exit(0);

            } else if (args[0].equals("--GUI")) {
                retry = true;
                clientView = new GUIView();
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (retry) {
                    // asks the user where to connect
                    clientView.askIpAddress();
                    clientView.askPortNumber();

                    synchronized (WAIT_FOR_IP_AND_PORT) {
                        try {
                            WAIT_FOR_IP_AND_PORT.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                    Socket server;

                    try {

                        // tries to connect with the specified ip address and port number
                        server = new Socket(ipAddress, portNumber);
                        // if connects, creates a network handler
                        NetworkHandler net = new NetworkHandler(server, clientView);
                        net.receiveMessage(); // receives a message from the server
                        retry = false;
                    } catch (IOException e) {
                        System.out.println("cannot connect to server :\\");
                        System.exit(0);
//                        boolean tmp_retry;
//                        tmp_retry = clientView.askRetry();
//                        synchronized (WAIT_FOR_RETRY) {
//                            try {
//                                WAIT_FOR_RETRY.wait();
//                            } catch (InterruptedException ex) {
//                                ex.printStackTrace();
//                            }
//                        }
//                        tmp_retry = clientView.askRetry();
                    }
                }
                System.out.println("ok, bye!");
                System.exit(0);


            } else System.out.println("argument not recognized: add --CLI for cli, --GUI for gui");
        } else System.out.println("argument not recognized: add --CLI for cli, --GUI for gui");

    }

}
