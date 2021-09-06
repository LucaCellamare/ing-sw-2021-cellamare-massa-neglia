package it.polimi.ingsw.server.Controller;

import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.server.Model.Enums.MarbleEnum;
import it.polimi.ingsw.server.Model.Enums.ResourceEnum;
import it.polimi.ingsw.server.Model.Game;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.SendMessageToClient;
import it.polimi.ingsw.server.VirtualView;
import java.nio.file.FileAlreadyExistsException;
import java.util.EnumMap;
import java.util.Arrays;

import static org.junit.Assert.*;

public class VirtualViewStub extends VirtualView {
    private Game game;
    private MarbleEnum[] chosenRes;


    private boolean runGame;

    private SetupControllerTest setupControllerTest;

    public VirtualViewStub() {
        super(new SendMessageToClient(new ServerStub()));
        runGame = false;
    }

    public VirtualViewStub(Game game) {
        super(new SendMessageToClient(new ServerStub()));
        this.game = game;
        runGame = false;
    }

    public VirtualViewStub(Game game, SetupControllerTest setupControllerTest) {
        super(new SendMessageToClient(new ServerStub()));
        this.game = game;
        this.setupControllerTest = setupControllerTest;
        runGame = false;
    }

    public void update(int playerIndex, Message updateMsg) {
        Player player = game.getPlayers().get(playerIndex);
        if (updateMsg instanceof ChooseLeaderCard) {
            assertEquals(player.getLeaderCardsToChoose(), ((ChooseLeaderCard) updateMsg).getToChoose());
        } else if (updateMsg instanceof ChooseInitialResources) {
            ChooseInitialResources msg = (ChooseInitialResources) updateMsg;
            assertEquals(player.getFaithTrack().getPlayerPosition(), msg.getNFaithPoints());
            int initialResources;
            switch (playerIndex) {
                case 0:
                    initialResources = 0;
                    break;
                case 1:
                case 2:
                    initialResources = 1;
                    break;
                case 3:
                    initialResources = 2;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + playerIndex);
            }
            assertEquals(initialResources, msg.getNResources());
            assertTrue(msg.getWarehouse().isEmpty());
        } else if (updateMsg instanceof SetupComplete) {
            SetupComplete msg = (SetupComplete) updateMsg;

            assertEquals(game.getPlayers().size(), msg.getTotal());

            assertEquals(Arrays.stream(setupControllerTest.readyPlayer).filter(x -> x).count(), msg.getnReady());

        } else if (updateMsg instanceof ResourceMarketAction) {
            ResourceMarketAction msg = (ResourceMarketAction) updateMsg;
            MarbleEnum[][] marketStructure = game.getTable().getResourceMarket().getMarketStructure();
            assertArrayEquals(marketStructure, msg.getResourceMarketStructure());
        } else if (updateMsg instanceof SelectedResourceFromMarket) {
            SelectedResourceFromMarket msg = (SelectedResourceFromMarket) updateMsg;

            EnumMap<ResourceEnum, Integer> resourcesObtained = game.getTable().getResourceMarket().obtainResourceFromMarbleList(chosenRes);
            if(resourcesObtained.containsKey(ResourceEnum.FAITH)) {
                assertEquals((long)resourcesObtained.get(ResourceEnum.FAITH), game.getCurrentPlayer().getFaithTrack().getPlayerPosition());
                resourcesObtained.remove(ResourceEnum.FAITH);
            }
            if (game.getCurrentPlayer().getWhiteMarbleLeaderAbilities().size() == 1 && resourcesObtained.containsKey(ResourceEnum.NONE)) {
                ResourceEnum switchRes = game.getCurrentPlayer().getWhiteMarbleLeaderAbilities().get(0);
                if (resourcesObtained.containsKey(switchRes))
                    resourcesObtained.replace(switchRes, resourcesObtained.get(switchRes) + resourcesObtained.get(ResourceEnum.NONE));
                else resourcesObtained.put(switchRes, resourcesObtained.get(ResourceEnum.NONE));
            }
            resourcesObtained.remove(ResourceEnum.NONE);

            assertEquals(resourcesObtained, msg.getNewResourceList());



        } else if (updateMsg instanceof GameOver) {
            GameOver msg = (GameOver) updateMsg;
            assertEquals(player.getFaithTrack().getPlayerVictoryPoints(),msg.getYourFaithPoints());

        }else if (updateMsg instanceof TotalVictoryPoints) {
            TotalVictoryPoints msg = (TotalVictoryPoints) updateMsg;
            assertEquals(player.getTotalVictoryPoints(),msg.getTotalVictoryPoints());
        }
    }

    public void update(RunGame m) {
        runGame = true;
    }

    public boolean checkRunGame() {
        return runGame;
    }

    public void setChosenRes(MarbleEnum[] chosenRes) {
        this.chosenRes = chosenRes;
    }

    @Override
    public void setGame(Game game) {
        super.setGame(game);
        this.game = game;
    }

}
