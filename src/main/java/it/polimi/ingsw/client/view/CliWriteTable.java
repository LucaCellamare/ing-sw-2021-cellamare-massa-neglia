package it.polimi.ingsw.client.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CliWriteTable {
    private static final String HORIZ_SEPARATOR = "-";
    private static final String VERT_SEPARATOR = "|";
    private static final String JOIN_SEPARATOR = " ";
    private final List<String[]> rowsMarket = new ArrayList<>();
    private String[] headers;
    private boolean rightAlign;

    public CliWriteTable() {
    }

    public void setRightAlign(boolean rightAlign) {
        this.rightAlign = rightAlign;
    }

    public void setTableHead(String[] headers) {
        this.headers = headers;
    }

    public void addRow(String[] cells) {
        rowsMarket.add(cells);
    }

    public void print() {
        int[] maxWidths = headers != null ?
                Arrays.stream(headers).mapToInt(String::length).toArray() : null;

        for (String[] cells : rowsMarket) {
            if (maxWidths == null) {
                maxWidths = new int[cells.length];
            }
            if (cells.length != maxWidths.length) {
                throw new IllegalArgumentException("Number of row-cells and headers should be consistent");
            }
            for (int i = 0; i < cells.length; i++) {
                maxWidths[i] = Math.max(maxWidths[i], cells[i].length());
            }
        }

        if (headers != null) {
            printLine(maxWidths);
            printRow(headers, maxWidths);
            printLine(maxWidths);
        }
        for (String[] cells : rowsMarket) {
            printRow(cells, maxWidths);
        }
        if (headers != null) {
            printLine(maxWidths);
        }
    }

    private void printLine(int[] columnWidths) {
        for (int i = 0; i < columnWidths.length; i++) {
            String line = String.join("", Collections.nCopies(columnWidths[i] +
                    VERT_SEPARATOR.length() + 1, HORIZ_SEPARATOR));
            System.out.print(JOIN_SEPARATOR + line + (i == columnWidths.length - 1 ? JOIN_SEPARATOR : ""));
        }
        System.out.println();
    }

    private void printRow(String[] cells, int[] maxWidths) {
        for (int i = 0; i < cells.length; i++) {
            String s = cells[i];
            String verStrTemp = i == cells.length - 1 ? VERT_SEPARATOR : "";
            if (rightAlign) {
                System.out.printf("%s %" + maxWidths[i] + "s %s", VERT_SEPARATOR, s, verStrTemp);
            } else {
                System.out.printf("%s %-" + maxWidths[i] + "s %s", VERT_SEPARATOR, s, verStrTemp);
            }
        }
        System.out.println();
    }

//    public static void main(String[] args) {
//        //test code
//        CliWriteTable st = new CliWriteTable();
//        //st.setRightAlign(true);//if true then cell text is right aligned
//        st.setTableHead(new String[]{" // "," [1] ", " [2] ", " [3] "});//optional - if not used then there will be no header and horizontal lines
//        st.addRow(new String[]{" [1] ","super", "broccoli", "flexible"});
//        st.addRow(new String[]{" [2] ","assumption", "announcement", "reflection"});
//        st.addRow(new String[]{" [3] ","logic", "pleasant", "wild"});
//        st.print();
//    }
}
