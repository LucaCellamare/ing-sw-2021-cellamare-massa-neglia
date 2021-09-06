package it.polimi.ingsw.server.Model.PersonalBoard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import it.polimi.ingsw.exceptions.CannotInsertException;
import it.polimi.ingsw.server.Model.Cards.DevelopmentCard;
import it.polimi.ingsw.server.Model.Enums.LevelEnum;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Enums.TypeEnum;
import it.polimi.ingsw.server.Model.PersonalBoard.DevelopmentSlots;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class DevelopmentSlotsTest {
    DevelopmentSlots slots;

    int greenCardsInSlots = 0;
    int purpleCardsInSlots = 0;
    int blueCardsInSlots = 0;
    int yellowCardsInSlots = 0;

    LevelEnum maxGreenLevel;
    LevelEnum maxPurpleLevel;
    LevelEnum maxBlueLevel;
    LevelEnum maxYellowLevel;

    DevelopmentCard[] cards;

    String cardPath = "json/DevelopmentCards.json";

    private int lvlOneRand() {
        return new Random().nextInt(16);
    }

    private int lvlTwoRand() {
        return new Random().nextInt(16) + 16;
    }

    private int lvlThreeRand() {
        return new Random().nextInt(16) + 32;
    }

    private void randomInsert() {
        slots = new DevelopmentSlots();
        LevelEnum max = LevelEnum.NONE;

        List<Integer> greenCards = new ArrayList<>(asList(0, 4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44));
        List<Integer> purpleCards = new ArrayList<>(asList(1, 5, 9, 13, 17, 21, 25, 29, 33, 37, 41, 45));
        List<Integer> blueCards = new ArrayList<>(asList(2, 6, 10, 14, 18, 22, 26, 30, 34, 38, 42, 46));
        List<Integer> yellowCards = new ArrayList<>(asList(3, 7, 11, 15, 19, 23, 27, 31, 35, 39, 43, 47));

        int position;

        int i;
        Random rand = new Random();

        int greenCardsToInsert = rand.nextInt(16);
        int purpleCardsToInsert = rand.nextInt(16);
        int blueCardsToInsert = rand.nextInt(16);
        int yellowCardsToInsert = rand.nextInt(16);

        for (i = 0; i < greenCardsToInsert; i++) {
            try {
                position = rand.nextInt(greenCards.size());
                slots.addCard(cards[greenCards.get(position)], rand.nextInt(3));
                max = LevelEnum.compare(cards[greenCards.get(position)].getLevel(), max) > 0 ? cards[greenCards.get(position)].getLevel() : max;
                greenCardsInSlots++;
                greenCards.remove(position);
            } catch (CannotInsertException e) {
                //System.out.println(e.getMessage());
            }
        }

        maxGreenLevel = max;
        max = LevelEnum.NONE;

        for (i = 0; i < purpleCardsToInsert; i++) {
            try {
                position = rand.nextInt(purpleCards.size());
                slots.addCard(cards[purpleCards.get(position)], rand.nextInt(3));
                max = LevelEnum.compare(cards[purpleCards.get(position)].getLevel(), max) > 0 ? cards[purpleCards.get(position)].getLevel() : max;
                purpleCardsInSlots++;
                purpleCards.remove(position);
            } catch (CannotInsertException e) {
                //System.out.println(e.getMessage());
            }
        }

        maxPurpleLevel = max;
        max = LevelEnum.NONE;

        for (i = 0; i < blueCardsToInsert; i++) {
            try {
                position = rand.nextInt(blueCards.size());
                slots.addCard(cards[blueCards.get(position)], rand.nextInt(3));
                max = LevelEnum.compare(cards[blueCards.get(position)].getLevel(), max) > 0 ? cards[blueCards.get(position)].getLevel() : max;
                blueCardsInSlots++;
                blueCards.remove(position);
            } catch (CannotInsertException e) {
                //System.out.println(e.getMessage());
            }
        }

        maxBlueLevel = max;
        max = LevelEnum.NONE;

        for (i = 0; i < yellowCardsToInsert; i++) {
            try {
                position = rand.nextInt(yellowCards.size());
                slots.addCard(cards[yellowCards.get(position)], rand.nextInt(3));
                max = LevelEnum.compare(cards[yellowCards.get(position)].getLevel(), max) > 0 ? cards[yellowCards.get(position)].getLevel() : max;
                yellowCardsInSlots++;
                yellowCards.remove(position);
            } catch (CannotInsertException e) {
                //System.out.println(e.getMessage());
            }
        }
        maxYellowLevel = max;

    }

    @Before
    public void readCards() {
        InputStreamReader reader;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(EnumMap.class, (InstanceCreator<EnumMap<?, ?>>) type -> new EnumMap<ResourceEnum, Integer>(ResourceEnum.class))
                .create();

        try {
            reader = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(cardPath)));
            cards = gson.fromJson(reader, DevelopmentCard[].class);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void canInsertTest() {
        DevelopmentSlots developmentSlots = new DevelopmentSlots();

        DevelopmentCard LVLONECardFirstSlot = cards[lvlOneRand()];

        boolean illegalArgument = false;

        try {
            assertFalse(developmentSlots.canInsert(null, 0));
            fail("expected exception not thrown!");
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        illegalArgument = false;

        try {
            assertFalse(developmentSlots.canInsert(LVLONECardFirstSlot, -1));
            fail("expected exception not thrown!");
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        illegalArgument = false;

        try {
            assertFalse(developmentSlots.canInsert(LVLONECardFirstSlot, 7));
            fail("expected exception not thrown!");
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        assertTrue(developmentSlots.canInsert(LVLONECardFirstSlot, 1));

        DevelopmentCard LVLNONECARD = new DevelopmentCard(TypeEnum.GREEN, LevelEnum.NONE);
        assertFalse(developmentSlots.canInsert(LVLNONECARD, 0));
    }

    @Test
    public void addCardTest() {

        DevelopmentSlots developmentSlots = new DevelopmentSlots();

        int firstSlot = 0;
        int secondSlot = 1;
        int thirdSlot = 2;

        DevelopmentCard LVLONECardFirstSlot;
        DevelopmentCard LVLTWOCardFirstSlot;
        DevelopmentCard LVLTHREECardFirstSlot;
        DevelopmentCard tooMuchLVLTHREECardFirstSlot;

        DevelopmentCard LVLONECardSecondSlot;
        DevelopmentCard LVLTHREECardSecondSlot;

        DevelopmentCard LVLTWOCardThirdSlot;

        LVLONECardFirstSlot = cards[lvlOneRand()];
        LVLTWOCardFirstSlot = cards[lvlTwoRand()];
        LVLTHREECardFirstSlot = cards[lvlThreeRand()];
        tooMuchLVLTHREECardFirstSlot = cards[lvlThreeRand()];

        LVLONECardSecondSlot = cards[lvlOneRand()];
        LVLTHREECardSecondSlot = cards[lvlThreeRand()];

        LVLTWOCardThirdSlot = cards[lvlTwoRand()];

        boolean illegalArgument = false;

        try {
            developmentSlots.addCard(null, 0);
            fail("expected exception not thrown!");
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        } catch (CannotInsertException ignored) {
            fail("unexpected exception not thrown!");
        }

        assertTrue(illegalArgument);

        illegalArgument = false;

        try {
            developmentSlots.addCard(LVLONECardFirstSlot, -1);
            fail("expected exception not thrown!");
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        } catch (CannotInsertException ignored) {
        }

        assertTrue(illegalArgument);

        illegalArgument = false;

        try {
            developmentSlots.addCard(LVLONECardFirstSlot, 7);
            fail("expected exception not thrown!");
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        } catch (CannotInsertException ignored) {
        }

        assertTrue(illegalArgument);

        try {
            developmentSlots.addCard(LVLONECardFirstSlot, firstSlot);
            developmentSlots.addCard(LVLTWOCardFirstSlot, firstSlot);
            developmentSlots.addCard(LVLTHREECardFirstSlot, firstSlot);
            developmentSlots.addCard(tooMuchLVLTHREECardFirstSlot, firstSlot);
            fail("expected exception not thrown!");
        } catch (CannotInsertException e) {
            assertEquals(e.getNewCardLevel(), LevelEnum.THREE);
            assertEquals(e.getSlotNumber(), firstSlot);
            System.out.println(e.getMessage());
        }

        try {
            developmentSlots.addCard(LVLONECardSecondSlot, secondSlot);
            developmentSlots.addCard(LVLTHREECardSecondSlot, secondSlot);
            fail("expected exception not thrown!");
        } catch (CannotInsertException e) {
            assertEquals(e.getNewCardLevel(), LevelEnum.THREE);
            assertEquals(e.getSlotNumber(), secondSlot);
        }

        try {
            developmentSlots.addCard(LVLTWOCardThirdSlot, thirdSlot);
            fail("expected exception not thrown!");
        } catch (CannotInsertException e) {
            assertEquals(e.getNewCardLevel(), LevelEnum.TWO);
            assertEquals(e.getSlotNumber(), thirdSlot);
        }

        List<DevelopmentCard> playerCards = developmentSlots.getCards();

        assertEquals(4, playerCards.size());

        assertEquals(LVLONECardFirstSlot, playerCards.get(0));
        assertEquals(LVLTWOCardFirstSlot, playerCards.get(1));
        assertEquals(LVLTHREECardFirstSlot, playerCards.get(2));

        assertEquals(LVLONECardSecondSlot, playerCards.get(3));
    }

    @Test
    public void getCardTypesCountTest() {
        List<DevelopmentCard> playerCards;

        randomInsert();

        playerCards = slots.getCards();

        playerCards.forEach(System.out::println);
        System.out.println(greenCardsInSlots);
        System.out.println(purpleCardsInSlots);
        System.out.println(blueCardsInSlots);
        System.out.println(yellowCardsInSlots);

        EnumMap<TypeEnum, Integer> typesNumberMap = slots.getCardTypesCount();

        assertEquals(greenCardsInSlots, (int) typesNumberMap.getOrDefault(TypeEnum.GREEN, 0));
        assertEquals(purpleCardsInSlots, (int) typesNumberMap.getOrDefault(TypeEnum.PURPLE, 0));
        assertEquals(blueCardsInSlots, (int) typesNumberMap.getOrDefault(TypeEnum.BLUE, 0));
        assertEquals(yellowCardsInSlots, (int) typesNumberMap.getOrDefault(TypeEnum.YELLOW, 0));
    }

    @Test
    public void getMaxLevelFromCardTypeTest() {
        randomInsert();
        List<DevelopmentCard> playerCards = slots.getCards();

        playerCards.forEach(System.out::println);
        System.out.println(greenCardsInSlots);
        System.out.println(purpleCardsInSlots);
        System.out.println(blueCardsInSlots);
        System.out.println(yellowCardsInSlots);

        assertEquals(maxGreenLevel, slots.getMaxLevelFromCardType(TypeEnum.GREEN));
        assertEquals(maxPurpleLevel, slots.getMaxLevelFromCardType(TypeEnum.PURPLE));
        assertEquals(maxBlueLevel, slots.getMaxLevelFromCardType(TypeEnum.BLUE));
        assertEquals(maxYellowLevel, slots.getMaxLevelFromCardType(TypeEnum.YELLOW));


    }
}