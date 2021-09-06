package it.polimi.ingsw.server.Controller;

import it.polimi.ingsw.messages.ChooseResourceLocation;
import it.polimi.ingsw.messages.DiscardLeaderCard;
import it.polimi.ingsw.server.Controller.Handlers.DevelopmentSlotsHandler;
import it.polimi.ingsw.server.Model.*;
import it.polimi.ingsw.server.Model.Cards.DevelopmentCard;
import it.polimi.ingsw.server.Model.Cards.ProductionPower;
import it.polimi.ingsw.server.Model.Enums.*;
import it.polimi.ingsw.server.Model.PersonalBoard.DevelopmentSlots;
import it.polimi.ingsw.server.Model.PersonalBoard.DevelopmentSlotsTest;
import it.polimi.ingsw.server.Model.PersonalBoard.Strongbox;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Depot;
import it.polimi.ingsw.server.Model.PersonalBoard.Warehouse.Warehouse;
import it.polimi.ingsw.server.Model.Requirements.ResourceRequirement;
import it.polimi.ingsw.server.Model.Table.CardMarket.CardMarket;
import it.polimi.ingsw.server.Model.Table.ResourceMarket;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.EnumMap;

import static org.junit.Assert.*;

public class PhaseControllerTest {
    LeaderCardDeck leaderCardDeck = new LeaderCardDeck();
    Boolean[] readyPlayer = new Boolean[3];

    Game singlePlayerGame = new SinglePlayerGame();
    Game multiPlayerGame = new MultiplayerGame(3);

    Player singlePlayer = new Player("testPlayer", true);
    Player multiPlayerOne = new Player("testPlayerOne", true);
    Player multiPlayerTwo = new Player("testPlayerTwo", false);
    Player multiPlayerThree = new Player("testPlayerThree", false);

    VirtualViewStub singlePlayerVirtualView = new VirtualViewStub(singlePlayerGame);
    VirtualViewStub multiPlayerVirtualView = new VirtualViewStub(multiPlayerGame, new SetupControllerTest());

    SetupController singlePlayerSetupController = new SetupController(singlePlayerVirtualView, singlePlayerGame);
    SetupController multiPlayerSetupController = new SetupController(multiPlayerVirtualView, multiPlayerGame);


    PhaseController singlePlayerPhaseController = new PhaseController(singlePlayerVirtualView, singlePlayerGame);
    PhaseController multiPlayerPhaseController = new PhaseController(multiPlayerVirtualView, multiPlayerGame);


    @Before
    public void setUpPlayers() {
        singlePlayerGame.addPlayer(0, singlePlayer);

        multiPlayerGame.addPlayer(0, multiPlayerOne);
        multiPlayerGame.addPlayer(1, multiPlayerTwo);
        multiPlayerGame.addPlayer(2, multiPlayerThree);

        Arrays.fill(readyPlayer, false);

        singlePlayerSetupController.pickLeaderCard(0);

        multiPlayerSetupController.pickLeaderCard(0);
        multiPlayerSetupController.pickLeaderCard(1);
        singlePlayerSetupController.setReadyPlayer(0);
        multiPlayerSetupController.setReadyPlayer(0);
        multiPlayerSetupController.setReadyPlayer(2);
        multiPlayerSetupController.setReadyPlayer(1);
        singlePlayerSetupController.checkFinish(0);
        readyPlayer[1] = true;
        multiPlayerSetupController.checkFinish(1);
        readyPlayer[2] = true;
        multiPlayerSetupController.checkFinish(2);
        readyPlayer[0] = true;
        multiPlayerSetupController.checkFinish(0);

        multiPlayerSetupController.checkAllPLayerReady();

        singlePlayerGame.nextPlayer();
        multiPlayerGame.nextPlayer();
    }

    @Test
    public void getResourceMarketTest(){
        MarbleEnum[][] resourceMarket = singlePlayerGame.getTable().getResourceMarket().getMarketStructure();
        MarbleEnum[][] resourceMarketMulti = multiPlayerGame.getTable().getResourceMarket().getMarketStructure();

        assertNotNull(resourceMarket);
        assertNotNull(resourceMarketMulti);
    }

    @Test
    public void getCardMarketTest(){
        MarbleEnum[][] cardMarketSingle = singlePlayerGame.getTable().getResourceMarket().getMarketStructure();
        MarbleEnum[][] cardMarketMulti = multiPlayerGame.getTable().getResourceMarket().getMarketStructure();

        assertNotNull(cardMarketSingle);
        assertNotNull(cardMarketMulti);
    }

