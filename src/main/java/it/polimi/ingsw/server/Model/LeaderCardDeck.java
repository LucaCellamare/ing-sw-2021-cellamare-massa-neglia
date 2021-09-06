package it.polimi.ingsw.server.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.server.Model.Cards.LeaderCard;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.LeaderAbilities.*;
import it.polimi.ingsw.server.Model.Requirements.CardRequirement;
import it.polimi.ingsw.server.Model.Requirements.Requirement;
import it.polimi.ingsw.server.Model.Requirements.ResourceRequirement;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class LeaderCardDeck {
    /**
     * Collection of leader cards that has been shuffled, used to give four cards to every player randomly
     */
    private static ArrayDeque<LeaderCard> shuffledCard;
    /**
     * Ordered leader cards as read from file
     */
    private final LeaderCard[] leaderCards; // read them from file

    /**
     * Class constructor
     */
    public LeaderCardDeck() {
        shuffledCard = new ArrayDeque<>();
        leaderCards = readLeaderCards();

        // TODO switch back to shuffleLeaderCards after testing

//        getLeaderCards();
        shuffleLeaderCards();

        // TODO switch back to shuffleLeaderCards after testing
    }

    /**
     * Picks four card from the shuffled deck
     *
     * @return four random leader cards
     */
    public static ArrayList<LeaderCard> pick4Card() {
        ArrayList<LeaderCard> topCards = new ArrayList<>(4);

        for (int i = 0; i < 4; i++) {
            topCards.add(shuffledCard.pop());
        }

        return topCards;
    }

    /**
     * Shuffles the leader cards
     */
    private void shuffleLeaderCards() {
        ArrayList<LeaderCard> leaderCardsList = new ArrayList<>(Arrays.asList(leaderCards));

        int i, random;
        Random rand = new Random();
        for (i = 0; i < leaderCards.length; i++) {
            random = rand.nextInt(leaderCardsList.size());
            shuffledCard.add(leaderCardsList.get(random));
            leaderCardsList.remove(random);
        }
    }

    /**
     * used for testing and debug only, gives the first player all four leader cards of one typology by changing the sublist limits
     */
    private void getLeaderCards() {
        shuffledCard.addAll(Arrays.asList(leaderCards).subList(4, leaderCards.length));
        shuffledCard.addAll(Arrays.asList(leaderCards).subList(0, 4));
    }

    /**
     * Reads the leader cards from file
     *
     * @return the ordered array of leader cards as read from file
     */
    private LeaderCard[] readLeaderCards() {
        LeaderCard[] ret = null;

        String path = "json/LeaderCards.json";

        RuntimeTypeAdapterFactory<Requirement> cardFactory = RuntimeTypeAdapterFactory
                .of(Requirement.class, "reqType")
                .registerSubtype(ResourceRequirement.class, "resource")
                .registerSubtype(CardRequirement.class, "card");

        RuntimeTypeAdapterFactory<SpecialAbility> abilityFactory = RuntimeTypeAdapterFactory
                .of(SpecialAbility.class, "abilityType")
                .registerSubtype(DiscountAbility.class, "discount")
                .registerSubtype(StorageAbility.class, "storage")
                .registerSubtype(ProductionAbility.class, "production")
                .registerSubtype(WhiteMarbleAbility.class, "whiteMarble");

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(cardFactory)
                .registerTypeAdapterFactory(abilityFactory)
                .registerTypeAdapter(EnumMap.class, (InstanceCreator<EnumMap<?, ?>>) type -> new EnumMap<ResourceEnum, Integer>(ResourceEnum.class))
                .create();

        InputStreamReader reader;

        try {
            reader = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path)));
            ret = gson.fromJson(reader, LeaderCard[].class);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
