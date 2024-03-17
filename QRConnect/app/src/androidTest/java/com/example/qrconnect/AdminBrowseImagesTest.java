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
 * The AdminBrowseImagesTest class is responsible for testing functionalities in the admin browse images page.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AdminBrowseImagesTest {
    /**
     * Tests if the back button goes to the admin menu page.
     */
    @Test
    public void testBackButton(){
        ActivityScenario.launch(AdminBrowseImages.class);
        onView(withId(R.id.admin_browse_images_back_button)).perform(click());
        // Check if UI changed to AdminMenu
        ActivityScenario<AdminMenu> activityScenario = ActivityScenario.launch(AdminMenu.class);
        activityScenario.onActivity(activity -> {
            // Check if the current activity is AdminMenu
            assertThat(activity, instanceOf(AdminMenu.class));
        });
    }
}