package it.polimi.ingsw.server.Model.LeaderAbilities;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Player;

/**
 * Class that describes the ability that grants a discount of a resource
 * at the card market to the player who activates the ability
 *
 * @author Roberto Neglia
 */
public class DiscountAbility implements SpecialAbility {

    /**
     * The specific resource discounted from the market
     */
    private ResourceEnum resource;

    /**
     * The amount of discounted resources
     */
    private int quantity;

    public DiscountAbility() {
    }

    public DiscountAbility(ResourceEnum resource, int quantity) {
        this.resource = resource;
        this.quantity = quantity;
    }

    /**
     * @return the resource discounted
     */
    public ResourceEnum getResource() {
        return resource;
    }

    /**
     * @return the amount of resources discounted
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Activates the special ability
     *
     * @param p the player to whom the ability is activated
     */
    @Override
    public void activateAbility(Player p) {
        // ability to be added to a list in a PlayerHandler that at the start of
        // every turn applies every ability stored in that list
        // (Discount or WhiteMarble ability only)
        if(p == null) throw new IllegalArgumentException();
        p.addDiscountLeaderAbility(getResource());
    }

    @Override
    public String toString() {
        return "Gives you a discount of " + quantity
                + " " + resource + "s at the resource market";
    }
}