    @Test
    public void getResourcesFromChoiceTest() {


        TypeLineEnum typeSelected = TypeLineEnum.ROW;
        int selectedLine = 2;

        EnumMap<ResourceEnum, Integer> newResourcesSingle = singlePlayerPhaseController.getResourcesFromChoice(typeSelected,selectedLine);
        EnumMap<ResourceEnum, Integer> newResourcesMulti = multiPlayerPhaseController.getResourcesFromChoice(typeSelected,selectedLine);

        assertNotNull(newResourcesSingle);
        assertNotNull(newResourcesMulti);

        assertFalse(newResourcesSingle.containsKey(ResourceEnum.NONE));
        assertFalse(newResourcesSingle.containsKey(ResourceEnum.FAITH));

        assertFalse(newResourcesMulti.containsKey(ResourceEnum.NONE));
        assertFalse(newResourcesMulti.containsKey(ResourceEnum.FAITH));
    }

    @Test
    public void swapDepotsTest(){
        Warehouse warehouseSingle = singlePlayerGame.getCurrentPlayer().getWarehouse();

        warehouseSingle.getDepots().get(0).setResource(ResourceEnum.STONE);
        warehouseSingle.getDepots().get(0).put(1);
        warehouseSingle.getDepots().get(1).setResource(ResourceEnum.COIN);
        warehouseSingle.getDepots().get(1).put(1);

        Warehouse warehouseMulti = multiPlayerGame.getCurrentPlayer().getWarehouse();


        warehouseMulti.getDepots().get(0).setResource(ResourceEnum.STONE);
        warehouseMulti.getDepots().get(0).put(1);
        warehouseMulti.getDepots().get(1).setResource(ResourceEnum.COIN);
        warehouseMulti.getDepots().get(1).put(1);

        Player currePlayerMulti = multiPlayerGame.getCurrentPlayer();

        singlePlayerPhaseController.swapDepots(0,1,2);
        multiPlayerPhaseController.swapDepots(multiPlayerGame.getPlayers().indexOf(currePlayerMulti),1,2);

        assertEquals(warehouseSingle.getDepots().get(0).getResource(),ResourceEnum.COIN);
        assertEquals(warehouseSingle.getDepots().get(1).getResource(),ResourceEnum.STONE);
        assertEquals(warehouseSingle.getDepots().get(0).getAmountStored(),1);
        assertEquals(warehouseSingle.getDepots().get(1).getAmountStored(),1);

        assertEquals(warehouseMulti.getDepots().get(0).getResource(),ResourceEnum.COIN);
        assertEquals(warehouseMulti.getDepots().get(1).getResource(),ResourceEnum.STONE);
        assertEquals(warehouseMulti.getDepots().get(0).getAmountStored(),1);
        assertEquals(warehouseMulti.getDepots().get(1).getAmountStored(),1);

    }

    @Test
    public void payResourcesTest(){

        ResourceRequirement[] resourceRequested = new ResourceRequirement[1];
        EnumMap<ResourceEnum, Integer> resourceObtained = new EnumMap<ResourceEnum, Integer>(ResourceEnum.class);

        resourceRequested[0] = new ResourceRequirement(ResourceEnum.STONE,1);
        resourceObtained.put(ResourceEnum.SHIELD,1);


        Warehouse warehouseSingle = singlePlayerGame.getCurrentPlayer().getWarehouse();

        warehouseSingle.getDepots().get(0).setResource(ResourceEnum.STONE);
        warehouseSingle.getDepots().get(0).put(1);
        warehouseSingle.getDepots().get(1).setResource(ResourceEnum.COIN);
        warehouseSingle.getDepots().get(1).put(1);

        Warehouse warehouseMulti = multiPlayerGame.getCurrentPlayer().getWarehouse();


        warehouseMulti.getDepots().get(0).setResource(ResourceEnum.STONE);
        warehouseMulti.getDepots().get(0).put(1);
        warehouseMulti.getDepots().get(1).setResource(ResourceEnum.COIN);
        warehouseMulti.getDepots().get(1).put(1);

        Strongbox stronboxSingle = singlePlayerGame.getCurrentPlayer().getStrongbox();
        Strongbox stronboxMulti = multiPlayerGame.getCurrentPlayer().getStrongbox();
        stronboxSingle.insertResource(ResourceEnum.SERVANT,1);
        stronboxMulti.insertResource(ResourceEnum.SERVANT,1);

        EnumMap<ResourceEnum, Integer> payWarehouse = new EnumMap<ResourceEnum, Integer>(ResourceEnum.class);
        EnumMap<ResourceEnum, Integer> payStrongbox = new EnumMap<ResourceEnum, Integer>(ResourceEnum.class);

        payWarehouse.put(ResourceEnum.STONE,1);
        payWarehouse.put(ResourceEnum.COIN,1);
        payStrongbox.put(ResourceEnum.SERVANT,1);

        ChooseResourceLocation m = new ChooseResourceLocation(payWarehouse,payStrongbox);

        singlePlayerPhaseController.tryUseProductionPower(0,new ProductionPower(resourceRequested,resourceObtained));
        multiPlayerPhaseController.tryUseProductionPower(0,new ProductionPower(resourceRequested,resourceObtained));

        singlePlayerPhaseController.payResources(m);
        multiPlayerPhaseController.payResources(m);

        assertEquals(stronboxSingle.getTotalResourcesCount(),0);
        assertEquals(stronboxMulti.getTotalResourcesCount(),0);

        assertEquals(warehouseSingle.getTotalResourcesAmount(),0);
        assertEquals(warehouseMulti.getTotalResourcesAmount(),0);

    }

