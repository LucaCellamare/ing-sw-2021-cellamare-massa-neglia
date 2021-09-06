package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;

/**
 * Message received by the client when the insertion of a resource in a depot is successful.
 */
public class ConfirmInsert extends Message {
    private final ResourceEnum resource;

    private final int quantity;

    private final int depotIndex;

    public ConfirmInsert(ResourceEnum resource, int quantity, int depotIndex) {
        this.resource = resource;
        this.quantity = quantity;
        this.depotIndex = depotIndex;
    }

    public ResourceEnum getResource() {
        return resource;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getDepotIndex() {
        return depotIndex;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {
    }

    @Override
    public void read(ClientVisitor clientVisitor) {
        clientVisitor.elaborate(this);
    }
}
