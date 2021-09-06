package it.polimi.ingsw.server.Model.TokenEffects;

import it.polimi.ingsw.server.Model.ActionToken;
import it.polimi.ingsw.server.Model.FaithTrack.FaithTrack;
import it.polimi.ingsw.server.Model.SinglePlayerGame;
import it.polimi.ingsw.server.Model.TokenEffects.FaithAdvance;
import org.junit.Assert;
import org.junit.Test;


public class FaithAdvanceTest {

    @Test
    public void testFaithAdvance() {

        SinglePlayerGame game = new SinglePlayerGame();

        ActionToken faithAdvanceByOne = new ActionToken(new FaithAdvance(1, true));
        ActionToken faithAdvanceByTwo = new ActionToken(new FaithAdvance(2, false));

        FaithTrack lorenzoFaithTrack = game.getLorenzoFaithTrack();

        faithAdvanceByOne.playActionEffect(game);
        Assert.assertEquals(1, lorenzoFaithTrack.getPlayerPosition());
        faithAdvanceByTwo.playActionEffect(game);
        Assert.assertEquals(3, lorenzoFaithTrack.getPlayerPosition());
    }

}