package it.polimi.ingsw.client.handlers;

import it.polimi.ingsw.server.Model.Cards.ProductionPower;

import java.util.List;

/**
 * Utility class which holds information about the activation of production powers by the player in a turn
 */
public class ProductionPowersHandler {
    /**
     * List of all the production powers a player can activate in a turn
     */
    private static List<ProductionPower> productionPowers = null;

    /**
     * The current production power the player is activating
     */
    private static ProductionPower currentProductionPower;

    /**
     * @return all the production powers a player can activate in a turn
     */
    public static List<ProductionPower> getProductionPowers() {
        return productionPowers;
    }

    /**
     * @param productionPowers all the production powers a player can activate in a turn
     */
    public static void setProductionPowers(List<ProductionPower> productionPowers) {
        ProductionPowersHandler.productionPowers = productionPowers;
    }

    /**
     * Removes the production power activated by the player from the list of the ones that can be activated
     */
    public static void removeCurrentProductionPower() {
        productionPowers.remove(currentProductionPower);
        currentProductionPower = null;
    }

    /**
     * @return the current production power the player is activating
     */
    public static ProductionPower getCurrentProductionPower() {
        return currentProductionPower;
    }

    /**
     * @param currentProductionPower the current production power the player is activating
     */
    public static void setCurrentProductionPower(ProductionPower currentProductionPower) {
        ProductionPowersHandler.currentProductionPower = currentProductionPower;
    }
}
