package utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Nightingale
 * Date: 1/26/13
 * Time: 3:31 PM
 * To change this template use File | Settings | File Templates.
 */
public final class DateUtils {
    public static final String DATE_FORMAT_NOW = "dd/MM/yyyy, HH:mm:ss";

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return "[" + sdf.format(cal.getTime()) + "]";

    }

    public DateUtils () {

    }

    public int timeStamp() {

        java.util.Date date= new java.util.Date();
        Timestamp i = new Timestamp(date.getTime());

        return (int) i.getTime();
    }
}