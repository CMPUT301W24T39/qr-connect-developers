package com.example.qrconnect;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SelectEventPageTest {

    @Test
    public void testBackButton(){
        ActivityScenario.launch(SelectEventPage.class);
        onView(withId(R.id.arrow_back_2)).perform(click());
        ActivityScenario<QRCodeGeneratesPage> activityScenario = ActivityScenario.launch(QRCodeGeneratesPage.class);
        activityScenario.onActivity(activity -> {
            assertThat(activity, instanceOf(QRCodeGeneratesPage.class));
        });
    }
}
