package it.polimi.ingsw.server.Model.LeaderAbilities;

import it.polimi.ingsw.server.Model.Cards.ProductionPower;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.LeaderAbilities.ProductionAbility;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.PlayerStub;
import it.polimi.ingsw.server.Model.Requirements.ResourceRequirement;
import org.junit.Test;

import java.util.ArrayList;
import java.util.EnumMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ProductionAbilityTest {
    ResourceRequirement[] oneResourceRequested = { new ResourceRequirement(ResourceEnum.STONE, 1)};

    EnumMap<ResourceEnum, Integer> oneResourceObtained = new EnumMap<>(ResourceEnum.class){{
        put(ResourceEnum.FAITH, 1);
    }};

    ResourceRequirement[] twoResourcesRequested = {
            new ResourceRequirement(ResourceEnum.COIN, 1),
            new ResourceRequirement(ResourceEnum.SERVANT, 1)
    };

    EnumMap<ResourceEnum, Integer> threeResourcesObtained = new EnumMap<>(ResourceEnum.class ) {{
        put(ResourceEnum.SHIELD, 2);
        put(ResourceEnum.STONE, 2);
        put(ResourceEnum.FAITH, 1);
    }};

    ProductionPower oneToOne = new ProductionPower(oneResourceRequested, oneResourceObtained);
    ProductionPower twoToThree = new ProductionPower(twoResourcesRequested, threeResourcesObtained);

    ProductionAbility abilityOneToOne = new ProductionAbility(oneToOne);
    ProductionAbility abilityTwoToThree = new ProductionAbility(twoToThree);

    @Test
    public void getProductionPowerTest(){
        assertEquals(abilityOneToOne.getProductionPower(), oneToOne);
        assertNotEquals(abilityTwoToThree.getProductionPower(), oneToOne);
    }

    @Test
    public void activateAbilityTest(){
        Player playerActivateOneToOne = new PlayerStub();
        Player playerActivateBoth = new PlayerStub();

        abilityOneToOne.activateAbility(playerActivateOneToOne);
        abilityOneToOne.activateAbility(playerActivateBoth);
        abilityTwoToThree.activateAbility(playerActivateBoth);

        ArrayList<ProductionPower> oneToOneArrayList = new ArrayList<>();
        oneToOneArrayList.add(oneToOne);

        ArrayList<ProductionPower> both = new ArrayList<>();
        both.add(oneToOne);
        both.add(twoToThree);

        assertEquals(playerActivateOneToOne.getLeaderProductionPowers(), oneToOneArrayList);
        assertEquals(playerActivateBoth.getLeaderProductionPowers(), both);
    }
}