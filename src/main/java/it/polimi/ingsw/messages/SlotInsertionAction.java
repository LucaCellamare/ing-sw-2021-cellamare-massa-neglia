package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.Cards.DevelopmentCard;
import it.polimi.ingsw.server.Model.PersonalBoard.DevelopmentSlots;

/**
 * Bi-directional message:
 * - from client to server: the player tells the controller where to insert the bought card
 * - from server to client: if the player cannot insert the card in the selected slot, the controller tells him to retry
 */
public class SlotInsertionAction extends Message {

    private int slotNumber = 0;
    private DevelopmentCard cardToInsert = null;
    private DevelopmentSlots slots;
    private String errorMessage;

    public SlotInsertionAction(String errorMessage, DevelopmentCard cardToInsert, DevelopmentSlots slots) {
        this.errorMessage = errorMessage;
        this.cardToInsert = cardToInsert;
        this.slots = slots;
    }


    public SlotInsertionAction(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public DevelopmentCard getCardToInsert() {
        return cardToInsert;
    }

    public DevelopmentSlots getSlots() {
        return slots;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {
        clientVisitor.elaborate(this);
    }
}
