package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.VirtualView;

/**
 * Server side visitor of the visitor pattern.
 * It elaborates a message received by the client based on its type.
 *
 * @author Roberto Neglia
 */
public class ClientVisitor {

    /**
     * Reference to the virtual view, used to notify the controller for every type of message received.
     */
    private final VirtualView virtualView;

    /**
     * Class constructor.
     *
     * @param virtualView reference to the virtual view
     */
    public ClientVisitor(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    /**
     * Base method of the pattern, it's overloaded by the several types of messages, and based on that type it behaves differently.
     *
     * @param m message received
     */
    public void elaborate(Message m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(AskNPlayer m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(AskName m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(ChooseLeaderCard m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(ChooseInitialResources m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(TryInsert m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(ConfirmInsert m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(PlayerReady m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(CloseConnection event) {
    }

    public void elaborate(SwapDepot m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(ResourceMarketAction m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(SelectedResourceFromMarket m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(ProductionPowerAction m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(UseProductionPower m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(ChooseResourceLocation m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(EndTurn m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(ProductionPowerUsed m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(InsertJollyResource m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(DiscardResources m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(GetLeaderCard m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(DiscardLeaderCard m){
        virtualView.notifyObserver(m);
    }

    public void elaborate(ActivateLeaderCard m){
        virtualView.notifyObserver(m);
    }

    public void elaborate(CardMarketAction m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(SelectedCardFromMarket m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(SlotInsertionAction m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(AbortInsert m) {
        virtualView.notifyObserver(m);
    }

    public void elaborate(WhiteMarbleSelection m) {
        virtualView.notifyObserver(m);
    }

}
