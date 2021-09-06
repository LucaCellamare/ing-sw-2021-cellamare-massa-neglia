package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Warehouse;

import java.util.EnumMap;

/**
 * Message that asks the client to choose an initial resource, in case is needed.
 * It's also used to notify the server the chosen resource.
 */
public class ChooseInitialResources extends Message {

    private int playerIndex;

    private int nResources;

    private int nFaithPoints;

    private Warehouse warehouse;

    private EnumMap<ResourceEnum, Integer> pickedResources;

    public ChooseInitialResources(int playerIndex, int nResources, int nFaithPoints, Warehouse warehouse) {
        this.playerIndex = playerIndex;
        this.nResources = nResources;
        this.nFaithPoints = nFaithPoints;
        this.warehouse = warehouse;
    }

    public ChooseInitialResources(EnumMap<ResourceEnum, Integer> pickedResources) {
        this.pickedResources = pickedResources;
    }

    public int getNResources() {
        return nResources;
    }

    public int getNFaithPoints() {
        return nFaithPoints;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public EnumMap<ResourceEnum, Integer> getPickedResources() {
        return pickedResources;
    }

    public int getPlayerIndex() {
        return playerIndex;
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
