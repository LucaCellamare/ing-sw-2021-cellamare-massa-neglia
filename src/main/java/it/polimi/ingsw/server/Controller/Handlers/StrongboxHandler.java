package it.polimi.ingsw.server.Controller.Handlers;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.PersonalBoard.Strongbox;

import java.util.EnumMap;

/**
 * Used to keep some information about the strongbox of the current player during his turn
 */
public class StrongboxHandler {
    /**
     * Resources that the player obtained but that are not instantly inserted in his strongbox
     */
    private static EnumMap<ResourceEnum, Integer> toInsert = null;

    /**
     * Inserts the resource chosen by the players in place of the jolly
     *
     * @param resource resource chosen by the player
     * @param quantity quantity of resource to be inserted
     */
    public static void insert(ResourceEnum resource, int quantity) {
        toInsert.remove(ResourceEnum.JOLLY);
        toInsert.put(resource, toInsert.get(resource) + quantity);
    }

    /**
     * Merge the given resourceObtained EnumMap with the standard toInsert EnumMap
     *
     * @param resourceObtained EnumMap to be merged with toInsert
     */
    public static void insert(EnumMap<ResourceEnum, Integer> resourceObtained) {
        if (toInsert == null) {
            resetWaitingResources();
        }
        resourceObtained.forEach((resource, quantity) -> {
            int oldQuantity = toInsert.get(resource);
            toInsert.put(resource, oldQuantity + quantity);
        });
    }

    /**
     * Resets the resources inside of toInsert by setting them all back to zero
     */
    private static void resetWaitingResources() {
        toInsert = new EnumMap<>(ResourceEnum.class);
        toInsert.put(ResourceEnum.STONE, 0);
        toInsert.put(ResourceEnum.SHIELD, 0);
        toInsert.put(ResourceEnum.SERVANT, 0);
        toInsert.put(ResourceEnum.COIN, 0);
        toInsert.put(ResourceEnum.JOLLY, 0);
    }


    /**
     * Finally insert the resources inside of toInsert inside of the given strongbox
     *
     * @param playerStrongbox strongbox of the player which obtained the resources
     */
    public static void finallyInsert(Strongbox playerStrongbox) {
        if (toInsert != null) {
            toInsert.remove(ResourceEnum.JOLLY);
            toInsert.forEach(playerStrongbox::insertResource);
            toInsert = null;
        }
    }

}
