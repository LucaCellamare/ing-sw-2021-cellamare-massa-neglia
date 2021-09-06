package it.polimi.ingsw.server.Model.Cards;

import it.polimi.ingsw.server.Model.LeaderAbilities.SpecialAbility;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.Requirements.Requirement;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Describes the LeaderCard which the Player needs to choose at the beginning of a match
 *
 * @author Roberto Neglia
 */
public class LeaderCard implements Serializable {
    
    /**
     * unique path of the card
     */
    private String path;
    /**
     * unique id of the card
     */
    private int id;
    /**
     * Victory Points given to the player when activated
     */
    private int victoryPoints;
    /**
     * Cards and resources required to activate the leader card
     */
    private Requirement[] requirements;
    /**
     * Ability of the card that can be activated when all the requirements are satisfied
     */
    private SpecialAbility ability;
    /**
     * Flag that indicates if the leader card is already activated or not
     */
    private boolean isActive;

    public LeaderCard() {
    }

    public LeaderCard(String path,int id, int victoryPoints, Requirement[] requirements, SpecialAbility ability) {
        this.path = path;
        this.id = id;
        this.victoryPoints = victoryPoints;
        this.requirements = requirements;
        this.ability = ability;
        isActive = false;
    }

    /**
     * @return the path of the card
     */
    public String getPath() {
        return path;
    }

    /**
     * @return the id of the card
     */
    public int getId() {
        return id;
    }

    /**
     * @return the victory points of the card
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * @return the special ability of the leader
     */
    public SpecialAbility getAbility() {
        return ability;
    }

    /**
     * @return the cards and resources required by the leader card to be activated
     */
    public Requirement[] getRequirements() {
        return requirements;
    }

    /**
     * @return true if the leader card is already activated, false if not
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * activate the leader special ability
     *
     * @param p the player which is activating the leader card (the one who owns it)
     * @throws IllegalArgumentException the argument is a null pointer
     */
    public void activate(Player p) throws IllegalArgumentException {
        if (p == null) throw new IllegalArgumentException("Argument is null");
        ability.activateAbility(p);
        isActive = true;
    }

    /**
     * Checks if the card can be activated by the player given as a parameter
     *
     * @param p the player which we want to check if is able to activate the card
     * @return true if the card can be activated by the player, false if not
     */
    public boolean canBeActivated(Player p) {
        if (p == null) throw new IllegalArgumentException("Argument is null");
        int i;
        boolean retValue;

        retValue = true;
        for (i = 0; i < requirements.length && retValue; i++) {
            retValue = requirements[i].checkRequirement(p);
        }
        return retValue;
    }

    @Override
    public String toString() {
        return "Victory points = " + victoryPoints +
                ", to be activated requires: " + Arrays.toString(requirements) +
                ", his ability is: " + ability;
    }
}
