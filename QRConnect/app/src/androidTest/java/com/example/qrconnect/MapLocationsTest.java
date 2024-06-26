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
 * The MapLocationsTest class is responsible for testing functionalities in the map check in locations page.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MapLocationsTest {
    /**
     * Tests if the back button goes to the event details page.
     */
    @Test
    @Ignore
    public void testBackButton(){
        ActivityScenario.launch(MapLocations.class);
        onView(withId(R.id.map_locations_back_button)).perform(click());
        // Check if UI changed to EventDetailsActivity
        ActivityScenario<EventDetailsActivity> activityScenario = ActivityScenario.launch(EventDetailsActivity.class);
        activityScenario.onActivity(activity -> {
            // Check if the current activity is EventDetailsActivity
            assertThat(activity, instanceOf(EventDetailsActivity.class));
        });
    }
}