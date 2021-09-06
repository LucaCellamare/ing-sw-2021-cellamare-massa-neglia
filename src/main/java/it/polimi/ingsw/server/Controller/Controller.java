package it.polimi.ingsw.server.Controller;

import it.polimi.ingsw.exceptions.ConfirmDiscardResourceException;
import it.polimi.ingsw.exceptions.IllegalDepotResourceTypeException;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.server.Controller.Handlers.FaithTrackHandler;
import it.polimi.ingsw.server.Controller.Handlers.StrongboxHandler;
import it.polimi.ingsw.server.Controller.Handlers.WarehouseHandler;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Game;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Warehouse;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.VirtualView;

import java.util.EnumMap;
import java.util.List;


/**
 * Controller part of the MVC pattern, it observes the view for input events from the user
 * and controls the flow of the game
 */
public abstract class Controller { // temporary basic controller part of the mvc pattern, observes the view for input from the user events
    // and controls the flow of the game

    /**
     * Reference to the virtual view, used to communicate with the real view Client side
     */
    private final VirtualView virtualView; // reference to the virtual view

    /**
     * Controls the setup of the game
     */
    protected SetupController setupController;

    /**
     * Controls the turn cycle during the game (after the setup)
     */
    protected PhaseController phaseController;

    /**
     * Controls the end of the game (a player won, the others finish)
     */
    protected EndGameController endGameController;

    /**
     * Tells if the game is still during the setup phase
     */
    protected boolean isSetup;

    /**
     * Reference to the game, where all the data is stored
     */
    private Game game; // game instance

    /**
     * Number of players in a game, decided during the setup by the first player
     */
    private int nPlayers; // number of player of the game

