package com.fingy.robocall.util;

import org.joda.time.DateTime;

public class DateTimeUtil {
    private static final int EIGHT_THIRTY_PM_IN_MINUTES = 20 * 60 + 30;
    private static final int EIGHT_THIRTY_AM_IN_MINUTES = 8 * 60 + 30;

    public static DateTime toEightThirtyAmTomorrow(DateTime current) {
        return current.withTimeAtStartOfDay().plusDays(1).plusHours(8).plusMinutes(30);
    }

    public static boolean isAfterHalfPastEightPm(DateTime redialTime) {
        return redialTime.getMinuteOfDay() > EIGHT_THIRTY_PM_IN_MINUTES;
    }

    public static boolean isBeforeHalfPastEightAm(DateTime redialTime) {
        return redialTime.getMinuteOfDay() < EIGHT_THIRTY_AM_IN_MINUTES;
    }
}
