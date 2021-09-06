package it.polimi.ingsw.server.Model.LeaderAbilities;

import it.polimi.ingsw.server.Model.Player;

import java.io.Serializable;

/**
 * Interface to describe a generic ability that a leader may have:
 * - the additional storage ability
 * - the production power
 * - the discount at the card market
 * - the white marble modifier
 */
public interface SpecialAbility extends Serializable {
    /**
     * Generic method to be implemented in all specific ability classes to perform different actions on the player
     *
     * @param p the player to whom the ability is activated
     */
    void activateAbility(Player p);
}
