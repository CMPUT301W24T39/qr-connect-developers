package com.example.qrconnect;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Convert the String to Calendar
 */
public class DateConverter {
    public static Calendar stringToCalendar(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            Log.e("TimeConverter", "Date string is null or empty");
            return null;
        }

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
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
