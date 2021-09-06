package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.handlers.PlayerHandler;
import it.polimi.ingsw.client.handlers.ProductionPowersHandler;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.Model.Cards.ProductionPower;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;

import java.util.EnumMap;

/**
 * Client side visitor of the visitor pattern.
 * It elaborates a message received by the server based on its type.
 *
 * @author Roberto Neglia
 */
public class ServerVisitor {

    /**
     * Reference of the view part of the MVC pattern client side, used to notify the user for updates in the model and messages from the server
     */
    View view;


    public ServerVisitor(View view) {
        this.view = view;
    }

    /**
     * Base method of the pattern, it's overloaded by the several types of messages, and based on that type it behaves differently.
     *
     * @param m message received
     */
    public void elaborate(Message m) {
    }

    public void elaborate(AskNPlayer e) {
        view.askNPlayer();
    }

    public void elaborate(Connected e) {
        view.showWelcome(e.getConnectedIndex());
    }

    public void elaborate(CloseConnection e) {
        view.showConnectionClosed();
    }

    public void elaborate(AskName e) {
        view.askName(e);
    }

    public void elaborate(WaitForPlayers e) {
        view.showWait(e.getPlayersConnected(), e.getTotalPlayers());
    }

    public void elaborate(AlreadyFull e) {
        view.showFull();
    }

    public void elaborate(ChooseLeaderCard e) {
        view.chooseLeaderCards(e.getToChoose());
    }

    public void elaborate(ChooseInitialResources e) {
        view.askInitialResources(e.getPlayerIndex(), e.getNResources(), e.getNFaithPoints(), e.getWarehouse());
    }

    public void elaborate(InsertResources e) {
        view.askInsertWarehouse(e.getToInsert(), e.getWarehouse());
    }

    public void elaborate(TryInsert e) {
        if (e.getQuantity() == -1)
            view.showWrongDepot();
        else
            view.confirmDiscard(e);
    }

    public void elaborate(WarehouseUpdate m) {
        view.showWarehouse(m.getUpdatedWarehouse());
    }

    public void elaborate(SetupComplete m) {
        view.showWaitSetup(m.getnReady(), m.getTotal());
    }

    public void elaborate(TurnStartEvent e) {
        PlayerHandler.setAlreadyProductionActivated(false);
        view.showTurnMoves();
    }

    public void elaborate(WaitTurnEvent e) {
        view.waitYourTurn(e.getPosition(), e.getNickname());
    }

    public void elaborate(SwapDepot e) {
        view.elaborateSwapResult(e.getMessageResult(), e.getSuccessOperation(), e.getWarehouse());
    }

    public void elaborate(ResourceMarketAction e) {
        view.showResourceMarketStructure(e.getResourceMarketStructure());
    }

    public void elaborate(CardMarketAction e) {
        view.showCardMarketStructure(e.getCardMarketStructure(), e.getPlayerWarehouse(), e.getPlayerStrongbox());
    }

    public void elaborate(SlotInsertionAction e) {
        view.showInvalidInsertion(e.getErrorMessage());
        view.askSlotToInsert(e.getSlots(), e.getCardToInsert());
    }

    public void elaborate(DevelopmentSlotUpdate e) {
        view.showDevSlots(e.getSlots());
    }

    public void elaborate(InsertDevelopmentCard m) {
        view.showBoughtCard(m.getBoughtCard());
        view.askSlotToInsert(m.getSlots(), m.getBoughtCard());
    }

    public void elaborate(ProductionPowerAction e) {
        ProductionPowersHandler.setProductionPowers(e.getPlayerProductionPowers());
        view.showProductionPowers(e.getPlayerProductionPowers());
    }

    public void elaborate(ChooseResourceLocation m) {
        view.askPayLocation(m);
    }

    public void elaborate(StrongboxUpdate m) {
        view.showStrongbox(m.getUpdatedStrongbox());
    }

    public void elaborate(NotEnoughResources m) {
        view.notEnoughResources(m.getMissingRequirements(), m.getAction());
    }

    public void elaborate(EmptyDeck m) {
        view.emptyDeck();
    }

    public void elaborate(EndTurn m) {
        view.showEndTurnMoves();
    }

    public void elaborate(ProductionPowerUsed m) {
        ProductionPower usedProductionPower = ProductionPowersHandler.getCurrentProductionPower();
        EnumMap<ResourceEnum, Integer> obtainedResources = usedProductionPower.getResourceObtained();
        if (obtainedResources.containsKey(ResourceEnum.JOLLY))
            view.askJollyResources(obtainedResources, usedProductionPower.isBaseProductionPower());

        PlayerHandler.setAlreadyProductionActivated(true);
        view.showObtainedResources(ProductionPowersHandler.getCurrentProductionPower().getResourceObtained());
        ProductionPowersHandler.removeCurrentProductionPower();
        view.showProductionPowers(ProductionPowersHandler.getProductionPowers());
    }

    public void elaborate(SelectedResourceFromMarket m) {
        view.askHandleNewResourceOrSwap(m.getNewResourceList(), m.getWarehouse());
    }

    public void elaborate(DiscardResources m) {
        view.showEndTurnMoves();
    }

    public void elaborate(GetLeaderCard m) {
        if (m.isDiscardAction()) {
            view.selectLeaderCardToDiscard(m);
        } else {
            view.selectLeaderCardToActivate(m);
        }
    }

    public void elaborate(DiscardLeaderCard m) {
        view.showStatusDiscardLeaderCard(m);
    }

    public void elaborate(ActivateLeaderCard m) {

        if (m.getSuccessActivate() || (!m.getSuccessActivate() && m.getMissingRequirements() == null)) {
            view.showStatusActivateLeaderCard(m);
        } else {
            view.notEnoughResourcesActivateLeaderCard(m.getMissingRequirements(), m.getIsStartTurn());
            //view.notEnoughResources(m.getMissingRequirements(), ActionEnum.LEADER_ACTIVATION);
            //view.showStatusActivateLeaderCard(m);
        }

    }

    public void elaborate(TotalVictoryPoints m) {
        view.showTotalVictoryPoints(m.getTotalVictoryPoints());
    }

    public void elaborate(GameOver m) {
        view.showGameOver(m);
    }

    public void elaborate(FaithTrackUpdate m) {
        view.showFaithTrack(m.getUpdatedFaithTrack(), m.isLorenzo());
    }

    public void elaborate(LorenzoActionToken m) {
        view.showLorenzoActionToken(m.getLorenzoActionToken());
    }
}
