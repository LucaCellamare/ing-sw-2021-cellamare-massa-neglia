package it.polimi.ingsw.server.Model.FaithTrack;

import com.google.gson.Gson;
import it.polimi.ingsw.server.Model.Enums.TileStatusEnum;
import it.polimi.ingsw.server.Model.Enums.VaticanSectionEnum;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Objects;

/**
 * Describes the Faith Track proper to each player
 *
 * @author LucaCellamare
 */
public class FaithTrack implements Serializable {

    /**
     * The constant TRACK_SIZE.
     */
    public static final int TRACK_SIZE = 25;

    /**
     * The constant TRACK_SIZE.
     */
    private static final int TILE_SIZE = 3;
    /**
     * The pope favor tiles.
     */
    private final PopeFavorTile[] popeFavorTiles;
    /**
     * The Faith Track.
     */
    private final Cell[] track;
    /**
     * Flag which indicates if this is Lorenzo's Faith Track (single player game ONLY)
     */
    private final boolean lorenzo;
    /**
     * The player victory points.
     */
    private int playerVictoryPoints;
    /**
     * The player position.
     */
    private int playerPosition;

    /**
     * Class constructor
     */
    public FaithTrack(boolean lorenzo) {
        this.playerPosition = 0;
        this.playerVictoryPoints = 0;
        this.lorenzo = lorenzo;
        this.track = readTrack();

        getCell().setCurrentCell(true);

        this.popeFavorTiles = new PopeFavorTile[]{
                new PopeFavorTile(TileStatusEnum.COVERED, 2),
                new PopeFavorTile(TileStatusEnum.COVERED, 3),
                new PopeFavorTile(TileStatusEnum.COVERED, 4),
        };
    }

    /**
     * Reads the standard Faith Track from file
     *
     * @return the Cells which compose the Faith Track
     */
    private Cell[] readTrack() {
        String path = "json/FaithTrack.json";
        InputStreamReader fr;
        Gson gson;

        Cell[] returnCells = null;

        try {
            fr = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path)));
            gson = new Gson();

            returnCells = gson.fromJson(fr, Cell[].class);
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnCells;
    }

    /**
     * Gets the whole Faith Track
     *
     * @return the whole Faith Track
     */
    public Cell[] getTrack() {
        return track;
    }

    /**
     * Retrieves the number of the cell in which the player currently is
     *
     * @return the player position on the track
     */
    public int getPlayerPosition() {
        return playerPosition;
    }

    /**
     * Retrieves the victory points cumulated by the player on the track
     *
     * @return the cumulated points on the track
     */
    public int getPlayerVictoryPoints() {
        return playerVictoryPoints;
    }

    /**
     * Moves the player forward ONLY one position
     */
    public void incrementPlayerPosition() {
        getCell().setCurrentCell(false);
        playerPosition++;
        getCell().setCurrentCell(true);
    }

    /**
     * Gets the Pope favor tiles of the player (to see if the player obtained/discarded them or never reached the related sections)
     *
     * @return the player's Pope favor tiles
     */
    public PopeFavorTile[] getPopeFavorTiles() {
        return popeFavorTiles;
    }

    /**
     * Adds the victory points to the player
     *
     * @param victoryPoints victory points to be added
     */
    public void addVictoryPoints(int victoryPoints) {
        playerVictoryPoints += victoryPoints;
    }

    /**
     * A Vatican report takes place (any player reached a Pope cell in their Faith Track)
     * This method checks if the current player is inside the Vatican report section that has been activated,
     * and updates the related Pope favor tile to discard or obtain it
     *
     * @param activatedSection the Vatican section that has been activated
     */
    public void vaticanReport(VaticanSectionEnum activatedSection) {
        Cell currentCell = getCell();
        PopeFavorTile currentVaticanSectionTile = popeFavorTiles[activatedSection.ordinal()];
        if (currentVaticanSectionTile.getStatus() == TileStatusEnum.COVERED) {
            if (currentCell.getVaticanReportSection() == activatedSection) {
                currentVaticanSectionTile.setStatus(TileStatusEnum.OBTAINED);
                playerVictoryPoints += currentVaticanSectionTile.getVictoryPoints();
            } else currentVaticanSectionTile.setStatus(TileStatusEnum.THROWN);
        }
    }

    /**
     * Retrieves the cell on which the player currently is
     *
     * @return the current cell of the player
     */
    public Cell getCell() {
        return track[playerPosition];
    }

    /**
     * Prints the Faith Track in a String format
     *
     * @return the printable Faith Track
     */
    @Override
    public String toString() {
        String retString = "";

        retString = retString.concat("            +-----+~~~~~+~~~~~+~~~~~+~~~~~+-----+                       +-----+~~~~~+~~~~~+~~~~~+~~~~~+~~~~~+~~~~~+\n");
        retString = retString.concat(String.format("            | %s | %s | %s | %s | %s | %s |     +-----------+     | %s | %s | %s | %s | %s | %s | %s |\n", printCell(track[4]), printCell(track[5]), printCell(track[6]), printCell(track[7]), printCell(track[8]), printCell(track[9]), printCell(track[18]), printCell(track[19]), printCell(track[20]), printCell(track[21]), printCell(track[22]), printCell(track[23]), printCell(track[24])));
        retString = retString.concat("            +-----+~~~~~+~~~~~+~~~~~+~~~~~+-----+     |           |     +-----+~~~~~+~~~~~+~~~~~+~~~~~+~~~~~+~~~~~+\n");
        retString = retString.concat(String.format("            | %s |     |     %s     |     | %s |     |     %s     |     | %s |     |     %s     |               \n", printCell(track[3]), popeFavorTiles[0], printCell(track[10]), popeFavorTiles[1], printCell(track[17]), popeFavorTiles[2]));
        retString = retString.concat("+-----+-----+-----+     |           |     +-----+~~~~~+~~~~~+~~~~~+~~~~~+~~~~~+     |           |               \n");
        retString = retString.concat(String.format("| %s | %s | %s |     +-----------+     | %s | %s | %s | %s | %s | %s |     +-----------+               \n", printCell(track[0]), printCell(track[1]), printCell(track[2]), printCell(track[11]), printCell(track[12]), printCell(track[13]), printCell(track[14]), printCell(track[15]), printCell(track[16])));
        retString = retString.concat("+-----+-----+-----+                       +-----+~~~~~+~~~~~+~~~~~+~~~~~+~~~~~+                              \n");

        return retString;
    }

    /**
     * Utility method, just prints a single Cell based on its type
     *
     * @param c the Cell to be printed
     * @return the printable Cell
     */
    private String printCell(Cell c) {
        if (c.isCurrentCell()) {
            if (lorenzo)
                c.setCrossAscii('✞');
            else c.setCrossAscii('✝');
        }
        return c.toString();
    }
}
