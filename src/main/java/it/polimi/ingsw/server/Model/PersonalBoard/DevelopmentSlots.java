package it.polimi.ingsw.server.Model.PersonalBoard;

import it.polimi.ingsw.exceptions.CannotInsertException;
import it.polimi.ingsw.server.Model.Cards.DevelopmentCard;
import it.polimi.ingsw.server.Model.Enums.LevelEnum;
import it.polimi.ingsw.server.Model.Enums.TypeEnum;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

/**
 * Slots where the Player stores his DevelopmentCards
 *
 * @author Roberto Neglia
 */
public class DevelopmentSlots implements Serializable { // Observable

    /**
     * Max number of cards that a slot can hold
     */
    public static final int maxSize = 3;
    /**
     * Two dimensional collection:
     * - one dimension is for the slots of development cards
     * - the other dimension is for the development cards inside the slots
     */
    final private ArrayList<Deque<DevelopmentCard>> devSlots;

    /**
     * Class constructor, initialize the development slots
     */
    public DevelopmentSlots() {
        devSlots = new ArrayList<>();
        devSlots.add(new ArrayDeque<>(maxSize));
        devSlots.add(new ArrayDeque<>(maxSize));
        devSlots.add(new ArrayDeque<>(maxSize));
    }

    public static int getMaxSize() {
        return maxSize;
    }

    public Deque<DevelopmentCard> getSlot(int slotNumber) {
        return devSlots.get(slotNumber);
    }

    /**
     * Merges all the development cards inside all the slots in one list
     *
     * @return the list with all the cards a player has in the development slots
     */
    public List<DevelopmentCard> getCards() {
        return devSlots.stream()                            // Stream<Deque<DevelopmentCard>>
                .flatMap(Collection::stream)                // Stream<DevelopmentCard>
                .collect(Collectors.toList());
    }

    /**
     * Adds a card inside the specified slot
     *
     * @param newCard    card to be added
     * @param slotNumber the slot where to put the card
     * @throws CannotInsertException the card cannot be inserted either because the slot is empty
     *                               and the card level is greater than one or because the card
     *                               on the top of the slot is not of the previous level of the one to be added
     */
    public void addCard(DevelopmentCard newCard, int slotNumber) throws CannotInsertException {
        if (newCard == null) throw new IllegalArgumentException("Argument is null");
        if (slotNumber < 0 || slotNumber > 2) throw new IllegalArgumentException("Illegal slot number");
        if (canInsert(newCard, slotNumber))
            devSlots.get(slotNumber).addLast(newCard);
        else
            throw new CannotInsertException(slotNumber, newCard.getLevel());
    }

    /**
     * Helper method, checks if the card can be inserted inside the chosen development slot
     *
     * @param newCard    card to be added
     * @param slotNumber the slot where to put the card
     * @return true if the card can be inserted, false if not
     */
    public boolean canInsert(DevelopmentCard newCard, int slotNumber) {
        if (newCard == null) throw new IllegalArgumentException("Argument is null");
        if (slotNumber < 0 || slotNumber > 2) throw new IllegalArgumentException("Illegal slot number");
        Deque<DevelopmentCard> chosenSlot = devSlots.get(slotNumber);
        DevelopmentCard topCard;

        boolean retValue = chosenSlot.size() != maxSize;

        if (chosenSlot.isEmpty()) {
            if (LevelEnum.previousLevel(newCard.getLevel()) != LevelEnum.NONE || newCard.getLevel() == LevelEnum.NONE)
                retValue = false;
        } else {
            topCard = chosenSlot.getLast();
            if (LevelEnum.previousLevel(newCard.getLevel()) != topCard.getLevel())
                retValue = false;
        }
        return retValue;
    }

    /**
     * Counts all occurrences of every type of card inside the slots
     *
     * @return an map that link every type to the number of occurrences
     */
    public EnumMap<TypeEnum, Integer> getCardTypesCount() {
        List<DevelopmentCard> playerCards = getCards();
        return playerCards.stream()
                .collect(groupingBy(DevelopmentCard::getType, () -> new EnumMap<>(TypeEnum.class), summingInt(c -> 1)));
    }

    /**
     * Gets the max level stored int the slots for a specific type of card
     *
     * @param type type of the card we want to find the max level
     * @return the max level stored of the specified type of card
     */
    public LevelEnum getMaxLevelFromCardType(TypeEnum type) {
        if (type == TypeEnum.NONE) throw new IllegalArgumentException("Illegal card type");
        List<DevelopmentCard> playerCards = getCards();
        return playerCards.stream()
                .filter(c -> c.getType() == type)
                .max(Comparator.comparing(DevelopmentCard::getLevel))
                .map(DevelopmentCard::getLevel)
                .orElse(LevelEnum.NONE);
    }
}
