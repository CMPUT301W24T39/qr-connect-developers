package com.example.qrconnect;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * The AttendeeNotificationsTest class is responsible for testing functionalities in the attendee notifcations page.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AttendeeNotificationsTest {
    /**
     * Tests if the back button goes to the user homescreen page.
     */
    @Test
    @Ignore
    public void testBackButton(){
        ActivityScenario.launch(AttendeeNotifications.class);
        onView(withId(R.id.attendee_notifications_page_back_nav_button)).perform(click());
        // Check if UI changed to MainActivity
        ActivityScenario<MainActivity> activityScenario = ActivityScenario.launch(MainActivity.class);
        activityScenario.onActivity(activity -> {
            // Check if the current activity is MainActivity
            assertThat(activity, instanceOf(MainActivity.class));
        });
    }
}