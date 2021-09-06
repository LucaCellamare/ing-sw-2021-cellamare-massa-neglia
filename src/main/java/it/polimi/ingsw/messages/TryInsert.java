package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;

/**
 * Bi-directional message:
 * - from client to server: the player want to insert some resources inside a depot of the warehouse;
 * - from server to client: two different cases: a depot with that resource stored in it is already present in the warehouse, so it can't be put in another one;
 * the depot can't hold all the resources that the player wants to insert, so he have to decide if discard the remaining ones, or to retry
 */
public class TryInsert extends Message {

    private final ResourceEnum resource;

    private final int quantity;

    private final int chosenDepot;

    private int toDiscard;

    public TryInsert() {
        resource = ResourceEnum.NONE;
        quantity = -1;
        chosenDepot = -1;
    }

    public TryInsert(ResourceEnum resource, int quantity, int chosenDepot) {
        this.resource = resource;
        this.quantity = quantity;
        this.chosenDepot = chosenDepot;
    }

    public int getToDiscard() {
        return toDiscard;
    }

    public void setToDiscard(int toDiscard) {
        this.toDiscard = toDiscard;
    }

    public ResourceEnum getResource() {
        return resource;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getChosenDepot() {
        return chosenDepot;
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
