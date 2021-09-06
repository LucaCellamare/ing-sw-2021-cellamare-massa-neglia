package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.ServerVisitor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class that handles the communication with the server
 *
 * @author Roberto Neglia
 */
public class NetworkHandler extends Thread {

    /**
     * Socket used to communicate with the server
     */
    Socket server;

    /**
     * Output stream used to write messages on the socket
     */
    ObjectOutputStream oos;

    /**
     * Input stream used to read messages from the socket
     */
    ObjectInputStream ois;

    /**
     * Reference to the view used to interact with the user
     */
    View clientView;

    /**
     * Flag that indicates if the connection with the server is still open
     */
    private boolean open;

    /**
     * Class constructor
     *
     * @param server     reference to the socket for the communication with the server
     * @param clientView reference to the view
     */
    public NetworkHandler(Socket server, View clientView) {
        this.server = server;
        this.clientView = clientView;
        open = true;
        try {
            oos = new ObjectOutputStream(server.getOutputStream());
            ois = new ObjectInputStream(server.getInputStream());
        } catch (IOException e) {
            System.out.println("network handler error");
        }
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * Sends to the server by writing it on the output stream
     *
     * @param m message to be sent
     */
    public void sendMessage(Message m) {
        try {
            oos.writeObject(m);
            oos.reset();
        } catch (IOException e) {
            System.out.println("Send Message error");
        }
    }

    /**
     * Receives a message from the server by reading on the input stream and reads it
     */
    public void receiveMessage() {
        SendMessageToServer sendToServer = new SendMessageToServer(this);
        clientView.setSendToServer(sendToServer);
        Message received = null;
        while (open) {
            try {
                received = (Message) ois.readObject();
                received.read(new ServerVisitor(clientView));
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Server not reachable, closing game....");
                System.exit(0);
            }
        }
    }
}
