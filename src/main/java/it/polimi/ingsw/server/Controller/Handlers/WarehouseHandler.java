package it.polimi.ingsw.server.Controller.Handlers;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;

import java.util.EnumMap;

/**
 * Used to keep some information about the warehouse of the current player during his turn
 */
public class WarehouseHandler {
    /**
     * The resources obtained by the player that need to be inserted in the warehouse
     */
    private static EnumMap<ResourceEnum, Integer> toInsert;

    /**
     * @return the resources to be inserted in the player warehouse
     */
    public static EnumMap<ResourceEnum, Integer> getToInsert() {
        return toInsert;
    }

    /**
     * @param toInsert the resources to be inserted in the player warehouse
     */
    public static void setToInsert(EnumMap<ResourceEnum, Integer> toInsert) {
        WarehouseHandler.toInsert = toInsert;
    }

    /**
     * Removes the specified quantity of the specified resource from the EnumMap
     * (the player inserted them in the warehouse)
     *
     * @param toRemove resource to be removed from toInsert
     * @param quantity quantity of resource to be removed
     */
    public static void remove(ResourceEnum toRemove, int quantity) {
        int oldQuantity = toInsert.get(toRemove);
        if (oldQuantity - quantity <= 0)
            toInsert.remove(toRemove);
        else toInsert.replace(toRemove, oldQuantity - quantity);
    }

    /**
     * Removes all the specified resource from the EnumMap
     *
     * @param toRemove resource to be removed from toInsert
     */
    public static void completelyRemove(ResourceEnum toRemove) {
        toInsert.remove(toRemove);
    }

    /**
     * @return true if the player inserted all the resources in his warehouse, false if not
     */
    public static boolean isNotEmpty() {
        return !toInsert.isEmpty();
    }
}
