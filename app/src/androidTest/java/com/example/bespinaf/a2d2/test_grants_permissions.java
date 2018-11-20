package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.widget.Button;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RulesTest {

    @Rule
    public ActivityTestRule<Rules> ruleActivity = new ActivityTestRule<>(Rules.class);

    public Rules activity;
    public UiDevice device;
    public Instrumentation mInstrumentation;
    public Instrumentation.ActivityMonitor mRulesMonitor;

    //Indexes are language agnostic
    private static final int DENY_PERMISSION = 0;
    private static final int GRANT_PERMISSION = 1;

    @Before
    public void setUp(){
        mInstrumentation = getInstrumentation();
        mRulesMonitor = mInstrumentation.addMonitor(RequestRide.class.getName(),null,false);
        activity = ruleActivity.getActivity();
        device = UiDevice.getInstance(getInstrumentation());
    }

    @Test // Checking that the activity has loaded
    public void hasActivity(){
        assertNotNull(activity);
    }

    @Test //checks that the Rules text is displayed
    public void hasRulesText(){
        Context appContext = activity.getApplicationContext();
        String rulesText = appContext.getResources().getString(R.string.a2d2_rules_text);
        final UiObject rulesTextView = device.findObject(new UiSelector().text(rulesText));
        assertTrue(rulesTextView.exists());
    }

    @Test // Makes sure that the Agree button is on the screen
    public void hasButton(){
        Button testButtonAgree = activity.findViewById(R.id.button_rules_agree);
        assertNotNull(testButtonAgree);
    }

    @Test //Tests the user can see an error pop-up when they grant permissions
    public void canUserAgree() {
        //Clicks the agree button
        final Button testButtonAgree = activity.findViewById(R.id.button_rules_agree);
        assertNotNull(testButtonAgree);

        activity.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                testButtonAgree.performClick();
            }
        });
        //makes sure that there is a button with an index of "GRANT_PERMISSION"
        final UiObject buttonGrantLocationPermission = device.findObject(
                new UiSelector()
                        .clickable(true)
                        .checkable(false)
                        .index(GRANT_PERMISSION)
        );

        assertTrue(buttonGrantLocationPermission.exists());

        //In case the button does not exist [shouldn't trigger]
        activity.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                try {
                    buttonGrantLocationPermission.click();
                } catch (UiObjectNotFoundException e){ //add a valid handler [somehow]

                }
            }
        });
        //Checks that the Request ride page appears
        Activity mRequestRide = mInstrumentation.waitForMonitorWithTimeout(mRulesMonitor, 1000);
        assertNotNull(mRequestRide);

    }


}