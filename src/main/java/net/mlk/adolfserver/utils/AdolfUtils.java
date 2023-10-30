package net.mlk.adolfserver.utils;

import net.mlk.adolfserver.AdolfServerApplication;

import java.time.format.DateTimeParseException;

public class AdolfUtils {

    public static int tryParseInteger(String toParse) {
        try {
            return Integer.parseInt(toParse);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    public static boolean compareTimeDateFormat(String inputValue) {
        try {
            AdolfServerApplication.TIMEDATE_FORMAT.parse(inputValue);
            return true;
        } catch (DateTimeParseException dtpe) {
            return false;
        }
    }

    public static boolean compareDateFormat(String inputValue) {
        try {
            AdolfServerApplication.DATE_FORMAT.parse(inputValue);
            return true;
        } catch (DateTimeParseException dtpe) {
            return false;
        }
    }

}
