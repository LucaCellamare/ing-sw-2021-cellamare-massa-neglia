package it.polimi.ingsw.server.Model.LeaderAbilities;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.LeaderAbilities.WhiteMarbleAbility;
import it.polimi.ingsw.server.Model.LeaderCardDeck;
import it.polimi.ingsw.server.Model.Player;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WhiteMarbleAbilityTest {
    LeaderCardDeck deck = new LeaderCardDeck();
    WhiteMarbleAbility coinChangeAbility = new WhiteMarbleAbility(ResourceEnum.COIN);
    WhiteMarbleAbility stoneChangeAbility = new WhiteMarbleAbility(ResourceEnum.STONE);

    @Test
    public void activateAbilityTest() {
        Player p = null;
        boolean illegalArgument = false;
        try {
            coinChangeAbility.activateAbility(p);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);


        p = new Player("test", false);

        coinChangeAbility.activateAbility(p);

        List<ResourceEnum> expectedDiscountList = new ArrayList<>();
        expectedDiscountList.add(coinChangeAbility.getResource());
        expectedDiscountList.add(stoneChangeAbility.getResource());

        assertNotEquals(expectedDiscountList, p.getWhiteMarbleLeaderAbilities());

        stoneChangeAbility.activateAbility(p);

        assertEquals(expectedDiscountList, p.getWhiteMarbleLeaderAbilities());
    }
}