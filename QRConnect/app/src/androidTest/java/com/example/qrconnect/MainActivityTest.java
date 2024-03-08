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

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<MainActivity>(MainActivity.class);


    @Test
    public void testAddButton(){
        onView(withId(R.id.button_add_event)).perform(click());
        onData(is(instanceOf(String.class)))
                .inAdapterView(withId(R.id.event_list_list)).atPosition(eventDataList.size() - 1)
                .check(matches(withText("New Event "+numAddButtonClicked)));
    }
    @Test
    public void testActivitySwitchedFromMainActivity(){

        Intents.init();
        onView(withId(R.id.button_add_event)).perform(click());

        intended(IntentMatchers.hasComponent(QRCodeGeneratesPage.class.getName()));

        Intents.release();
    }

    @Test
    public void testIsBackButton1Worked(){

        onView(withId(R.id.button_add_event)).perform(click());

        Espresso.pressBack();
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()));

    }

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

    @Test
    public void testIsBackButton2Worked(){
        Intents.init();
        onView(withId(R.id.button_add_event)).perform(click());

        intended(IntentMatchers.hasComponent(QRCodeGeneratesPage.class.getName()));

        onView(withId(R.id.qr_code_image)).perform(longClick());

        onView(withId(R.id.nav_turn_right)).perform(click());

        Espresso.pressBack();
        onView(withId(R.id.navigation_view)).check(matches(isDisplayed()));

        Intents.release();

    }

}
