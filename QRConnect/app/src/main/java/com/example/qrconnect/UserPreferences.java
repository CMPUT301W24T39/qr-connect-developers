package com.example.qrconnect;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class sotres and retieves the user id from the device's local storage
 */
public class UserPreferences {

    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_USER_ID = "userId";

    // Save user ID locally
    public static void saveUserId(Context context, String userId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    // Retrieve user ID from local storage
    public static String getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USER_ID, null);
    }
}
