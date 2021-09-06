package it.polimi.ingsw.server.Model.Table.CardMarket;

import it.polimi.ingsw.server.Model.Cards.DevelopmentCard;
import it.polimi.ingsw.server.Model.Table.CardMarket.CardDeck;
import it.polimi.ingsw.server.Model.Table.CardMarket.CardMarket;
import junit.framework.TestCase;
import org.junit.Test;

public class CardDeckTest extends TestCase {
    @Test
    public void testPeek() {
        CardMarket cardMarket = new CardMarket();
        CardDeck[][] cardDecks = cardMarket.getMarketStructure();
        CardDeck cardDeck = cardDecks[0][2];
        DevelopmentCard[] arrayCards = cardDeck.getCards();
        assertEquals(cardDeck.peek(),arrayCards[0]);

    }
    @Test
    public void testPop() {
        CardMarket cardMarket = new CardMarket();
        CardDeck[][] cardDecks = cardMarket.getMarketStructure();
        CardDeck cardDeck = cardDecks[0][2];
        DevelopmentCard[] arrayCards = cardDeck.getCards();
        int expSize = cardDeck.getSize();
        DevelopmentCard expected = cardDeck.peek();
        assertEquals(arrayCards[0],expected);
        assertEquals(cardDeck.pop(),expected);
        assertEquals(expSize-1,cardDeck.getSize());
    }

    @Test
    public void testGetCards() {
        CardMarket cardMarket = new CardMarket();
        CardDeck[][] cardDecks = cardMarket.getMarketStructure();
        CardDeck cardDeck = cardDecks[0][2];
        DevelopmentCard[] arrayCards = cardDeck.getCards();
        int i = 0;
        while(cardDeck.getSize()>0){
            DevelopmentCard tmp = cardDeck.pop();
            assertEquals(arrayCards[i],tmp);
            i++;
        }
    }

    @Test
    public void testGetSize() {
        CardMarket cardMarket = new CardMarket();
        CardDeck[][] cardDecks = cardMarket.getMarketStructure();
        CardDeck cardDeck = cardDecks[0][2];
        DevelopmentCard[] arrayCards = cardDeck.getCards();
        int expSize = cardDeck.getSize();
        assertEquals(expSize,arrayCards.length);
        cardDeck.pop();
        assertEquals(expSize-1,cardDeck.getSize());
    }

}