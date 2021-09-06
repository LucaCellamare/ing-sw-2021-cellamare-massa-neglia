package it.polimi.ingsw.server.Model.PersonalBoard;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Describes the Strongbox of a Player
 *
 * @author Antonio Massa
 */

public class Strongbox implements Serializable {

    /**
     * Resources held in the strongbox
     */
    private final HashMap<ResourceEnum, Integer> resources;

    /**
     * Class builder
     */
    public Strongbox() {
        this.resources = new HashMap<>();
        resources.put(ResourceEnum.COIN, 0);
        resources.put(ResourceEnum.SERVANT, 0);
        resources.put(ResourceEnum.STONE, 0);
        resources.put(ResourceEnum.SHIELD, 0);
    }

    /**
     * Insert a new resource into the strongbox
     *
     * @param resource to insert
     * @param quantity of the resource to insert
     * @throws IllegalArgumentException Illegal resource or quantity passed as argument
     */
    public void insertResource(ResourceEnum resource, int quantity) {
        if (resource == ResourceEnum.NONE || quantity < 0) throw new IllegalArgumentException();
        int oldValue = resources.get(resource);
        resources.replace(resource, oldValue + quantity);
    }

    /**
     * Take a quantity of a resource from the strongbox
     *
     * @param resource type of resource want to remove
     * @param quantity of the resource
     */
    public void remove(ResourceEnum resource, int quantity) {
        int value = resources.get(resource);
        resources.replace(resource, value - quantity);
    }


    /**
     * Get the required resource quantity
     *
     * @param resource Resource whose quantity you want to know
     * @return quantity of the resource
     * @ ensures  resources.containsKey(resource) ? resource.get(resource) : 0
     */
    public int getResourceCount(ResourceEnum resource) {
        return resources.get(resource);
    }

    /**
     * @return the total amount of resources inside the strongbox
     */
    public int getTotalResourcesCount() {
        return resources.values().stream().reduce(0, Integer::sum);
    }
}
