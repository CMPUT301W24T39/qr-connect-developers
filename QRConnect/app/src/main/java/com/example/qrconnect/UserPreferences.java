package com.example.qrconnect;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class sotres and retieves the user id from the device's local storage
 */
public class UserPreferences {

    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_USER_ID = "userId";

    /**
     * Saves user ID locally
     * @param context The context of the application
     * @param userId The generated user ID
     */
    public static void saveUserId(Context context, String userId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    /**
     * Retrieves user ID from local storage
     * @param context The context of the application
     * @return The locally stored user ID
     */
    public static String getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USER_ID, null);
    }

    /**
     * Clears locally stored User ID so a new one can be generated.
     * This method is called when the user's profile has been deleted from Firebase
     * @param context The context of the application
     */
    public static void clearUserId(Context context) {
        // https://stackoverflow.com/questions/8034127/how-to-remove-some-key-value-pair-from-sharedpreferences, Yashwanth Kumar, March 28 2024
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.remove(KEY_USER_ID);
        editor.apply();
    }
}
