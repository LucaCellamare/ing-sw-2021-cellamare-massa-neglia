package it.polimi.ingsw.server.Model.Table.CardMarket;

import it.polimi.ingsw.server.Model.Cards.DevelopmentCard;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Random;


/**
 * The deck containing the Development Card which are exposed in the Card Market
 */
public class CardDeck implements Serializable {

    /**
     * The cards contained in the deck
     */
    private final ArrayDeque<DevelopmentCard> cards = new ArrayDeque<>();


    /**
     * Class constructor
     * Note that the arrangement of the cards is random.
     */
    public CardDeck(DevelopmentCard[] cardToShuffle) {
        shuffleArray(cardToShuffle);
        for (DevelopmentCard developmentCard : cardToShuffle) {
            this.cards.push(developmentCard);
        }
    }

    /**
     * Helper method to shuffle the cards when constructing the CardDeck
     *
     * @param array cards that needs to be shuffled
     */
    private static <V> void shuffleArray(V[] array) {
        int index;
        V temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    /**
     * Retrieves the card at the top of the Stack
     *
     * @return the top card of the stack
     */
    public DevelopmentCard peek() {
        return this.cards.peek();
    }

    /**
     * Retrieves the card at the top of the Stack and removes it from the stack
     *
     * @return the removed card that was at the top of the stack
     */
    public DevelopmentCard pop() {
        return this.cards.pop();
    }

    /**
     * Retrieves the cards inside the Deck
     *
     * @return the cards present in the deck
     */
    public DevelopmentCard[] getCards() {
        return this.cards.toArray(new DevelopmentCard[0]);
    }

    /**
     * Retrieves the size of the Deck
     *
     * @return the deck size
     */
    public int getSize() {
        return cards.size();
    }

}
