package it.polimi.ingsw.server.Model.TokenEffects;

import it.polimi.ingsw.server.Model.Enums.TypeEnum;
import it.polimi.ingsw.server.Model.SinglePlayerGame;
import it.polimi.ingsw.server.Model.Table.CardMarket.CardMarket;
import it.polimi.ingsw.server.Model.Table.Table;

/**
 * Describes the specific Discard Effect contained in some Action Token used in the Single Player mode
 */
public class DiscardEffect implements ActionEffect {

    /**
     * The color of the Development Card to be discarded form the Card market
     */
    private final TypeEnum colorCard;

    /**
     * Quantity of cards to be discarded from the Card Market
     */
    private final int quantity;

    /**
     * Class Constructor
     *
     * @param colorCard color of the cards to be discarded
     * @param quantity  the quantity of cards to be discarded
     */
    public DiscardEffect(TypeEnum colorCard, int quantity) {
        this.colorCard = colorCard;
        this.quantity = quantity;
    }

    /**
     * Retrieves the quantity of cards to be discarded
     *
     * @return the quantity of cards to be discarded
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Retrieves the color of the cards to be discarded
     *
     * @return the color of the cards to be discarded
     */
    public TypeEnum getColorCard() {
        return colorCard;
    }

    /**
     * Activates the Discard Effect, removing from the Card Market a certain amount of cards of a specific color
     *
     * @param game the game in which is contained the CardMarket
     */
    @Override
    public void useActionEffect(SinglePlayerGame game) {
        Table table = game.getTable();
        CardMarket cardMarket = table.getCardMarket();
        cardMarket.removeCardsOfColor(quantity, colorCard);
    }

    @Override
    public String toString() {
        return "discards " + quantity + " " + colorCard + " cards!";
    }
}
