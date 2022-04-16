package hcmut.thesis.gpduserver.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    private static final int ONE_DAY_TIME = 24 * 60 * 60 * 1000;

    public static long atEndOfDay() {
        return (System.currentTimeMillis() / ONE_DAY_TIME + 1) * ONE_DAY_TIME - 1;
    }

    public static String convertTimestampToUTC(long timestamp) {
        Date date = new Date(timestamp * 1000);
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String text = sdf.format(date);
        return text;
    }
}
