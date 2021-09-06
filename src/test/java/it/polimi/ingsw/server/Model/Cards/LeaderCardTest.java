package it.polimi.ingsw.server.Model.Cards;

import it.polimi.ingsw.exceptions.CannotActivateAbilityException;
import it.polimi.ingsw.exceptions.CannotInsertException;
import it.polimi.ingsw.exceptions.MaxLeaderCardsException;
import it.polimi.ingsw.server.Model.Enums.LevelEnum;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Enums.TypeEnum;
import it.polimi.ingsw.server.Model.LeaderAbilities.ProductionAbility;
import it.polimi.ingsw.server.Model.LeaderAbilities.SpecialAbility;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.PlayerStub;
import it.polimi.ingsw.server.Model.Requirements.CardRequirement;
import it.polimi.ingsw.server.Model.Requirements.Requirement;
import it.polimi.ingsw.server.Model.Requirements.ResourceRequirement;
import org.junit.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;


public class LeaderCardTest {
    String pathFirst = "Masters of Renaissance_Cards_FRONT_3mmBleed_1-49";
    int activableCardId = 49;
    int activableCardVictoryPoints;
    CardRequirement firstActivableCardReq = new CardRequirement(TypeEnum.NONE, LevelEnum.TWO, 2);
    CardRequirement secondActivableCardReq = new CardRequirement(TypeEnum.BLUE, LevelEnum.NONE, 1);
    CardRequirement thirdActivableCardReq = new CardRequirement(TypeEnum.GREEN, LevelEnum.ONE, 1);
    ResourceRequirement firstActivableResReq = new ResourceRequirement(ResourceEnum.SHIELD, 2);

    Requirement[] activableLeaderRequirement = new Requirement[]{firstActivableCardReq, secondActivableCardReq, thirdActivableCardReq, firstActivableResReq};

    ProductionPower leaderPower = new ProductionPower(
            new ResourceRequirement[]{
                    new ResourceRequirement(ResourceEnum.COIN, 1)
            },
            new EnumMap<>(ResourceEnum.class) {{
                put(ResourceEnum.FAITH, 2);
            }});
    SpecialAbility leaderAbility = new ProductionAbility(leaderPower);

    LeaderCard activableCard = new LeaderCard(pathFirst, activableCardId, activableCardVictoryPoints, activableLeaderRequirement, leaderAbility);

    CardRequirement firstNotActivableCardReq = new CardRequirement(TypeEnum.NONE, LevelEnum.THREE, 1);

    Requirement[] firstNotActivableRequirement = new Requirement[]{firstNotActivableCardReq};

    CardRequirement secondNotActivableCardReq = new CardRequirement(TypeEnum.GREEN, LevelEnum.NONE, 3);

    Requirement[] secondNotActivableRequirement = new Requirement[]{secondNotActivableCardReq};

    String pathSecond = "Masters of Renaissance_Cards_FRONT_3mmBleed_1-50";
    String pathThird = "Masters of Renaissance_Cards_FRONT_3mmBleed_1-51";
    int firstNotActivableCardId = 50;

    LeaderCard firstNotActivableCard = new LeaderCard(pathSecond, 50, 5, firstNotActivableRequirement, leaderAbility);

    LeaderCard secondNotActivableCard = new LeaderCard(pathThird, 51, 6, secondNotActivableRequirement, leaderAbility);

    private Player createPlayer() {
        PlayerStub p;
//        DevelopmentSlots slots = new DevelopmentSlots();
//
//        try {
//            slots.addCard(new DevelopmentCard(TypeEnum.GREEN, LevelEnum.ONE), 2);
//            slots.addCard(new DevelopmentCard(TypeEnum.YELLOW, LevelEnum.ONE), 0);
//            slots.addCard(new DevelopmentCard(TypeEnum.BLUE, LevelEnum.TWO), 0);
//            slots.addCard(new DevelopmentCard(TypeEnum.PURPLE, LevelEnum.TWO), 2);
//        } catch (CannotInsertException e) {
//            e.printStackTrace();
//        }

        p = new PlayerStub();

        try {
            p.getSlots().addCard(new DevelopmentCard(TypeEnum.GREEN, LevelEnum.ONE), 2);
            p.getSlots().addCard(new DevelopmentCard(TypeEnum.YELLOW, LevelEnum.ONE), 0);
            p.getSlots().addCard(new DevelopmentCard(TypeEnum.BLUE, LevelEnum.TWO), 0);
            p.getSlots().addCard(new DevelopmentCard(TypeEnum.PURPLE, LevelEnum.TWO), 2);
        } catch (CannotInsertException ignored) {
        }

        p.insertResources(new HashMap<>() {{
            put(ResourceEnum.COIN, 2);
            put(ResourceEnum.SHIELD, 5);
        }});

        return p;
    }

