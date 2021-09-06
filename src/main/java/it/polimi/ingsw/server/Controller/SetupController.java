package it.polimi.ingsw.server.Controller;

import it.polimi.ingsw.exceptions.MaxLeaderCardsException;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.server.Controller.Handlers.FaithTrackHandler;
import it.polimi.ingsw.server.Controller.Handlers.WarehouseHandler;
import it.polimi.ingsw.server.Model.Cards.LeaderCard;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Game;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Warehouse;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.VirtualView;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

/**
 * Sub-Controller that handles the initial phase of a game, where the player obtains the leader cards and his initial resources
 */
public class SetupController {
    /**
     * Reference to the game, where all the data is stored
     */
    private final Game game;
    /**
     * Reference to the virtual view, used to communicate with the real view Client side
     */
    private final VirtualView virtualView;
    /**
     * Array that contains true in position i if the player in position i is ready and has finished his setup
     */
    private final Boolean[] readyPlayer;

    /**
     * Class constructor
     *
     * @param virtualView reference to the virtual view
     * @param game        reference to the game
     */
    public SetupController(VirtualView virtualView, Game game) {
        this.virtualView = virtualView;
        this.game = game;
        readyPlayer = new Boolean[game.getPlayers().size()];
        Arrays.fill(readyPlayer, false);
    }

    /**
     * Asks the player the two leader cards he wants to keep between the four he picked randomly
     *
     * @param playerIndex the player that we are communicating with
     */
    public void pickLeaderCard(int playerIndex) {
        List<LeaderCard> leaderCards;
        List<Player> players = game.getPlayers();
        Player p = players.get(playerIndex);
        p.setGamePhase(GamePhase.CHOOSE_LEADER_CARD);
        leaderCards = p.getLeaderCardsToChoose();
        virtualView.update(playerIndex, new ChooseLeaderCard(leaderCards));
    }

    /**
     * The player has chosen the leader cards, so i give them to him
     *
     * @param connectedPlayerIndex the player that we are communicating with
     * @param firstLeaderCard      index of the first leader card chosen
     * @param secondLeaderCard     index of the second leader card chosen
     */
    public void insertLeaderCard(int connectedPlayerIndex, int firstLeaderCard, int secondLeaderCard) {
        List<Player> players = game.getPlayers();
        Player p = players.get(connectedPlayerIndex);
        List<LeaderCard> playerLeaderCards = p.getLeaderCardsToChoose();
        try { // could probably remove the exception
            p.setLeaderCard(playerLeaderCards.get(firstLeaderCard));
            p.setLeaderCard(playerLeaderCards.get(secondLeaderCard));
            p.setGamePhase(GamePhase.CHOOSE_INITIAL_RESOURCES);
        } catch (MaxLeaderCardsException e) {
            e.printStackTrace();
        }

        System.out.println(p.getNickname() + " has chosen the following leader cards: ");
        System.out.println(p.getLeaderCards().get(0));
        System.out.println(p.getLeaderCards().get(1));
        System.out.println();
    }

    /**
     * Asks the player which initial resource(s) he wants
     *
     * @param playerIndex the player that we are communicating with
     */
    public void pickInitialResources(int playerIndex) {
        int nResources, nFaithPoints;
        if (playerIndex == 0) { // first player: no initial resources, possibly a message?
            nResources = 0;
            nFaithPoints = 0;
            System.out.println("first player");
        } else if (playerIndex == 1) { // second player: one initial resource but no faith points
            nResources = 1;
            nFaithPoints = 0;
            System.out.println("second player");
        } else if (playerIndex == 2) { // third player: one initial resource and one faith point
            nResources = 1;
            nFaithPoints = 1;
            System.out.println("third player");
        } else { // fourth player: two initial resources and one faith point
            nResources = 2;
            nFaithPoints = 1;
            System.out.println("fourth player");
        }
        Player p = game.getPlayers().get(playerIndex);
        Warehouse playerWarehouse = p.getWarehouse();
        FaithTrackHandler.updatePlayerPosition(p.getFaithTrack(), nFaithPoints);
        virtualView.update(playerIndex, new ChooseInitialResources(playerIndex, nResources, nFaithPoints, playerWarehouse));
    }

    /**
     * The player has chosen the initial resources, so i put them in the warehouse handler until the player choose where to put them
     *
     * @param toInsert initial resources chosen by the player
     */
    public void setInitialResources(EnumMap<ResourceEnum, Integer> toInsert) {
        WarehouseHandler.setToInsert(toInsert);
    }

    /**
     * Sets to true the player flag inside the readyPlayer array
     *
     * @param playerIndex the player that we are communicating with
     */
    protected synchronized void setReadyPlayer(int playerIndex) {
        this.readyPlayer[playerIndex] = true;
    }

    /**
     * Checks if all the connected players are ready to play
     *
     * @return true if all the players finished the setup, false if not
     */
    protected boolean checkAllPLayerReady() {
        boolean allReady = true;
        for (boolean b : readyPlayer) {
            if (!b) {
                allReady = false;
                break;
            }
        }
        return allReady;
    }

    /**
     * When a player finishes the setup, checks if all the players have finished by looking the readyPlayer array
     *
     * @param lastReady the last player that finished the setup
     */
    public void checkFinish(int lastReady) {
        setReadyPlayer(lastReady);
        game.getPlayers().get(lastReady).setGamePhase(GamePhase.GAME);
        if (!checkAllPLayerReady())
            virtualView.update(lastReady, new SetupComplete((int) Arrays.stream(readyPlayer).filter(x -> x).count(), readyPlayer.length));
        else {
            virtualView.update(new RunGame());
        }
    }

    /**
     * Tells the players connected to wait for the players that disconnected, if he did during the setup
     */
    public void waitDisconnected() {
        Message m = new SetupComplete((int) Arrays.stream(readyPlayer).filter(x -> x).count(), readyPlayer.length);
        List<Player> players = game.getPlayers();
        int i = 0;
        for (Boolean p : readyPlayer) {
            if (p && players.get(i).isConnected())
                virtualView.update(i, m);
            i++;
        }
    }


}
