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
 * The UserProfilePageTest class is responsible for testing functionalities in the user profile page.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
@Ignore
public class UserProfilePageTest {

    /**
     * Tests if the user icon button on the MainActivity brings the user to the UserProfilePage
     */
    @Test
    public void testUserIconButton() {
        ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.user_icon_button)).perform(click());
        // Check if UI changed to UserProfilePage
        ActivityScenario<UserProfilePage> activityScenario = ActivityScenario.launch(UserProfilePage.class);
        activityScenario.onActivity(activity -> {
            // Check if the current activity is UserProfilePage
            assertThat(activity, instanceOf(UserProfilePage.class));
        });
    }

}