    /**
     * Class constructor
     *
     * @param virtualView reference to the virtual view
     */
    public Controller(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    /**
     * @return the number of players in the game
     */
    public int getNPlayers() {
        return nPlayers;
    }

    /**
     * @param nPlayers the number of players in the game
     */
    public void setNPlayers(int nPlayers) {
        this.nPlayers = nPlayers;
    }

    /**
     * @return the reference to the game instance of this match, could be single player or multi player
     */
    public Game getGame() {
        return game;
    }

    /**
     * @param game reference to the game instance of this match, could be single player or multi player
     */
    protected void setGame(Game game) {
        this.game = game;
    }

    /**
     * General update message called by the virtual view when an event happen.
     * For each event there is an overload of this one with the specific Message event as a parameter:
     * based on that, the controller behaves differently
     *
     * @param m the event that updates the game
     */
    public void update(Message m) {
    }

    /**
     * The player sent his name, the controller creates a new Player instance
     *
     * @param e the message sent from the client with the player's name
     */
    public abstract void update(AskName e);

    /**
     * The player sent the two leader cards he has chosen during the setup from the four he picked,
     * then asks the player to pick the initial resources
     *
     * @param m the message that contains the chosen leader cards
     */
    public void update(ChooseLeaderCard m) {
        setupController.insertLeaderCard(m.getSender(), m.getFirstChosenCard(), m.getSecondChosenCard());
        setupController.pickInitialResources(m.getSender());
    }

    /**
     * The controller asks the client again for the initial resources, since the player disconnected before choosing them
     *
     * @param m the message that contains the info about the reconnected player
     */
    public void update(ReconnectInitialResources m) {
        setupController.pickInitialResources(m.getReconnectedPlayerIndex());
    }

    /**
     * The player sent the initial resources he has chosen during the setup based on his position in the turn cycle
     *
     * @param m the message that contains the chosen initial resources
     */
    public void update(ChooseInitialResources m) {
        setupController.setInitialResources(m.getPickedResources());
    }

    /**
     * The player tells the server that he finished his setup of the game, and that he's ready to play
     *
     * @param m the info about the player that's ready
     */
    public void update(PlayerReady m) {
        setupController.checkFinish(m.getSender());
    }

    /**
     * Communicates to the connected clients to wait for disconnected players to finish their setup
     */
    public void update(WaitSetup m) {
        setupController.waitDisconnected();
    }

    /**
     * The players has chosen to go to the resource market as an action during his turn,
     * so the controller sends to him the resource market structure to make him choose for resources
     */
    public void update(ResourceMarketAction m) {
        phaseController.getResourceMarket(m.getSender());
    }

    /**
     * The player has chosen which resources to take from the market, so the controller gets and sends them to him,
     * asking where to insert them in the warehouse
     *
     * @param m message that contains the info about which resources the player has chosen to take
     */
    public void update(SelectedResourceFromMarket m) {
        EnumMap<ResourceEnum, Integer> newResources = phaseController.getResourcesFromChoice(m.getTypeSelected(), m.getSelectedLine());

        // devo passare warehouse e la lista al metodo che già esiste che si chiama in cliview.askInsertWarehouse
        List<Player> players = game.getPlayers();
        Player p = players.get(m.getSender());
        Warehouse playerWarehouse = p.getWarehouse();
        WarehouseHandler.setToInsert(newResources); // settare quando creo nuove risorse nell'handler
        virtualView.update(m.getSender(), new SelectedResourceFromMarket(newResources, playerWarehouse));
    }

    /**
     * The player tries to insert a resource in the warehouse, in a depot he has chosen:
     * if the depot can store that resource, than the controller inserts it, otherwise he tells the player that he can't,
     * or that he needs to discard some resources if he keeps going
     *
     * @param m message that contains the information about the resource the player wants to insert, how much, and where
     */
    public void update(TryInsert m) {
        List<Player> players = game.getPlayers();
        Player p = players.get(m.getSender());
        Warehouse playerWarehouse = p.getWarehouse();

        try {
            playerWarehouse.tryInsert(m.getResource(), m.getQuantity(), m.getChosenDepot());
            playerWarehouse.insert(m.getResource(), m.getQuantity(), m.getChosenDepot());
            WarehouseHandler.remove(m.getResource(), m.getQuantity());

            if (isSetup) {
                if (WarehouseHandler.isNotEmpty())
                    virtualView.update(m.getSender(), new InsertResources(WarehouseHandler.getToInsert(), playerWarehouse));
                else setupController.checkFinish(m.getSender());
            } else
                virtualView.update(m.getSender(), new SelectedResourceFromMarket(WarehouseHandler.getToInsert(), playerWarehouse));
        } catch (ConfirmDiscardResourceException e) {
            m.setToDiscard(e.getResourceQty());
            FaithTrackHandler.addFaithPointsToOtherPlayers(e.getResourceQty());
            virtualView.update(m.getSender(), m);
        } catch (IllegalDepotResourceTypeException e) {
            if (e.isResourceAlreadyPresent()) {
                virtualView.update(m.getSender(), new TryInsert());
                virtualView.update(m.getSender(), new SelectedResourceFromMarket(WarehouseHandler.getToInsert(), playerWarehouse));
            }
        }
    }

    /**
     * Definitely inserts the resource in the chosen depot and discards ones that can't be inserted in it
     *
     * @param m contains the info about the resource to insert, it's quantity, and the depot where to put it
     */
    public void update(ConfirmInsert m) {
        List<Player> players = game.getPlayers();
        Player p = players.get(m.getSender());
        Warehouse playerWarehouse = p.getWarehouse();

        if(m.getQuantity() > 0)
        {
            playerWarehouse.insert(m.getResource(), m.getQuantity(), m.getDepotIndex());
        }
        WarehouseHandler.completelyRemove(m.getResource());

        discardResource(m.getSender(), FaithTrackHandler.getFaithPointsGivenToOtherPlayers());

        virtualView.update(m.getSender(), new SelectedResourceFromMarket(WarehouseHandler.getToInsert(), playerWarehouse));
    }

    /**
     * The player decides he doesn't want to insert the resource in that depot (in case he wants to put too many resources in a small depot)
     * so the controller asks him again where to insert them
     */
    public void update(AbortInsert m) {
        Player currentPlayer = game.getCurrentPlayer();
        virtualView.update(m.getSender(), new SelectedResourceFromMarket(WarehouseHandler.getToInsert(), currentPlayer.getWarehouse()));
    }

    /**
     * The player decides to discard the remaining resources that he has to insert
     *
     * @param m message that contains the resources that will be discarded
     */
    public void update(DiscardResources m) {
        discardResource(m.getSender(), m.getResourceToDiscard().values().stream().reduce(0, Integer::sum));
        virtualView.update(m.getSender(), new EndTurn());
    }

    /**
     * The player wants to swap two depots
     *
     * @param m message that contains the two depot indexes that the player wants to swap
     */
    public void update(SwapDepot m) {
        phaseController.swapDepots(m.getSender(), m.getDepotIndex1(), m.getDepotIndex2());
    }

    /**
     * The players decides to go to the card market as a turn action
     */
    public void update(CardMarketAction m) {
        phaseController.getCardMarket(m.getSender());
    }

    /**
     * The players wants to buy a development card from the card market
     *
     * @param m message that contains the coordinates inside the card market of the card the player wants to buy
     */
    public void update(SelectedCardFromMarket m) {
        phaseController.tryBuyDevelopmentCard(m.getSender(), m.getLevel(), m.getColor());
    }

    /**
     * The players has chosen a slot where to insert a recently bought card
     *
     * @param m message that contains the number of the slot where to insert the card
     */
    public void update(SlotInsertionAction m) {
        phaseController.tryInsertCard(m.getSender(), m.getSlotNumber());
    }

    /**
     * The players wants to activate some production powers as a turn action
     */
    public void update(ProductionPowerAction m) {
        phaseController.getProductionPowers(m.getSender());
    }

    /**
     * The player has chosen a production power to activate
     *
     * @param m message that contains the production power the player wants to activate
     */
    public void update(UseProductionPower m) {
        phaseController.tryUseProductionPower(m.getSender(), m.getSelectedProductionPower());
    }

    /**
     * The players has to pay for a card or a production power
     *
     * @param m message that contains the info about the payment
     */
    public void update(ChooseResourceLocation m) {
        phaseController.payResources(m);
    }

    /**
     * The player finished to activate production powers, so the controller will insert all the resources obtained from the activation to the player's strongbox
     */
    public void update(ProductionPowerUsed m) {
        Player currPlayer = game.getCurrentPlayer();
        StrongboxHandler.finallyInsert(currPlayer.getStrongbox());
        virtualView.update(m.getSender(), new StrongboxUpdate(currPlayer.getStrongbox()));
        virtualView.update(m.getSender(), new EndTurn());
    }

    /**
     * The player obtained from a production power a jolly resources, and has chosen a resource that needs to be managed somehow
     *
     * @param m message that contains the chosen resource
     */
    public void update(InsertJollyResource m) {
        if (m.getChosenResource() != ResourceEnum.FAITH) {
            StrongboxHandler.insert(m.getChosenResource(), m.getQuantity());
        } else {
            FaithTrackHandler.addFaithPointsToCurrentPlayer(m.getQuantity());
            FaithTrackHandler.updatePlayerPosition(game.getCurrentPlayer().getFaithTrack(), FaithTrackHandler.getFaithPointsGivenToCurrentPlayer());
        }
    }

    /**
     * A vatican report occurred during the game, this method handles the faith tracks of all the players in the game
     *
     * @param e message that contains the info about the vatican section activated
     */
    public abstract void update(VaticanReport e);

    /**
     * The player has ended his turn
     */
    public void update(EndTurn m) {
        endTurn(m.getSender());
    }

    /**
     * The player wants to activate or discard a leader card
     */
    public void update(GetLeaderCard m) {
        phaseController.getLeaderCard(m);
    }

    /**
     * The player wants to activate a leader card
     *
     * @param m contains the id of the card that the player wants to activate
     */
    public void update(ActivateLeaderCard m) {
        phaseController.activateLeaderCard(m);
    }

    /**
     * The player wants to discard a leader card
     *
     * @param m contains the id of the card that the player wants to discard
     */
    public void update(DiscardLeaderCard m) {
        phaseController.discardLeaderCard(m);
    }

    /**
     * The player obtained white marbles from the resource market and he has two
     * leader abilities that allow him to choose between two resources
     * from every white marble.
     * Here i update the resources that needs to be inserted inside the warehouse with the ones he has chosen from his leader abilities
     *
     * @param m message that contains the resources chosen by the player
     */
    public void update(WhiteMarbleSelection m) {
        EnumMap<ResourceEnum, Integer> newResources = m.getNewResources();
        EnumMap<ResourceEnum, Integer> oldResources = WarehouseHandler.getToInsert();

        for (ResourceEnum res : newResources.keySet()) {
            if (oldResources.containsKey(res))
                oldResources.replace(res, oldResources.get(res) + newResources.get(res));
            else oldResources.put(res, newResources.get(res));
        }

        oldResources.remove(ResourceEnum.NONE);

        WarehouseHandler.setToInsert(oldResources);
    }

    /**
     * Tells if the game is in the setup phase (the players are still choosing their nickname/initial resources/leader cards)
     *
     * @return true if the game is still in setup, false otherwise
     */
    public boolean isSetup() {
        return isSetup;
    }

    /**
     * All the players have finished their setup, so the game can start by cycling through turns
     */
    public void runGame() {
        isSetup = false;
        game.nextPlayer();
        System.out.println("SONO IL RUN GAME");
        //while(!win){ this while is wrong
        // the win condition is checked every time a turn is ended
        // so in the PhaseController endTurn() method there will be a check that checks if a player has won tha game
        // the endTurn() method is called every time a client ends his turn
        // il metodo controlla se un giocatore ha vinto
        // se nessuno ha vinto, incrementa il currentPlayer (game.nextPlayer()) e richiama playTurn()
        // se qualcuno ha vinto, il controllo passa al EndgameController che si occuperà di far giocare gli ultimi turni ai rimanenti giocatori in ordine
        phaseController.playTurn();
        //}
        //endGame();
    }

    /**
     * The player has ended his turn
     */
    public abstract void endTurn(int playerIndex);

    /**
     * The game is finished (a player has won), so the phase controller switches to the end game controller
     */
    public void endGame() {
        endGameController.playLastTurn();
    }

    /**
     * The player has chosen to discard some resources, so he gives to other players faith points
     *
     * @param playerIndex index of the player that discarded the resources
     * @param quantity    quantity of resources discarded
     */
    public abstract void discardResource(int playerIndex, int quantity);

    /**
     * After a player disconnects, the game needs to go on
     */
    public abstract void continueGameAfterDisconnection();
}
