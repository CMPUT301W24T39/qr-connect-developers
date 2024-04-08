package com.example.qrconnect;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * The AttendeeBrowseEventsTest class is responsible for testing functionalities in the attendee browse events page.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AttendeeBrowseEventsTest {
    /**
     * Tests if the back button goes to the user homescreen page.
     */
    public ActivityScenarioRule<AttendeeBrowseEvents> scenario = new ActivityScenarioRule<AttendeeBrowseEvents>(AttendeeBrowseEvents.class);

    @Test
    public void clickEvent_opensSignupDetailsActivity() {
        ActivityScenario.launch(AttendeeBrowseEvents.class);
        // Click on an event in the list
        onView(withId(R.id.attendee_browse_event_list))
                .perform(click());
        ActivityScenario<SignupDetailsActivity> activityScenario = ActivityScenario.launch(SignupDetailsActivity.class);

        activityScenario.onActivity(activity -> {
            // Check if the current activity is SingupDetails
            assertThat(activity, instanceOf(SignupDetailsActivity.class));
        });
    }
    @Test
    public void testBackButton(){
        ActivityScenario.launch(AttendeeBrowseEvents.class);
        onView(withId(R.id.attendee_browse_events_back_button)).perform(click());
        // Check if UI changed to MainActivity
        ActivityScenario<MainActivity> activityScenario = ActivityScenario.launch(MainActivity.class);
        activityScenario.onActivity(activity -> {
            // Check if the current activity is MainActivity
            assertThat(activity, instanceOf(MainActivity.class));
        });
    }
}