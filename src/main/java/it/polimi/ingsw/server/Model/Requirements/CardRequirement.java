package it.polimi.ingsw.server.Model.Requirements;

import it.polimi.ingsw.server.Model.Cards.DevelopmentCard;
import it.polimi.ingsw.server.Model.Enums.LevelEnum;
import it.polimi.ingsw.server.Model.Enums.TypeEnum;
import it.polimi.ingsw.server.Model.Player;

import java.util.List;

/**
 * Describes a single Card required by a Leader Card to be activated
 *
 * @author Roberto Neglia
 */
public class CardRequirement implements Requirement {
    /**
     * Type (color) of the card required
     */
    private TypeEnum cardType;
    /**
     * Level of the card required
     */
    private LevelEnum cardLevel;
    /**
     * Quantity of cards with this type and level required
     */
    private int quantity;

    public CardRequirement() {
    }

    public CardRequirement(TypeEnum cardType, LevelEnum cardLevel, int quantity) {
        this.cardType = cardType;
        this.cardLevel = cardLevel;
        this.quantity = quantity;
    }

    /**
     * @return the type of the card required
     */
    public TypeEnum getCardType() {
        return cardType;
    }

    /**
     * @return the level of the card required
     */
    public LevelEnum getLevel() {
        return cardLevel;
    }

    /**
     * @return the quantity of cards of this type and level required
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Checks if the player has the card (or cards) described in this instance
     *
     * @param p the player that we want to check if satisfies the requirement
     * @return true if he owns the card (or cards), false if not
     */
    @Override
    public boolean checkRequirement(Player p) throws IllegalArgumentException {
        if (p == null) throw new IllegalArgumentException();
        List<DevelopmentCard> playerCards = p.getSlots().getCards();
        DevelopmentCard requiredCard = new DevelopmentCard(cardType, cardLevel);

        return quantity <= playerCards.stream().filter(requiredCard::equals).count();
    }

    @Override
    public String toString() {
        if (cardType == TypeEnum.NONE)
            return quantity + " level " + cardLevel + " card(s)";
        else if (cardLevel == LevelEnum.NONE)
            return quantity + " " + cardType + " card(s)";
        return quantity + " " + cardType + " level " + cardLevel + " card(s)";
    }
}