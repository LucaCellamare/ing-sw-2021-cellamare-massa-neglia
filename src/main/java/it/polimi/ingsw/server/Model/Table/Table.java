package it.polimi.ingsw.server.Model.Table;

import it.polimi.ingsw.server.Model.Table.CardMarket.CardMarket;

/**
 * The area in common for the players during a match
 *
 * @author Antonio Massa
 */
public class Table { // Observable
    /**
     * The resource market, common to all the players
     */
    private final ResourceMarket resourceMarket;

    /**
     * The card market, common to all the players
     */
    private final CardMarket cardMarket;

    /**
     * Class Constructor
     */
    public Table() {
        resourceMarket = new ResourceMarket();
        cardMarket = new CardMarket();
    }

    /**
     * Retrieves the ResouorceMarket
     *
     * @return the ResourceMarket
     */
    public ResourceMarket getResourceMarket() {
        return resourceMarket;
    }

    /**
     * Retrieves the CardMarket Structur
     *
     * @return the CardMarket
     */
    public CardMarket getCardMarket() {
        return cardMarket;
    }

}
