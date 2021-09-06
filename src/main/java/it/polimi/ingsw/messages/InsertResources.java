package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Warehouse;

import java.util.EnumMap;

/**
 * Message used to asks the Client to insert some resources inside the various depots of the Warehouse.
 */
public class InsertResources extends Message {

    private final EnumMap<ResourceEnum, Integer> toInsert;

    private final Warehouse warehouse;

    public InsertResources(EnumMap<ResourceEnum, Integer> toInsert, Warehouse warehouse) {
        this.toInsert = toInsert;
        this.warehouse = warehouse;
    }

    public EnumMap<ResourceEnum, Integer> getToInsert() {
        return toInsert;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    @Override
    public void read(ServerVisitor serverVisitor) {
        serverVisitor.elaborate(this);
    }

    @Override
    public void read(ClientVisitor clientVisitor) {
    }
}
