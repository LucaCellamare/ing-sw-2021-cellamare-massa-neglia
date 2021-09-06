package it.polimi.ingsw.server.Model;

import it.polimi.ingsw.server.Model.Enums.TypeEnum;
import it.polimi.ingsw.server.Model.FaithTrack.FaithTrack;
import it.polimi.ingsw.server.Model.TokenEffects.DiscardEffect;
import it.polimi.ingsw.server.Model.TokenEffects.FaithAdvance;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Random;


/**
 * Describes a Game in mode Single Player
 */
public class SinglePlayerGame extends Game {
    /**
     * Deck containing the ActionTokens
     */
    private final ArrayDeque<ActionToken> actionDeck;

    /**
     * Faith track of Lorenzo's the Great
     */
    private final FaithTrack LorenzoFaithTrack;

    /**
     * Class Constructor
     */
    public SinglePlayerGame() {
        super(1);
        this.actionDeck = new ArrayDeque<>();
        LorenzoFaithTrack = new FaithTrack(true);
        initTokens();
    }

    /**
     * Helper method to shuffle the ActionTokens
     *
     * @param array tokens that needs to be shuffled
     */
    private static <V> void shuffleArray(V[] array) {
        int index;
        V temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    /**
     * Gets Lorenzo's FaithTrack
     *
     * @return Lorenzo's FaithTrack
     */
    public FaithTrack getLorenzoFaithTrack() {
        return LorenzoFaithTrack;
    }

    /**
     * Method for the Init with shuffling of the ActionToken Deck
     */
    private void initTokens() {
        ActionToken[] initialDeck = {
                new ActionToken(new DiscardEffect(TypeEnum.GREEN, 2)),
                new ActionToken(new DiscardEffect(TypeEnum.BLUE, 2)),
                new ActionToken(new DiscardEffect(TypeEnum.PURPLE, 2)),
                new ActionToken(new DiscardEffect(TypeEnum.YELLOW, 2)),
                new ActionToken(new FaithAdvance(2, false)),
                new ActionToken(new FaithAdvance(1, true))
        };
        shuffleArray(initialDeck);
        this.actionDeck.addAll(Arrays.asList(initialDeck));
    }

    /**
     * Method to shuffle the ActionToken Deck
     */
    public void shuffleTokens() {
        this.actionDeck.clear();
        initTokens();

    }

    /**
     * Gets the Action Token at the top of the ActionToken Deck
     *
     * @return picked Action Token
     */
    public ActionToken pickToken() {
        ActionToken toReturn = actionDeck.removeFirst();
        actionDeck.addLast(toReturn);
        return toReturn;
    }
}
