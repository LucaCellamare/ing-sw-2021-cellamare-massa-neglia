package it.polimi.ingsw.server.Model.PersonalBoard.Warehouse;

import it.polimi.ingsw.exceptions.ConfirmDiscardResourceException;
import it.polimi.ingsw.exceptions.DepotNotFoundException;
import it.polimi.ingsw.exceptions.IllegalDepotResourceTypeException;
import it.polimi.ingsw.exceptions.IllegalDepotSwapException;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.LeaderAbilities.StorageAbility;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Depot;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Warehouse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class WarehouseTest {

    Warehouse warehouse = new Warehouse();

    public void setDepotWarehouse() {
        warehouse.getDepotFromCapacity(1).setResource(ResourceEnum.SHIELD);
        warehouse.getDepotFromCapacity(1).put(1);
        warehouse.getDepotFromCapacity(2).setResource(ResourceEnum.STONE);
        warehouse.getDepotFromCapacity(2).put(2);
    }

    @Test
    public void getDepotsTest() {
        setDepotWarehouse();
        ArrayList<Depot> listDepots;
        listDepots = warehouse.getDepots();
        assertEquals(listDepots.size(), 3);
    }

    @Test
    public void removeTest() {
        setDepotWarehouse();
        HashMap<ResourceEnum, Integer> resources = new HashMap<>();
        resources.put(ResourceEnum.SHIELD, 1);
        resources.put(ResourceEnum.STONE, 1);

        warehouse.remove(resources);

        assertEquals(warehouse.getDepotFromCapacity(1).getAmountStored(), 0);
        assertEquals(warehouse.getDepotFromCapacity(2).getAmountStored(), 1);
        assertEquals(warehouse.getDepotFromCapacity(1).getResource(), ResourceEnum.NONE);
        assertEquals(warehouse.getDepotFromCapacity(2).getResource(), ResourceEnum.STONE);

        assertFalse(warehouse.isEmpty());

        warehouse.remove(ResourceEnum.STONE, 1);

        assertEquals(warehouse.getDepotFromCapacity(2).getAmountStored(), 0);
        assertEquals(warehouse.getDepotFromCapacity(2).getResource(), ResourceEnum.NONE);

        assertTrue(warehouse.isEmpty());

    }

    @Test
    public void insertTest() {
        setDepotWarehouse();
        warehouse.getDepotFromCapacity(3).setResource(ResourceEnum.SERVANT);

        boolean illegalArgument = false;

        try {
            warehouse.getDepotFromCapacity(0);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        illegalArgument = false;

        try {
            warehouse.getDepotFromCapacity(9);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        HashMap<ResourceEnum, Integer> resources = new HashMap<>();

        warehouse.insert(ResourceEnum.SERVANT, 2, 2);

        assertEquals(warehouse.getDepotFromCapacity(1).getAmountStored(), 1);
        assertEquals(warehouse.getDepotFromCapacity(2).getAmountStored(), 2);
        assertEquals(warehouse.getDepotFromCapacity(3).getAmountStored(), 2);
        assertEquals(warehouse.getDepotFromCapacity(1).getResource(), ResourceEnum.SHIELD);
        assertEquals(warehouse.getDepotFromCapacity(2).getResource(), ResourceEnum.STONE);
        assertEquals(warehouse.getDepotFromCapacity(3).getResource(), ResourceEnum.SERVANT);

        illegalArgument = false;

        try{
            warehouse.insert(ResourceEnum.STONE, -1,1);
        }catch (IllegalArgumentException e){
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        illegalArgument = false;

        try {
            warehouse.insert(ResourceEnum.COIN, 1,-1);
        }catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);
    }

    @Test
    public void getResourceAmountTest() {
        setDepotWarehouse();
        int qty = warehouse.getResourceAmount(ResourceEnum.SHIELD);
        assertEquals(qty, 1);
    }

    @Test
    public void moveTest() {
        setDepotWarehouse();
        warehouse.getDepotFromCapacity(3).setResource(ResourceEnum.SERVANT);
        warehouse.getDepotFromCapacity(3).put(2);

        boolean illegalArgument = false;

        Depot dpt1 = new Depot(1);
        Depot dpt2 = new Depot(2);
        Depot cantSwapDpt = new Depot(1);
        try {
            cantSwapDpt = warehouse.getDepotFromResourceType(ResourceEnum.SHIELD);
            dpt1 = warehouse.getDepotFromResourceType(ResourceEnum.SERVANT);
            dpt2 = warehouse.getDepotFromResourceType(ResourceEnum.STONE);
            Depot fail = warehouse.getDepotFromResourceType(ResourceEnum.NONE);
            fail("expected exception not thrown");
        } catch (DepotNotFoundException e) {
            fail();
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        boolean depotNotFound = false;

        try {
            Depot fail = warehouse.getDepotFromResourceType(ResourceEnum.COIN);
            fail("expected exception not thrown");
        } catch (DepotNotFoundException e) {
            depotNotFound = true;
        }

        assertTrue(depotNotFound);

        try {
            warehouse.move(dpt1, dpt2);
        } catch (IllegalDepotSwapException e) {
            fail();
        }
        assertEquals(dpt1.getResource(), ResourceEnum.STONE);
        assertEquals(dpt2.getResource(), ResourceEnum.SERVANT);
        assertEquals(dpt1.getAmountStored(), 2);
        assertEquals(dpt2.getAmountStored(), 2);

        boolean illegalSwap = false;

        try{
            warehouse.move(cantSwapDpt, dpt2);
        } catch (IllegalDepotSwapException e) {
            illegalSwap = true;
        }
        assertTrue(illegalSwap);

    }

    @Test
    public void addDepotFromAbilityTest() throws DepotNotFoundException {
        ResourceEnum resourceStorageAbility = ResourceEnum.COIN;
        int sizeStorageAbility = 2;
        StorageAbility storageAbility = new StorageAbility(resourceStorageAbility, sizeStorageAbility);

        warehouse.addDepotFromAbility(storageAbility.getResource(), storageAbility.getSize());

        assertEquals(warehouse.getDepots().size(), 4);
        try {
            assertEquals(warehouse.getLeaderAbilityDepots().get(0).getSize(), sizeStorageAbility);
            assertEquals(warehouse.getLeaderAbilityDepots().get(0).getResource(), resourceStorageAbility);
        } catch (DepotNotFoundException e) {
            assertNull(warehouse.getLeaderAbilityDepots());
        }
    }

    @Test
    public void tryInsertTest() {
        setDepotWarehouse();
        int result;
        try {
            warehouse.tryInsert(ResourceEnum.SHIELD, 1, 0);
            result = 0;
        } catch (IllegalDepotResourceTypeException e) {
            result = 1;
        } catch (ConfirmDiscardResourceException e) {
            result = 2;
        }
        assertEquals(result, 2);
        try {
            warehouse.tryInsert(ResourceEnum.SERVANT, 1, 1);
            result = 0;
        } catch (IllegalDepotResourceTypeException e) {
            result = 1;
        } catch (ConfirmDiscardResourceException e) {
            result = 2;
        }
        assertEquals(result, 1);
        warehouse.getDepotFromCapacity(3).setResource(ResourceEnum.SERVANT);
        try {
            warehouse.tryInsert(ResourceEnum.SERVANT, 1, 2);
            result = 0;
        } catch (IllegalDepotResourceTypeException e) {
            result = 1;
        } catch (ConfirmDiscardResourceException e) {
            result = 2;
        }
        assertEquals(result, 0);

        warehouse.getDepotFromCapacity(3).setResource(ResourceEnum.NONE);
        try {
            warehouse.tryInsert(ResourceEnum.SERVANT, 1, 2);
            result = 0;
        } catch (IllegalDepotResourceTypeException e) {
            result = 1;
        } catch (ConfirmDiscardResourceException e) {
            result = 2;
        }
        assertEquals(result, 0);
    }
}