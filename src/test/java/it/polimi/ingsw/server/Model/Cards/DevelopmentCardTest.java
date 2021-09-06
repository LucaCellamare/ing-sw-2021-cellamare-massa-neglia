package it.polimi.ingsw.server.Model.Cards;

import it.polimi.ingsw.exceptions.CannotInsertException;
import it.polimi.ingsw.server.Model.Enums.LevelEnum;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Enums.TypeEnum;
import it.polimi.ingsw.server.Model.LeaderCardDeck;
import it.polimi.ingsw.server.Model.PersonalBoard.DevelopmentSlots;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.PlayerStub;
import it.polimi.ingsw.server.Model.Requirements.ResourceRequirement;
import org.junit.Test;

import java.util.EnumMap;
import java.util.HashMap;

import static org.junit.Assert.*;

public class DevelopmentCardTest {
    ResourceRequirement[] oneResourcePrice = new ResourceRequirement[]{
            new ResourceRequirement(ResourceEnum.SHIELD, 2)
    };

    ResourceRequirement[] twoResourcesPrice = new ResourceRequirement[]{
            new ResourceRequirement(ResourceEnum.STONE, 3),
            new ResourceRequirement(ResourceEnum.SERVANT, 3)
    };

    ResourceRequirement[] resourceRequestedProdPwr = new ResourceRequirement[]{
            new ResourceRequirement(ResourceEnum.COIN, 1)
    };

    EnumMap<ResourceEnum, Integer> resourceObtainedProdPwr = new EnumMap<>(ResourceEnum.class) {{
        put(ResourceEnum.FAITH, 1);
    }};

    ProductionPower productionPower = new ProductionPower(resourceRequestedProdPwr, resourceObtainedProdPwr);

    int firstId = 1;
    int secondId = 2;
    int thirdId = 3;

    int firstVictoryPoints = 5;
    int secondVictoryPoints = 5;
    int thirdVictoryPoints = 1;
    String firstPath = "Masters of Renaissance_Cards_FRONT_3mmBleed_1-1";
    String secondPath = "Masters of Renaissance_Cards_FRONT_3mmBleed_1-2";
    String thirdPath = "Masters of Renaissance_Cards_FRONT_3mmBleed_1-3";

    DevelopmentCard cardOneResPrice = new DevelopmentCard(firstPath, 1,
            5,
            oneResourcePrice,
            productionPower,
            TypeEnum.GREEN,
            LevelEnum.ONE);
    DevelopmentCard cardTwoResPrice = new DevelopmentCard(secondPath, 2,
            5,
            twoResourcesPrice,
            productionPower,
            TypeEnum.YELLOW,
            LevelEnum.TWO);
    DevelopmentCard cardAbnPrice = new DevelopmentCard(thirdPath, 3,
            1,
            new ResourceRequirement[]{new ResourceRequirement(ResourceEnum.SHIELD, 0)},
            productionPower,
            TypeEnum.PURPLE,
            LevelEnum.ONE);

    DevelopmentCard levelTooHigh = new DevelopmentCard(thirdPath, 3,
            1,
            new ResourceRequirement[]{new ResourceRequirement(ResourceEnum.SHIELD, 0)},
            productionPower,
            TypeEnum.PURPLE,
            LevelEnum.THREE);

    LeaderCardDeck leaderCardDeck = new LeaderCardDeck();

    @Test
    public void canBeBoughtTest() {
        HashMap<ResourceEnum, Integer> resourcesForBoth = new HashMap<>() {{
            put(ResourceEnum.SHIELD, 2);
            put(ResourceEnum.STONE, 5);
            put(ResourceEnum.SERVANT, 4);
        }};
        Player canBuyBoth = new PlayerStub(resourcesForBoth);

        HashMap<ResourceEnum, Integer> resourcesForOne = new HashMap<>() {{
            put(ResourceEnum.STONE, 3);
            put(ResourceEnum.SERVANT, 3);
        }};
        Player canBuyOnlyOne = new PlayerStub(resourcesForOne);

        Player canBuyNeither = new PlayerStub();

        assertTrue(cardOneResPrice.canBeBought(canBuyBoth));
        assertFalse(cardOneResPrice.canBeBought(canBuyOnlyOne));
        assertFalse(cardOneResPrice.canBeBought(canBuyNeither));
        try {
            DevelopmentSlots playerSlots = canBuyBoth.getSlots();
            playerSlots.addCard(cardOneResPrice, 1);
        } catch (CannotInsertException e) {
            fail("unexpected exception thrown");
            e.printStackTrace();
        }
        assertFalse(cardTwoResPrice.canBeBought(canBuyOnlyOne)); // not enough space in dev slots
        assertTrue(cardTwoResPrice.canBeBought(canBuyBoth));    // can insert in slots after he bought cardOneResPrice
        assertTrue(cardAbnPrice.canBeBought(canBuyNeither));
        assertTrue(cardAbnPrice.canBeBought(canBuyOnlyOne));

        assertFalse(levelTooHigh.canBeBought(canBuyBoth));

        Player discountPlayer = new PlayerStub();

        System.out.println(cardOneResPrice.toString());
        discountPlayer.addDiscountLeaderAbility(cardOneResPrice.getPrice()[0].getResourceType());
        for (int i = 0; i < oneResourcePrice.length; i++) {
            if (i == 0)
                discountPlayer.getStrongbox().insertResource(oneResourcePrice[i].getResourceType(), oneResourcePrice[i].getQuantity() - 1);
            else
                discountPlayer.getStrongbox().insertResource(oneResourcePrice[i].getResourceType(), oneResourcePrice[i].getQuantity());
        }
        assertTrue(cardOneResPrice.canBeBought(discountPlayer));
    }

    @Test
    public void getIdTest() {
        assertEquals(firstId, cardOneResPrice.getId());
    }

    @Test
    public void getVictoryPointsTest() {
        assertEquals(secondVictoryPoints, cardTwoResPrice.getVictoryPoints());
    }

    @Test
    public void getTypeTest() {
        assertEquals(TypeEnum.PURPLE, cardAbnPrice.getType());
    }

    @Test
    public void getLevelTest() {
        assertEquals(LevelEnum.ONE, cardOneResPrice.getLevel());
    }

    @Test
    public void getPriceTest() {
        assertArrayEquals(oneResourcePrice, cardOneResPrice.getPrice());
        assertArrayEquals(twoResourcesPrice, cardTwoResPrice.getPrice());
    }

    @Test
    public void getProductionPowerTest() {
        assertEquals(productionPower, cardTwoResPrice.getProductionPower());
    }
}