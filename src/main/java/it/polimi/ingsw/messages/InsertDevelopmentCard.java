package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.Cards.DevelopmentCard;
import it.polimi.ingsw.server.Model.PersonalBoard.DevelopmentSlots;

/**
 * Message used that asks the Client to insert a new Development Card he/she has just bought.
 * It's also used as a response to be sent to the server
 */
public class InsertDevelopmentCard extends Message {

    private DevelopmentCard cardToInsert = null;
    private DevelopmentSlots slots = null;

    public InsertDevelopmentCard(DevelopmentSlots slots, DevelopmentCard cardToInsert) {
        this.cardToInsert = cardToInsert;
        this.slots = slots;
    }

    public DevelopmentCard getBoughtCard() {
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
