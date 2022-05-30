package hcmut.thesis.gpduserver.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class TimeUtils {
    public static final int ONE_DAY_IN_MILLIS_SECOND = 24 * 60 * 60 * 1000;
    public static final int ONE_HOUR_IN_MILLIS_SECOND = 60 * 60 * 1000;
    public static final int ONE_MINUTE_IN_MILLIS_SECOND = 60 * 1000;

    public static long atEndOfDay() {
        return (System.currentTimeMillis() / ONE_DAY_IN_MILLIS_SECOND + 1) * ONE_DAY_IN_MILLIS_SECOND - 1;
    }

    public static String convertTimestampToUTC(long timestamp) {
        Date date = new Date(timestamp * 1000);
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String text = sdf.format(date);
        return text;
    }

    public static long generateRandomTimeInToday(long start) {
        return ThreadLocalRandom.current().nextLong(start, atEndOfDay());
    }

    public static long generateRandomTimeInToday(long start, long endTime) {
        return ThreadLocalRandom.current().nextLong(start, endTime);
    }
}
