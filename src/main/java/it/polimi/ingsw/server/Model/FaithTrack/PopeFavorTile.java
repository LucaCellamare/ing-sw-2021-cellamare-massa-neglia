package it.polimi.ingsw.server.Model.FaithTrack;

import it.polimi.ingsw.server.Model.Enums.TileStatusEnum;

import java.io.Serializable;

/**
 * The tile present in the FaithTrack which gives additional VictoryPoints to the Players that are inside the corresponding VaticanReportSection when the corresponding PopeZone is reached by any Player
 */
public class PopeFavorTile implements Serializable {

    /**
     * The victoryPoints.
     */
    private final int victoryPoints;
    /**
     * The Status.
     */
    private TileStatusEnum status;

    /**
     * Class constructor
     *
     * @param status        status of PopeFavorTile
     * @param victoryPoints victoryPoints assigned to the Tile
     */
    public PopeFavorTile(TileStatusEnum status, int victoryPoints) {
        this.status = status;
        this.victoryPoints = victoryPoints;
    }


    /**
     * Retrieves the status of the tile
     *
     * @return the status of the tile
     */
    public TileStatusEnum getStatus() {
        return status;
    }

    /**
     * Update the status of the tile
     *
     * @param status new status of the tile
     */
    public void setStatus(TileStatusEnum status) {
        this.status = status;
    }

    /**
     * Retrieves the victoryPoints assigned to the tile
     *
     * @return the victoryPoints of the tile
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    @Override
    public String toString() {
        switch (status) {
            case COVERED:
                return "C";
            case THROWN:
                return "T";
            case OBTAINED:
                return "O";
        }
        return "";
    }
}
