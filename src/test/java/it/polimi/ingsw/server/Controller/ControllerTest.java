package it.polimi.ingsw.server.Controller;

import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.server.Controller.Handlers.WarehouseHandler;
import it.polimi.ingsw.server.Model.*;
import it.polimi.ingsw.server.Model.Cards.LeaderCard;
import it.polimi.ingsw.server.Model.Enums.MarbleEnum;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Enums.TypeLineEnum;
import it.polimi.ingsw.server.Model.Table.ResourceMarket;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ControllerTest {
    LeaderCardDeck leaderCardDeck = new LeaderCardDeck();

    Player singlePlayer = new Player("testPlayer", true);
    Player multiPlayerOne = new Player("testPlayerOne", true);
    Player multiPlayerTwo = new Player("testPlayerTwo", false);
    Player multiPlayerThree = new Player("testPlayerThree", false);

    VirtualViewStub singlePlayerVirtualView = new VirtualViewStub();
    VirtualViewStub multiPlayerVirtualView = new VirtualViewStub();

    Controller singlePlayerController = new SinglePlayerController(singlePlayerVirtualView);
    Controller multiPlayerController = new MultiPlayerController(multiPlayerVirtualView, 3);


    Game singlePlayerGame = singlePlayerController.getGame();
    Game multiPlayerGame = multiPlayerController.getGame();

    @Before
    public void setup() {
        singlePlayerVirtualView.setGame(singlePlayerGame);
        multiPlayerVirtualView.setGame(multiPlayerGame);

        singlePlayerGame.addPlayer(0, singlePlayer);

        multiPlayerGame.addPlayer(0, multiPlayerOne);
        multiPlayerGame.addPlayer(1, multiPlayerTwo);
        multiPlayerGame.addPlayer(2, multiPlayerThree);
    }

    @Test
    public void testUpdateAskName() {
        Game singlePlayerGame = new SinglePlayerGame();
        VirtualViewStub askNameVW = new VirtualViewStub(singlePlayerGame);
        Controller askNameController = new SinglePlayerController(askNameVW);
        askNameVW.setGame(askNameController.getGame());

        askNameController.update(new AskName("testPlayerOne"));

        assertEquals(1, askNameController.getGame().getPlayers().size());
        assertEquals("testPlayerOne", askNameController.getGame().getPlayers().get(0).getNickname());
    }

    @Test
    public void testUpdateChooseLeaderCard() {
        int firstLeaderCard = 3, secondLeaderCard = 1;

        ChooseLeaderCard m = new ChooseLeaderCard(firstLeaderCard, secondLeaderCard);
        m.setSender(0);

        singlePlayerController.update(m);

        List<LeaderCard> chosenLeaderCard = new ArrayList<>();
        chosenLeaderCard.add(singlePlayer.getLeaderCardsToChoose().get(firstLeaderCard));
        chosenLeaderCard.add(singlePlayer.getLeaderCardsToChoose().get(secondLeaderCard));

        assertEquals(chosenLeaderCard, singlePlayer.getLeaderCards());
    }

    @Test
    public void testUpdateReconnectInitialResources() {
        multiPlayerController.update(new ReconnectInitialResources(2));
    }

    @Test
    public void testUpdateChooseInitialResources() {
        EnumMap<ResourceEnum, Integer> initialResources = new EnumMap<>(ResourceEnum.class);

        initialResources.put(ResourceEnum.COIN, 1);
        initialResources.put(ResourceEnum.STONE, 1);

        multiPlayerController.update(new ChooseInitialResources(initialResources));

        assertEquals(initialResources, WarehouseHandler.getToInsert());
    }

    @Test
    public void testUpdateResourceMarketAction() {
        ResourceMarketAction m = new ResourceMarketAction();
        m.setSender(0);
        singlePlayerController.update(m);
    }

    @Test
    public void testUpdateSelectedResourceFromMarket() {
        singlePlayerGame.setCurrentPlayer(0);

        ResourceMarket resourceMarket = singlePlayerGame.getTable().getResourceMarket();
        MarbleEnum[] singleChosenRes = new MarbleEnum[4];

        for (int i = 0; i < singleChosenRes.length; i++) {
            singleChosenRes[i] = resourceMarket.getMarbleFromCoordinates(2, i);
        }

        singlePlayerVirtualView.setChosenRes(singleChosenRes);

        SelectedResourceFromMarket singleM = new SelectedResourceFromMarket(TypeLineEnum.ROW, 2);
        singleM.setSender(0);
        singlePlayerController.update(singleM);

        resourceMarket = multiPlayerGame.getTable().getResourceMarket();
        MarbleEnum[] multiChosenRes = new MarbleEnum[3];

        for (int i = 0; i < multiChosenRes.length; i++) {
            multiChosenRes[i] = resourceMarket.getMarbleFromCoordinates(i, 3);
        }

        multiPlayerVirtualView.setChosenRes(multiChosenRes);

        multiPlayerGame.setCurrentPlayer(2);
        multiPlayerThree.addWhiteMarbleLeaderAbility(ResourceEnum.STONE);
        SelectedResourceFromMarket multiM = new SelectedResourceFromMarket(TypeLineEnum.COL, 3);
        multiM.setSender(2);
        multiPlayerController.update(multiM);
    }

}