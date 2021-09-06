package it.polimi.ingsw.server.Model.TokenEffects;

import it.polimi.ingsw.server.Controller.Handlers.FaithTrackHandler;
import it.polimi.ingsw.server.Model.FaithTrack.FaithTrack;
import it.polimi.ingsw.server.Model.SinglePlayerGame;

/**
 * Describes the specific FaithAdvance Effect contained in some Action Token used in the Single Player mode
 */
public class FaithAdvance implements ActionEffect {

    /**
     * Quantity of steps of Faith Advance of this Action Effect
     */
    private final int quantity;

    /**
     * Describes if the Action Deck needs to be shuffled when this effect is used
     */
    private final boolean shuffle;

    /**
     * Class Constructor
     *
     * @param quantity number of steps of Faith Advance
     * @param shuffle  boolean indicating whether to shuffle the Action Deck when activating this effect
     */
    public FaithAdvance(int quantity, boolean shuffle) {
        this.quantity = quantity;
        this.shuffle = shuffle;
    }


    /**
     * Activates the Faith Advance Effect, advancing the Black Cross by a certain number of steps
     *
     * @param game the game in which is contained the the Black Cross to be advanced
     */
    @Override
    public void useActionEffect(SinglePlayerGame game) {
        FaithTrack lorenzoFaithTrack = game.getLorenzoFaithTrack();
        FaithTrackHandler.updatePlayerPosition(lorenzoFaithTrack, this.quantity);
        if (this.shuffle) {
            game.shuffleTokens();
        }
    }

    @Override
    public String
    toString() {
        if (shuffle) {
            return "moves " + quantity + " cells forward the black cross and shuffles again the action token deck!";
        } else return "moves " + quantity + " cells forward the black cross!";

    }
}