    @Test
    public void canBeActivatedTest() {
        Player p = createPlayer();

        assertTrue(activableCard.canBeActivated(p));
        assertFalse(firstNotActivableCard.canBeActivated(p));
        assertFalse(secondNotActivableCard.canBeActivated(p));

        boolean illegalArgument = false;

        try {
            activableCard.canBeActivated(null);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);
    }

    @Test
    public void getIdTest() {
        assertEquals(firstNotActivableCardId, firstNotActivableCard.getId());
    }

    @Test
    public void getVictoryPointsTest() {
        assertEquals(activableCardVictoryPoints, activableCard.getVictoryPoints());
    }

    @Test
    public void getRequirementsTest() {
        assertArrayEquals(secondNotActivableRequirement, secondNotActivableCard.getRequirements());
    }

    @Test
    public void getAbilityTest() {
        assertEquals(leaderAbility, activableCard.getAbility());
    }

    @Test
    public void activateTest() {
        boolean illegalArgument = false;
        Player p = createPlayer();
        try {
            p.setLeaderCard(activableCard);
            p.setLeaderCard(firstNotActivableCard);
            p.activateLeaderCard(0);
            p.activateLeaderCard(1);
            fail("expected exception not thrown");
        } catch (CannotActivateAbilityException e) {
            ArrayList<Requirement> notFulfilled = new ArrayList<>();
            notFulfilled.add(firstNotActivableCardReq);
            assertEquals(notFulfilled, e.getMissingRequirements());
        } catch (MaxLeaderCardsException e) {
            fail();
        }
        try {
            p.activateLeaderCard(2);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        } catch (CannotActivateAbilityException ignored) {
        }
        List<ProductionPower> playerProductionPowers = p.getLeaderProductionPowers();

        assertTrue(illegalArgument);
        assertEquals(playerProductionPowers.get(playerProductionPowers.size() - 1), leaderPower);
        assertTrue(activableCard.isActive());

        illegalArgument = false;

        try {
            activableCard.activate(null);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);
    }

//    LeaderCard[] leaderCards;
//    private void readCards(){
//        String path = "src/main/resources/json/LeaderCards.json";
//
//        RuntimeTypeAdapterFactory<Requirement> cardFactory = RuntimeTypeAdapterFactory
//                .of(Requirement.class, "reqType")
//                .registerSubtype(ResourceRequirement.class, "resource")
//                .registerSubtype(CardRequirement.class, "card");
//
//        RuntimeTypeAdapterFactory<SpecialAbility> abilityFactory = RuntimeTypeAdapterFactory
//                .of(SpecialAbility.class, "abilityType")
//                .registerSubtype(DiscountAbility.class, "discount")
//                .registerSubtype(StorageAbility.class, "storage")
//                .registerSubtype(ProductionAbility.class, "production")
//                .registerSubtype(WhiteMarbleAbility.class, "whiteMarble");
//
//        Gson gson = new GsonBuilder().registerTypeAdapterFactory(cardFactory).registerTypeAdapterFactory(abilityFactory).create();
//
//        FileReader reader;
//
//        try {
//            reader = new FileReader(path);
//            leaderCards = gson.fromJson(reader, LeaderCard[].class);
//            reader.close();
////            leaderCards[(int) Arrays.stream(leaderCards).filter(Objects::nonNull).count()] = newCard;
////            writer = new FileWriter(path);
////            gson.toJson(leaderCards, writer);
////            writer.close();
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//    }
}