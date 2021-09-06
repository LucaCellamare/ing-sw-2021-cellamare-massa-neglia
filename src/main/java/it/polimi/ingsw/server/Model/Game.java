package it.polimi.ingsw.server.Model;

import it.polimi.ingsw.server.Model.Table.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes the model Game
 *
 * @author Antonio Massa
 */

public abstract class Game {
    /**
     * Instance of Table, which represents the common part of the game to all the players
     */
    private final Table table;
    /**
     * Deck of leader cards from where all the players initially pick four cards
     */
    private final LeaderCardDeck leaderCardDeck;
    /**
     * List of all the players that will play the game
     */
    private final List<Player> players;
    /**
     * Player that is playing a turn
     */
    private Player currentPlayer;

    /**
     * Class constructor
     *
     * @param nPlayers number of players in this game
     */
    public Game(int nPlayers) {
        this.table = new Table();
        leaderCardDeck = new LeaderCardDeck();
        players = new ArrayList<>();
        for (int i = 0; i < nPlayers; i++)
            players.add(null);
    }

    /**
     * Retrieve the table of the game
     *
     * @return table
     */
    public Table getTable() {
        return table;
    }

    /**
     * Retrieve the currentPlayer
     *
     * @return current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Used for debug and testing only, sets the current player as the player in the given position
     *
     * @param playerPosition position of the player connected that is set as the current player
     */
    public void setCurrentPlayer(int playerPosition) {
        this.currentPlayer = players.get(playerPosition);
    }

    public void addPlayer(int position, Player newPlayer) {
        players.remove(position);
        players.add(position, newPlayer);
    }

    /**
     * @return the list of players that are playing
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Puts the next player as the current player when his turn has finished
     */
    public void nextPlayer() {  // aggiungere un flag a player che mi dice se è ancora collegato, se non è collegato salta il turno (chiama due volte nextPlayer())
        if (currentPlayer == null)
            currentPlayer = players.get(0);
        else {
            Player oldPlayer = currentPlayer;
            int currPlayerPosition = players.indexOf(oldPlayer);
            if (currPlayerPosition + 1 == players.size())
                currentPlayer = players.get(0);
            else
                currentPlayer = players.get(currPlayerPosition + 1);

            if (!currentPlayer.isConnected() && somebodyConnected())
                nextPlayer();
        }
    }

    /**
     * Checks if any player is still connected to the game
     *
     * @return true if any player is still connected, false if not
     */
    private boolean somebodyConnected() {
        for (Player p : players) {
            if (p.isConnected())
                return p.isConnected();
        }
        return false;
    }
}