    @Test
    public void discardLeaderCardTest(){
        singlePlayerSetupController.insertLeaderCard(0,0,1);
        Player currPlayerMulti = multiPlayerGame.getCurrentPlayer();

        multiPlayerSetupController.insertLeaderCard(multiPlayerGame.getPlayers().indexOf(currPlayerMulti),0,1);

        DiscardLeaderCard mSingle = new DiscardLeaderCard(singlePlayerGame.getCurrentPlayer().getLeaderCards().get(0),true,false);
        DiscardLeaderCard mMulti = new DiscardLeaderCard(currPlayerMulti.getLeaderCards().get(0),true,false);
        mMulti.setSender(multiPlayerGame.getPlayers().indexOf(currPlayerMulti));

        singlePlayerPhaseController.discardLeaderCard(mSingle);
        multiPlayerPhaseController.discardLeaderCard(mMulti);

        assertEquals(singlePlayerGame.getCurrentPlayer().getFaithTrack().getPlayerPosition(),1);
        assertEquals(multiPlayerGame.getCurrentPlayer().getFaithTrack().getPlayerPosition(),1);
    }

    @Test
    public void tryInsertCardTest(){
        DevelopmentSlots slot = multiPlayerGame.getCurrentPlayer().getSlots();
        DevelopmentCard devCard = new DevelopmentCard(" ",1,5,new ResourceRequirement[1],new ProductionPower(),TypeEnum.PURPLE,LevelEnum.ONE);

        Player currPlayer = multiPlayerGame.getCurrentPlayer();
        DevelopmentSlotsHandler.setCardToInsert(devCard);
        multiPlayerPhaseController.tryInsertCard(multiPlayerGame.getPlayers().indexOf(currPlayer),1);

        assertEquals(1, slot.getCards().size());
    }

    @Test
    public void tryBuyDevelopmentCardTest(){
        Player currPlayer = multiPlayerGame.getCurrentPlayer();
        Warehouse currPlayerWarehouse = currPlayer.getWarehouse();
        Strongbox currPlayerStrongbox = currPlayer.getStrongbox();

        currPlayerWarehouse.getDepots().get(0).setResource(ResourceEnum.STONE);
        currPlayerWarehouse.getDepots().get(0).put(1);
        currPlayerWarehouse.getDepots().get(1).setResource(ResourceEnum.COIN);
        currPlayerWarehouse.getDepots().get(1).put(2);
        currPlayerWarehouse.getDepots().get(2).setResource(ResourceEnum.SHIELD);
        currPlayerWarehouse.getDepots().get(2).put(3);

        currPlayerStrongbox.insertResource(ResourceEnum.STONE,10);
        currPlayerStrongbox.insertResource(ResourceEnum.COIN,10);
        currPlayerStrongbox.insertResource(ResourceEnum.SHIELD,10);
        currPlayerStrongbox.insertResource(ResourceEnum.SERVANT,10);

        multiPlayerPhaseController.tryBuyDevelopmentCard(multiPlayerGame.getPlayers().indexOf(currPlayer),0,0);

        CardMarket cardMarket = multiPlayerGame.getTable().getCardMarket();

        assertEquals(0, DevelopmentSlotsHandler.getChosenCardColor());
        assertEquals(0, DevelopmentSlotsHandler.getChosenCardLevel());

    }


}