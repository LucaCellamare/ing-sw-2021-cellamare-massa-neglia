package it.polimi.ingsw.server.Model.LeaderAbilities;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.LeaderAbilities.StorageAbility;
import it.polimi.ingsw.server.Model.LeaderCardDeck;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Depot;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.PlayerStub;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StorageAbilityTest {
    LeaderCardDeck leaderCardDeck = new LeaderCardDeck();
    ResourceEnum shieldResource = ResourceEnum.SHIELD;
    int shieldStorageSize = 2;
    ResourceEnum stoneResource = ResourceEnum.STONE;
    int stoneStorageSize = 4;

    StorageAbility shieldStorageAbility = new StorageAbility(shieldResource, shieldStorageSize);
    StorageAbility stoneStorageAbility = new StorageAbility(stoneResource, stoneStorageSize);

    @Test
    public void activateAbilityTest(){
        int firstLeaderAbilityDepotPosition = 3;
        int secondLeaderAbilityDepotPosition = 4;
        Player p = new PlayerStub();
        Depot additionalDepot = new Depot(stoneStorageAbility.getSize());
        additionalDepot.setResource(stoneStorageAbility.getResource());

        boolean illegalArgument = false;

        try{
            stoneStorageAbility.activateAbility(null);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        stoneStorageAbility.activateAbility(p);
        Depot test = p.getWarehouse().getDepots().get(firstLeaderAbilityDepotPosition);
        assertEquals(additionalDepot.getSize(), test.getSize());
        assertEquals(additionalDepot.getResource(), test.getResource());

        additionalDepot = new Depot(shieldStorageAbility.getSize());
        additionalDepot.setResource(shieldStorageAbility.getResource());
        shieldStorageAbility.activateAbility(p);

        test = p.getWarehouse().getDepots().get(secondLeaderAbilityDepotPosition);
        assertEquals(additionalDepot.getSize(), test.getSize());
        assertEquals(additionalDepot.getResource(), test.getResource());
    }

}