package it.polimi.ingsw.client.handlers;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class which holds some information about the turn of the player that may be useful for the view
 */
public class PlayerHandler {
    /**
     * Flag which indicates if the player has already activated at least one production power:
     * in that case, he can't go back to the main menu, but he can only end his turn
     */
    private static boolean alreadyProductionActivated;

    /**
     * If the player activated a leader ability that switches the white marble with a specific resource,
     * that resource is inserted in here
     */
    private List<ResourceEnum> whiteMarbleLeaderAbility;

    /**
     * If the player activated a leader ability that grants him a discount of a specific resource
     * at the card market, that resource is inserted in here
     */
    private List<ResourceEnum> discountLeaderAbility;

    public PlayerHandler() {
        whiteMarbleLeaderAbility = new ArrayList<>();
        discountLeaderAbility = new ArrayList<>();
        alreadyProductionActivated = false;
    }

    /**
     * @return true if the player already activated a production power in it's turn, false if not
     */
    public static boolean isAlreadyProductionActivated() {
        return alreadyProductionActivated;
    }

    /**
     * @param alreadyProductionActivated the value to be set to the flag alreadyProductionActivated
     */
    public static void setAlreadyProductionActivated(boolean alreadyProductionActivated) {
        PlayerHandler.alreadyProductionActivated = alreadyProductionActivated;
    }

    /**
     * @return the resources a player can choose instead of the white marble in case of a leader ability activated
     */
    public List<ResourceEnum> getWhiteMarbleLeaderAbility() {
        return whiteMarbleLeaderAbility;
    }

    /**
     * @param whiteMarbleLeaderAbility the list of resources the player can choose instead of the white marble if he activated a leader ability
     */
    public void setWhiteMarbleLeaderAbility(List<ResourceEnum> whiteMarbleLeaderAbility) {
        this.whiteMarbleLeaderAbility = whiteMarbleLeaderAbility;
    }

    /**
     * @return the list of resources discounted to the player at the card market if he activated the leader ability
     */
    public List<ResourceEnum> getDiscountLeaderAbility() {
        return discountLeaderAbility;
    }

    /**
     * @param discountLeaderAbility the list of resources discounted to the player at the card market if he activated the leader ability
     */
    public void setDiscountLeaderAbility(List<ResourceEnum> discountLeaderAbility) {
        this.discountLeaderAbility = discountLeaderAbility;
    }
}
