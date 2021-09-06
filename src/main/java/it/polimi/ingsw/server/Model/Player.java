package it.polimi.ingsw.server.Model;


import it.polimi.ingsw.exceptions.CannotActivateAbilityException;
import it.polimi.ingsw.exceptions.CannotInsertException;
import it.polimi.ingsw.exceptions.MaxLeaderCardsException;
import it.polimi.ingsw.server.Controller.GamePhase;
import it.polimi.ingsw.server.Controller.Handlers.FaithTrackHandler;
import it.polimi.ingsw.server.Model.Cards.DevelopmentCard;
import it.polimi.ingsw.server.Model.Cards.LeaderCard;
import it.polimi.ingsw.server.Model.Cards.ProductionPower;
import it.polimi.ingsw.server.Model.Enums.CellTypeEnum;
import it.polimi.ingsw.server.Model.Enums.LevelEnum;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Enums.TypeEnum;
import it.polimi.ingsw.server.Model.FaithTrack.FaithTrack;
import it.polimi.ingsw.server.Model.PersonalBoard.DevelopmentSlots;
import it.polimi.ingsw.server.Model.PersonalBoard.Strongbox;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Warehouse;
import it.polimi.ingsw.server.Model.Requirements.ResourceRequirement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class which describes a player of the game
 *
 * @author Roberto Neglia
 */
public class Player implements Serializable {

    /**
     * Nickname of the player (must be unique)
     */
    final private String nickname;
    /**
     * Indicates if the player is the first
     */
    final private boolean inkwell;
    /**
     * Developments slots where the player can insert the development cards bought from the market
     */
    private final DevelopmentSlots devSlots;
    /**
     * Warehouse where the player can store the resources obtained from the resource market and that can be used to
     * produce other resources via the production powers
     */
    private final Warehouse warehouse;
    /**
     * Strongbox where the player can store the resources obtained from the use of production powers
     */
    private final Strongbox strongbox;
    /**
     * Personal faith track of the player, where are memorized his total faith points
     */
    private final FaithTrack faithTrack;
    /**
     * Contains the four initial leader cards picked by the player during the setup of the game.
     * He later chooses only two of them that he will keep during the whole game
     */
    final private List<LeaderCard> leaderCardsToChoose;
    /**
     * The leader cards owned by the player
     */
    private final List<LeaderCard> leaderCards;
    /**
     * This is the base production power common to every player
     */
    private final ProductionPower baseProductionPower;
    /**
     * Utility list of production powers given to the player by his visible development cards.
     * It's size is given by the player's development slots' number:
     * the position i of a production power inside this list is the production power of the
     * development card on the top of the development slot number i
     */
    private final List<ProductionPower> devProductionPowers;
    /**
     * Utility list of production powers given to the player when he activates leader cards with a production power
     * as a special ability
     */
    private final List<ProductionPower> leaderProductionPowers;
    /**
     * Maximum number of leader cards a player can have
     */
    private final int maxLeaderCards;
    /**
     * Inside this list the are all the resource can be discounted for buying development cards
     */
    private final List<ResourceEnum> discountLeaderAbilities;
    /**
     * Inside this list the are all the resource can be swapped for a white marble in the resource market
     */
    private final List<ResourceEnum> whiteMarbleLeaderAbilities;
    /**
     * Utility flag which indicates if a player has reached one of the game ending conditions
     */
    private boolean isTerminator;

    private boolean isConnected;
    /**
     * All the victory points cumulated by the player during the game
     */
    private int totalVictoryPoints;

    private GamePhase gamePhase;

    /**
     * Player constructor, sets his name and if he's first, then instantiates all his components
     *
     * @param nickname identifier of the player in the match
     * @param inkwell  first player or not
     */
    public Player(String nickname, boolean inkwell) {
        this.nickname = nickname;
        this.inkwell = inkwell;
        maxLeaderCards = 2;
        devSlots = new DevelopmentSlots();
        warehouse = new Warehouse();
        strongbox = new Strongbox();
        faithTrack = new FaithTrack(false);
        leaderCardsToChoose = LeaderCardDeck.pick4Card();
        leaderCards = new ArrayList<>(maxLeaderCards);
        devProductionPowers = new ArrayList<>(DevelopmentSlots.maxSize) {{
            add(new ProductionPower());
            add(new ProductionPower());
            add(new ProductionPower());
        }};
        baseProductionPower = createBaseProductionPower();
        leaderProductionPowers = new ArrayList<>();
        isTerminator = false;
        whiteMarbleLeaderAbilities = new ArrayList<>();
        discountLeaderAbilities = new ArrayList<>();
        isConnected = true;

        // TODO remove the initial resources after testing

//        strongbox.insertResource(ResourceEnum.COIN, 10);
//        strongbox.insertResource(ResourceEnum.STONE, 10);
//        strongbox.insertResource(ResourceEnum.SERVANT, 10);
//        strongbox.insertResource(ResourceEnum.SHIELD, 10);
//
//        try {
//            devSlots.addCard(new DevelopmentCard(TypeEnum.YELLOW, LevelEnum.ONE), 0);
//            devSlots.addCard(new DevelopmentCard(TypeEnum.YELLOW, LevelEnum.TWO), 0);
//            devSlots.addCard(new DevelopmentCard(TypeEnum.BLUE, LevelEnum.ONE), 1);
//            devSlots.addCard(new DevelopmentCard(TypeEnum.BLUE, LevelEnum.TWO), 1);
//            devSlots.addCard(new DevelopmentCard(TypeEnum.GREEN, LevelEnum.ONE), 2);
//            devSlots.addCard(new DevelopmentCard(TypeEnum.PURPLE, LevelEnum.TWO), 2);
//        } catch (CannotInsertException e) {
//            e.printStackTrace();
//        }

        // TODO remove the initial resources after testing

    }

