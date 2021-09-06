package it.polimi.ingsw.server.Model.Cards;

import it.polimi.ingsw.server.Model.Enums.LevelEnum;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Enums.TypeEnum;
import it.polimi.ingsw.server.Model.PersonalBoard.DevelopmentSlots;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.Requirements.ResourceRequirement;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Class that describes a development card
 *
 * @author Roberto Neglia
 */
public class DevelopmentCard implements Serializable {
    /**
     * unique path of the card
     */
    private String path;
    /**
     * unique id of the card
     */
    private int id;
    /**
     * VictoryPoints given to the player if he owns it
     */
    private int victoryPoints;
    /**
     * Resources required to buy the card from the market
     */
    private ResourceRequirement[] price;
    /**
     * Production power specific of the card
     */
    private ProductionPower productionPower;
    /**
     * The type (color) of the card
     */
    private TypeEnum type;
    /**
     * The level of the card
     */
    private LevelEnum level;

    public DevelopmentCard() {
    }

    public DevelopmentCard(TypeEnum type, LevelEnum level) {
        this.type = type;
        this.level = level;
    }

    /**
     * Class constructor
     *
     * @param id              id of the card
     * @param victoryPoints   points given to the player when owned
     * @param price           price of the card
     * @param productionPower production power of the card
     * @param type            type (color) of the card
     * @param level           level of the card
     */
    public DevelopmentCard(String path, int id, int victoryPoints, ResourceRequirement[] price, ProductionPower productionPower, TypeEnum type, LevelEnum level) {
        this.path = path;
        this.id = id;
        this.victoryPoints = victoryPoints;
        this.price = price;
        this.productionPower = productionPower;
        this.type = type;
        this.level = level;
    }

    /**
     * Checks if the player can buy the card with his resources
     *
     * @param p player we want to check if owns enough resources to buy the card
     * @return true if the player can buy the card, false if not
     */
    public boolean canBeBought(Player p) {
        int i;
        int numberOfDevSlots = 3;
        boolean enoughSpace;
        boolean enoughRequirements;
        DevelopmentSlots playerSlots;

        playerSlots = p.getSlots();

        enoughSpace = false;
        for (i = 0; i < numberOfDevSlots && !enoughSpace; i++)
            enoughSpace = playerSlots.canInsert(this, i);

        enoughRequirements = true;

        //DEVO APPLICARE LO SCONTO QUA
        if (p.getDiscountLeaderAbilities().size() == 0) {
            for (i = 0; i < price.length && enoughRequirements; i++)
                enoughRequirements = price[i].checkRequirement(p);
        } else {
            ResourceRequirement[] wrapperPrice;
            wrapperPrice = deepCopy(price);

            for (ResourceRequirement resReq : wrapperPrice) {
                for (ResourceEnum resDisc : p.getDiscountLeaderAbilities()) {
                    if (resReq.getResourceType().equals(resDisc)) {
                        resReq.applyDiscount(1);
                    }
                }
            }

            for (i = 0; i < wrapperPrice.length && enoughRequirements; i++)
                enoughRequirements = wrapperPrice[i].checkRequirement(p);
        }

        return enoughSpace && enoughRequirements;
    }

    /**
     * Does a deep copy of an array of resource requirements
     *
     * @param array array to be copied
     * @return a deep copy of the array
     */
    private ResourceRequirement[] deepCopy(ResourceRequirement[] array) {
        ResourceRequirement[] deepCopy = new ResourceRequirement[array.length];

        for (int i = 0; i < array.length; i++) {
            deepCopy[i] = new ResourceRequirement(array[i].getResourceType(), array[i].getQuantity());
        }

        return deepCopy;
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
     * @return the type (card)
     */
    public TypeEnum getType() {
        return type;
    }

    /**
     * @return the level of the card
     */
    public LevelEnum getLevel() {
        return level;
    }

    /**
     * @return the price of the card
     */
    public ResourceRequirement[] getPrice() {
        return price;
    }

    /**
     * @return the production power specific of the development card
     */
    public ProductionPower getProductionPower() {
        return productionPower;
    }

    @Override
    public String toString() {
        return "DevCard{" +
                "level=" + level +
                ", price=" + Arrays.toString(price) +
                ", prodPower=" + productionPower +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DevelopmentCard that = (DevelopmentCard) o;
        if (type != TypeEnum.NONE && level != LevelEnum.NONE) {
            return type == that.type
                    && level == that.level;
        } else if (type != TypeEnum.NONE) {
            return type == that.type;
        } else if (level != LevelEnum.NONE) {
            return level == that.level;
        } else return false;
    }
}