package it.polimi.ingsw.server.Model.Table.CardMarket;

import it.polimi.ingsw.server.Model.Enums.TypeEnum;
import it.polimi.ingsw.server.Model.SinglePlayerGame;
import it.polimi.ingsw.server.Model.Table.CardMarket.CardDeck;
import it.polimi.ingsw.server.Model.Table.CardMarket.CardMarket;
import org.junit.Test;

import static org.junit.Assert.*;

public class CardMarketTest {
    @Test
    public void testGetIndexByColor() {
        SinglePlayerGame game = new SinglePlayerGame();
        CardMarket cardMarket = game.getTable().getCardMarket();
        int expected = 1;
        int obtained = cardMarket.getIndexByColor(TypeEnum.PURPLE);
        assertEquals(expected, obtained);
        expected = 0;
        obtained = cardMarket.getIndexByColor(TypeEnum.GREEN);
        assertEquals(expected, obtained);
        expected = 3;
        obtained = cardMarket.getIndexByColor(TypeEnum.YELLOW);
        assertEquals(expected, obtained);
        expected = 2;
        obtained = cardMarket.getIndexByColor(TypeEnum.BLUE);
        assertEquals(expected, obtained);

    }

    @Test
    public void testRemove() {
        SinglePlayerGame game = new SinglePlayerGame();
        CardMarket cardMarket = game.getTable().getCardMarket();
        CardDeck tmpDeck = cardMarket.getMarketStructure()[0][2];
        int originalSize = tmpDeck.getSize();
        int expectedSize = originalSize - 1;
        cardMarket.remove(TypeEnum.GREEN, 2);
        int obtained = cardMarket.getMarketStructure()[0][2].getSize();
        assertEquals(expectedSize, obtained);

    }

    @Test
    public void emptyColumnTest() {
        CardMarket cardMarket = new CardMarket();

        int greenColumnPosition = cardMarket.getIndexByColor(TypeEnum.GREEN);

        cardMarket.removeCardsOfColor(10, TypeEnum.GREEN);
        assertFalse(cardMarket.emptyColumn(greenColumnPosition));

        cardMarket.removeCardsOfColor(2, TypeEnum.GREEN);
        assertTrue(cardMarket.emptyColumn(greenColumnPosition));
    }

}