package it.polimi.ingsw.server.Model.Requirements;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.LeaderCardDeck;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.PlayerStub;
import it.polimi.ingsw.server.Model.Requirements.ResourceRequirement;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class ResourceRequirementTest {
    ResourceEnum firstReqRes = ResourceEnum.COIN;
    int firstReqQ = 2;
    ResourceRequirement regularRequirement = new ResourceRequirement(firstReqRes, firstReqQ);

    ResourceEnum secondReqRes = ResourceEnum.STONE;
    int secondReqQ = 0;
    ResourceRequirement abnormalRequirement = new ResourceRequirement(secondReqRes, secondReqQ);

    ResourceRequirement satisfiableJollyRequirement = new ResourceRequirement(ResourceEnum.JOLLY, 1);
    ResourceRequirement unsatisfiableJollyRequirement = new ResourceRequirement(ResourceEnum.JOLLY, 40);


    @Before
    public void setup() {
        LeaderCardDeck leaderCardDeck = new LeaderCardDeck();
    }

    @Test
    public void getQuantityTest() {
        assertEquals(firstReqQ, regularRequirement.getQuantity());
    }

    @Test
    public void getResourceTypeTest() {
        assertEquals(firstReqRes, regularRequirement.getResourceType());
    }

    @Test
    public void applyDiscountTest() {
        regularRequirement.applyDiscount(1);

        assertEquals(1, regularRequirement.getQuantity());
    }

    @Test
    public void checkRequirementTest() {
        HashMap<ResourceEnum, Integer> res = new HashMap<>();

        res.put(ResourceEnum.COIN, 2);
        res.put(ResourceEnum.SERVANT, 3);
        Player playerWithEnoughCoins = new PlayerStub(res);

        res.replace(ResourceEnum.COIN, 1);
        Player playerWithoutEnoughCoins = new PlayerStub(res);

        playerWithEnoughCoins.getWarehouse().insert(ResourceEnum.SHIELD, 3,2);

        res.remove(ResourceEnum.COIN);
        res.put(ResourceEnum.SHIELD, 10);
        Player playerWithoutCoins = new PlayerStub(res);

        res.put(ResourceEnum.STONE, 1);
        Player playerWithResourcesForAbnormalRequirement = new PlayerStub(res);

        boolean illegalArgument = false;

        try{
            regularRequirement.checkRequirement(null);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);


        assertTrue(regularRequirement.checkRequirement(playerWithEnoughCoins));
        assertTrue(satisfiableJollyRequirement.checkRequirement(playerWithEnoughCoins));
        assertTrue(satisfiableJollyRequirement.checkRequirement(playerWithoutCoins));
        assertFalse(unsatisfiableJollyRequirement.checkRequirement(playerWithoutCoins));
        assertFalse(regularRequirement.checkRequirement(playerWithoutEnoughCoins));
        assertFalse(regularRequirement.checkRequirement(playerWithoutCoins));
        assertTrue(abnormalRequirement.checkRequirement(playerWithResourcesForAbnormalRequirement));
        playerWithResourcesForAbnormalRequirement.getStrongbox().remove(ResourceEnum.STONE, 1);
        Player playerWithoutResourceForAbnormalRequirement = playerWithResourcesForAbnormalRequirement;
        assertTrue(abnormalRequirement.checkRequirement(playerWithoutResourceForAbnormalRequirement));
    }

    @Test
    public void equalsTest() {
        ResourceRequirement regularRequirementCopy = new ResourceRequirement(ResourceEnum.COIN, 2);
        assertEquals(regularRequirementCopy, regularRequirement);
        assertNotEquals(regularRequirement, abnormalRequirement);
        assertNotEquals(regularRequirement, null);
        assertNotEquals(regularRequirement, new Object());
        assertEquals(regularRequirement, regularRequirement);
    }
}
