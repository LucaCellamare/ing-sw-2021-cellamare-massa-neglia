package it.polimi.ingsw.server.Model.Cards;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.LeaderCardDeck;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.PlayerStub;
import it.polimi.ingsw.server.Model.Requirements.ResourceRequirement;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.Test;

import java.util.EnumMap;
import java.util.HashMap;

import static org.junit.Assert.*;

public class ProductionPowerTest {
    LeaderCardDeck leaderCardDeck = new LeaderCardDeck();
    ResourceRequirement[] oneResourceRequested = {new ResourceRequirement(ResourceEnum.STONE, 1)};
    ResourceRequirement[] oneJollyResourceRequested = {new ResourceRequirement(ResourceEnum.JOLLY, 1)};
    ResourceRequirement[] otherOneResourceRequested = {new ResourceRequirement(ResourceEnum.SHIELD, 1)};

    EnumMap<ResourceEnum, Integer> oneResourceObtained = new EnumMap<>(ResourceEnum.class) {{
        put(ResourceEnum.FAITH, 1);
    }};

    EnumMap<ResourceEnum, Integer> oneJollyResourceObtained = new EnumMap<>(ResourceEnum.class) {{
        put(ResourceEnum.JOLLY, 1);
    }};

    ResourceRequirement[] twoResourcesRequested = {
            new ResourceRequirement(ResourceEnum.COIN, 1),
            new ResourceRequirement(ResourceEnum.SERVANT, 1)
    };

    EnumMap<ResourceEnum, Integer> threeResourcesObtained = new EnumMap<>(ResourceEnum.class) {{
        put(ResourceEnum.SHIELD, 2);
        put(ResourceEnum.STONE, 2);
        put(ResourceEnum.FAITH, 1);
    }};

    ProductionPower oneToOne = new ProductionPower(oneResourceRequested, oneResourceObtained);
    ProductionPower oneToThree = new ProductionPower(otherOneResourceRequested, threeResourcesObtained);
    ProductionPower twoToThree = new ProductionPower(twoResourcesRequested, threeResourcesObtained);

    ProductionPower jollyOneToOne = new ProductionPower(oneJollyResourceRequested, oneJollyResourceObtained);

    @Test
    public void canBeUsedTest() {
        HashMap<ResourceEnum, Integer> trueOneToOneFalseTwoToThree = new HashMap<>() {{
            put(ResourceEnum.COIN, 2);
            put(ResourceEnum.STONE, 2);
        }};

        HashMap<ResourceEnum, Integer> falseOneToOneTrueTwoToThree = new HashMap<>() {{
            put(ResourceEnum.COIN, 4);
            put(ResourceEnum.SERVANT, 1);
        }};

        HashMap<ResourceEnum, Integer> bothFalse = new HashMap<>() {{
        }};

        HashMap<ResourceEnum, Integer> bothTrue = new HashMap<>() {{
            put(ResourceEnum.COIN, 1);
            put(ResourceEnum.SERVANT, 1);
            put(ResourceEnum.STONE, 3);
        }};

        Player playerOnlyOneToOne = new PlayerStub(trueOneToOneFalseTwoToThree);
        Player playerOnlyTwoToThree = new PlayerStub(falseOneToOneTrueTwoToThree);
        Player playerBoth = new PlayerStub(bothTrue);
        Player playerNeither = new PlayerStub(bothFalse);

        assertTrue(oneToOne.canBeUsed(playerOnlyOneToOne));
        assertFalse(oneToOne.canBeUsed(playerOnlyTwoToThree));
        assertTrue(oneToOne.canBeUsed(playerBoth));
        assertFalse(oneToOne.canBeUsed(playerNeither));

        assertFalse(twoToThree.canBeUsed(playerOnlyOneToOne));
        assertTrue(twoToThree.canBeUsed(playerOnlyTwoToThree));
        assertTrue(twoToThree.canBeUsed(playerBoth));
        assertFalse(twoToThree.canBeUsed(playerNeither));

        assertTrue(jollyOneToOne.canBeUsed(playerOnlyOneToOne));
        assertTrue(jollyOneToOne.canBeUsed(playerOnlyTwoToThree));
        assertTrue(jollyOneToOne.canBeUsed(playerBoth));
        assertFalse(jollyOneToOne.canBeUsed(playerNeither));

        boolean illegalArgument = false;

        try {
            oneToOne.canBeUsed(null);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        assertNotEquals(jollyOneToOne, null);
        assertNotEquals(jollyOneToOne, new Object());


    }

    @Test
    public void getResourceRequestedTest() {
        assertArrayEquals(oneToOne.getResourceRequested(), oneResourceRequested);
        assertArrayEquals(twoToThree.getResourceRequested(), twoResourcesRequested);
        assertThat(oneToThree.getResourceRequested(), IsNot.not(IsEqual.equalTo(oneResourceRequested)));
        assertThat(oneToOne.getResourceRequested(), IsNot.not(IsEqual.equalTo(twoResourcesRequested)));
    }

    @Test
    public void getResourceObtainedTest() {
        assertEquals(oneToOne.getResourceObtained(), oneResourceObtained);
        assertEquals(twoToThree.getResourceObtained(), threeResourcesObtained);
        assertNotEquals(oneToOne.getResourceObtained(), threeResourcesObtained);
        assertNotEquals(twoToThree.getResourceObtained(), oneResourceObtained);
    }


}