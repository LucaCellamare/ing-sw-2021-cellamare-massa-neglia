package it.polimi.ingsw.server.Controller.Handlers;

import it.polimi.ingsw.messages.VaticanReport;
import it.polimi.ingsw.server.Controller.Controller;
import it.polimi.ingsw.server.Model.Enums.TileStatusEnum;
import it.polimi.ingsw.server.Model.Enums.VaticanSectionEnum;
import it.polimi.ingsw.server.Model.FaithTrack.Cell;
import it.polimi.ingsw.server.Model.FaithTrack.FaithTrack;
import it.polimi.ingsw.server.Model.FaithTrack.PopeFavorTile;

import static it.polimi.ingsw.server.Model.Enums.CellTypeEnum.VICTORY_POINTS;

/**
 * Used to keep some information about the faith track of the players during a turn
 */
public class FaithTrackHandler {
    /**
     * Instance of the controller
     */
    private static Controller controller;
    /**
     * Faith points obtained by the current player during it's turn
     */
    private static int faithPointsGivenToCurrentPlayer;
    /**
     * Faith points given to the other players during a turn
     */
    private static int faithPointsGivenToOtherPlayers;

    /**
     * @param controller Instance of the controller
     */
    public static void setController(Controller controller) {
        FaithTrackHandler.controller = controller;
    }

    /**
     * Adds the specified amount of faith points to the current player
     *
     * @param faithPointsGivenToCurrentPlayer to be added at the faith points obtained by the current player
     */
    public static void addFaithPointsToCurrentPlayer(int faithPointsGivenToCurrentPlayer) {
        FaithTrackHandler.faithPointsGivenToCurrentPlayer += faithPointsGivenToCurrentPlayer;
    }

    /**
     * Adds the specified amount of faith points to the other players
     *
     * @param faithPointsGivenToOtherPlayers to be added at the faith points given to the other players
     */
    public static void addFaithPointsToOtherPlayers(int faithPointsGivenToOtherPlayers) {
        FaithTrackHandler.faithPointsGivenToOtherPlayers += faithPointsGivenToOtherPlayers;
    }

    /**
     * @return Faith points obtained by the current player
     */
    public static int getFaithPointsGivenToCurrentPlayer() {
        return faithPointsGivenToCurrentPlayer;
    }

    /**
     * @return Faith points given to the other players
     */
    public static int getFaithPointsGivenToOtherPlayers() {
        return faithPointsGivenToOtherPlayers;
    }

    /**
     * Resets the faith points obtained and given
     */
    public static void reset() {
        faithPointsGivenToOtherPlayers = 0;
        faithPointsGivenToCurrentPlayer = 0;
    }

    /**
     * Updates the position in the faith track of the specified amount of position
     *
     * @param playerFaithTrack faith track of the player that received the faith points
     * @param increment        number of position to advance
     */
    public static void updatePlayerPosition(FaithTrack playerFaithTrack, int increment) {
        Cell currentCell;

        for (int i = 0; i < increment && playerFaithTrack.getPlayerPosition() < FaithTrack.TRACK_SIZE - 1; i++) {
            playerFaithTrack.incrementPlayerPosition();
            currentCell = playerFaithTrack.getCell();

            switch (currentCell.getCellType()) {
                case POPE_SPACE:
                    VaticanSectionEnum currentVaticanSection = currentCell.getVaticanReportSection();

                    PopeFavorTile currentVaticanSectionTile = playerFaithTrack.getPopeFavorTiles()[currentVaticanSection.ordinal()];

                    TileStatusEnum currentVaticanSectionTileStatus = currentVaticanSectionTile.getStatus();

                    if (currentVaticanSectionTileStatus == TileStatusEnum.COVERED) {
                        playerFaithTrack.addVictoryPoints(currentVaticanSectionTile.getVictoryPoints());
                        currentVaticanSectionTile.setStatus(TileStatusEnum.OBTAINED);
                        controller.update(new VaticanReport(currentVaticanSection));
                    }
                    break;
                case VICTORY_POINTS:
                    int points = currentCell.getPoints();

                    if (points > 1) {
                        Cell[] trackCopy = playerFaithTrack.getTrack();
                        int pos = playerFaithTrack.getPlayerPosition();
                        int cont = 1;
                        while (trackCopy[pos - cont].getCellType() != VICTORY_POINTS) {
                            cont++;
                        }

                        playerFaithTrack.addVictoryPoints(-(trackCopy[pos - cont].getPoints()));
                    }
                    playerFaithTrack.addVictoryPoints(currentCell.getPoints());
                    break;
                case LAST:
                    playerFaithTrack.addVictoryPoints(-16);
                    playerFaithTrack.addVictoryPoints(currentCell.getPoints());
                    currentVaticanSection = currentCell.getVaticanReportSection();

                    currentVaticanSectionTile = playerFaithTrack.getPopeFavorTiles()[currentVaticanSection.ordinal()];

                    currentVaticanSectionTileStatus = currentVaticanSectionTile.getStatus();

                    if (currentVaticanSectionTileStatus == TileStatusEnum.COVERED) {
                        playerFaithTrack.addVictoryPoints(currentVaticanSectionTile.getVictoryPoints());
                        currentVaticanSectionTile.setStatus(TileStatusEnum.OBTAINED);
                        controller.update(new VaticanReport(currentVaticanSection));
                    }
                    break;
            }
        }

    }
}
