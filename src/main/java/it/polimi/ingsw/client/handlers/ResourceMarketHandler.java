package it.polimi.ingsw.client.handlers;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;

import java.util.EnumMap;

/**
 * Utility class used to hold information about the remaining resources to be inserted in the player warehouse after he went to the market
 */
public class ResourceMarketHandler {

    /**
     * The remaining resources that have to be inserted in the warehouse
     */
    private static EnumMap<ResourceEnum, Integer> toInsert = null;

    /**
     * @return the remaining resources that have to be inserted in the warehouse
     */
    public static EnumMap<ResourceEnum, Integer> getToInsert() {
        return toInsert;
    }

    /**
     * @param toInsert The remaining resources that have to be inserted in the warehouse
     */
    public static void setToInsert(EnumMap<ResourceEnum, Integer> toInsert) {
        cleanUpList();
        ResourceMarketHandler.toInsert = toInsert;
    }

    /**
     * Empties the remaining resources to insert
     */
    private static void cleanUpList() {
        toInsert = null;
    }

}
