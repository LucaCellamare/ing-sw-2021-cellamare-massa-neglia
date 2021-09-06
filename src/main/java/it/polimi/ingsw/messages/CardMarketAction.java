package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.PersonalBoard.Strongbox;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Warehouse;
import it.polimi.ingsw.server.Model.Table.CardMarket.CardDeck;

/**
 * Message that tells the Server that the client has chosen to go to Card Market.
 * It's also used to send to the client all the cards that are available in the Card Market.
 */
public class CardMarketAction extends Message {

    private CardDeck[][] cardMarketStructure;

    private Warehouse playerWarehouse;
    private Strongbox playerStrongbox;

    public CardMarketAction() {
    }

    public CardMarketAction(CardDeck[][] cardMarketStructure, Warehouse playerWarehouse, Strongbox playerStrongbox) {
        this.cardMarketStructure = cardMarketStructure;
        this.playerWarehouse = playerWarehouse;
        this.playerStrongbox = playerStrongbox;
    }

    public Warehouse getPlayerWarehouse() {
        return playerWarehouse;
    }

    public Strongbox getPlayerStrongbox() {
        return playerStrongbox;
    }

    public CardDeck[][] getCardMarketStructure() {
        return cardMarketStructure;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {
        clientVisitor.elaborate(this);
    }
}
