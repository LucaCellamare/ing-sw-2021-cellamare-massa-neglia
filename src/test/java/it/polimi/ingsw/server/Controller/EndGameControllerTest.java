package it.polimi.ingsw.server.Controller;

import it.polimi.ingsw.server.Model.Game;
import it.polimi.ingsw.server.Model.MultiplayerGame;
import it.polimi.ingsw.server.Model.Player;
import it.polimi.ingsw.server.Model.SinglePlayerGame;
import org.junit.Before;
import org.junit.Test;

public class EndGameControllerTest {

    Game singlePlayerGame = new SinglePlayerGame();
    Game multiPlayerGame = new MultiplayerGame(3);

    Player singlePlayer = new Player("testPlayer", true);
    Player multiPlayerOne = new Player("testPlayerOne", true);
    Player multiPlayerTwo = new Player("testPlayerTwo", false);
    Player multiPlayerThree = new Player("testPlayerThree", false);

    VirtualViewStub singlePlayerVirtualView = new VirtualViewStub(singlePlayerGame);
    VirtualViewStub multiPlayerVirtualView = new VirtualViewStub(multiPlayerGame);

    PhaseController singlePlayerPhaseController = new PhaseController(singlePlayerVirtualView, singlePlayerGame);
    PhaseController multiPlayerPhaseController = new PhaseController(multiPlayerVirtualView, multiPlayerGame);

    EndGameController endGameControllerSingle = new EndGameController(singlePlayerVirtualView, singlePlayerGame, singlePlayerPhaseController);
    EndGameController endGameControllerMulti = new EndGameController(multiPlayerVirtualView, multiPlayerGame, multiPlayerPhaseController);


    @Before
    public void setUpPlayers() {
        singlePlayerGame.addPlayer(0, singlePlayer);

        multiPlayerGame.addPlayer(0, multiPlayerOne);
        multiPlayerGame.addPlayer(1, multiPlayerTwo);
        multiPlayerGame.addPlayer(2, multiPlayerThree);
    }
    @Test
    public void countVictoryPointsTest() {
        singlePlayer.setTotalVictoryPoints(100);
        multiPlayerOne.setTotalVictoryPoints(100);
        multiPlayerTwo.setTotalVictoryPoints(80);
        multiPlayerThree.setTotalVictoryPoints(120);
        endGameControllerMulti.playLastTurn();
        endGameControllerSingle.playLastTurn();
    }
}
