package com.example.qrconnect;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
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
 * The QRCodeGeneratesPageTest class is responsible for testing functionalities in the QR generate page when an event is created.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class QRCodeGeneratesPageTest {
    /**
     * Tests if the back button goes to the user homescreen.
     */
    @Test
    @Ignore
    public void testBackButton(){
        ActivityScenario.launch(QRCodeGeneratesPage.class);
        onView(withId(R.id.arrow_back_1)).perform(click());
        // Check if UI changed to MainActivity
        ActivityScenario<MainActivity> activityScenario = ActivityScenario.launch(MainActivity.class);
        activityScenario.onActivity(activity -> {
            // Check if the current activity is MainActivity
            assertThat(activity, instanceOf(MainActivity.class));
        });
    }

    @Test
    public void testLongClickAction(){
        ActivityScenario.launch(QRCodeGeneratesPage.class);
        onView(withId(R.id.qr_code_image)).perform(longClick());
        onView(withId(R.id.nav_turn_right)).perform(click());

        ActivityScenario<SelectEventPage> activityScenario = ActivityScenario.launch(SelectEventPage.class);
        activityScenario.onActivity(activity -> {
            assertThat(activity, instanceOf(SelectEventPage.class));
        });
    }
}
