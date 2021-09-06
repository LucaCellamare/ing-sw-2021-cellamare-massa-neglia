package it.polimi.ingsw.server.Model;

import it.polimi.ingsw.exceptions.CannotActivateAbilityException;
import it.polimi.ingsw.exceptions.CannotInsertException;
import it.polimi.ingsw.exceptions.MaxLeaderCardsException;
import it.polimi.ingsw.messages.AskName;
import it.polimi.ingsw.messages.VaticanReport;
import it.polimi.ingsw.server.Controller.Controller;
import it.polimi.ingsw.server.Controller.Handlers.FaithTrackHandler;
import it.polimi.ingsw.server.Model.Cards.DevelopmentCard;
import it.polimi.ingsw.server.Model.Cards.LeaderCard;
import it.polimi.ingsw.server.Model.Cards.ProductionPower;
import it.polimi.ingsw.server.Model.Enums.LevelEnum;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Enums.TypeEnum;
import it.polimi.ingsw.server.Model.FaithTrack.FaithTrack;
import it.polimi.ingsw.server.Model.LeaderAbilities.ProductionAbility;
import it.polimi.ingsw.server.Model.LeaderAbilities.WhiteMarbleAbility;
import it.polimi.ingsw.server.Model.Requirements.CardRequirement;
import it.polimi.ingsw.server.Model.Requirements.Requirement;
import it.polimi.ingsw.server.Model.Requirements.ResourceRequirement;
import org.junit.Test;

