package com.example.qrconnect;

import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SplashActivityTest {

    @Rule
    public ActivityScenarioRule<SplashActivity> activityScenarioRule =
            new ActivityScenarioRule<>(SplashActivity.class);

    @Test
    public void splashActivityTest() {
        // Check if the root layout is displayed
        Espresso.onView(withId(R.id.splash_layout)).check(matches(isDisplayed()));
    }
}