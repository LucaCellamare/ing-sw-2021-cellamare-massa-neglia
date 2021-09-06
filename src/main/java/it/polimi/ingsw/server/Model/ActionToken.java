package it.polimi.ingsw.server.Model;

import it.polimi.ingsw.server.Model.TokenEffects.ActionEffect;

import java.io.Serializable;

/**
 * Describes an Action Token from the CardDeck used in the Single Player mode
 */
public class ActionToken implements Serializable {

    /**
     * The action effect of a token
     */
    private final ActionEffect actionEffect;

    /**
     * Class Constructor
     *
     * @param actionEffect the action effect
     */
    public ActionToken(ActionEffect actionEffect) {
        this.actionEffect = actionEffect;
    }

    /**
     * Activates the action effect of this action Token
     *
     * @param game the game in which the effect will be activated
     */
    public void playActionEffect(SinglePlayerGame game) {
        actionEffect.useActionEffect(game);
    }

    @Override
    public String toString() {
        return actionEffect.toString();
    }
}
