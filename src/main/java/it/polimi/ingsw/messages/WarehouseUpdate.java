package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Warehouse;

import java.io.Serializable;

/**
 * Message only sent from the server to the client to update the player about his warehouse
 */
public class WarehouseUpdate extends Message implements Serializable {
    private final Warehouse updatedWarehouse;

    public WarehouseUpdate(Warehouse updatedWarehouse) {
        this.updatedWarehouse = updatedWarehouse;
    }

    public Warehouse getUpdatedWarehouse() {
        return updatedWarehouse;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {

    }
}
