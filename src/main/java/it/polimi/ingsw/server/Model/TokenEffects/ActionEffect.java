package it.polimi.ingsw.server.Model.TokenEffects;


import it.polimi.ingsw.server.Model.SinglePlayerGame;

import java.io.Serializable;

/**
 * Interface to describe a single generic ActionEffect contained in an ActionToken, which can be either:
 * - a DiscardEffect
 * - a FaithAdvance
 */
public interface ActionEffect extends Serializable {

    /**
     * Generic method to be implemented in all specific Action Effect class to use the effect of the Action Token
     *
     * @param p the player to whom the ability is activated
     */
    void useActionEffect(SinglePlayerGame game);
}
