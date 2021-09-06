package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.utils.StringUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Executable class that starts a server for the Maestri del Rinascimento game.
 *
 * @author Roberto Neglia
 */
public class Server {
    private final static int DEFAULT_PORT_NUMBER = 4687;
    private final static int MIN_PORT_NUMBER = 1024;
    private final static int MAX_PORT_NUMBER = 65535;

    /**
     * Port number where the server listens to in order to connect with clients.
     */
    private static int portNumber;

    private static Scanner kbdScanner;

    /**
     * List of clients (handler) that are waiting to know if they can play or not.
     */
    private final ArrayList<ClientConnectionHandler> waitingClients = new ArrayList<>();

    /**
     * List of clients (handler) that are already accepted to a game.
     */
    private final ArrayList<ClientConnectionHandler> connectedClients = new ArrayList<>(); // clients that are already accepted to a game

    /*
    RESILIENZA DISCONNESSIONI:
        giocatore si disconnette, setto flag a disconnesso, connection handler si chiude, il server è ancora in ascolto
        il giocatore si riconnette, si crea una nuova socket:
                                                    - posso controllare già lato server che fosse un giocatore collegato (con la mappa nickname -> posizione nella lista dei client connessi)
                                                    - posso rifare il percorso di inizializzazione del giocatore e controllare dal model i nickname, e nel caso ci sia impostare la posizione in connected clients in base alla posizione del player nella lista dei giocatori del model
     */


    /**
     * Utility map used to solve the index of the connected clients inside the list based on their nickname.
     */
    private final HashMap<String, Integer> connectedNicknameSolver;

    /**
     * Class constructor
     *
     * @param portNumber port number where the server starts listening for connections
     */
    public Server(int portNumber) {
        Server.portNumber = portNumber;
        connectedNicknameSolver = new HashMap<>();
    }

    /**
     * Entry point to start the executable.
     */
    public static void main(String[] args) {
        boolean retry = true;
        kbdScanner = new Scanner(System.in);

        ServerSocket serverSocket = null;
        Server server = null;

        while (retry) {
            setPortNumber(); // asks for a port number to listen to
            server = new Server(portNumber);
            try {
                serverSocket = new ServerSocket(portNumber); // creates a server socket to accept multiple clients connections
                retry = false;
            } catch (IOException e) {
                System.out.println("[ SERVER ] couldn't start a socket on that port, i'm sorry");
                System.out.println("[ SERVER ] want to retry?");
                String answer = kbdScanner.nextLine();
                if (!answer.equals("yes")) {
                    System.out.println("[ SERVER ] ok, bye!");
                    retry = false;
                }
            }
        }
        System.out.println("[ SERVER ] waiting for clients to connect...");
        assert serverSocket != null;
        server.acceptWaitingClients(serverSocket); // method that accepts a connection with new clients
        kbdScanner.close();
        System.exit(0);
    }

    /**
     * Asks the user for a port number to listen to.
     */
    private static void setPortNumber() { // asks for a port number to listen to
        String answer;
        portNumber = -1;
        while (portNumber == -1) {
            System.out.println("[ SERVER ] which port should i listen to? [DEFAULT: " + DEFAULT_PORT_NUMBER + "]");
            answer = kbdScanner.nextLine();
            if (answer.isEmpty()) portNumber = DEFAULT_PORT_NUMBER;
            else if (StringUtils.isNumeric(answer)) {
                portNumber = Integer.parseInt(answer);
                if (portNumber < MIN_PORT_NUMBER || portNumber > MAX_PORT_NUMBER) {
                    System.out.println("[ SERVER ] sorry, but that's an illegal port");
                    portNumber = -1;
                }
            } else System.out.println("[ SERVER ] NaN [NOT A NUMBER]");
        }
    }

    /**
     * Connected clients getter.
     *
     * @return the list of connected clients handlers
     */
    public ArrayList<ClientConnectionHandler> getConnectedClients() {
        return connectedClients;
    }

    /**
     * Waiting clients getter.
     *
     * @return the list of waiting clients handlers
     */
    public ArrayList<ClientConnectionHandler> getWaitingClients() {
        return waitingClients;
    }

    /**
     * Moves a client handler from the waiting list to the connected list (when the client is connected to a game).
     *
     * @param waitingClientIndex index of the waiting client handler inside the list that needs to be moved to the connected list
     */
    public int fromWaitingToConnected(int waitingClientIndex) {
        int position;
        synchronized (connectedClients) {
            position = connectedClients.indexOf(null);

            if (position != -1) {
                connectedClients.set(position, waitingClients.get(waitingClientIndex));
                connectedClients.get(position).setConnectedPlayerIndex(position);
            } else {
                connectedClients.add(waitingClients.get(waitingClientIndex));
                connectedClients.get(connectedClients.size() - 1).setConnectedPlayerIndex(connectedClients.size() - 1);
                position = connectedClients.size() - 1;
            }

        }
        synchronized (waitingClients) {
            waitingClients.remove(waitingClientIndex);
        }
        updateWaitingClientIndex(waitingClientIndex);
        return position;
    }

