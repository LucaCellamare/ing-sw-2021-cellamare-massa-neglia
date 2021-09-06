package it.polimi.ingsw.server.Model.Requirements;

import it.polimi.ingsw.exceptions.CannotInsertException;
import it.polimi.ingsw.server.Model.Cards.DevelopmentCard;
import it.polimi.ingsw.server.Model.Cards.ProductionPower;
import it.polimi.ingsw.server.Model.Enums.LevelEnum;
import it.polimi.ingsw.server.Model.Enums.TypeEnum;
import it.polimi.ingsw.server.Model.LeaderCardDeck;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.PlayerStub;
import it.polimi.ingsw.server.Model.Requirements.CardRequirement;
import it.polimi.ingsw.server.Model.Requirements.ResourceRequirement;
import org.junit.Test;

import static org.junit.Assert.*;

public class CardRequirementTest {
    LeaderCardDeck leaderCardDeck = new LeaderCardDeck();
    TypeEnum typeOnlyTypeReq = TypeEnum.YELLOW;
    LevelEnum levelOnlyLevelReq = LevelEnum.TWO;
    TypeEnum typeTypeAndLevelReq = TypeEnum.PURPLE;
    LevelEnum levelTypeAndLevelReq = LevelEnum.THREE;

    CardRequirement onlyType = new CardRequirement(typeOnlyTypeReq, LevelEnum.NONE, 1);
    CardRequirement onlyLevel = new CardRequirement(TypeEnum.NONE, levelOnlyLevelReq, 1);
    CardRequirement typeAndLevel = new CardRequirement(typeTypeAndLevelReq, levelTypeAndLevelReq, 1);

    @Test
    public void getCardTypeTest() {
        assertEquals(typeOnlyTypeReq, onlyType.getCardType());
        assertEquals(typeTypeAndLevelReq, typeAndLevel.getCardType());
    }

    @Test
    public void getLevelTest() {
        assertEquals(levelOnlyLevelReq, onlyLevel.getLevel());
        assertEquals(levelTypeAndLevelReq, typeAndLevel.getLevel());
    }

    @Test
    public void getQuantityTest() {
        assertEquals(1, onlyType.getQuantity());
        assertEquals(1, onlyLevel.getQuantity());
        assertEquals(1, typeAndLevel.getQuantity());
    }

    @Test
    public void checkRequirementTest() {
        String firstPath = "Masters of Renaissance_Cards_FRONT_3mmBleed_1-1";
        DevelopmentCard firstOnlyTypeOK = new DevelopmentCard(firstPath, 1,
                1,
                new ResourceRequirement[]{new ResourceRequirement()},
                new ProductionPower(),
                typeOnlyTypeReq,
                LevelEnum.ONE
        );
        String sixthPath = "Masters of Renaissance_Cards_FRONT_3mmBleed_1-6";

        DevelopmentCard secondOnlyTypeOK = new DevelopmentCard(sixthPath, 6,
                7,
                new ResourceRequirement[]{new ResourceRequirement()},
                new ProductionPower(),
                typeOnlyTypeReq,
                LevelEnum.TWO
        );
        String secondPath = "Masters of Renaissance_Cards_FRONT_3mmBleed_1-6";

        DevelopmentCard onlyLevelOK = new DevelopmentCard(secondPath, 2,
                1,
                new ResourceRequirement[]{new ResourceRequirement()},
                new ProductionPower(),
                TypeEnum.GREEN,
                levelOnlyLevelReq);
        String thirdPath = "Masters of Renaissance_Cards_FRONT_3mmBleed_1-3";

        DevelopmentCard uselessLVLONECard = new DevelopmentCard(thirdPath, 3,
                3,
                new ResourceRequirement[]{new ResourceRequirement()},
                new ProductionPower(),
                TypeEnum.BLUE,
                LevelEnum.ONE);

        String fourthPath = "Masters of Renaissance_Cards_FRONT_3mmBleed_1-4";

        DevelopmentCard typeAnLevelOK = new DevelopmentCard(fourthPath, 4,
                4,
                new ResourceRequirement[]{new ResourceRequirement()},
                new ProductionPower(),
                TypeEnum.PURPLE,
                LevelEnum.THREE);

        String fifthPath = "Masters of Renaissance_Cards_FRONT_3mmBleed_1-5";

        DevelopmentCard uselessLVLTWOCard = new DevelopmentCard(fifthPath, 5,
                5,
                new ResourceRequirement[]{new ResourceRequirement()},
                new ProductionPower(),
                TypeEnum.PURPLE,
                LevelEnum.TWO);

        Player playerWithOnlyType = new PlayerStub();
        Player playerWithOnlyLevel = new PlayerStub();
        Player playerWithTypeAndLevel = new PlayerStub();

        try {
            playerWithOnlyType.getSlots().addCard(firstOnlyTypeOK, 0);
            playerWithOnlyType.getSlots().addCard(secondOnlyTypeOK, 0);

            playerWithOnlyLevel.getSlots().addCard(uselessLVLONECard, 1);
            playerWithOnlyLevel.getSlots().addCard(onlyLevelOK, 1);

            playerWithTypeAndLevel.getSlots().addCard(uselessLVLONECard, 2);
            playerWithTypeAndLevel.getSlots().addCard(uselessLVLTWOCard, 2);
            playerWithTypeAndLevel.getSlots().addCard(typeAnLevelOK, 2);
        } catch (CannotInsertException e) {
            e.printStackTrace();
        }

        boolean illegalArgument = false;

        try {
            onlyType.checkRequirement(null);
        } catch (IllegalArgumentException e) {
            illegalArgument = true;
        }

        assertTrue(illegalArgument);

        assertTrue(onlyType.checkRequirement(playerWithOnlyType));
        assertTrue(onlyLevel.checkRequirement(playerWithOnlyType));
        assertFalse(typeAndLevel.checkRequirement(playerWithOnlyType));

        assertFalse(onlyType.checkRequirement(playerWithOnlyLevel));
        assertTrue(onlyLevel.checkRequirement(playerWithOnlyLevel));
        assertFalse(typeAndLevel.checkRequirement(playerWithOnlyLevel));

        assertFalse(onlyType.checkRequirement(playerWithTypeAndLevel));
        assertTrue(onlyLevel.checkRequirement(playerWithTypeAndLevel));
        assertTrue(typeAndLevel.checkRequirement(playerWithTypeAndLevel));


        System.out.println(onlyType);
        System.out.println(onlyLevel);
        System.out.println(typeAndLevel);

    }
}