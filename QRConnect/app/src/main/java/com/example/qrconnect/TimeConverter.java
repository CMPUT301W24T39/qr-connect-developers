package com.example.qrconnect;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Convert the String to Calendar
 */
public class TimeConverter {
    public static Calendar stringToCalendar(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            Log.e("TimeConverter", "Time string is null or empty");
            return null;
        }

        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.US);
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(format.parse(dateTimeStr));
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
