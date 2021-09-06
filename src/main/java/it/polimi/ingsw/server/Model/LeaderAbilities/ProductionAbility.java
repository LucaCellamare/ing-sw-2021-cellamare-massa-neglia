package it.polimi.ingsw.server.Model.LeaderAbilities;

import it.polimi.ingsw.server.Model.Cards.ProductionPower;
import it.polimi.ingsw.server.Model.Player;

/**
 * Class that describes the ability that adds an additional ProductionPower to the player
 *
 * @author Roberto Neglia
 */
public class ProductionAbility implements SpecialAbility {
    /**
     * The ProductionPower to be added
     */
    final private ProductionPower productionPower;

    /**
     * Class constructor
     *
     * @param productionPower the ProductionPower that the ability will add to the player
     */
    public ProductionAbility(ProductionPower productionPower) {
        this.productionPower = productionPower;
    }

    /**
     * Gets the ProductionPower that the ability will add to the player
     *
     * @return the ProductionPower
     */
    public ProductionPower getProductionPower() {
        return productionPower;
    }

    /**
     * Activates the special ability by adding the ProductionPower to the list of ProductionPowers of the player
     *
     * @param p the player to whom the ability is activated
     */
    @Override
    public void activateAbility(Player p) {
        p.addLeaderProductionPower(productionPower);
    }

    @Override
    public String toString() {
        return "gives you the production power = " + productionPower;
    }
}