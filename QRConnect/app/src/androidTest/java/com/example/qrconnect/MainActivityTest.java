package com.example.qrconnect;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.example.qrconnect.MainActivity.eventDataList;
import static com.example.qrconnect.MainActivity.numAddButtonClicked;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
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
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<MainActivity>(MainActivity.class);


    /**
     * Tests if the profile icon button goes to the user profile page activity.
     */
    @Test
    public void testProfileButton(){
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
    public void testGoToQRCodeGeneratesPageButton(){
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
        onView(withId(R.id.qr_code_scanner_button)).perform(click());
        // Check if UI changed to QRCodeCheckInActivity
        ActivityScenario<QRCodeCheckInActivity> activityScenario = ActivityScenario.launch(QRCodeCheckInActivity.class);
        activityScenario.onActivity(activity -> {
            // Check if the current activity is QRCodeCheckInActivity
            assertThat(activity, instanceOf(QRCodeCheckInActivity.class));
        });
    }

    /**
     * Tests the add button, specifically the adding an event, functionality in the user homepage.
     */
    @Test
    public void testAddButton(){
        onView(withId(R.id.button_add_event)).perform(click());
        onData(is(instanceOf(String.class)))
                .inAdapterView(withId(R.id.event_list_list)).atPosition(eventDataList.size() - 1)
                .check(matches(withText("New Event "+numAddButtonClicked)));
    }

    /**
     * Test the intent navigation going from MainActivity to QRCodeGeneratesPage
     */
    @Test
    public void testActivitySwitchedFromMainActivity(){
        Intents.init();
        onView(withId(R.id.button_add_event)).perform(click());

        intended(IntentMatchers.hasComponent(QRCodeGeneratesPage.class.getName()));

        Intents.release();
    }

    /**
     * Tests back button feature for QR generators.
     */
    @Test
    public void testIsBackButton1Worked(){
        onView(withId(R.id.button_add_event)).perform(click());

        Espresso.pressBack();
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));

    }

    /**
     * Tests the add event button to change navigation from the user homepage and changes to generated QR codes.
     */
    @Test
    public void testIsActivitySwitchedFromQRCodeGeneratesPage(){
        Intents.init();
        onView(withId(R.id.button_add_event)).perform(click());

        intended(IntentMatchers.hasComponent(QRCodeGeneratesPage.class.getName()));

        onView(withId(R.id.qr_code_image)).perform(longClick());

        onView(withId(R.id.nav_turn_right)).perform(click());

        intended(IntentMatchers.hasComponent(SelectEventPage.class.getName()));

        Intents.release();
    }

    /**
     * Tests the back button feature of fragment that opens when long pressing generated QR codes.
     */
    @Test
    public void testIsBackButton2Worked(){
        Intents.init();
        onView(withId(R.id.button_add_event)).perform(click());

        intended(IntentMatchers.hasComponent(QRCodeGeneratesPage.class.getName()));

        onView(withId(R.id.qr_code_image)).perform(longClick());

        onView(withId(R.id.nav_turn_right)).perform(click());

        Espresso.pressBack();
        intended(IntentMatchers.hasComponent(QRCodeGeneratesPage.class.getName()));

        Intents.release();
    }
}

