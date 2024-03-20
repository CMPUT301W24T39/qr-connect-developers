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
 * The AdminMenuTest class is responsible for testing functionalities in the admin menu page.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AdminMenuTest {
    /**
     * Tests if the back button goes to the admin QR scan page.
     */
    @Test
    public void testBackButton(){
        ActivityScenario.launch(AdminMenu.class);
        onView(withId(R.id.admin_menu_back_button)).perform(click());
        // Check if UI changed to AdminQRScan
        ActivityScenario<AdminQRScan> activityScenario = ActivityScenario.launch(AdminQRScan.class);
        activityScenario.onActivity(activity -> {
            // Check if the current activity is AdminQRScan
            assertThat(activity, instanceOf(AdminQRScan.class));
        });
    }

    /**
     * Tests if the browse profiles button goes to the admin browse profiles page.
     */
    @Test
    public void testBrowseProfilesButton(){
        ActivityScenario.launch(AdminMenu.class);
        onView(withId(R.id.admin_browse_profiles_button)).perform(click());
        // Check if UI changed to AdminBrowseProfiles
        ActivityScenario<AdminBrowseProfiles> activityScenario = ActivityScenario.launch(AdminBrowseProfiles.class);
        activityScenario.onActivity(activity -> {
            // Check if the current activity is AdminBrowseProfiles
            assertThat(activity, instanceOf(AdminBrowseProfiles.class));
        });
    }

    /**
     * Tests if the browse images button goes to the admin browse images page.
     */
    @Test
    public void testBrowseImagesButton(){
        ActivityScenario.launch(AdminMenu.class);
        onView(withId(R.id.admin_browse_images_button)).perform(click());
        // Check if UI changed to AdminBrowseImages
        ActivityScenario<AdminBrowseImages> activityScenario = ActivityScenario.launch(AdminBrowseImages.class);
        activityScenario.onActivity(activity -> {
            // Check if the current activity is AdminBrowseImages
            assertThat(activity, instanceOf(AdminBrowseImages.class));
        });
    }

    /**
     * Tests if the browse events button goes to the admin browse events page.
     */
    @Test
    public void testBrowseEventsButton(){
        ActivityScenario.launch(AdminMenu.class);
        onView(withId(R.id.admin_browse_events_button)).perform(click());
        // Check if UI changed to AdminBrowseEvents
        ActivityScenario<AdminBrowseEvents> activityScenario = ActivityScenario.launch(AdminBrowseEvents.class);
        activityScenario.onActivity(activity -> {
            // Check if the current activity is AdminBrowseEvents
            assertThat(activity, instanceOf(AdminBrowseEvents.class));
        });
    }
}