    /**
     * Adjusts the position of the connection handler inside the connected list for a player already in the game but that has reconnected
     *
     * @param playerNickname         nickname of the player reconnected
     * @param reconnectedPlayerIndex new position of the player reconnected
     */
    public void adjustConnectedIndex(String playerNickname, int reconnectedPlayerIndex) {
        int oldConnectedIndex = connectedNicknameSolver.get(playerNickname);
        if (oldConnectedIndex != reconnectedPlayerIndex) {
            synchronized (connectedClients) {
                ClientConnectionHandler temp = connectedClients.get(oldConnectedIndex);
                connectedClients.set(oldConnectedIndex, connectedClients.get(reconnectedPlayerIndex));
                connectedClients.set(reconnectedPlayerIndex, temp);
            }
        }
    }

    /**
     * Sets to null the connection handler of the given client in the connected list
     *
     * @param connectedClientIndex position of the connected client which connection handler will be set to null
     */
    public void setNullInConnected(int connectedClientIndex) {
        synchronized (connectedClients) {
            connectedClients.set(connectedClientIndex, null);
        }
    }


    /**
     * Completely removes the player from the connected list
     *
     * @param connectedClientIndex index of the player to be removed
     */
    public void completelyRemoveFromConnected(int connectedClientIndex) {
        synchronized (connectedClients) {
            connectedClients.remove(connectedClientIndex);
        }
        updateConnectedClientIndex(connectedClientIndex);
    }

    /**
     * Updates the index attribute inside all waiting clients handlers starting from the index passed as a parameter
     * (when a client is moved to the connected list, all the waiting clients shift of one position, hence their handler need to be updated)
     *
     * @param waitingClientIndex the index from where the handlers need to be updated
     */
    public void updateWaitingClientIndex(int waitingClientIndex) {
        synchronized (waitingClients) {
            for (int i = waitingClientIndex; i < waitingClients.size(); i++)
                waitingClients.get(i).setWaitingPlayerIndex(i);
        }
    }

    /**
     * Updates the index attribute inside all connected clients handlers starting from the index passed as a parameter
     * (when a client disconnects from the game, all the waiting clients shift of one position, hence their handler need to be updated)
     *
     * @param connectedClientIndex the index from where the handlers need to be updated
     */
    public void updateConnectedClientIndex(int connectedClientIndex) {
        synchronized (connectedClients) {
            for (int i = connectedClientIndex; i < connectedClients.size(); i++)
                connectedClients.get(i).setConnectedPlayerIndex(i);
        }
    }

    /**
     * Sends a message to all connected clients
     *
     * @param m the message to be sent to all the connected clients
     */
    public void broadcastConnectedClients(Message m) {
        for (ClientConnectionHandler chs : connectedClients)
            if (chs != null)
                chs.sendMessage(m);
    }

    /**
     * Sends a message only to a specified connected client
     *
     * @param m                    the message to be sent
     * @param connectedClientIndex the index of the client inside the connected list
     */
    public void sendToConnectedClient(Message m, int connectedClientIndex) { // sends the specified message to the specified client inside the ConnectedClient list
        connectedClients.get(connectedClientIndex).sendMessage(m);
    }

    /**
     * Sends a message only to a specified waiting client
     *
     * @param m                  the message to be sent
     * @param waitingClientIndex the index of the client inside the waiting list
     */
    public void sendToWaitingClient(Message m, int waitingClientIndex) { // sends the specified message to a specified client inside the waiting list
        waitingClients.get(waitingClientIndex).sendMessage(m);
    }

    /**
     * Accepts a connection with a new client, creates and starts the relative connection handler
     *
     * @param serverSocket the socket where the server is waiting for connections
     */
    public void acceptWaitingClients(ServerSocket serverSocket) {
        VirtualView virtualView = new VirtualView(new SendMessageToClient(this)); // creates a new Virtual View that will be part of the distributed mvc pattern
        // SendMessageToClient is a service class that creates the right message to be sent to the client for every situation
        try {
            while (true) {
                Socket client;

                client = serverSocket.accept(); // waits for a new client to connect
                System.out.println("[ SERVER ] client " + client.toString() + " connected to the server");

                ClientConnectionHandler connectionHandler = new ClientConnectionHandler(this, client, waitingClients.size(), virtualView); // creates a handler for the connection with
                // the newly connected client
                waitingClients.add(connectionHandler); // adds the newly connected client to the waiting list

                connectionHandler.start(); // starts the handle thread for the newly connected client
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    closeServerHandlers();
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves the
     * nickname -> position inside the connected list
     * in the NicknameSolver HashMap
     *
     * @param nickname       name of the player
     * @param connectedIndex position of the player
     */
    public void setConnectedNickname(String nickname, int connectedIndex) {
        connectedNicknameSolver.put(nickname, connectedIndex);
    }

    /**
     * Closes all the connection handlers of the connected players
     */
    private void closeServerHandlers() {
        synchronized (connectedClients) {
            if (waitingClients.size() > 0) {
                for (ClientConnectionHandler chs : waitingClients)
                    chs.close();
            } else {
                for (ClientConnectionHandler chs : connectedClients)
                    chs.close();
            }
            connectedClients.clear();
            waitingClients.clear();
        }
    }

    public int getPortNumber() {
        return portNumber;
    }

    /**
     * @return the nickname solver
     */
    public HashMap<String, Integer> getConnectedNicknameSolver() {
        return connectedNicknameSolver;
    }
}
