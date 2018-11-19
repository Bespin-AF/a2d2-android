package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.widget.Button;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Request;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class RulesTest {
    @Rule
    public ActivityTestRule<Rules> ruleActivity = new ActivityTestRule<>(Rules.class);

    public Rules activity;
    public UiDevice device;
    public Instrumentation mInstrumentation;
    public Instrumentation.ActivityMonitor mRulesMonitor;

    //Indexes are language agnostic
    private static final int GRANT_PERMISSION = 1;

    @Before
    public void setUp(){
        mInstrumentation = getInstrumentation();
        mRulesMonitor = mInstrumentation.addMonitor(RequestRide.class.getName(),null,false);
        activity = ruleActivity.getActivity();
        device = UiDevice.getInstance(getInstrumentation());
    }

    @Test
    public void hasActivity(){
        assertNotNull(activity);
    }

    @Test
    public void hasRulesText(){
        Context appContext = activity.getApplicationContext();
        String rulesText = appContext.getResources().getString(R.string.a2d2_rules_text);
        final UiObject rulesTextView = device.findObject(new UiSelector().text(rulesText));
        assertTrue(rulesTextView.exists());
    }

    @Test
    public void hasButton(){
        Button testButtonAgree = activity.findViewById(R.id.button_rules_agree);
        assertNotNull(testButtonAgree);
    }

    @Test
    public void canUserAgree() {
        final Button testButtonAgree = activity.findViewById(R.id.button_rules_agree);
        assertNotNull(testButtonAgree);

        activity.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                testButtonAgree.performClick();
            }
        });

        final UiObject buttonAllowLocationPermission = device.findObject(
                new UiSelector()
                        .clickable(true)
                        .checkable(false)
                        .index(GRANT_PERMISSION)
        );

        assertTrue(buttonAllowLocationPermission.exists());

        //If the button doesn't exist I'm not sure how it would've gotten past the above point. However, the click is run in the ui thread so some weird scoping stuff is happening

        activity.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                try {
                    buttonAllowLocationPermission.click();
                } catch (UiObjectNotFoundException e){
                    //What do I do here?
                }
            }
        });

        Activity mRulesActivity = mInstrumentation.waitForMonitorWithTimeout(mRulesMonitor, 1000);
        assertNotNull(mRulesActivity);
    }
}