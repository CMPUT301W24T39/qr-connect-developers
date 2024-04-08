package com.example.qrconnect;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class stores and retrieves the user id from the device's local storage
 */
public class UserPreferences {

    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_USER_ID = "userId";

    // Save user ID locally

    /**
     * Save the user id locally
     * @param context the context of this class
     * @param userId the id of the user
     */
    public static void saveUserId(Context context, String userId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    // Retrieve user ID from local storage

    /**
     * Retrieve user ID from local storage
     * @param context the context of this class
     * @return a String
     */
    public static String getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USER_ID, null);
    }

    public static void clearUserId(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.remove(KEY_USER_ID);
        editor.apply();
    }
}
