package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.PersonalBoard.DevelopmentSlots;

/**
 * Message used to tell the Client that the Development Slots have been updated
 * and to show him/her the new updated arrangement.
 */
public class DevelopmentSlotUpdate extends Message{
    private DevelopmentSlots slots;
    public DevelopmentSlotUpdate(DevelopmentSlots slots) {
        this.slots = slots;
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
    }
}
