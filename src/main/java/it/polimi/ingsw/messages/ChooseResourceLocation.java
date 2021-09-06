package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.PersonalBoard.Strongbox;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Warehouse;
import it.polimi.ingsw.server.Model.Requirements.ResourceRequirement;

import java.util.EnumMap;

/**
 * Message that asks the Client whether the resources to be paid can be taken from the Warehouse or from the Strongbox.
 * It's also used to send to the server the resources to be taken both from the Warehouse and from the Strongbox.
 */
public class ChooseResourceLocation extends Message {
    private Strongbox playerStrongbox;
    private Warehouse playerWarehouse;
    private ResourceRequirement[] toPay;
    private EnumMap<ResourceEnum, Integer> payWarehouse;
    private EnumMap<ResourceEnum, Integer> payStrongbox;

    public ChooseResourceLocation(ResourceRequirement[] toPay, Strongbox inStrongbox, Warehouse inWarehouse) {
        this.playerStrongbox = inStrongbox;
        this.playerWarehouse = inWarehouse;
        this.toPay = toPay;
    }

    public ChooseResourceLocation(EnumMap<ResourceEnum, Integer> payWarehouse, EnumMap<ResourceEnum, Integer> payStrongbox) {
        this.payWarehouse = payWarehouse;
        this.payStrongbox = payStrongbox;
    }

    public Strongbox getPlayerStrongbox() {
        return playerStrongbox;
    }

    public Warehouse getPlayerWarehouse() {
        return playerWarehouse;
    }

    public ResourceRequirement[] getToPay() {
        return toPay;
    }

    public EnumMap<ResourceEnum, Integer> getPayWarehouse() {
        return payWarehouse;
    }

    public EnumMap<ResourceEnum, Integer> getPayStrongbox() {
        return payStrongbox;
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
