package it.polimi.ingsw.server.Model.FaithTrack;

import it.polimi.ingsw.server.Model.Enums.CellTypeEnum;
import it.polimi.ingsw.server.Model.Enums.VaticanSectionEnum;

import java.io.Serializable;

/**
 * Describes a single Cell which compose the FaithTrack
 */

public class Cell implements Serializable {

    /**
     * Describes the cell number
     */
    private final int number;

    /**
     * Describes the cell's type
     */
    private final CellTypeEnum cellType;

    /**
     * Describes the vaticanReportSection associated to the cell
     */
    private final VaticanSectionEnum vaticanReportSection;

    /**
     * Describes the victoryPoints guaranteed by reaching the cell
     */
    private final int points;

    /**
     * Flag which indicates if the player is in this Cell
     */
    private boolean currentCell;

    /**
     * Char that contains the cross of the player which owns the Faith Track
     * (Lorenzo and other players have different crosses)
     */
    private char crossAscii;

    /**
     * Class constructor
     *
     * @param number               number of the cell
     * @param cellType             type of the cell
     * @param vaticanReportSection vaticanReportSection associated to the cell
     * @param points               victoryPoints guaranteed by reaching the cell
     */
    public Cell(int number, CellTypeEnum cellType, VaticanSectionEnum vaticanReportSection, int points) {
        this.number = number;
        this.cellType = cellType;
        this.vaticanReportSection = vaticanReportSection;
        this.points = points;
    }

    /**
     * Retrieves the number of the cell
     *
     * @return the number of the cell
     */
    public int getNumber() {
        return number;
    }

    /**
     * Retrieves the type of the cell
     *
     * @return the type of the cell
     */
    public CellTypeEnum getCellType() {
        return cellType;
    }

    /**
     * Retrieves the vaticanReportSection associated to the cell
     *
     * @return the vaticanReportSection associated to the cell
     */
    public VaticanSectionEnum getVaticanReportSection() {
        return vaticanReportSection;
    }

    /**
     * Retrieves the victoryPoints guaranteed by reaching the cell
     *
     * @return the victoryPoints guaranteed by reaching the cell
     */
    public int getPoints() {
        return points;
    }

    /**
     * @return true if the player is in the current cell, false otherwise
     */
    public boolean isCurrentCell() {
        return currentCell;
    }

    /**
     * Sets the currentCell flag
     *
     * @param currentCell true if the player is in the current cell, false otherwise
     */
    public void setCurrentCell(boolean currentCell) {
        this.currentCell = currentCell;
    }

    /**
     * Sets the cross char that will be used to print the Faith Track based on who owns it
     *
     * @param crossAscii the char to be used to print the cross
     */
    public void setCrossAscii(char crossAscii) {
        this.crossAscii = crossAscii;
    }

    /**
     * Prints the Cell in a String format
     *
     * @return the printable Cell
     */
    @Override
    public String toString() {
        if (currentCell)
            return " " + crossAscii + " ";
        else if (cellType == CellTypeEnum.POPE_SPACE)
            return " Ÿ ";
        else if (cellType == CellTypeEnum.VICTORY_POINTS)
            return Integer.toString(points).length() == 1 ? " +" + points + "" : "+" + points;
        else if (cellType == CellTypeEnum.LAST)
            return " ¥ ";
        else return "   ";
    }
}
