package it.polimi.ingsw.client;

import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.server.Model.Cards.LeaderCard;
import it.polimi.ingsw.server.Model.Cards.ProductionPower;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Enums.TypeLineEnum;

import java.util.EnumMap;

/**
 * Class that creates messages that will be sent to the server
 *
 * @author Roberto Neglia
 */
public class SendMessageToServer {

    /**
     * Reference to the network handler used by the client to communicate with the server
     */
    NetworkHandler net; // the handler for the connection with the server

    /**
     * Class constructor
     *
     * @param net reference to the network handler
     */
    public SendMessageToServer(NetworkHandler net) {
        this.net = net;
    }

    // message where the client tells the server how many player there are

    /**
     * Generate a message where the client tells the server how many players there are in the game
     *
     * @param nPlayer number of players
     */
    public void sendNPlayer(int nPlayer) {
        Message m = new AskNPlayer(nPlayer);
        send(m);
    }

    /**
     * Generate a message where the client tells the server his nickname for the game
     *
     * @param name nickname of the player
     */
    public void sendName(String name) {
        Message m = new AskName(name);
        send(m);
    }

    public void sendReconnectedName(String name, int reconnectedIndex) {
        Message m = new AskName(name, reconnectedIndex);
        send(m);
    }

    public void sendChosenCard(int[] chosenCards) {
        Message m = new ChooseLeaderCard(chosenCards[0], chosenCards[1]);
        send(m);
    }

    public void sendInitialResources(EnumMap<ResourceEnum, Integer> initialResources) {
        Message m = new ChooseInitialResources(initialResources);
        send(m);
    }

    public void sendTryInsert(ResourceEnum resource, int quantity, int depotIndex) {
        Message m = new TryInsert(resource, quantity, depotIndex);
        send(m);
    }

    public void confirmInsert(ResourceEnum resource, int quantity, int depotIndex) {
        Message m = new ConfirmInsert(resource, quantity, depotIndex);
        send(m);
    }

    public void abortInsert() {
        Message m = new AbortInsert();
        send(m);
    }

    public void sendReady() {
        Message m = new PlayerReady();
        send(m);
    }

    /**
     * Generate a message where the client tells the server he wants to close the connection
     */
    public void sendCloseConnection() {
//        Message m = new CloseConnection();
//        send(m);
        net.setOpen(false);
    }

    public void sendResourceMarketRequest() {
        Message m = new ResourceMarketAction();
        send(m);
    }

    public void sendSelectedResourceFromMarket(TypeLineEnum typeSelected, int indexSelected) {
        Message m = new SelectedResourceFromMarket(typeSelected, indexSelected);
        send(m);
    }

    public void sendSelectedCardFromMarket(int levelSelected, int colorSelected) {
        Message m = new SelectedCardFromMarket(levelSelected, colorSelected);
        send(m);
    }

    public void sendProductionPowerRequest() {
        Message m = new ProductionPowerAction();
        send(m);
    }

    public void sendTryUseProductionPower(ProductionPower p) {
        Message m = new UseProductionPower(p);
        send(m);
    }

    public void sendChosenResourceLocation(ChooseResourceLocation m) {
        send(m);
    }

    public void insertObtainedResources() {
        Message m = new ProductionPowerUsed();
        send(m);
    }

    public void sendJollyResource(ResourceEnum chosenResource, int quantity) {
        Message m = new InsertJollyResource(chosenResource, quantity);
        send(m);
    }

    public void endTurn() {
        Message m = new EndTurn();
        send(m);
    }

    public void sendSwapDepotsRequest(int idxDepot1, int idxDepot2) {
        Message m = new SwapDepot(idxDepot1, idxDepot2);
        send(m);
    }

    public void sendDiscardResources(EnumMap<ResourceEnum, Integer> resoures) {
        Message m = new DiscardResources(resoures);
        send(m);
    }

    public void sendObtainLeaderCard(boolean isDiscardAction, boolean isStartTurn) {
        Message m = new GetLeaderCard(isDiscardAction, isStartTurn);
        send(m);
    }

    public void sendDiscardLeaderCard(boolean isDiscardAction, boolean isStartTurn, LeaderCard leaderCard) {
        Message m = new DiscardLeaderCard(leaderCard, isDiscardAction, isStartTurn);
        send(m);
    }

    public void sendActivateLeaderCard(boolean isDiscardAction, boolean isStartTurn, LeaderCard leaderCard) {
        Message m = new ActivateLeaderCard(leaderCard, isDiscardAction, isStartTurn);
        send(m);
    }


    public void sendCardMarketRequest() {
        Message m = new CardMarketAction();
        send(m);
    }

    public void sendSelectedSlotToInsertCard(int slotNumber) {
        Message m = new SlotInsertionAction(slotNumber);
        send(m);
    }

    public void sendWhiteMarbleSelection(EnumMap<ResourceEnum, Integer> newResources) {
        Message m = new WhiteMarbleSelection(newResources);
        send(m);
    }

    /**
     * Actually sends the message to the server using the network handler
     *
     * @param m the message to be sent
     */
    private void send(Message m) {
        net.sendMessage(m);
    }
}
