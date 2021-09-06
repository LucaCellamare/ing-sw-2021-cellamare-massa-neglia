package it.polimi.ingsw.server.Model.PersonalBoard.Warehouse;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;

import java.io.Serializable;

/**
 * Describes a generic Depot contained in the Warehouse of a Player
 *
 * @author Antonio Massa
 */

public class Depot implements Serializable {

    /**
     * Size of the depot
     */
    private final int size;
    /**
     * Resource currently held in the depot
     */
    private ResourceEnum resource;
    /**
     * Space currently used
     */
    private int used;
    /**
     * Flag that indicates if the Depot is a leader ability Depot (doesn't change the held resource)
     */
    private boolean leaderAbility;


    /**
     * Class builder
     *
     * @param size of the depot
     * @ requires 0 < size <= 3
     */
    public Depot(int size) {
        this.size = size;
        this.used = 0;
        this.resource = ResourceEnum.NONE;
        leaderAbility = false;
    }

    public Depot(int size, boolean leaderAbility) {
        this(size);
        this.leaderAbility = leaderAbility;
    }

    /**
     * Getter
     *
     * @return the actual type of resource allocate to this depot
     */
    public ResourceEnum getResource() {
        return resource;
    }

    /**
     * Setter type of resource for this depot and reset the used space value
     *
     * @param resource
     * @ requires resource == COIN || resource == SHIELD || resource == SERVANT || resource == STONE
     */
    public void setResource(ResourceEnum resource) {
        this.resource = resource;
        this.used = 0;
    }

    /**
     * Getter
     *
     * @return the size of the depot
     */
    public int getSize() {
        return size;
    }

    /**
     * Add the amount of resource on the depot
     *
     * @param quantity of ResourceEnum added
     */
    public void put(int quantity) {
        this.used += quantity;
    }

    /**
     * @return free space of this depot
     */
    public int getFreeSpace() {
        return this.size - this.used;
    }

    /**
     * Remove the qty from the depot
     *
     * @param quantity quantity of resource to remove
     */
    public void remove(int quantity) {
        this.used -= quantity;
        if (this.used == 0 && !leaderAbility)
            resource = ResourceEnum.NONE;
    }


    /**
     * @return the qty of resource stored
     */
    public int getAmountStored() {
        return this.used;
    }

    /**
     * @return true if the depot is of a leader ability, false if not
     */
    public boolean isLeaderAbility() {
        return leaderAbility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Depot depot = (Depot) o;
        return size == depot.size && used == depot.used && resource == depot.resource;
    }

}
