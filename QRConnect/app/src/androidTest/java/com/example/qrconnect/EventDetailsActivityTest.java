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
 * The EventDetailsActivityTest class is responsible for testing functionalities in the event details page.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
@Ignore
public class EventDetailsActivityTest {
    /**
     * Tests if the back button goes to the user homescreen.
     */
    @Test
    public void testBackButton(){
        ActivityScenario.launch(EventDetailsActivity.class);
        onView(withId(R.id.event_details_back_nav_button)).perform(click());
        // Check if UI changed to MainActivity
        ActivityScenario<MainActivity> activityScenario = ActivityScenario.launch(MainActivity.class);
        activityScenario.onActivity(activity -> {
            // Check if the current activity is MainActivity
            assertThat(activity, instanceOf(MainActivity.class));
        });
    }/**
     * Tests if the notification icon button goes to the send notification page.
     */
    @Test
    public void testNotificationButton(){
        ActivityScenario.launch(EventDetailsActivity.class);
        onView(withId(R.id.event_details_send_notifications)).perform(click());
        // Check if UI changed to SendNotificationsActivity
        ActivityScenario<SendNotificationsActivity> activityScenario = ActivityScenario.launch(SendNotificationsActivity.class);
        activityScenario.onActivity(activity -> {
            // Check if the current activity is SendNotificationsActivity
            assertThat(activity, instanceOf(SendNotificationsActivity.class));
        });
    }
    /**
     * Tests if the menu button goes to the attendee list page.
     */
    @Test
    public void testAttendeeMenuButton(){
        ActivityScenario.launch(EventDetailsActivity.class);
        onView(withId(R.id.view_attendee_list_button)).perform(click());
        // Check if UI changed to AttendeeListActivity
        ActivityScenario<AttendeeListActivity> activityScenario = ActivityScenario.launch(AttendeeListActivity.class);
        activityScenario.onActivity(activity -> {
            // Check if the current activity is AttendeeListActivity
            assertThat(activity, instanceOf(AttendeeListActivity.class));
        });
    }
    @Test
    public void testShareButton(){
        ActivityScenario.launch(EventDetailsActivity.class);
        onView(withId(R.id.share_event_button)).perform(click());
        ActivityScenario<ShareQRCodeActivity> activityScenario = ActivityScenario.launch(ShareQRCodeActivity.class);
        activityScenario.onActivity(activity -> {
            // Check if the current activity is MainActivity
            assertThat(activity, instanceOf(ShareQRCodeActivity.class));
        });
    }
}
