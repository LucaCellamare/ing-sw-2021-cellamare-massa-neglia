package it.polimi.ingsw.exceptions;

import it.polimi.ingsw.server.Model.Enums.LevelEnum;

public class CannotInsertException extends Exception {
    int slotNumber;
    LevelEnum newCardLevel;
    public CannotInsertException(int slotNumber, LevelEnum newCardLevel) {
        super("You tried to insert a card with level " + newCardLevel + " in a incompatible development slot.");
        this.newCardLevel = newCardLevel;
        this.slotNumber = slotNumber;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public LevelEnum getNewCardLevel() {
        return newCardLevel;
    }

}
