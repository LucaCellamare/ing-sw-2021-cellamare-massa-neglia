package it.polimi.ingsw.server.Model.LeaderAbilities;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Player;

/**
 * Class that describes the ability that adds an additional resource depot
 * to the player, with the same characteristics as those in the warehouse
 * (but only for a specific resource)
 *
 * @author Roberto Neglia
 */
public class StorageAbility implements SpecialAbility {
    /**
     * The resource that can be stored in the additional depot
     */
    private ResourceEnum resource;
    /**
     * The size of the additional depot (how many resources can be stored)
     */
    private int size;

    public StorageAbility() {
    }

    public StorageAbility(ResourceEnum resource, int size) {
        this.resource = resource;
        this.size = size;
    }

    /**
     * @return the resource that can be stored
     */
    public ResourceEnum getResource() {
        return resource;
    }

    /**
     * @return the size of the additional depot
     */
    public int getSize() {
        return size;
    }

    /**
     * activates the special ability by adding the additional depot
     * to the warehouse of the player
     *
     * @param p the player to whom the ability is activated
     */
    @Override
    public void activateAbility(Player p) {
        if (p == null) throw new IllegalArgumentException();
        p.getWarehouse().addDepotFromAbility(resource, size);
    }

    @Override
    public String toString() {
        return "Let's you add a depot in the warehouse" +
                " that can hold only " + size + " " + resource + "s";
    }
}
