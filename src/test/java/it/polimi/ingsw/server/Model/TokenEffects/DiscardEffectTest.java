package it.polimi.ingsw.server.Model.TokenEffects;

import it.polimi.ingsw.server.Model.ActionToken;
import it.polimi.ingsw.server.Model.Cards.DevelopmentCard;
import it.polimi.ingsw.server.Model.SinglePlayerGame;
import it.polimi.ingsw.server.Model.Table.CardMarket.CardDeck;
import it.polimi.ingsw.server.Model.Table.CardMarket.CardMarket;
import it.polimi.ingsw.server.Model.TokenEffects.DiscardEffect;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static it.polimi.ingsw.server.Model.Enums.TypeEnum.GREEN;
import static org.junit.Assert.assertEquals;

public class DiscardEffectTest {
    @Test
    public void testDiscardEffect() {
        SinglePlayerGame game = new SinglePlayerGame();
        int discardQuantity = 2;
        CardMarket cardMarket = game.getTable().getCardMarket();
        DiscardEffect effect = new DiscardEffect(GREEN, discardQuantity);
        ActionToken greenDiscardToken = new ActionToken(effect);
        CardDeck initialDeck = cardMarket.getMarketStructure()[cardMarket.getIndexByColor(GREEN)][0];
        DevelopmentCard[] initialCards = Arrays.copyOf(cardMarket.getMarketStructure()[cardMarket.getIndexByColor(GREEN)][0].getCards(), initialDeck.getSize());
        DevelopmentCard[] discardedCards = new DevelopmentCard[discardQuantity];
        int j = initialCards.length - 1;
        for(int i = 0; i < discardQuantity; i++) {
            discardedCards[i] = initialCards[j];
            assertEquals(effect.getColorCard(), discardedCards[i].getType());
            j--;
        }

        assertEquals(discardQuantity, discardedCards.length);

        DevelopmentCard[] expectedCards = Arrays.copyOf(cardMarket.getMarketStructure()[cardMarket.getIndexByColor(GREEN)][0].getCards(), initialDeck.getSize() - discardQuantity);

        greenDiscardToken.playActionEffect(game);
        DevelopmentCard[] finalCards = cardMarket.getMarketStructure()[cardMarket.getIndexByColor(GREEN)][0].getCards();
        Assert.assertArrayEquals(expectedCards, finalCards);
    }

}