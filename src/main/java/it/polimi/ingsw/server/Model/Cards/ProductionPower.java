package it.polimi.ingsw.server.Model.Cards;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.Requirements.ResourceRequirement;

import java.io.Serializable;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Objects;

/**
 * Describes a production power, with the resources required to be used and the resources obtained when used
 *
 * @author Roberto Neglia
 */
public class ProductionPower implements Serializable {
    /**
     * Resources required to be used
     */
    private ResourceRequirement[] resourceRequested;
    /**
     * Resources obtained when the production is used
     */
    private EnumMap<ResourceEnum, Integer> resourceObtained;
    /**
     * Flag that indicates if it's the base production power
     */
    private boolean isBaseProductionPower = false;

    public ProductionPower() {
    }

    /**
     * Constructor
     *
     * @param resourceRequested array of resources requested to use the production
     * @param resourceObtained  map of resources obtained with the production
     */
    public ProductionPower(ResourceRequirement[] resourceRequested, EnumMap<ResourceEnum, Integer> resourceObtained) {
        this.resourceRequested = resourceRequested;
        this.resourceObtained = resourceObtained;
    }

    /**
     * Checks if the player can use the production power
     *
     * @param p player whose resources will be checked
     * @return true if the player can use the production, false if not
     */
    public boolean canBeUsed(Player p) {
        if (p == null) throw new IllegalArgumentException("Argument is null");
        boolean retValue, jollyCheck;
        int totalJollyRequests = 0;
        int i;
        retValue = true;
        for (i = 0; i < resourceRequested.length && retValue; i++) {
            if (resourceRequested[i].getResourceType() == ResourceEnum.JOLLY)
                totalJollyRequests += resourceRequested[i].getQuantity();
            retValue = resourceRequested[i].checkRequirement(p);
        }
        jollyCheck = p.getWarehouse().getTotalResourcesAmount() + p.getStrongbox().getTotalResourcesCount() >= totalJollyRequests;
        return retValue && jollyCheck;
    }

    /**
     * Gets the resources requested to use the production
     *
     * @return the resources required by the production
     */
    public ResourceRequirement[] getResourceRequested() {
        return resourceRequested;
    }

    /**
     * Gets the resources obtained with the production
     *
     * @return the resources that the production creates
     */
    public EnumMap<ResourceEnum, Integer> getResourceObtained() {
        return resourceObtained;
    }

    /**
     * @return true if it's the base production power, false if not
     */
    public boolean isBaseProductionPower() {
        return isBaseProductionPower;
    }

    /**
     * @param baseProductionPower value to be set
     */
    public void setBaseProductionPower(boolean baseProductionPower) {
        isBaseProductionPower = baseProductionPower;
    }

    @Override
    public String toString() {
        String resourceObtainedString = "";
        for (ResourceEnum r : ResourceEnum.values()) {
            if (resourceObtained.containsKey(r))
                resourceObtainedString = resourceObtainedString.concat(resourceObtained.get(r) + " " + r + "(s) ");
        }
        return "requires " + Arrays.toString(resourceRequested) +
                ", gives you " + resourceObtainedString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductionPower that = (ProductionPower) o;
        return Arrays.equals(resourceRequested, that.resourceRequested) && Objects.equals(resourceObtained, that.resourceObtained);
    }
}
