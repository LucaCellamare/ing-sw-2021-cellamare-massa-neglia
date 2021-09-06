package it.polimi.ingsw.server.Model.Requirements;

import it.polimi.ingsw.server.Model.Player;

import java.io.Serializable;

/**
 * Interface to describe a single generic requirement, which when put together form:
 * - a DevelopmentCard resource price from the market
 * - a LeaderCard requirement
 * - a ProductionPower resource requirement
 */

public interface Requirement extends Serializable {
    /**
     * Generic method to be implemented in all the specific requirement classes to check the specific requirement
     *
     * @param p the player that we want to check the requirement
     * @return true if the requirement is fulfilled, false if not
     */
    boolean checkRequirement(Player p);
}