import java.util.EnumMap;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerTest {
    LeaderCardDeck leaderCardDeck = new LeaderCardDeck();
    Player p = new Player("testPlayer", true);

    @Test
    public void createBaseProductionPowerTest() {
        ProductionPower baseProductionPower = new ProductionPower(
                new ResourceRequirement[]{
                        new ResourceRequirement(ResourceEnum.JOLLY, 1),
                        new ResourceRequirement(ResourceEnum.JOLLY, 1)
                },
                new EnumMap<>(ResourceEnum.class) {{
                    put(ResourceEnum.JOLLY, 1);
                }});

        assertEquals(baseProductionPower, p.getBaseProductionPower());
    }

    @Test
    public void setLeaderCardTest() {
        boolean maxReached = false;
        String pathFirst = "Masters of Renaissance_Cards_FRONT_3mmBleed_1-49";
        LeaderCard firstCard = new LeaderCard(pathFirst, 49,
                5,
                new Requirement[]{
                        new ResourceRequirement(ResourceEnum.COIN, 4),
                        new CardRequirement(TypeEnum.GREEN, LevelEnum.NONE, 1)
                },
                new ProductionAbility(p.getBaseProductionPower())
        );
        String pathSecond = "Masters of Renaissance_Cards_FRONT_3mmBleed_1-50";
        LeaderCard secondCard = new LeaderCard(pathSecond, 50,
                10,
                new Requirement[]{
                        new CardRequirement(TypeEnum.NONE, LevelEnum.TWO, 2),
                        new ResourceRequirement(ResourceEnum.STONE, 1)
                },
                new ProductionAbility(
                        new ProductionPower(
                                new ResourceRequirement[]{
                                        new ResourceRequirement(ResourceEnum.COIN, 1)
                                },
                                new EnumMap<>(ResourceEnum.class) {{
                                    put(ResourceEnum.FAITH, 2);
                                }}
                        )
                )
        );

        LeaderCard uselessCard = new LeaderCard();

        try {
            p.setLeaderCard(firstCard);
            assertEquals(1, p.getLeaderCards().size());
            assertEquals(firstCard, p.getLeaderCards().get(0));

            p.setLeaderCard(secondCard);
            assertEquals(2, p.getLeaderCards().size());
            assertEquals(secondCard, p.getLeaderCards().get(1));

            p.setLeaderCard(uselessCard);
            fail("expected exception not thrown");
        } catch (MaxLeaderCardsException ignored) {
            maxReached = true;
        }

        assertTrue(maxReached);

    }

    @Test
    public void removeLeaderCardTest() {
        int positionLeaderCardToRemove = 0;
        int positionCannotRemove = 3;
        boolean cannotRemove = false, illegalArgument = false;
        String pathFirst = "Masters of Renaissance_Cards_FRONT_3mmBleed_1-49";
        LeaderCard firstCard = new LeaderCard(pathFirst, 49,
                5,
                new Requirement[]{
                        new ResourceRequirement(ResourceEnum.COIN, 4),
                        new CardRequirement(TypeEnum.GREEN, LevelEnum.NONE, 1)
                },
                new ProductionAbility(p.getBaseProductionPower())
        );
        String pathSecond = "Masters of Renaissance_Cards_FRONT_3mmBleed_1-50";
        LeaderCard secondCard = new LeaderCard(pathSecond, 50,
                10,
                new Requirement[]{
                        new CardRequirement(TypeEnum.NONE, LevelEnum.TWO, 2),
                        new ResourceRequirement(ResourceEnum.STONE, 1)
                },
                new ProductionAbility(
                        new ProductionPower(
                                new ResourceRequirement[]{
                                        new ResourceRequirement(ResourceEnum.COIN, 1)
                                },
                                new EnumMap<>(ResourceEnum.class) {{
                                    put(ResourceEnum.FAITH, 2);
                                }}
                        )
                )
        );

        try {
            p.setLeaderCard(firstCard);
            p.setLeaderCard(secondCard);
        } catch (MaxLeaderCardsException e) {
            fail("unexpected exception thrown");
        }

        try {
            p.removeLeaderCard(-1);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        illegalArgument = false;

        try {
            p.removeLeaderCard(6);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        try {
            p.removeLeaderCard(positionLeaderCardToRemove);
            p.removeLeaderCard(positionCannotRemove);
        } catch (IllegalArgumentException e) {
            cannotRemove = true;
        }
        assertEquals(1, p.getLeaderCards().size());
        assertEquals(secondCard, p.getLeaderCards().get(0));
        assertTrue(cannotRemove);
    }

    @Test
    public void activateLeaderCardTest() {
        ProductionPower firstCardProdPower = new ProductionPower(
                new ResourceRequirement[]{
                        new ResourceRequirement(ResourceEnum.COIN, 1)
                },
                new EnumMap<>(ResourceEnum.class) {{
                    put(ResourceEnum.FAITH, 2);
                }}
        );

        Requirement secondCardMissingRequirement = new CardRequirement(TypeEnum.NONE, LevelEnum.TWO, 2);
        String pathFirst = "Masters of Renaissance_Cards_FRONT_3mmBleed_1-49";
        LeaderCard firstCard = new LeaderCard(pathFirst, 49,
                5,
                new Requirement[]{
                        new ResourceRequirement(ResourceEnum.COIN, 4),
                        new CardRequirement(TypeEnum.GREEN, LevelEnum.NONE, 1)
                },
                new ProductionAbility(firstCardProdPower)
        );
        String pathSecond = "Masters of Renaissance_Cards_FRONT_3mmBleed_1-50";
        LeaderCard secondCard = new LeaderCard(pathSecond, 50,
                10,
                new Requirement[]{
                        secondCardMissingRequirement,
                        new ResourceRequirement(ResourceEnum.STONE, 1)
                },
                new WhiteMarbleAbility(ResourceEnum.COIN)
        );

        try {
            p.setLeaderCard(firstCard);
            p.setLeaderCard(secondCard);
        } catch (MaxLeaderCardsException ignored) {
            fail("unexpected exception thrown");
        }

        p.getWarehouse().insert(ResourceEnum.STONE, 1, 0);
        p.getWarehouse().insert(ResourceEnum.COIN, 2, 1);
        p.getStrongbox().insertResource(ResourceEnum.COIN, 2);
        String firstPath = "Masters of Renaissance_Cards_FRONT_3mmBleed_1-1";

        try {
            p.getSlots().addCard(new DevelopmentCard(firstPath, 1,
                    5,
                    new ResourceRequirement[]{new ResourceRequirement()},
                    new ProductionPower(
                            new ResourceRequirement[]{new ResourceRequirement(ResourceEnum.COIN, 1)},
                            new EnumMap<>(ResourceEnum.class) {{
                                put(ResourceEnum.FAITH, 1);
                            }}),
                    TypeEnum.GREEN,
                    LevelEnum.ONE
            ), 0);
        } catch (CannotInsertException e) {
            fail("unexpected exception thrown");
        }

        boolean illegalArgument = false;

        try {
            p.activateLeaderCard(-1);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        } catch (CannotActivateAbilityException ignored) {
        }

        assertTrue(illegalArgument);

        illegalArgument = false;

        try {
            p.activateLeaderCard(5);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        } catch (CannotActivateAbilityException ignored) {
        }

        assertTrue(illegalArgument);

        try {
            p.activateLeaderCard(0);
            p.activateLeaderCard(1);
            fail("expected exception not thrown");
        } catch (CannotActivateAbilityException e) {
            List<Requirement> missingRequirements = e.getMissingRequirements();
            assertFalse(e.isAlreadyActivated());
            assertEquals(1, missingRequirements.size());
            assertEquals(secondCardMissingRequirement, missingRequirements.get(0));
        }

        assertEquals(firstCardProdPower, p.getLeaderProductionPowers().get(0));
        assertEquals(0, p.getLeaderCardPositionById(49));

        try {
            p.activateLeaderCard(0);
        } catch (CannotActivateAbilityException e) {
            assertTrue(e.isAlreadyActivated());
        }

        illegalArgument = false;

        try {
            p.addLeaderProductionPower(null);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        illegalArgument = false;

        WhiteMarbleAbility abnormalAbility = new WhiteMarbleAbility(ResourceEnum.NONE);

        try {
            p.addWhiteMarbleLeaderAbility(abnormalAbility.getResource());
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        illegalArgument = false;

        try {
            p.addWhiteMarbleLeaderAbility(null);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        illegalArgument = false;

        try {
            p.addDiscountLeaderAbility(ResourceEnum.NONE);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        illegalArgument = false;

        try {
            p.addDiscountLeaderAbility(null);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);
    }

    @Test
    public void updateDevProductionPowerTest() {
        int slotWhereToInsert = 2;
        int slotWhereCantInsert = 3;

        boolean illegalPositionCantInsert = false;

        ProductionPower firstCardProdPower = new ProductionPower(
                new ResourceRequirement[]{
                        new ResourceRequirement(ResourceEnum.COIN, 1),
                        new ResourceRequirement(ResourceEnum.SHIELD, 1)
                },
                new EnumMap<>(ResourceEnum.class) {{
                    put(ResourceEnum.STONE, 2);
                }}
        );
        String firstPath = "Masters of Renaissance_Cards_FRONT_3mmBleed_1-1";

        DevelopmentCard newCard = new DevelopmentCard(firstPath, 1,
                5,
                new ResourceRequirement[]{new ResourceRequirement()},
                firstCardProdPower,
                TypeEnum.GREEN,
                LevelEnum.ONE
        );

        boolean illegalArgument = false;

        try {
            p.updateDevProductionPower(newCard.getProductionPower(), -1);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        illegalArgument = false;

        try {
            p.updateDevProductionPower(newCard.getProductionPower(), 8);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        illegalArgument = false;

        try {
            p.updateDevProductionPower(null, slotWhereToInsert);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        try {
            p.getSlots().addCard(newCard, slotWhereToInsert);
            p.updateDevProductionPower(newCard.getProductionPower(), slotWhereToInsert);
            p.updateDevProductionPower(newCard.getProductionPower(), slotWhereCantInsert);
        } catch (CannotInsertException e) {
            fail("unexpected exception thrown");
        } catch (IllegalArgumentException e) {
            illegalPositionCantInsert = true;
        }

        assertEquals(0, p.getDevProductionPowers().indexOf(firstCardProdPower));
        assertEquals(1, p.getAllProductionPowers().indexOf(firstCardProdPower));
        assertEquals(firstCardProdPower, p.getDevProductionPowers().get(0));
        assertTrue(illegalPositionCantInsert);
    }

    @Test
    public void isTerminatorTest() {
        FaithTrack playerFaithTrack = p.getFaithTrack();
        FaithTrackHandler.setController(new Controller(null) {
            @Override
            public void update(AskName e) {
            }

            @Override
            public void continueGameAfterDisconnection() {
            }

            @Override
            public void discardResource(int playerIndex, int quantity) {
            }

            @Override
            public void endTurn(int playerIndex) {
            }

            @Override
            public void update(VaticanReport e) {
            }
        });
        FaithTrackHandler.updatePlayerPosition(playerFaithTrack, 30);
        assertTrue(p.isTerminator());
    }

}