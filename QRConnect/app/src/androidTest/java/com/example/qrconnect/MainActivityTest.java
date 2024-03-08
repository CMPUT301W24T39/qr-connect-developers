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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * The MainActivityTest class is responsible for testing functionalities in the main user homepage.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    /**
     * Tests if the profile icon button goes to the user profile page activity.
     */
    @Test
    public void testProfileButton(){
        ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.user_icon_button)).perform(click());
        // Check if UI changed to UserProfilePage
        ActivityScenario<UserProfilePage> activityScenario = ActivityScenario.launch(UserProfilePage.class);
        activityScenario.onActivity(activity -> {
            // Check if the current activity is UserProfilePage
            assertThat(activity, instanceOf(UserProfilePage.class));
        });
    }

    /**
     * Tests if the add event button goes to the create event page with the generated QR codes.
     */
    @Test
    public void testAddEventButton(){
        ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.button_add_event)).perform(click());
        // Check if UI changed to QRCodeGeneratesPage
        ActivityScenario<QRCodeGeneratesPage> activityScenario = ActivityScenario.launch(QRCodeGeneratesPage.class);
        activityScenario.onActivity(activity -> {
            // Check if the current activity is QRCodeGeneratesPage
            assertThat(activity, instanceOf(QRCodeGeneratesPage.class));
        });
    }

    /**
     * Tests if the scan QR code goes to the scan QR page.
     */
    @Test
    public void testScanQRButton(){
        ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.qr_code_scanner_button)).perform(click());
        // Check if UI changed to QRCodeCheckInActivity
        ActivityScenario<QRCodeCheckInActivity> activityScenario = ActivityScenario.launch(QRCodeCheckInActivity.class);
        activityScenario.onActivity(activity -> {
            // Check if the current activity is QRCodeCheckInActivity
            assertThat(activity, instanceOf(QRCodeCheckInActivity.class));
        });
    }
}