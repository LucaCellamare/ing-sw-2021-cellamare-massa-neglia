package it.polimi.ingsw.server.Controller.Handlers;

import it.polimi.ingsw.server.Model.Cards.DevelopmentCard;

/**
 * Used to keep some information about the development slots of the current player during his turn
 */
public class DevelopmentSlotsHandler {
    /**
     * Bought card that has to be inserted
     */
    private static DevelopmentCard cardToInsert;
    /**
     * Level of the card chosen by the player from the card market
     */
    private static int chosenCardLevel = 0;
    /**
     * Color of the card chosen by the player from the card market
     */
    private static int chosenCardColor = 0;

    /**
     * @return the card to be inserted in the slots
     */
    public static DevelopmentCard getCardToInsert() {
        return cardToInsert;
    }

    /**
     * @param cardToInsert the card to be inserted in the slots
     */
    public static void setCardToInsert(DevelopmentCard cardToInsert) {
        DevelopmentSlotsHandler.cardToInsert = cardToInsert;
    }

    /**
     * @return the Level of the card chosen by the player from the card market
     */
    public static int getChosenCardLevel() {
        return chosenCardLevel;
    }

    /**
     * @param chosenCardLevel Level of the card chosen by the player from the card market
     */
    public static void setChosenCardLevel(int chosenCardLevel) {
        DevelopmentSlotsHandler.chosenCardLevel = chosenCardLevel;
    }

    /**
     * @return Color of the card chosen by the player from the card market
     */
    public static int getChosenCardColor() {
        return chosenCardColor;
    }

    /**
     * @param chosenCardColor Color of the card chosen by the player from the card market
     */
    public static void setChosenCardColor(int chosenCardColor) {
        DevelopmentSlotsHandler.chosenCardColor = chosenCardColor;
    }

}
