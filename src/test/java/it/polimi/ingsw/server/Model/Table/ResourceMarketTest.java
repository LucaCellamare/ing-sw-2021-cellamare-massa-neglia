package it.polimi.ingsw.server.Model.Table;

import it.polimi.ingsw.server.Model.Enums.MarbleEnum;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Enums.TypeLineEnum;
import it.polimi.ingsw.server.Model.Table.ResourceMarket;
import org.junit.Assert;
import org.junit.Test;

import java.util.EnumMap;

import static org.junit.Assert.assertEquals;


public class ResourceMarketTest {

    @Test
    public void testResourcesObtainedFromRow() {

        ResourceMarket resourceMarket = new ResourceMarket();
        MarbleEnum[] expected = (resourceMarket.getMarketStructure()[0]);
        MarbleEnum[] obtained = resourceMarket.obtainResources(TypeLineEnum.ROW, 0);

        Assert.assertArrayEquals( expected, obtained );
    }

    @Test
    public void testResourcesObtainedFromCol() {

        ResourceMarket resourceMarket = new ResourceMarket();

        MarbleEnum[] expected = new MarbleEnum[resourceMarket.getRowSize()];
        for(int row = 0; row < expected.length; row++) {
            expected[row] = resourceMarket.getMarketStructure()[row][1];
        }

        MarbleEnum[] obtained = resourceMarket.obtainResources(TypeLineEnum.COL, 1);
        Assert.assertArrayEquals( expected, obtained );
    }


    @Test
    public void testUpdatedArrangementAfterRowInsertion() {

        ResourceMarket resourceMarket = new ResourceMarket();
        MarbleEnum spare = resourceMarket.getSpareMarble();
        MarbleEnum futureSpare = resourceMarket.getMarbleFromCoordinates(0,0);
        MarbleEnum[] obtained = resourceMarket.obtainResources(TypeLineEnum.ROW, 0);

        assertEquals(futureSpare, resourceMarket.getSpareMarble());
        assertEquals(spare, resourceMarket.getMarbleFromCoordinates(0, 3));

    }

    @Test
    public void testUpdatedArrangementAfterColInsertion() {

        ResourceMarket resourceMarket = new ResourceMarket();
        MarbleEnum spare = resourceMarket.getSpareMarble();
        MarbleEnum futureSpare = resourceMarket.getMarbleFromCoordinates(0,1);
        MarbleEnum[] obtained = resourceMarket.obtainResources(TypeLineEnum.COL, 1);

        assertEquals(futureSpare, resourceMarket.getSpareMarble());
        assertEquals(spare, resourceMarket.getMarbleFromCoordinates(2, 1));

    }

    @Test
    public void obtainResourcesFromMarbleListTest() {
        ResourceMarket resourceMarket = new ResourceMarket();
        MarbleEnum[] obtainedResources = new MarbleEnum[]{MarbleEnum.WHITE, MarbleEnum.PURPLE,MarbleEnum.BLUE, MarbleEnum.PURPLE};
        EnumMap<ResourceEnum, Integer> resources = new EnumMap<>(ResourceEnum.class);

        resources.put(ResourceEnum.SERVANT, 2);
        resources.put(ResourceEnum.SHIELD, 1);
        resources.put(ResourceEnum.NONE,1);

        assertEquals(resources, resourceMarket.obtainResourceFromMarbleList(obtainedResources));

    }


}