package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.PersonalBoard.Strongbox;

/**
 * Message only sent from the server to the client to update the player about his strongbox
 */
public class StrongboxUpdate extends Message {
    private Strongbox updatedStrongbox;

    public StrongboxUpdate(Strongbox updatedStrongbox) {
        this.updatedStrongbox = updatedStrongbox;
    }

    public Strongbox getUpdatedStrongbox() {
        return updatedStrongbox;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {

    }
}
