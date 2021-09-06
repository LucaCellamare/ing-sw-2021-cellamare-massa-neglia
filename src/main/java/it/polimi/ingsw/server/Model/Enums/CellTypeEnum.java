package it.polimi.ingsw.server.Model.Enums;

/**
 * Enumerates all possible types of Cell which compose the FaithTrack
 */

public enum CellTypeEnum {
    /**
     * Just a normal cell, does nothing
     */
    NORMAL,
    /**
     * Cell which activates a Vatican Report
     */
    POPE_SPACE,
    /**
     * Cell which gives the player a specified amount of Victory Points
     */
    VICTORY_POINTS,
    /**
     * Last cell of the FaithTrack
     */
    LAST
}
