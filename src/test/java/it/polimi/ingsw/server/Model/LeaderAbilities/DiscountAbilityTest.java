package it.polimi.ingsw.server.Model.LeaderAbilities;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.LeaderAbilities.DiscountAbility;
import it.polimi.ingsw.server.Model.LeaderCardDeck;
import it.polimi.ingsw.server.Model.Player;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DiscountAbilityTest {
    LeaderCardDeck deck = new LeaderCardDeck();
    int servantDiscountQuantity = 1;
    int coinDiscountQuantity = 2;
    DiscountAbility servantDiscount = new DiscountAbility(ResourceEnum.SERVANT, servantDiscountQuantity);
    DiscountAbility coinDiscount = new DiscountAbility(ResourceEnum.COIN,coinDiscountQuantity);

    @Test
    public void activateAbilityTest() {
        Player p = new Player("test", false);

        boolean illegalArgument = false;

        try{
            coinDiscount.activateAbility(null);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        coinDiscount.activateAbility(p);

        List<ResourceEnum> expectedDiscountList = new ArrayList<>();
        expectedDiscountList.add(coinDiscount.getResource());
        expectedDiscountList.add(servantDiscount.getResource());

        assertNotEquals(expectedDiscountList, p.getDiscountLeaderAbilities());

        servantDiscount.activateAbility(p);

        assertEquals(expectedDiscountList, p.getDiscountLeaderAbilities());
    }

    @Test
    public void getResourceTest(){
        assertEquals(ResourceEnum.SERVANT, servantDiscount.getResource());
        assertEquals(ResourceEnum.COIN, coinDiscount.getResource());
    }

    @Test
    public void getQuantityTest(){
        assertEquals(coinDiscountQuantity, coinDiscount.getQuantity());
        assertEquals(servantDiscountQuantity, servantDiscount.getQuantity());
    }
}