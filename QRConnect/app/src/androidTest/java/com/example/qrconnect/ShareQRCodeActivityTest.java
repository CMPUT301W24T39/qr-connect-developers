package com.example.qrconnect;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasType;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.google.android.material.datepicker.CompositeDateValidator.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
@Ignore
public class ShareQRCodeActivityTest {

    @Test
    public void testShareButton(){
        ActivityScenario.launch(ShareQRCodeActivity.class);
        onView(withId(R.id.share_button)).perform(click());
        Intents.init();
        try {
            intended(hasAction(Intent.ACTION_SEND));
            intended(hasType("image/*"));
        } finally {
            Intents.release();
        }
    }
}
