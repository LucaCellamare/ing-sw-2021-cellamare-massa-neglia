package it.polimi.ingsw.server.Model.LeaderAbilities;

import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Player;

/**
 * Class that describes the ability that let the player get a specific resource
 * from the resource market when there is a white marble between the marbles in
 * the row or column he choose to get
 *
 * @author Roberto Neglia
 */
public class WhiteMarbleAbility implements SpecialAbility {
    /**
     * The resource that will be get instead of nothing when a white marble
     * is obtained from the resource market
     */
    private ResourceEnum resource;

    public WhiteMarbleAbility() {
    }

    public WhiteMarbleAbility(ResourceEnum resource) {
        this.resource = resource;
    }

    /**
     * @return the resource that will be replaced
     */
    public ResourceEnum getResource() {
        return resource;
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
        p.addWhiteMarbleLeaderAbility(getResource());
    }

    @Override
    public String toString() {
        return "Let's you change the white marble in the" +
                " resource market when picked up to a " + resource;
    }
}
