package it.polimi.ingsw.server.Model.PersonalBoard;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.PersonalBoard.Strongbox;
import org.junit.Test;

import static org.junit.Assert.*;

public class StrongboxTest {

    Strongbox strongbox = new Strongbox();

    @Test
    public void resourceInsertTest() {
        ResourceEnum firstResource = ResourceEnum.COIN;
        int firstQuantity = 3;
        ResourceEnum secondResource = ResourceEnum.STONE;
        int secondQuantity = 2;
        try {
            strongbox.insertResource(firstResource, firstQuantity);
            strongbox.insertResource(secondResource, secondQuantity);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(firstQuantity, strongbox.getResourceCount(firstResource));
        assertEquals(secondQuantity, strongbox.getResourceCount(secondResource));
    }

    @Test
    public void testRemove() {


        ResourceEnum firstResource = ResourceEnum.COIN;
        int firstQuantity = 3;
        ResourceEnum secondResource = ResourceEnum.STONE;
        int secondQuantity = 2;

        boolean illegalArgument = false;

        try {
            strongbox.insertResource(firstResource, firstQuantity);
            strongbox.insertResource(secondResource, secondQuantity);
            strongbox.insertResource(ResourceEnum.NONE, 5);
            fail("expected exception not thrown");
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        illegalArgument = false;

        try {
            strongbox.insertResource(ResourceEnum.COIN, -1);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        strongbox.remove(ResourceEnum.COIN, 3);
        strongbox.remove(ResourceEnum.STONE, 1);

        assertEquals(0, strongbox.getResourceCount(ResourceEnum.COIN));
        assertEquals(1, strongbox.getResourceCount(ResourceEnum.STONE));

    }
}