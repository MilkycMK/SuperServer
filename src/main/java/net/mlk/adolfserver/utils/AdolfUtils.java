package net.mlk.adolfserver.utils;

public class AdolfUtils {

    public static int tryParseInteger(String toParse) {
        try {
            return Integer.parseInt(toParse);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

}
