package it.polimi.ingsw.server.Model.Table.CardMarket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import it.polimi.ingsw.server.Model.Cards.DevelopmentCard;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Enums.TypeEnum;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.*;

import static java.util.Arrays.asList;

public class CardMarket implements Serializable {


    /**
     * The constant DEV_CARDS_SIZE.
     */
    private static final int DEV_CARDS_SIZE = 48;
    /**
     * The constant ROW_SIZE.
     */
    private static final int ROW_SIZE = 3;

    /**
     * The constant COL_SIZE.
     */
    private static final int COL_SIZE = 4;

    /**
     * The index relative to every Card Color.
     */
    private final HashMap<TypeEnum, Integer> colorIndexes;

    /**
     * The Market Structure.
     */
    private final CardDeck[][] cardDeckGrid = new CardDeck[ROW_SIZE][COL_SIZE];


    /**
     * Class constructor
     * Note that the arrangement of the cards inside every CardDeck is random and is managed with CardDeck constructor.
     */
    public CardMarket() {

        this.colorIndexes = new HashMap<>();
        colorIndexes.put(TypeEnum.GREEN, 0);
        colorIndexes.put(TypeEnum.PURPLE, 1);
        colorIndexes.put(TypeEnum.BLUE, 2);
        colorIndexes.put(TypeEnum.YELLOW, 3);

        DevelopmentCard[] cardsToInsert = readCardsFile();
        List<List<Integer>> cardByColorList = new ArrayList<>();
        List<Integer> greenCards = new ArrayList<>(asList(0, 4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44));
        List<Integer> purpleCards = new ArrayList<>(asList(1, 5, 9, 13, 17, 21, 25, 29, 33, 37, 41, 45));
        List<Integer> blueCards = new ArrayList<>(asList(2, 6, 10, 14, 18, 22, 26, 30, 34, 38, 42, 46));
        List<Integer> yellowCards = new ArrayList<>(asList(3, 7, 11, 15, 19, 23, 27, 31, 35, 39, 43, 47));

        cardByColorList.add(greenCards);
        cardByColorList.add(purpleCards);
        cardByColorList.add(blueCards);
        cardByColorList.add(yellowCards);

        for (int i = 0; i < COL_SIZE; i++) {
            List<Integer> tmpList = cardByColorList.get(i);
            DevelopmentCard[] tmpArray = new DevelopmentCard[4];
            for (int j = 0; j < ROW_SIZE; j++) {
                tmpArray[0] = cardsToInsert[tmpList.get(j * 4)];
                tmpArray[1] = cardsToInsert[tmpList.get(j * 4 + 1)];
                tmpArray[2] = cardsToInsert[tmpList.get(j * 4 + 2)];
                tmpArray[3] = cardsToInsert[tmpList.get(j * 4 + 3)];
                CardDeck tmpDeck = new CardDeck(tmpArray);
//                System.out.println("size of deck ["+i+"]"+"["+j+"] is : " + tmpDeck.getSize());
                this.cardDeckGrid[j][i] = tmpDeck;
            }

        }

    }

    /**
     * Retrieves the ROW SIZE
     */
    public static int getRowSize() {
        return ROW_SIZE;
    }

    /**
     * Retrieves the COL SIZE
     */
    public static int getColSize() {
        return COL_SIZE;
    }

    /**
     * Retrieves the Development cards that needs to be inserted inside the CardMarket
     *
     * @return the Development Cards
     */
    private DevelopmentCard[] readCardsFile() {
        String cardPath = "json/DevelopmentCards.json";
        InputStreamReader reader;
        DevelopmentCard[] cards = new DevelopmentCard[DEV_CARDS_SIZE];

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
        return cards;
    }

    /**
     * Retrieves the CardMarket Structure in which the DevelopmentCards that can be bought are exposed
     *
     * @return the CardMarket Structure
     */
    public CardDeck[][] getMarketStructure() {
        CardDeck[][] deepCardMarketCopy = new CardDeck[ROW_SIZE][COL_SIZE];
        for (int i = 0; i < ROW_SIZE; i++) {
            deepCardMarketCopy[i] = Arrays.copyOf(this.cardDeckGrid[i], COL_SIZE);
            for (int j = 0; j < COL_SIZE; j++) {
                deepCardMarketCopy[i] = Arrays.copyOf(this.cardDeckGrid[i], COL_SIZE);
            }
        }
        return deepCardMarketCopy;
    }

    /**
     * Retrieves the index corresponding to the Card Color
     *
     * @param color the color for which to retrieve the index
     * @return the index of the Card Color
     */
    public int getIndexByColor(TypeEnum color) {
        return colorIndexes.get(color);
    }

    /**
     * Removes a card of the level specified and the color specified if present, otherwise do nothing
     *
     * @param typeEnum color of the card to be removed
     * @param level    level of the card to be removed
     */
    public void remove(TypeEnum typeEnum, int level) {
        if (getMarketStructure()[getIndexByColor(typeEnum)][level].getSize() > 0) {
            getMarketStructure()[getIndexByColor(typeEnum)][level].pop();
        }
    }

    /**
     * Gets all the decks in a specified column
     *
     * @param chosenColumn the chosen column
     * @return all the decks in the chosen column
     */
    private CardDeck[] getColumn(int chosenColumn) {
        CardDeck[] column = new CardDeck[cardDeckGrid.length];
        for (int i = 0; i < column.length; i++)
            column[i] = cardDeckGrid[i][chosenColumn];
        return column;
    }

    /**
     * Removes cards of a specified color from the market, starting from the lowest level
     *
     * @param quantity quantity of cards to discard
     * @param color    color of the cards to discard
     */
    public void removeCardsOfColor(int quantity, TypeEnum color) {
        int columnIndex = getIndexByColor(color);
        CardDeck[] column = getColumn(columnIndex);

        int cardsDiscarded = 0;
        int i = 0;

        while (i < ROW_SIZE && cardsDiscarded < quantity) {
            if (column[i].getSize() > 0) {
                column[i].pop();
                cardsDiscarded++;
            } else i++;
        }
    }

    /**
     * Checks if a column of the card market is empty
     *
     * @param chosenColumn column to be checked
     * @return true if the column is empty, false if not
     */
    public boolean emptyColumn(int chosenColumn) {
        CardDeck[] column = getColumn(chosenColumn);

        boolean empty = true;

        for (int i = 0; i < column.length && empty; i++) {
            if (column[i].getSize() != 0) {
                empty = false;
            }
        }

        return empty;
    }
}