    /**
     * Tells if the player is still connected to the game and is playing
     *
     * @return true if is still connected, false if not
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Sets the connected flag to the given value
     *
     * @param connected value to set
     */
    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    /**
     * Helper method which adds the common base production power to the player production powers list
     */
    private ProductionPower createBaseProductionPower() {
        ResourceRequirement[] baseProductionPowerResourceRequired = {
                new ResourceRequirement(ResourceEnum.JOLLY, 1),
                new ResourceRequirement(ResourceEnum.JOLLY, 1)
        };
        EnumMap<ResourceEnum, Integer> baseProductionPowerResourceObtained = new EnumMap<>(ResourceEnum.class) {{
            put(ResourceEnum.JOLLY, 1);
        }};
        ProductionPower baseProductionPower = new ProductionPower(baseProductionPowerResourceRequired, baseProductionPowerResourceObtained);
        baseProductionPower.setBaseProductionPower(true);
        return baseProductionPower;
    }

    /**
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return true if the player is the first player, false if not
     */
    public boolean isFirst() {
        return inkwell;
    }

    /**
     * @return true if the player has satisfied one of the winning conditions, false if not
     */
    public boolean isTerminator() {
        if (faithTrack.getCell().getCellType() == CellTypeEnum.LAST)
            isTerminator = true;
        if (devSlots.getCards().size() == 7)
            isTerminator = true;
        return isTerminator;
    }

    /**
     * @return the developments slots of the player
     */
    public DevelopmentSlots getSlots() {
        return devSlots;
    }

    /**
     * @return the warehouse of the player
     */
    public Warehouse getWarehouse() {
        return warehouse;
    }

    /**
     * @return the strongbox of the player
     */
    public Strongbox getStrongbox() {
        return strongbox;
    }

    /**
     * @return the faith track of the player
     */
    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    /**
     * @return the leader cards picked by the player during the setup of the game
     */
    public List<LeaderCard> getLeaderCardsToChoose() {
        return leaderCardsToChoose;
    }

    /**
     * @return the leader cards of the player
     */
    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    /**
     * @return the base production power common to all the players
     */
    public ProductionPower getBaseProductionPower() {
        return baseProductionPower;
    }

    /**
     * @return the production powers from the player's visible development cards
     */
    public List<ProductionPower> getDevProductionPowers() {
        return devProductionPowers.stream().filter(pp -> Objects.nonNull(pp.getResourceObtained())).collect(Collectors.toList());
    }

    /**
     * @return the production powers given to the player when he activates a leader card with that ability
     */
    public List<ProductionPower> getLeaderProductionPowers() {
        return leaderProductionPowers;
    }

    /**
     * @return the list of discount ability activated by the user
     */
    public List<ResourceEnum> getDiscountLeaderAbilities() {
        return discountLeaderAbilities;
    }

    /**
     * @return the list of the white marble ability activated by the user
     */
    public List<ResourceEnum> getWhiteMarbleLeaderAbilities() {
        return whiteMarbleLeaderAbilities;
    }

    /**
     * @return all the production powers combined
     */
    public List<ProductionPower> getAllProductionPowers() {
        List<ProductionPower> productionPowers = new ArrayList<>();
        productionPowers.add(baseProductionPower);
        productionPowers.addAll(getDevProductionPowers());
        productionPowers.addAll(getLeaderProductionPowers());
        return productionPowers;
    }

    /**
     * @return the total amount of victory points cumulated by the player during the game
     */
    public int getTotalVictoryPoints() {
        return totalVictoryPoints;
    }

