package it.polimi.ingsw.client.view.CustomSwing;

import com.google.gson.Gson;
import it.polimi.ingsw.server.Model.Enums.CellTypeEnum;
import it.polimi.ingsw.server.Model.Enums.VaticanSectionEnum;
import it.polimi.ingsw.server.Model.FaithTrack.Cell;
import it.polimi.ingsw.server.Model.FaithTrack.FaithTrack;
import it.polimi.ingsw.server.Model.FaithTrack.PopeFavorTile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import static it.polimi.ingsw.utils.ImageUtil.getScaledImage;

public class FaithTrackPanel extends JPanel {

    private final CoordinatesCell[] coordianates;

    public FaithTrackPanel(FaithTrack updatedFaithTrack, boolean isLorenzo) {
        super(new GridBagLayout());

        coordianates = initCoordinates();

        GridBagConstraints gbc;

        for (Cell cell : updatedFaithTrack.getTrack()) {
            gbc = new GridBagConstraints();
            JPanel cellTrack = new JPanel();
            cellTrack.setPreferredSize(new Dimension(50, 50));
            if (cell.getCellType().equals(CellTypeEnum.NORMAL)) {
                cellTrack.setBackground(Color.lightGray);
                cellTrack.setBorder(BorderFactory.createLineBorder(Color.black));
            } else {
                if (cell.getCellType().equals(CellTypeEnum.POPE_SPACE)) {
                    cellTrack.setBackground(Color.red);
                    cellTrack.setBorder(BorderFactory.createLineBorder(Color.red));

                } else {
                    if (cell.getCellType().equals(CellTypeEnum.LAST)) {
                        cellTrack.setBackground(Color.green);
                        cellTrack.setBorder(BorderFactory.createLineBorder(Color.green));
                    } else {
                        cellTrack.setBackground(Color.yellow);
                        cellTrack.setBorder(BorderFactory.createLineBorder(Color.white));
                    }
                }
            }
            if(!cell.getVaticanReportSection().equals(VaticanSectionEnum.NONE))
            {
                cellTrack.setBorder(BorderFactory.createLineBorder(Color.red));
            }
            if(cell.getPoints() != 0)
            {
                JLabel point = new JLabel();
                point.setText(String.valueOf(cell.getPoints()));
                cellTrack.setLayout(new GridBagLayout());
                cellTrack.add(point);
            }

            if (cell.isCurrentCell()) {
                ImageIcon icon;
                try {
                    if(!isLorenzo)
                    {
                        icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/faith.jpg"))));
                    }else{
                        icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/black_cross.jpg"))));
                    }
                    icon = new ImageIcon(getScaledImage(icon.getImage(), 36, 36));
                    JLabel imageFaith = new JLabel(icon);
                    cellTrack.setLayout(new GridBagLayout());
                    cellTrack.add(imageFaith);
                } catch (IOException e) {
                    icon = null;
                    cellTrack.setBackground(Color.pink);
                }

            }
            assert coordianates != null;
            gbc.gridx = coordianates[cell.getNumber()].x;
            gbc.gridy = coordianates[cell.getNumber()].y;

            add(cellTrack, gbc);

        }

        //cell card to turn up
        for (int i = 26; i < 29; i++) {
            gbc = new GridBagConstraints();
            JPanel cellTrack = new JPanel();
            cellTrack.setPreferredSize(new Dimension(50, 50));
            cellTrack.setBorder(BorderFactory.createLineBorder(Color.black));

            PopeFavorTile popeFavorTile = updatedFaithTrack.getPopeFavorTiles()[i - 26];

            ImageIcon icon;

            switch (i) {
                case 26:
                    switch (popeFavorTile.getStatus()) {
                        case COVERED:
                            cellTrack.setBackground(Color.black);
                            break;
                        case OBTAINED:
                            try {
                                icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/yellow_tile.jpg"))));
                                icon = new ImageIcon(getScaledImage(icon.getImage(), 50, 50));
                                JLabel imageFaith = new JLabel(icon);
                                cellTrack.setLayout(new GridBagLayout());
                                cellTrack.add(imageFaith);
                            } catch (IOException e) {
                                icon = null;
                                cellTrack.setBackground(Color.orange);
                            }
                            break;
                        case THROWN:
                                cellTrack.setBackground(Color.lightGray);
                            break;
                    }
                    break;
                case 27:
                    switch (popeFavorTile.getStatus()) {
                        case COVERED:
                            cellTrack.setBackground(Color.black);
                            break;
                        case OBTAINED:

                            try {
                                icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/orange_tile.jpg"))));
                                icon = new ImageIcon(getScaledImage(icon.getImage(), 50, 50));
                                JLabel imageFaith = new JLabel(icon);
                                cellTrack.setLayout(new GridBagLayout());
                                cellTrack.add(imageFaith);
                            } catch (IOException e) {
                                icon = null;
                                cellTrack.setBackground(Color.orange);
                            }
                            break;
                        case THROWN:

                            cellTrack.setBackground(Color.lightGray);

                            break;
                    }
                    break;
                case 28:
                    switch (popeFavorTile.getStatus()) {
                        case COVERED:
                            cellTrack.setBackground(Color.black);
                            break;
                        case OBTAINED:

                            try {
                                icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jpg/red_tile.jpg"))));
                                icon = new ImageIcon(getScaledImage(icon.getImage(), 50, 50));
                                JLabel imageFaith = new JLabel(icon);
                                cellTrack.setLayout(new GridBagLayout());
                                cellTrack.add(imageFaith);
                            } catch (IOException e) {
                                icon = null;
                                cellTrack.setBackground(Color.orange);
                            }
                            break;
                        case THROWN:

                            cellTrack.setBackground(Color.lightGray);

                            break;
                    }
                    break;
            }

//            switch (popeFavorTile.getStatus()) {
//                case COVERED:
//                    cellTrack.setBackground(Color.orange);
//                    break;
//                case OBTAINED:
//                    cellTrack.setBackground(Color.GREEN);
//                    break;
//                case THROWN:
//                    cellTrack.setBackground(Color.lightGray);
//
//                    break;
//            }

            assert coordianates != null;
            gbc.gridx = coordianates[i].x;
            gbc.gridy = coordianates[i].y;
            gbc.gridwidth = 2;
            gbc.gridheight = 2;

            add(cellTrack, gbc);
        }

    }

    private CoordinatesCell[] initCoordinates() {
        String path = "json/CoordinatesFaithTrack.json";
        InputStreamReader fr;
        Gson gson;

        CoordinatesCell[] returnCells = null;

        try {
            fr = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path)));
            gson = new Gson();

            returnCells = gson.fromJson(fr, CoordinatesCell[].class);

            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnCells;
    }

    static class CoordinatesCell {

        private final int x;
        private final int y;
        private int index;

        public CoordinatesCell(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

    }
}
