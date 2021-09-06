package it.polimi.ingsw.server.Model.Requirements;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.PersonalBoard.Strongbox;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Warehouse;
import it.polimi.ingsw.server.Model.Player;

import java.util.Arrays;

/**
 * Describes a single Resource required by a Card, or a ProductionPower
 *
 * @author Roberto Neglia
 */

public class ResourceRequirement implements Requirement {
    /**
     * Type of the resource required
     */
    private ResourceEnum resourceType;
    /**
     * Quantity required of the resource
     */
    private int quantity;

    public ResourceRequirement() {
    }

    /**
     * Constructor
     *
     * @param resourceType type of the resource required
     * @param quantity     quantity required of the resource
     */
    public ResourceRequirement(ResourceEnum resourceType, int quantity) {
        this.resourceType = resourceType;
        this.quantity = quantity;
    }

    /**
     * Gets the info about the quantity required
     *
     * @return the quantity required
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Gets the info about the type of the resource required
     *
     * @return the resource required
     */
    public ResourceEnum getResourceType() {
        return resourceType;
    }

    /**
     * Checks if the player satisfies the single resource required
     *
     * @param p the player that we want to check if has the requirement
     * @return true if the requirement is fulfilled, false if not
     */
    @Override
    public boolean checkRequirement(Player p) throws IllegalArgumentException {
        if (p == null) throw new IllegalArgumentException();

        ResourceEnum[] resources = Arrays.copyOfRange(ResourceEnum.values(), 0, 4);

        int qtyInWarehouse = 0, qtyInStrongbox = 0, qtyRemaining = quantity;

        Warehouse playerWarehouse = p.getWarehouse();
        Strongbox playerStrongbox = p.getStrongbox();
        if (resourceType == ResourceEnum.JOLLY) {
            for (ResourceEnum r : resources) {
                qtyInWarehouse = playerWarehouse.getResourceAmount(r);
                if (qtyInWarehouse >= quantity)
                    break;
                qtyInStrongbox = playerStrongbox.getResourceCount(r);
                if (qtyInStrongbox >= quantity)
                    break;
            }
        } else {
            qtyInWarehouse = playerWarehouse.getResourceAmount(resourceType);
            qtyInStrongbox = playerStrongbox.getResourceCount(resourceType);
        }
        if (qtyInWarehouse >= qtyRemaining)
            return true;
        qtyRemaining -= qtyInWarehouse;
        return qtyInStrongbox >= qtyRemaining;
    }

    @Override
    public String toString() {
        return quantity + " " + resourceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceRequirement that = (ResourceRequirement) o;
        return quantity == that.quantity && resourceType == that.resourceType;
    }

    public void applyDiscount(int qtyToDiscount)
    {
        this.quantity = this.quantity - qtyToDiscount;
    }
}