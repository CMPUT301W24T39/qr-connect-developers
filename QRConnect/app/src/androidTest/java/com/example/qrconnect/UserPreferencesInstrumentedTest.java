package com.example.qrconnect;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class contains instrumented tests for the {@link UserPreferences} class.
 * These tests ensure the functionality of saving and retrieving user ID from SharedPreferences.
 */
@RunWith(AndroidJUnit4.class)
public class UserPreferencesInstrumentedTest {

    private SharedPreferences sharedPreferences;
    /**
     * Sets up the SharedPreferences for testing.
     * Loads SharedPreferences using the application context.
     */
    @Before
    public void setUp() {
        // load shared Preferences
        Context context = ApplicationProvider.getApplicationContext();
        sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
    }
    /**
     * Clear  SharedPreferences after each test.
     */
    @After
    public void tearDown() {
        sharedPreferences.edit().clear().apply();
    }
    /**
     * Tests the {@link UserPreferences#saveUserId(Context, String)} and {@link
     * UserPreferences#getUserId(Context)} methods.It saves a user ID, retrieves it, and asserts
     * that the retrieved user ID matches the saved one.
     */
    @Test
    public void testSaveAndGetUserId() {
        // Test saving and retrieving user ID
        String userId = "testUserId";
        UserPreferences.saveUserId(ApplicationProvider.getApplicationContext(), userId);
        String retrievedUserId = UserPreferences.getUserId(ApplicationProvider.getApplicationContext());
        assertEquals(userId, retrievedUserId);
    }
}
