package kg.sunrise.infoapteka.utils.parsers;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeParser {

    public static String parseFromISO(String sourceISO, DateTimePattern destinationPattern) throws ParseException {
        return formatDateTime(sourceISO, DateTimePattern.DATE_TIME_ISO, destinationPattern);
    }

    public static String parseToISO(String sourceDateTime, DateTimePattern sourcePattern) throws ParseException {
        return formatDateTime(sourceDateTime, sourcePattern, DateTimePattern.DATE_TIME_ISO);
    }

    public static String formatDateTime(String sourceDateTime, DateTimePattern sourcePattern, DateTimePattern destinationPattern) throws ParseException {
        SimpleDateFormat sourceFormat = new SimpleDateFormat(sourcePattern.getPattern());
        SimpleDateFormat destinationFormat = new SimpleDateFormat(destinationPattern.getPattern());

        return destinationFormat.format(sourceFormat.parse(sourceDateTime));
    }
}


