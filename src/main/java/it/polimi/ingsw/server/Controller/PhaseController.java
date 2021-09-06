package it.polimi.ingsw.server.Controller;


import it.polimi.ingsw.exceptions.CannotActivateAbilityException;
import it.polimi.ingsw.exceptions.CannotInsertException;
import it.polimi.ingsw.exceptions.IllegalDepotSwapException;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.server.Controller.Handlers.DevelopmentSlotsHandler;
import it.polimi.ingsw.server.Controller.Handlers.FaithTrackHandler;
import it.polimi.ingsw.server.Controller.Handlers.StrongboxHandler;
import it.polimi.ingsw.server.Model.Cards.DevelopmentCard;
import it.polimi.ingsw.server.Model.Cards.LeaderCard;
import it.polimi.ingsw.server.Model.Cards.ProductionPower;
import it.polimi.ingsw.server.Model.Enums.ActionEnum;
import it.polimi.ingsw.server.Model.Enums.MarbleEnum;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Enums.TypeLineEnum;
import it.polimi.ingsw.server.Model.Game;
import it.polimi.ingsw.server.Model.PersonalBoard.DevelopmentSlots;
import it.polimi.ingsw.server.Model.PersonalBoard.Strongbox;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Depot;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Warehouse;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.Requirements.Requirement;
import it.polimi.ingsw.server.Model.Requirements.ResourceRequirement;
import it.polimi.ingsw.server.Model.Table.CardMarket.CardDeck;
import it.polimi.ingsw.server.Model.Table.CardMarket.CardMarket;
import it.polimi.ingsw.server.Model.Table.ResourceMarket;
import it.polimi.ingsw.server.VirtualView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Sub-Controller that handles the central part of the game, that is the cycle through the turns
 */
public class PhaseController {
    private final Game game;
    private final VirtualView virtualView;
    private ActionEnum CHOSEN_ACTION;
    //DEBUG
    //boolean first = true;
    //END DEBUG

    /**
     * Constructor
     * @param virtualView virtualview instance to manage client-server communication
     * @param game game instance to manage
     */

    public PhaseController(VirtualView virtualView, Game game) {
        this.virtualView = virtualView;
        this.game = game;
    }


