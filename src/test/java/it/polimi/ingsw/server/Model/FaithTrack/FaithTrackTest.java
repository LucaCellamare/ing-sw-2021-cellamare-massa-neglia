package it.polimi.ingsw.server.Model.FaithTrack;

import it.polimi.ingsw.messages.AskName;
import it.polimi.ingsw.messages.VaticanReport;
import it.polimi.ingsw.server.Controller.Controller;
import it.polimi.ingsw.server.Controller.Handlers.FaithTrackHandler;
import it.polimi.ingsw.server.Model.Enums.CellTypeEnum;
import it.polimi.ingsw.server.Model.Enums.TileStatusEnum;
import it.polimi.ingsw.server.Model.Enums.VaticanSectionEnum;
import it.polimi.ingsw.server.Model.FaithTrack.Cell;
import it.polimi.ingsw.server.Model.FaithTrack.FaithTrack;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FaithTrackTest {
    FaithTrack firstPlayerFaithTrack = new FaithTrack(true);
    FaithTrack secondPlayerFaithTrack = new FaithTrack(false);
    FaithTrack thirdPlayerFaithTrack = new FaithTrack(false);

    Controller controller = new Controller(null) {
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
            firstPlayerFaithTrack.vaticanReport(e.getActivatedSection());
            secondPlayerFaithTrack.vaticanReport(e.getActivatedSection());
            thirdPlayerFaithTrack.vaticanReport(e.getActivatedSection());
        }
    };

    @Before
    public void setUp() {
        FaithTrackHandler.setController(controller);
    }

    @Test
    public void faithTrackTest() {
        FaithTrackHandler.updatePlayerPosition(secondPlayerFaithTrack, 6);
        FaithTrackHandler.updatePlayerPosition(firstPlayerFaithTrack, 10);
        FaithTrackHandler.updatePlayerPosition(thirdPlayerFaithTrack, 4);

        Cell firstPlayerCurrentCell = firstPlayerFaithTrack.getCell();

        assertEquals(10, firstPlayerFaithTrack.getPlayerPosition());
        assertEquals(4 + 2, firstPlayerFaithTrack.getPlayerVictoryPoints());
        assertTrue(firstPlayerCurrentCell.isCurrentCell());
        assertEquals(10, firstPlayerCurrentCell.getNumber());
        assertEquals(TileStatusEnum.OBTAINED, firstPlayerFaithTrack.getPopeFavorTiles()[0].getStatus());

        assertEquals(6, secondPlayerFaithTrack.getPlayerPosition());
        assertEquals(2 + 2, secondPlayerFaithTrack.getPlayerVictoryPoints());
        assertEquals(TileStatusEnum.OBTAINED, secondPlayerFaithTrack.getPopeFavorTiles()[0].getStatus());

        assertEquals(4, thirdPlayerFaithTrack.getPlayerPosition());
        assertEquals(1, thirdPlayerFaithTrack.getPlayerVictoryPoints());
        assertEquals(TileStatusEnum.THROWN, thirdPlayerFaithTrack.getPopeFavorTiles()[0].getStatus());

        System.out.println(firstPlayerFaithTrack);
        System.out.println(secondPlayerFaithTrack);
        System.out.println(thirdPlayerFaithTrack);
    }

    @Test
    public void cellTest() {
        Cell normalCell = new Cell(0, CellTypeEnum.NORMAL, VaticanSectionEnum.NONE, 0);
        Cell vpCell = new Cell(1, CellTypeEnum.VICTORY_POINTS, VaticanSectionEnum.NONE, 4);
        Cell popeCell = new Cell(2, CellTypeEnum.POPE_SPACE, VaticanSectionEnum.FIRST, 0);
        Cell lastCell = new Cell(3, CellTypeEnum.LAST, VaticanSectionEnum.THIRD, 20);
        normalCell.setCrossAscii('N');
        vpCell.setCrossAscii('V');

        normalCell.setCurrentCell(true);
        assertEquals(" N ", normalCell.toString());
        normalCell.setCurrentCell(false);
        vpCell.setCurrentCell(true);
        assertEquals(" V ", vpCell.toString());
        vpCell.setCurrentCell(false);

        assertEquals("   ", normalCell.toString());
        assertEquals(" +4", vpCell.toString());
        assertEquals(" Ÿ ", popeCell.toString());
        assertEquals(" ¥ ", lastCell.toString());

    }

    @Test
    public void TileTest() {
        FaithTrackHandler.updatePlayerPosition(secondPlayerFaithTrack, 5);
        System.out.println(firstPlayerFaithTrack);
        System.out.println(secondPlayerFaithTrack);

        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------");

        FaithTrackHandler.updatePlayerPosition(firstPlayerFaithTrack, 8);
        System.out.println(firstPlayerFaithTrack);
        System.out.println(secondPlayerFaithTrack);

        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------");

        FaithTrackHandler.updatePlayerPosition(firstPlayerFaithTrack, 2);
        System.out.println(firstPlayerFaithTrack);
        System.out.println(secondPlayerFaithTrack);

        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------");

        FaithTrackHandler.updatePlayerPosition(secondPlayerFaithTrack, 11);
        System.out.println(firstPlayerFaithTrack);
        System.out.println(secondPlayerFaithTrack);


        System.out.println(thirdPlayerFaithTrack);
    }
}