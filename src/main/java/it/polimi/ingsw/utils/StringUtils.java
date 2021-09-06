package it.polimi.ingsw.utils;

public class StringUtils {

    public static boolean isNumeric(String s) {
        try {
            Integer.parseInt(s);
            return !s.isBlank() && !s.isEmpty();
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isIpAddress(String ip) {
        try {
            if (ip == null || ip.isBlank() || ip.endsWith(".")) return false;
            String[] ipTokens = ip.split("\\.");
            if (ipTokens.length != 4) return false;
            int i;
            for (String s : ipTokens) {
                i = Integer.parseInt(s);
                if (i < 0 || i > 255) return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
