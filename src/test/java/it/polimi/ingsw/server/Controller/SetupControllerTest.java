package it.polimi.ingsw.server.Controller;

import it.polimi.ingsw.server.Controller.Handlers.WarehouseHandler;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.EnumMap;

import static org.junit.Assert.*;

public class SetupControllerTest {
    LeaderCardDeck leaderCardDeck = new LeaderCardDeck();
    Boolean[] readyPlayer = new Boolean[3];

    Game singlePlayerGame = new SinglePlayerGame();
    Game multiPlayerGame = new MultiplayerGame(3);

    Player singlePlayer = new Player("testPlayer", true);
    Player multiPlayerOne = new Player("testPlayerOne", true);
    Player multiPlayerTwo = new Player("testPlayerTwo", false);
    Player multiPlayerThree = new Player("testPlayerThree", false);

    VirtualViewStub singlePlayerVirtualView = new VirtualViewStub(singlePlayerGame);
    VirtualViewStub multiPlayerVirtualView = new VirtualViewStub(multiPlayerGame, this);

    SetupController singlePlayerSetupController = new SetupController(singlePlayerVirtualView, singlePlayerGame);
    SetupController multiPlayerSetupController = new SetupController(multiPlayerVirtualView, multiPlayerGame);

    @Before
    public void setUpPlayers() {
        singlePlayerGame.addPlayer(0, singlePlayer);

        multiPlayerGame.addPlayer(0, multiPlayerOne);
        multiPlayerGame.addPlayer(1, multiPlayerTwo);
        multiPlayerGame.addPlayer(2, multiPlayerThree);

        Arrays.fill(readyPlayer, false);
    }

    @Test
    public void pickLeaderCardTest() {

        singlePlayerSetupController.pickLeaderCard(0);

        multiPlayerSetupController.pickLeaderCard(0);
        multiPlayerSetupController.pickLeaderCard(1);
    }

    @Test
    public void insertLeaderCard() {
        int firstLeaderCard = 3, secondLeaderCard = 0;

        singlePlayerSetupController.insertLeaderCard(0, firstLeaderCard, secondLeaderCard);


        assertEquals(singlePlayerGame.getPlayers().get(0).getLeaderCardsToChoose().get(firstLeaderCard), singlePlayer.getLeaderCards().get(0));
        assertEquals(singlePlayerGame.getPlayers().get(0).getLeaderCardsToChoose().get(secondLeaderCard), singlePlayer.getLeaderCards().get(1));
    }

    @Test
    public void pickInitialResources() {
        singlePlayerSetupController.pickInitialResources(0);

        multiPlayerSetupController.pickInitialResources(0);
        multiPlayerSetupController.pickInitialResources(2);
    }

    @Test
    public void setInitialResources() {
        EnumMap<ResourceEnum, Integer> toInsert = new EnumMap<>(ResourceEnum.class) {{
            put(ResourceEnum.COIN, 1);
            put(ResourceEnum.STONE, 2);
        }};

        EnumMap<ResourceEnum, Integer> toInsertCopy = new EnumMap<>(ResourceEnum.class) {{
            put(ResourceEnum.COIN, 1);
            put(ResourceEnum.STONE, 2);
        }};

        singlePlayerSetupController.setInitialResources(toInsert);

        assertEquals(toInsertCopy, WarehouseHandler.getToInsert());
    }

    @Test
    public void setReadyPlayer() {
        singlePlayerSetupController.setReadyPlayer(0);
        assertTrue(singlePlayerSetupController.checkAllPLayerReady());

        multiPlayerSetupController.setReadyPlayer(0);
        multiPlayerSetupController.setReadyPlayer(2);

        assertFalse(multiPlayerSetupController.checkAllPLayerReady());

        multiPlayerSetupController.setReadyPlayer(1);

        assertTrue(multiPlayerSetupController.checkAllPLayerReady());
    }

    @Test
    public void checkFinish() {
        singlePlayerSetupController.checkFinish(0);

        assertTrue(singlePlayerVirtualView.checkRunGame());

        readyPlayer[1] = true;
        multiPlayerSetupController.checkFinish(1);
        readyPlayer[2] = true;
        multiPlayerSetupController.checkFinish(2);

        assertFalse(multiPlayerVirtualView.checkRunGame());

        readyPlayer[0] = true;
        multiPlayerSetupController.checkFinish(0);

        assertTrue(multiPlayerVirtualView.checkRunGame());
    }
}