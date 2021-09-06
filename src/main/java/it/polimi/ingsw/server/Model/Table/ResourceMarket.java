package it.polimi.ingsw.server.Model.Table;

import it.polimi.ingsw.server.Model.Enums.MarbleEnum;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Enums.TypeLineEnum;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Random;

/**
 * The Resource Market where the Players can get Resources
 */

public class ResourceMarket {

    /**
     * The constant ROW_SIZE.
     */
    private static final int ROW_SIZE = 3;

    /**
     * The constant COL_SIZE.
     */
    private static final int COL_SIZE = 4;

    /**
     * The Market Structure.
     */
    private final MarbleEnum[][] marketStructure = new MarbleEnum[ROW_SIZE][COL_SIZE];

    /**
     * Map that matches a resource to every marble color
     */
    private final EnumMap<MarbleEnum, ResourceEnum> marbleToResource = new EnumMap<>(MarbleEnum.class);

    /**
     * The Spare Marble that is present on the side slide.
     */
    private MarbleEnum spareMarble;

    /**
     * Class constructor
     * Note that the arrangement of the marbles is random.
     */
    public ResourceMarket() {
        MarbleEnum[] marblesToInsert = new MarbleEnum[]{MarbleEnum.WHITE, MarbleEnum.WHITE, MarbleEnum.WHITE, MarbleEnum.WHITE, MarbleEnum.BLUE, MarbleEnum.BLUE, MarbleEnum.GRAY, MarbleEnum.GRAY, MarbleEnum.YELLOW, MarbleEnum.YELLOW, MarbleEnum.PURPLE, MarbleEnum.PURPLE, MarbleEnum.RED};
        shuffleArray(marblesToInsert);
        int k = 0;
        for (int i = 0; i < ROW_SIZE; i++) {
            for (int j = 0; j < COL_SIZE; j++) {
                this.marketStructure[i][j] = marblesToInsert[k++];

            }

        }
        this.spareMarble = marblesToInsert[k];

        marbleToResource.put(MarbleEnum.BLUE, ResourceEnum.SHIELD);
        marbleToResource.put(MarbleEnum.YELLOW, ResourceEnum.COIN);
        marbleToResource.put(MarbleEnum.GRAY, ResourceEnum.STONE);
        marbleToResource.put(MarbleEnum.PURPLE, ResourceEnum.SERVANT);
        marbleToResource.put(MarbleEnum.RED, ResourceEnum.FAITH);
        marbleToResource.put(MarbleEnum.WHITE, ResourceEnum.NONE);
    }


    /**
     * Helper method to shuffle the marbles when constructing the ResourceMarket
     *
     * @param array marbles that needs to be shuffled
     */
    private static <V> void shuffleArray(V[] array) {
        int index;
        V temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    /**
     * Retrieves the marble at the ROW and COLUMN specified
     *
     * @param i selected ROW
     * @param j selected COLUMN
     * @return the marble at the coordinates given in input
     */
    public MarbleEnum getMarbleFromCoordinates(int i, int j) {
        return this.marketStructure[i][j];
    }

    /**
     * Retrieves the number of ROWS
     *
     * @return the number of ROWS
     */
    public int getRowSize() {
        return ROW_SIZE;
    }

    /**
     * Retrieves the number of COLUMNS
     *
     * @return the number of COLUMNS
     */
    public int getColSize() {
        return COL_SIZE;

    }

    /**
     * Retrieves the Market Structure in which the marbles are contained
     *
     * @return the Market Structure
     */
    public MarbleEnum[][] getMarketStructure() {
        MarbleEnum[][] deepMarbleStructureCopy = new MarbleEnum[ROW_SIZE][COL_SIZE];
        for (int i = 0; i < ROW_SIZE; i++)
            deepMarbleStructureCopy[i] = Arrays.copyOf(this.marketStructure[i], COL_SIZE);
        return deepMarbleStructureCopy;
    }

    /**
     * Retrieves the Spare Marble that needs to be inserted in the MarketStructure from the side slide
     *
     * @return the Spare Marble
     */
    public MarbleEnum getSpareMarble() {
        return spareMarble;
    }


    /**
     * Helper method to update the marble arrangement after a player has inserted the spare marble
     *
     * @param chosenLine indicates whether the spare marble has been inserted in a ROW or in a COLUMN
     * @param number     number of ROW or COLUMN of the insertion
     */
    private void updateMarbleArrangement(TypeLineEnum chosenLine, int number) {

        //ROW_INSERTION
        if (chosenLine == TypeLineEnum.ROW) {
            MarbleEnum futureSpareMarble = this.marketStructure[number][0];
            for (int i = 0; i < COL_SIZE - 1; i++) {
                this.marketStructure[number][i] = this.marketStructure[number][i + 1];
            }
            this.marketStructure[number][COL_SIZE - 1] = this.spareMarble;
            this.spareMarble = futureSpareMarble;
        }
        //COLUMN INSERTION
        else {
            MarbleEnum futureSpareMarble = this.marketStructure[0][number];
            for (int i = 0; i < ROW_SIZE - 1; i++) {
                this.marketStructure[i][number] = this.marketStructure[i + 1][number];
            }
            this.marketStructure[ROW_SIZE - 1][number] = this.spareMarble;
            this.spareMarble = futureSpareMarble;

        }
    }

    /**
     * Retrieves the Resources obtained from the Market Structure
     *
     * @param chosenLine indicates whether the spare marble has been inserted in a ROW or in a COLUMN
     * @param number     number of ROW or COLUMN of the insertion
     * @return resources obtained from the ResourceMarket
     */
    public MarbleEnum[] obtainResources(TypeLineEnum chosenLine, int number) {
        MarbleEnum[] line = (chosenLine == TypeLineEnum.ROW) ? this.marketStructure[number] : Arrays.stream(this.marketStructure).map(i -> i[number]).toArray(MarbleEnum[]::new);
        MarbleEnum[] deepLineCopy = Arrays.copyOf(line, line.length);
        updateMarbleArrangement(chosenLine, number);
        return deepLineCopy;
    }

    /**
     * DA COMPLETARE
     *
     * @param marbleList DA COMPLETARE
     * @return DA COMPLETARE
     */
    public EnumMap<ResourceEnum, Integer> obtainResourceFromMarbleList(MarbleEnum[] marbleList) {
        EnumMap<ResourceEnum, Integer> resources = new EnumMap<>(ResourceEnum.class);
        if (marbleList.length != 0) {
            for (MarbleEnum marble : marbleList) {
                ResourceEnum givenResource = marbleToResource.get(marble);
                if (resources.containsKey(givenResource))
                    resources.replace(givenResource, resources.get(givenResource) + 1);
                else
                    resources.put(givenResource, 1);
            }
            //resources.remove(ResourceEnum.NONE);
        }
        return resources;
    }

}
