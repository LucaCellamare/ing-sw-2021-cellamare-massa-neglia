package it.polimi.ingsw.server.Model.PersonalBoard.Warehouse;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Depot;
import org.junit.Test;

import static org.junit.Assert.*;

public class DepotTest {
        Depot depot1 = new Depot(1);
        Depot depot2 = new Depot(2);
        Depot depot3 = new Depot(3);

    @Test
    public void setResource(){
        this.depot1.setResource(ResourceEnum.COIN);
        this.depot2.setResource(ResourceEnum.STONE);

        assertEquals(depot1.getResource(),ResourceEnum.COIN);
        assertEquals(depot2.getResource(),ResourceEnum.STONE);
        assertEquals(depot3.getResource(),ResourceEnum.NONE);

        this.depot3.setResource(ResourceEnum.SHIELD);
        assertEquals(depot3.getResource(),ResourceEnum.SHIELD);
    }

    @Test
    public void put(){
        this.depot1.setResource(ResourceEnum.COIN);
        assertEquals(depot1.getFreeSpace(),1);
        this.depot3.setResource(ResourceEnum.SHIELD);
        assertEquals(depot3.getFreeSpace(),3);

        this.depot3.put(2);
        assertEquals(depot3.getFreeSpace(),1);
        this.depot3.put(1);
        assertEquals(depot3.getFreeSpace(),0);

        this.depot3.setResource(ResourceEnum.COIN);
        assertEquals(depot3.getFreeSpace(),3);
    }

    @Test
    public void remove(){
        this.depot3.setResource(ResourceEnum.COIN);
        assertEquals(depot3.getFreeSpace(),3);
        this.depot3.put(3);
        this.depot3.remove(2);
        assertEquals(depot3.getFreeSpace(),2);
    }
}