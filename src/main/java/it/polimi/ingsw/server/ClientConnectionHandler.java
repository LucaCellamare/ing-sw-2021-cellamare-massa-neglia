package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.ClientVisitor;
import it.polimi.ingsw.messages.CloseConnection;
import it.polimi.ingsw.messages.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Handler class of the connection with a specific client.
 * Used to communicate with it, it contains methods to send a message to him and to receive a message from him.
 *
 * @author Roberto Neglia
 */
public class ClientConnectionHandler extends Thread {

    private final Server server;

    /**
     * Socket used to communicate with the client.
     */
    private final Socket client;

    /**
     * Reference to the virtual view, server side implementation of the view part of the MVC pattern.
     */
    private final VirtualView virtualView;

    /**
     * Tells if the client is still connected (the socket is still open).
     */
    private boolean keepAlive = true;

    /**
     * Index of the client handler inside the waiting list.
     */
    private int waitingPlayerIndex;

    /**
     * Index of the client handler inside the connected list.
     */
    private int connectedPlayerIndex;

    /**
     * Output stream used to write object to the client.
     */
    private ObjectOutputStream oos;

    /**
     * Input stream to receive a object from the client.
     */
    private ObjectInputStream ois;

    /**
     * Class constructor
     *
     * @param client             socket used to communicate with the client
     * @param waitingPlayerIndex initial position of the client handler inside the waiting list
     * @param virtualView        reference to the virtual view
     */
    public ClientConnectionHandler(Server server, Socket client, int waitingPlayerIndex, VirtualView virtualView) {
        this.server = server;
        this.client = client;
        this.waitingPlayerIndex = waitingPlayerIndex;
        this.virtualView = virtualView;
        try {
            oos = new ObjectOutputStream(client.getOutputStream());
            ois = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the position of the client handler inside the waiting list.
     *
     * @param waitingPlayerIndex the position inside the waiting list
     */
    public void setWaitingPlayerIndex(int waitingPlayerIndex) {
        this.waitingPlayerIndex = waitingPlayerIndex;
    }

    /**
     * Sets the position of the client handler inside the connected list.
     *
     * @param connectedPlayerIndex the position inside the connected list
     */
    public void setConnectedPlayerIndex(int connectedPlayerIndex) {
        this.connectedPlayerIndex = connectedPlayerIndex;
    }

    /**
     * Change the flag value
     *
     * @param keepAlive true if the client is connected, false if he has disconnected
     */
    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    /**
     * Tells if a client is still connected to the server
     *
     * @return true if it's still connected, false if not
     */
    public boolean isStillAlive() {
        return keepAlive;
    }

    /**
     * Method overridden from Java Thread class, executed when a handler is started
     */
    public void run() {
        virtualView.newPlayer(waitingPlayerIndex); // sends the initial message to the client newly connected
        startListening(); // starts listening to the stream for messages received from the client
    }

    /**
     * The handler starts listening for messages on the input stream of the socket
     */
    public void startListening() {
        while (keepAlive) {
            Message received = null;
            try {
                received = (Message) ois.readObject(); // reads a message from the input stream
            } catch (SocketException | ClassNotFoundException e) {
                handleDisconnection();
                keepAlive = false;
            } catch (EOFException e) {
                handleDisconnection();
            } catch (IOException e) {
                handleDisconnection();
                e.printStackTrace();
            }
            if (received instanceof CloseConnection) { // receives a close connection message, the client wants to disconnect
                System.out.println("[ SERVER ] closing connection with " + client);
                sendMessage(new CloseConnection());
                close();
                setKeepAlive(false);
            }
            if (received != null) {
                received.setSender(connectedPlayerIndex);
                received.read(new ClientVisitor(virtualView)); // reads the received message using the visitor pattern
            }
        }
        close();
    }

    /**
     * Sends the message to the client on the output stream of the socket
     *
     * @param m the message to be sent
     */
    public void sendMessage(Message m) {
        try {
            if (keepAlive) {
                oos.writeObject(m); // writes the message on the output stream
                oos.reset();
            }
        } catch (SocketException e){
            System.out.println("SocketException sendmessage");

        } catch (IOException e) {
            System.out.println("IOEXCEPTION sendmessage");
        }
    }

    /**
     * Handles the disconnection of the player from the server
     */
    public void handleDisconnection() {
        virtualView.handleDisconnectedPlayer(connectedPlayerIndex);

        close();
    }


    /**
     * Closes the socket
     */
    public void close() {
        keepAlive = false;
        try {
            ois.close();
            oos.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
