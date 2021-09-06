package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;

/**
 * Message used to handle the insertion of a "Jolly resource" which is a resource obtained from a production power.
 */
public class InsertJollyResource extends Message {
    private final ResourceEnum chosenResource;
    private final int quantity;

    public InsertJollyResource(ResourceEnum chosenResource, int quantity) {
        this.chosenResource = chosenResource;
        this.quantity = quantity;
    }

    public ResourceEnum getChosenResource() {
        return chosenResource;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {

    }

    @Override
    public void read(ClientVisitor clientVisitor) {
        clientVisitor.elaborate(this);
    }
}
