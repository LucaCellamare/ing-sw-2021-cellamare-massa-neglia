package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.SendMessageToServer;
import it.polimi.ingsw.client.handlers.PlayerHandler;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.server.Model.ActionToken;
import it.polimi.ingsw.server.Model.Cards.DevelopmentCard;
import it.polimi.ingsw.server.Model.Cards.LeaderCard;
import it.polimi.ingsw.server.Model.Cards.ProductionPower;
import it.polimi.ingsw.server.Model.Enums.ActionEnum;
import it.polimi.ingsw.server.Model.Enums.MarbleEnum;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.FaithTrack.FaithTrack;
import it.polimi.ingsw.server.Model.PersonalBoard.DevelopmentSlots;
import it.polimi.ingsw.server.Model.PersonalBoard.Strongbox;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Warehouse;
import it.polimi.ingsw.server.Model.Requirements.Requirement;
import it.polimi.ingsw.server.Model.Table.CardMarket.CardDeck;

import java.util.EnumMap;
import java.util.List;

/**
 * Interface of the view part of the MVC pattern.
 * It contains methods that will be overridden by the CLI or by the GUI and will interacts with the user differently
 *
 * @author Roberto Neglia
 */
public interface View {

    PlayerHandler playerHandler = new PlayerHandler();

    int getConnectedIndex();

    /**
     * Setter for the SendMessageToServer object
     *
     * @param sendToServer instance of the class to communicate with the server
     */
    void setSendToServer(SendMessageToServer sendToServer);

    /**
     * Asks the user for the ip address of the server to connect to
     *
     * @return the ip address chosen by the user
     */
    String askIpAddress();

    /**
     * Asks the user for the port number where the server is listening to
     *
     * @return the port number chosen by the user
     */
    int askPortNumber();

    /**
     * Asks the user if he wants to retry to insert a command
     *
     * @return true if the user wants to retry, false if not
     */
    boolean askRetry();

    /**
     * Asks the user the number of players that will play the game, and sends it to the server
     */
    void askNPlayer();

    /**
     * Asks the user for a nickname, and sends it to the server
     * @param e
     */
    void askName(AskName e);

    /**
     * Shows the user a welcome message when correctly connected to a game
     *
     * @param connectedIndex index of connection
     */
    void showWelcome(int connectedIndex);

    /**
     * Shows the user a message that tells him the lobby he tried to connect is full
     */
    void showFull();

    /**
     * Shows the user a message that tells him to wait for other players to join the game
     *
     * @param connectedPlayers number of connected players
     * @param totalPlayers     total number of players needed
     */
    void showWait(int connectedPlayers, int totalPlayers);

    /**
     * Shows the user a message that tells him the connection is successfully closed
     */
    void showConnectionClosed();

    /**
     * Asks the user to choose two of the four drawn leader cards
     * @param leaderCard list of leader cards randomly drawn from the deck
     */
    void chooseLeaderCards(List<LeaderCard> leaderCard);

    /**
     * Shows the user the number of starting resources he can get based on his turn
     * @param playerIndex index of the player position
     * @param nResources number of the resources to choose
     * @param nFaithPoints number of faith point to assign
     * @param warehouse updated Warehouse
     */
    void askInitialResources(int playerIndex, int nResources, int nFaithPoints, Warehouse warehouse);

    /**
     * Asks the user where and which resources to place in the warehouse
     * @param resources list of resources to insert
     * @param warehouse updated Warehouse
     */
    void askInsertWarehouse(EnumMap<ResourceEnum, Integer> resources, Warehouse warehouse);

    /**
     * asks the user what action he wants to take with the new resources, whether to add them, discard them or manage his own depots
     * @param resources list of resources to manage
     * @param warehouse updated Warehouse
     */
    void askHandleNewResourceOrSwap(EnumMap<ResourceEnum, Integer> resources, Warehouse warehouse);

    /**
     * Asks the user what depots he wants to swap
     * @param resources list of resources to manage
     * @param warehouse updated Warehouse
     */
    void askSwapDepots(EnumMap<ResourceEnum, Integer> resources, Warehouse warehouse);

    /**
     * Shows the updated warehouse
     * @param warehouse updated Warehouse
     */
    void showWarehouse(Warehouse warehouse);

    /**
     * Asks the user for confirmation to discard the remaining resources
     * @param m Message confirming the insertion of the new resources and any info such as the obligation to discard
     *         the excess resources to carry out the insertion
     */
    void confirmDiscard(TryInsert m);

    /**
     * Shows the main menu at the beginning of the turn with the possible actions to be performed to play your turn
     */
    void showTurnMoves();

    /**
     * Shows the main menu at the end of the shift with the possible actions to perform to end your turn
     */
    void showEndTurnMoves();

    /**
     * Shows the result of the swap operation between two depots
     * @param message Error/ Success message to show to user
     * @param success if the action performed is successfully completed
     * @param warehouse updated Warehouse
     */
    void elaborateSwapResult(String message, boolean success, Warehouse warehouse);