    /**
     * Notifies the various clients connected in the game that the turn has changed and
     * if the client matches the current player, it starts playing the turn otherwise it is put on hold
     */
    public void playTurn() {
        List<Player> players = game.getPlayers();
        Player currentPlayer = game.getCurrentPlayer();
        int position = players.indexOf(currentPlayer);

// TODO debug lines, remove after

//        FaithTrackHandler.addFaithPointsToCurrentPlayer(15);
//        FaithTrackHandler.updatePlayerPosition(currentPlayer.getFaithTrack(), FaithTrackHandler.getFaithPointsGivenToCurrentPlayer());

// TODO debug lines, remove after

        FaithTrackHandler.reset();

        Message m;

        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).isConnected()) {
                if (i == position) {
                    m = new TurnStartEvent(position, currentPlayer.getNickname());
                } else {
                    m = new WaitTurnEvent(position, currentPlayer.getNickname());
                }
                virtualView.update(i, m);
            }

        }
    }

    /**
     * Send the current state of the resource market to the current player
     * @param positionPlayer player position to identify the client with which to communicate the response
     */
    public void getResourceMarket(int positionPlayer) {
        MarbleEnum[][] resourceMarketStructure = game.getTable().getResourceMarket().getMarketStructure();

        virtualView.update(positionPlayer, new ResourceMarketAction(resourceMarketStructure));
    }

    /**
     * Send the current state of the card market to the current player
     * @param positionPlayer player position to identify the client with which to communicate the response
     */
    public void getCardMarket(int positionPlayer) {
        CardDeck[][] cardMarketStructure = game.getTable().getCardMarket().getMarketStructure();
        Player currPlayer = game.getCurrentPlayer();
        Warehouse playerWarehouse = currPlayer.getWarehouse();
        Strongbox playerStrongbox = currPlayer.getStrongbox();
        virtualView.update(positionPlayer, new CardMarketAction(cardMarketStructure, playerWarehouse, playerStrongbox));
    }

    /**
     * Send the resources corresponding to the column / row chosen from the resource market to the current player.
     * Conversions of white and red marbles are also managed based on active or not leader powers
     * @param typeSelected row or column to identify the correspondence with the selectedLine
     * @param selectedLine line of the resource market
     * @return List of obtained resources
     */
    public EnumMap<ResourceEnum, Integer> getResourcesFromChoice(TypeLineEnum typeSelected, int selectedLine) {
        CHOSEN_ACTION = ActionEnum.RESOURCE_MARKET;
        ResourceMarket resourceMarket = game.getTable().getResourceMarket();
        MarbleEnum[] newMarbles = resourceMarket.obtainResources(typeSelected, selectedLine);
        EnumMap<ResourceEnum, Integer> newResources = resourceMarket.obtainResourceFromMarbleList(newMarbles);

        //controllo per eliminare o convertire le risorse in base alla leader card ability whitemarble
        Player currPlayer = game.getCurrentPlayer();

        if (newResources.containsKey(ResourceEnum.NONE)) {
            if (currPlayer.getWhiteMarbleLeaderAbilities().size() == 0) {
                newResources.remove(ResourceEnum.NONE);
            } else if (currPlayer.getWhiteMarbleLeaderAbilities().size() == 1) {
                ResourceEnum resourceToSwitch = currPlayer.getWhiteMarbleLeaderAbilities().get(0);
                if (newResources.containsKey(resourceToSwitch)) {
                    newResources.replace(resourceToSwitch, newResources.get(ResourceEnum.NONE) + newResources.get(resourceToSwitch));
                } else {
                    newResources.put(resourceToSwitch, newResources.get(ResourceEnum.NONE));
                }
                newResources.remove(ResourceEnum.NONE);
            }
        }

        if (newResources.containsKey(ResourceEnum.FAITH)) {
            FaithTrackHandler.updatePlayerPosition(game.getCurrentPlayer().getFaithTrack(), newResources.get(ResourceEnum.FAITH));

//            game.getCurrentPlayer().getFaithTrack().updatePlayerPosition(newResources.get(ResourceEnum.FAITH));
            newResources.remove(ResourceEnum.FAITH);
        }

        return newResources;
    }

    /**
     * Try swapping the two selected depots with each other and send the result to the current player
     * @param positionPLayer player position to identify the client with which to communicate the response
     * @param depotChosen1 index of the first depot
     * @param depotChosen2 index of the second depot
     */
    public void swapDepots(int positionPLayer, int depotChosen1, int depotChosen2) {
        List<Player> players = game.getPlayers();
        Player p = players.get(positionPLayer);
        Depot dp1 = p.getWarehouse().getDepots().get(depotChosen1 - 1);
        Depot dp2 = p.getWarehouse().getDepots().get(depotChosen2 - 1);
        String messageResult;
        try {
            p.getWarehouse().move(dp1, dp2);
            messageResult = "successfully swapped depots";
            Message m = new SwapDepot(messageResult, true, p.getWarehouse());
            virtualView.update(positionPLayer, m);
        } catch (IllegalDepotSwapException e) {
            messageResult = "swap not allowed: you can't swap depot: " + depotChosen1 + "with depot: " + depotChosen2;
            Message m = new SwapDepot(messageResult, false, p.getWarehouse());
            virtualView.update(positionPLayer, m);
        }
    }

    /**
     * Send production powers to the current player
     * @param playerIndex player position to identify the client with which to communicate the response
     */
    public void getProductionPowers(int playerIndex) {
        Player p = game.getCurrentPlayer();


        // ONLY FOR DEBUG
        /*
        if (first) {
            Warehouse playerWarehouse = p.getWarehouse();
            Strongbox playerStrongbox = p.getStrongbox();

            DevelopmentSlots playerSlots = p.getSlots();

            playerWarehouse.insert(ResourceEnum.SHIELD, 1, 1);
            //playerWarehouse.insert(ResourceEnum.COIN, 2, 2);

            //playerStrongbox.insertResource(ResourceEnum.STONE, 3);
            playerStrongbox.insertResource(ResourceEnum.COIN, 1);
            //playerStrongbox.insertResource(ResourceEnum.SHIELD, 2);

            CardDeck[][] decks = game.getTable().getCardMarket().getMarketStructure();

            DevelopmentCard card1 = decks[0][0].pop();

            DevelopmentCard card2 = decks[3][0].pop();

            try {
                playerSlots.addCard(card1, 0);
                p.updateDevProductionPower(card1.getProductionPower(), 0);
                playerSlots.addCard(card2, 2);
                p.updateDevProductionPower(card2.getProductionPower(), 2);
            } catch (CannotInsertException e) {
                e.printStackTrace();
            }
            first = false;
        }
        */
        // END DEBUG SECTION

        List<ProductionPower> playerProductionPowers = p.getAllProductionPowers();
        virtualView.update(playerIndex, new ProductionPowerAction(playerProductionPowers));
    }

    /**
     * Check the possibility of using the production powers and send the result to the current player:
     * It requests to pay the resources to activate it or notifies the inability to activate
     * @param playerIndex player position to identify the client with which to communicate the response
     * @param p production power to use
     */
    public void tryUseProductionPower(int playerIndex, ProductionPower p) {
        CHOSEN_ACTION = ActionEnum.PRODUCTION_POWER;

        Player currentPlayer = game.getCurrentPlayer();
        List<Requirement> missingRequirements = new ArrayList<>();

        if (p.canBeUsed(currentPlayer)) {
            EnumMap<ResourceEnum, Integer> obtainedResources = p.getResourceObtained();
            FaithTrackHandler.addFaithPointsToCurrentPlayer(obtainedResources.getOrDefault(ResourceEnum.FAITH, 0));
            obtainedResources.remove(ResourceEnum.FAITH);
            StrongboxHandler.insert(p.getResourceObtained());
            askPayResources(playerIndex, p.getResourceRequested());
        } else {
            ResourceRequirement[] resourceRequirements = p.getResourceRequested();
            for (ResourceRequirement r : resourceRequirements) {
                if (!r.checkRequirement(currentPlayer))
                    missingRequirements.add(r);
                else {
                    if (r.getResourceType() == ResourceEnum.JOLLY && Arrays.stream(resourceRequirements).anyMatch(requirement -> requirement.getResourceType() == ResourceEnum.JOLLY))
                        missingRequirements.add(r);
                }
            }
            virtualView.update(playerIndex, new NotEnoughResources(missingRequirements, ActionEnum.PRODUCTION_POWER));
        }
    }

    /**
     * Requires the current player to manage the necessary resources to be paid for activating the power or buy card
     * @param playerIndex player position to identify the client with which to communicate the response
     * @param requirements resources needed to activate the power of production
     */
    private void askPayResources(int playerIndex, ResourceRequirement[] requirements) {
        Player currentPlayer = game.getCurrentPlayer();
        Strongbox playerStrongbox = currentPlayer.getStrongbox();
        Warehouse playerWarehouse = currentPlayer.getWarehouse();

        virtualView.update(playerIndex, new ChooseResourceLocation(requirements, playerStrongbox, playerWarehouse));
    }

    /**
     * Use the indicated resources to pay for activating a power and notify the current
     * player of the new status of the warehouse and strongbox
     * @param m Message containing the correspondences between the resources useful for the payment of a power and the
     *          place from which to withdraw them, strongbox or warehouse
     */
    public void payResources(ChooseResourceLocation m) {
        Player currentPlayer = game.getCurrentPlayer();
        Warehouse playerWarehouse = currentPlayer.getWarehouse();
        Strongbox playerStrongbox = currentPlayer.getStrongbox();

        EnumMap<ResourceEnum, Integer> payWarehouse = m.getPayWarehouse();
        EnumMap<ResourceEnum, Integer> payStrongbox = m.getPayStrongbox();

        payWarehouse.forEach(playerWarehouse::remove);
        payStrongbox.forEach(playerStrongbox::remove);

        endAction(m.getSender());

        virtualView.update(m.getSender(), new WarehouseUpdate(currentPlayer.getWarehouse()));
        virtualView.update(m.getSender(), new StrongboxUpdate(currentPlayer.getStrongbox()));
    }

    /**
     * Concludes the actions of the turn in the case of use of a production power
     * or a purchase of a card (inserts the card into the player's board) and notifies the changes
     * @param playerIndex player position to identify the client with which to communicate the response
     */
    public void endAction(int playerIndex) {
        Player currentPlayer = game.getCurrentPlayer();
        switch (CHOSEN_ACTION) {
            case PRODUCTION_POWER:
                virtualView.update(playerIndex, new ProductionPowerUsed());

                FaithTrackHandler.updatePlayerPosition(currentPlayer.getFaithTrack(), FaithTrackHandler.getFaithPointsGivenToCurrentPlayer());
                FaithTrackHandler.reset();
//                FaithTrack playerFaithTrack = currentPlayer.getFaithTrack();
//                playerFaithTrack.updatePlayerPosition(FaithTrackHandler.getFaithPointsGivenToCurrentPlayer());
                break;
            case BUY_CARD:
                CardMarket cardMarket = game.getTable().getCardMarket();
                DevelopmentCard cardToInsert = cardMarket.getMarketStructure()[DevelopmentSlotsHandler.getChosenCardLevel()][DevelopmentSlotsHandler.getChosenCardColor()].pop();
                DevelopmentSlotsHandler.setCardToInsert(cardToInsert);
                virtualView.update(playerIndex, new InsertDevelopmentCard(currentPlayer.getSlots(), cardToInsert));
                break;
        }
    }

    /**
     * Send the player a list of their leader cards
     * @param m Message containing information on the current status of the turn to manage the response sent to the player
     *          and indicates the request to show the leader cards
     */
    public void getLeaderCard(GetLeaderCard m) {
        List<Player> players = game.getPlayers();
        Player p = players.get(m.getSender());
        List<LeaderCard> leaderCards = p.getLeaderCards().stream().filter(leaderCard -> !leaderCard.isActive()).collect(Collectors.toList());

        virtualView.update(m.getSender(), new GetLeaderCard(leaderCards, m.isDiscardAction(), m.isStartTurn()));
    }

    /**
     * Attempt to discard the selected leader card and notify the current player of the result
     * @param m Message containing information on the current status of the turn to manage the response sent to the player
     *          and indicates the request for discard of a leader card
     */
    public void discardLeaderCard(DiscardLeaderCard m) {
        List<Player> players = game.getPlayers();
        Player p = players.get(m.getSender());

        boolean successDiscard;
        int idxLeaderCard = p.getLeaderCardPositionById(m.getLeaderCard().getId());

        if (idxLeaderCard != -1) {
            p.removeLeaderCard(idxLeaderCard);

            FaithTrackHandler.updatePlayerPosition(p.getFaithTrack(), 1);

            successDiscard = true;

        } else {
            successDiscard = false;
        }

        virtualView.update(m.getSender(), new DiscardLeaderCard(m.getIsDiscardAction(), m.getIsStartTurn(), successDiscard));

    }

    /**
     * Attempt to activate the selected leader card and notify the current player of the result
     * @param m Message containing information on the current status of the turn to manage the response sent to the player
     *          and indicates the request for activation of a leader card
     */
    public void activateLeaderCard(ActivateLeaderCard m) {
        List<Player> players = game.getPlayers();
        Player p = players.get(m.getSender());

        boolean successActivate = false;
        int idxLeaderCard = p.getLeaderCardPositionById(m.getLeaderCard().getId());

        if (idxLeaderCard != -1) {
            try {
                p.activateLeaderCard(idxLeaderCard);
                successActivate = true;
                virtualView.update(m.getSender(), new ActivateLeaderCard(m.getIsDiscardAction(), m.getIsStartTurn(), successActivate, p.getWhiteMarbleLeaderAbilities(), p.getDiscountLeaderAbilities()));

            } catch (CannotActivateAbilityException e) {
                virtualView.update(m.getSender(), new ActivateLeaderCard(m.getIsDiscardAction(), m.getIsStartTurn(), successActivate, e.getMissingRequirements()));
            }

        }


    }

    /**
     * tries to insert the card into a slot indicated on the current player's board and notifies him of the result
     * @param playerIndex player position to identify the client with which to communicate the response
     * @param slotNumber number of the slot where to insert the card
     */
    public void tryInsertCard(int playerIndex, int slotNumber) {
        Player currentPlayer = game.getCurrentPlayer();
        DevelopmentSlots slots = currentPlayer.getSlots();
        DevelopmentCard toInsert = DevelopmentSlotsHandler.getCardToInsert();

        try {
            slots.addCard(toInsert, slotNumber);
            DevelopmentSlotsHandler.setCardToInsert(null);
            currentPlayer.updateDevProductionPower(toInsert.getProductionPower(), slotNumber);
            virtualView.update(playerIndex, new DevelopmentSlotUpdate(currentPlayer.getSlots()));
            virtualView.update(playerIndex, new EndTurn());

        } catch (CannotInsertException e) {
            virtualView.update(playerIndex, new SlotInsertionAction(e.getMessage(), toInsert, slots));
        }

    }

    /**
     * attempts to buy the card and notifies the current player of the result
     * @param playerIndex player position to identify the client with which to communicate the response
     * @param level level of the card selected
     * @param color color of the card selected
     */
    public void tryBuyDevelopmentCard(int playerIndex, int level, int color) {
        CHOSEN_ACTION = ActionEnum.BUY_CARD;
        Player currentPlayer = game.getCurrentPlayer();
        if (game.getTable().getCardMarket().getMarketStructure()[level][color].getSize() > 0) {
            DevelopmentCard selectedCard = game.getTable().getCardMarket().getMarketStructure()[level][color].peek();
            List<Requirement> missingRequirements = new ArrayList<>();
            if (selectedCard.canBeBought(currentPlayer)) {
                DevelopmentSlotsHandler.setChosenCardLevel(level);
                DevelopmentSlotsHandler.setChosenCardColor(color);

                //applico effettivamente sconto se c'è
                if (currentPlayer.getDiscountLeaderAbilities().size() == 0) {
                    askPayResources(playerIndex, selectedCard.getPrice());
                } else {
                    ResourceRequirement[] wrappedPrice = new ResourceRequirement[selectedCard.getPrice().length];
                    System.arraycopy(selectedCard.getPrice(), 0, wrappedPrice, 0, selectedCard.getPrice().length);

                    for (ResourceRequirement resReq : wrappedPrice) {
                        for (ResourceEnum resDisc : currentPlayer.getDiscountLeaderAbilities()) {
                            if (resReq.getResourceType().equals(resDisc)) {
                                resReq.applyDiscount(1);
                            }
                        }
                    }
                    askPayResources(playerIndex, wrappedPrice);

                }


            } else {
                //not enough resources
                ResourceRequirement[] resourceRequirements = selectedCard.getPrice();
                for (ResourceRequirement r : resourceRequirements) {
                    if (!r.checkRequirement(currentPlayer))
                        missingRequirements.add(r);
                }
                //TODO : check if level was too high
                virtualView.update(playerIndex, new NotEnoughResources(missingRequirements, ActionEnum.BUY_CARD));
                getCardMarket(playerIndex);
            }
        } else {
            virtualView.update(playerIndex, new EmptyDeck());
            getCardMarket(playerIndex);
        }

    }

}