    /**
     * Sets the total amount of victory points cumulated by the player during the game
     *
     * @param totalVictoryPoints the total amount of victory points cumulated by the player during the game
     */
    public void setTotalVictoryPoints(int totalVictoryPoints) {
        this.totalVictoryPoints = totalVictoryPoints;
    }

    /**
     * Used to add the leader cards chosen by the player at the start of the game
     *
     * @param newCard leader card of the player to be added
     * @throws MaxLeaderCardsException the player has already reached the max number of leader cards he can possess
     */
    public void setLeaderCard(LeaderCard newCard) throws MaxLeaderCardsException {
        if (leaderCards.size() == maxLeaderCards) throw new MaxLeaderCardsException();
        leaderCards.add(newCard);
    }

    /**
     * Used when the player wants to discard a leader card
     *
     * @param leaderCardPosition the position of the card inside the leader card list
     */
    public void removeLeaderCard(int leaderCardPosition) throws IllegalArgumentException {
        if (leaderCardPosition < 0 || leaderCardPosition >= leaderCards.size())
            throw new IllegalArgumentException("Illegal leader card position");
        leaderCards.remove(leaderCardPosition);
    }

    /**
     * Adds the leader production power when activated to the utility list
     *
     * @param productionPower the production power to be added
     * @throws IllegalArgumentException the argument is a null reference
     */
    public void addLeaderProductionPower(ProductionPower productionPower) throws IllegalArgumentException {
        if (productionPower == null) throw new IllegalArgumentException("Argument is null");
        leaderProductionPowers.add(productionPower);
    }

    /**
     * @param resource resource to convert from the white marble
     * @throws IllegalArgumentException the arugment in a null reference or is a not valide resource
     */
    public void addWhiteMarbleLeaderAbility(ResourceEnum resource) throws IllegalArgumentException {
        if (resource == null || resource.equals(ResourceEnum.NONE))
            throw new IllegalArgumentException("Argument is null");
        whiteMarbleLeaderAbilities.add(resource);
    }

    /**
     * @param resource resource to convert from the white marble
     * @throws IllegalArgumentException the arugment in a null reference or is a not valide resource
     */
    public void addDiscountLeaderAbility(ResourceEnum resource) throws IllegalArgumentException {
        if (resource == null || resource.equals(ResourceEnum.NONE))
            throw new IllegalArgumentException("Argument is null");
        discountLeaderAbilities.add(resource);
    }

    /**
     * Activates a leader card
     *
     * @param leaderCardPosition position of the leader card to be activated inside the leader card list
     * @throws IllegalArgumentException       the position given of the card to be activated is illegal
     * @throws CannotActivateAbilityException the ability cannot be activated, either because is already activated
     *                                        or because one or more requirement cannot be fulfilled
     */
    public void activateLeaderCard(int leaderCardPosition) throws IllegalArgumentException, CannotActivateAbilityException {
        if (leaderCardPosition < 0 || leaderCardPosition >= maxLeaderCards)
            throw new IllegalArgumentException("Illegal leader card position");
        LeaderCard toBeActivated = leaderCards.get(leaderCardPosition);
        if (toBeActivated.isActive()) throw new CannotActivateAbilityException(toBeActivated.isActive());
        if (!toBeActivated.canBeActivated(this))
            throw new CannotActivateAbilityException(this, toBeActivated.getRequirements());
        toBeActivated.activate(this);
    }

    /**
     * Updates the development production powers list, by replacing the old production power with a new one
     * (to be used when the player buy a new card)
     *
     * @param position           the position of the production power to be updated (number of the dev slot where the card will be inserted)
     * @param newProductionPower the new production power that can be used
     */
    public void updateDevProductionPower(ProductionPower newProductionPower, int position) {
        if (position < 0 || position >= DevelopmentSlots.maxSize)
            throw new IllegalArgumentException("Illegal production power position");
        if (newProductionPower == null) throw new IllegalArgumentException("Argument is null");
        devProductionPowers.set(position, newProductionPower);
    }

    /**
     * Gets the leader card position inside the leaderCards list of the leader card with the specified id
     *
     * @param idLeaderCard the id of the leader card we are looking for
     * @return the position of the leader card in the leaderCards list
     */
    public int getLeaderCardPositionById(int idLeaderCard) {
        return IntStream.range(0, getLeaderCards().size())
                .filter(i -> getLeaderCards().get(i).getId() == idLeaderCard)
                .findFirst()
                .orElse(-1);
    }

    /**
     * Gets in which phase the player is
     *
     * @return the phase of the game in which the player is
     */
    public GamePhase getGamePhase() {
        return gamePhase;
    }

    /**
     * Sets the phase of the game in which the player is
     *
     * @param gamePhase the phase to be set
     */
    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }
}
