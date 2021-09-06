package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.*;

import java.util.Objects;

/**
 * Class that creates messages that will be sent to clients.
 *
 * @author Roberto Neglia
 */
public class SendMessageToClient {

    /**
     * Server reference used to send messages to a specified client or broadcast them to all.
     */
    private final Server server;
    private Integer askingName;
    /**
     * Flag which indicates if the lobby has been fully completed at least one time
     */
    private boolean lobbyCompleteOnce;

    /**
     * Class constructor.
     *
     * @param server the server reference
     */
    public SendMessageToClient(Server server) {
        this.server = server;
        askingName = 0;
        lobbyCompleteOnce = false;
    }

    public int getAskingName() {
        return askingName;
    }

    public void decrementAskingName() {
        synchronized (server) {
            askingName--;
        }
    }

    public void incrementAskingName() {
        synchronized (server) {
            askingName++;
        }
    }

    /**
     * @return the server instance
     */
    public Server getServer() {
        return server;
    }

    /**
     * Creates messages specific only for the first client that connects, which will decide the number of players of the game
     */
    public void sendToFirst() {
        // the client is the first in the waiting list and no other clients are connected to the server -> he's the first player
        server.fromWaitingToConnected(0); // so i move him to the connected clients
        sendConnected(0); // i tell him he's connected
        askNPlayer(0); // i ask him the number of players
    }

    /**
     * Creates messages that will be sent to all the other clients connected to a game, that are not the first
     *
     * @param waitingPlayerIndex position of the client inside the waiting list
     * @param totPlayers         total number of players of the game, decided by the first client connected
     */
    public void sendToOther(int waitingPlayerIndex, int totPlayers) {
        if (server.getConnectedClients().stream().filter(Objects::nonNull).count() == totPlayers) // if the lobby is full
            sendAlreadyFull(waitingPlayerIndex); // i tell that to the waiting client
        else { // if it's not full

            int playerIndex = server.fromWaitingToConnected(waitingPlayerIndex); // i move the waiting client to the ConnectedClients list (he's accepted to the game)

            sendConnected(playerIndex); // i tell him that
            if (server.getConnectedClients().stream().filter(Objects::nonNull).count() == totPlayers) { // if the newly accepted client completely fills the lobby
                if (!lobbyCompleteOnce) {
                    askName();
                    askingName = totPlayers;
                    lobbyCompleteOnce = true;
                } else {
                    incrementAskingName();
                    server.sendToConnectedClient(new AskName(playerIndex), playerIndex);
                }
            } else {
                if (!lobbyCompleteOnce)
                    sendWait(totPlayers); // otherwise i tell him to wait for other players
                else {
                    incrementAskingName();
                    server.sendToConnectedClient(new AskName(playerIndex), playerIndex);
                }
            }
        }
    }

    /**
     * @return true if the lobby has been fully completed once, false if not
     */
    public boolean isLobbyCompleteOnce() {
        return lobbyCompleteOnce;
    }

    /**
     * Creates a message that tells the client that correctly joined a game
     *
     * @param connectedPlayerIndex the index of the client inside the connected list
     */
    public void sendConnected(int connectedPlayerIndex) { // tells the client he's accepted to the game
        Message m = new Connected(connectedPlayerIndex);
        server.sendToConnectedClient(m, connectedPlayerIndex);
    }

    /**
     * Creates a message that tells to all the clients to wait for other players to connect to the game
     */
    public void sendWait() {
        Message m = new WaitForPlayers();
        server.broadcastConnectedClients(m);
    }

    /**
     * Creates a message that tells to all the clients to wait for other players to connect to the game
     *
     * @param totalPlayers total number of players of the game
     */
    public void sendWait(int totalPlayers) {
        Message m = new WaitForPlayers(server.getConnectedClients().size(), totalPlayers);
        server.broadcastConnectedClients(m);
    }

    /**
     * Creates a message that tells to the specified waiting client that the lobby for the game is already full
     *
     * @param clientIndex position of the client inside the waiting list
     */
    public void sendAlreadyFull(int clientIndex, boolean waiting) {
        Message m = new AlreadyFull();
        if (waiting)
        {
            server.sendToWaitingClient(m, clientIndex);
            server.getWaitingClients().get(clientIndex).setKeepAlive(false);
        }
        else server.sendToConnectedClient(m, clientIndex);
    }

    public void sendAlreadyFull(int waitingClientIndex) {
        sendAlreadyFull(waitingClientIndex, true);
    }

    /**
     * Creates a message that asks for the number of players to the first one
     *
     * @param connectedPlayerIndex the index of the client inside the connected list
     */
    public void askNPlayer(int connectedPlayerIndex) { // asks to the first player the number of players
        Message m = new AskNPlayer();
        server.sendToConnectedClient(m, connectedPlayerIndex);
    }

    /**
     * Creates a message that asks for a nickname to be set in the game to all the clients
     */
    public void askName() {
        Message m = new AskName();
        server.broadcastConnectedClients(m);
    }

    /**
     * Creates a message that asks for a nickname to be set in the game to the specified client
     */
    public void askName(int connectedPlayerIndex) {
        Message m = new AskName();
        server.sendToConnectedClient(m, connectedPlayerIndex);
    }

    /**
     * Creates a message that asks for a nickname to be set in the game to the specified client another time
     */
    public void askName(int connectedPlayerIndex, boolean again) {
        Message m = new AskName(again);
        server.sendToConnectedClient(m, connectedPlayerIndex);
    }

    /**
     * Generic method that sends a generic Message to the specified client
     *
     * @param playerIndex index of the client the message will be sent
     * @param m           message to be sent
     */
    public void sendMessage(int playerIndex, Message m) {
        server.sendToConnectedClient(m, playerIndex);
    }

    /**
     * Sends the specified message to all the players connected
     *
     * @param m message to be sent
     */
    public void sendBroadcastMessage(Message m) {
        server.broadcastConnectedClients(m);
    }

    /**
     * Removes a player from the connected list
     *
     * @param playerPosition index of the player to be removed
     */
    public void completelyRemoveFromConnected(int playerPosition) {
        server.completelyRemoveFromConnected(playerPosition);
    }

    /**
     * Sets to null the connection handler of the player inside the connected list
     *
     * @param playerPosition index of the player of which the connection handler needs to be set to null
     */
    public void setNullInConnected(int playerPosition) {
        server.setNullInConnected(playerPosition);
    }

}