    /**
     * Show the player the current structure of the resource market
     * @param resourceMarketStructure Resource market structure
     */
    void showResourceMarketStructure(MarbleEnum[][] resourceMarketStructure);

    /**
     * asks the player what action he wants to carry out with the resources to manage
     */
    void showNewResourcesManagementRequest();

    /**
     * Shows each player a wait message until all players have finished the setup phase before the game starts
     * @param playersReady number of ready players who have completed the setup phase
     * @param total number of the players in this game
     */
    void showWaitSetup(int playersReady, int total);

    /**
     * Show the player the powers of productions available and activated in the turn
     * @param playerProductionPowers List of production powers
     */
    void showProductionPowers(List<ProductionPower> playerProductionPowers);

    /**
     * Asks the player to choose the location of the resources from which to withdraw to buy or finish
     * a purchase / activation of production power
     * @param m message to contains all informations about the warehouse, strongbox and the position of all resources
     */
    void askPayLocation(ChooseResourceLocation m);

    /**
     * Shows the current strongbox of the player
     * @param playerStrongbox updated strongbox
     */
    void showStrongbox(Strongbox playerStrongbox);

    /**
     * Show the player the message indicating that he does not have enough resources to perform the action
     * @param requirements list of missing requirements
     * @param action type of action performed
     */
    void notEnoughResources(List<Requirement> requirements, ActionEnum action);

    /**
     * Tells the user that he cannot activate the leader card due to lack of required resources
     * @param requirements list of missing requirements
     * @param isStartTurn if the activation was required at the start of turn or the end
     */
    void notEnoughResourcesActivateLeaderCard(List<Requirement> requirements,Boolean isStartTurn);

    /**
     * Shows the user the new resources obtained after going to the resource market
     * @param obtainedResources list of resources obtained from the market
     */
    void showObtainedResources(EnumMap<ResourceEnum, Integer> obtainedResources);

    /**
     * Asks the player for the choice to convert the Jolly resources obtained
     * @param obtainedResources list of obtained resources
     * @param isBaseProductionPower indicates if the production power is the base one
     */
    void askJollyResources(EnumMap<ResourceEnum, Integer> obtainedResources, boolean isBaseProductionPower);

    /**
     * Show users the match result when a victory condition is reached by one of the players
     * @param m message for stored the information abount winner and other players
     */
    void showGameOver(GameOver m);

    /**
     * Allows the user to select the lead card to discard
     * @param m message for store the leader card to discard
     */
    void selectLeaderCardToDiscard(GetLeaderCard m);

    /**
     *  Allows the user to select the lead card to activate
     * @param m message for store the leader card to activate
     */
    void selectLeaderCardToActivate(GetLeaderCard m);

    /**
     * Shows the result of discarding a leader card
     * @param m message for store the leader card to activate
     */
    void showStatusDiscardLeaderCard(DiscardLeaderCard m);

    /**
     * Shows the result of activating a leader card
     * @param m message for store the leader card to activate
     */
    void showStatusActivateLeaderCard(ActivateLeaderCard m);

    /**
     * Show the player the card market
     * @param cardMarketStructure Actual card market structure
     * @param playerWarehouse updated Warehouse
     * @param playerStrongbox updated Strongbox
     */
    void showCardMarketStructure(CardDeck[][] cardMarketStructure, Warehouse playerWarehouse, Strongbox playerStrongbox);

    /**
     * Asks the user to indicate the color and level of the paper they want to purchase
     */
    void showNewCardsManagementRequest();

    /**
     * Show the player the purchased card
     * @param boughtCard purchased card
     */
    void showBoughtCard(DevelopmentCard boughtCard);

    /**
     *  Show the player the production card slots on his board
     * @param slots player slots
     */
    void showDevSlots(DevelopmentSlots slots);

    /**
     * Asks the user to indicate the slot in which to place the newly purchased card
     * @param slots player slots
     * @param boughtCard purchased card
     */
    void askSlotToInsert(DevelopmentSlots slots, DevelopmentCard boughtCard);

    /**
     * It indicates to the user an error in inserting the production card in its slots
     * @param errorMessage error message
     */
    void showInvalidInsertion(String errorMessage);

    /**
     * Shows the updated faith track
     * @param updatedFaithTrack updated faith track
     * @param lorenzo if is the lorenzo's faith track
     */
    void showFaithTrack(FaithTrack updatedFaithTrack, boolean lorenzo);

    /**
     * shows the users victory points
     * @param victoryPoints amount of victory points
     */
    void showTotalVictoryPoints(int victoryPoints);

    /**
     * Shows the action performed by Lorenzo the Magnificent
     * @param lorenzoToken action performed by lorenzo
     */
    void showLorenzoActionToken(ActionToken lorenzoToken);

    /**
     *  Show the player a waiting message until the start of his shift
     * @param playerTurn index of player turn
     * @param nickname player nickanem
     */
    void waitYourTurn(int playerTurn, String nickname);

    /**
     * Shows the player that the selected deck is empty
     */
    void emptyDeck();

    /**
     * Shows the player an error message about the inability to place the chosen resource in the selected repository
     */
    void showWrongDepot();
}
