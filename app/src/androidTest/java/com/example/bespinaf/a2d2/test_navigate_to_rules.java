package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.widget.Button;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class test_navigate_to_rules {
    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    public MainActivity activity;
    public UiDevice device;
    public Instrumentation mInstrumentation;
    public Instrumentation.ActivityMonitor mRulesMonitor;

    @Before
    public void setUp(){
        activity = mainActivity.getActivity();
        mInstrumentation = getInstrumentation();
        device = UiDevice.getInstance(mInstrumentation);
        mRulesMonitor = mInstrumentation.addMonitor(Rules.class.getName(),null,false);
    }

    //Test for if button is there
    @Test
    public void hasActivity(){
        assertNotNull(activity);
    }

    //Button id is R.id.button_navigate_to_rules ; fails to compile if provided however since it doesn't exist
    @Test
    public void hasButton(){
        Button btnNavigateToRules = activity.findViewById(R.id.button);
        assertNotNull(btnNavigateToRules);
    }

    //Button id is R.id.button_navigate_to_rules ; fails to compile if provided however since it doesn't exist
    @Test
    public void canNavigate(){
        final Button btnNavigateToRules = activity.findViewById(R.id.button);
        assertNotNull(btnNavigateToRules);

        activity.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                btnNavigateToRules.performClick();
                System.out.println("Button click successful.");
            }
        });

        Activity mRulesActivity = mInstrumentation.waitForMonitorWithTimeout(mRulesMonitor, 1000);
        assertNotNull(mRulesActivity);
    }

    @Test //checks that the Rules text is displayed
    public void hasRulesText() {
        canNavigate();

        Context appContext = activity.getApplicationContext();
        String rulesText = appContext.getResources().getString(R.string.a2d2_rules_text);
        final UiObject rulesTextView = device.findObject(new UiSelector().text(rulesText));
        assertTrue(rulesTextView.exists());
    }

    @After
    public void tearDown() throws Exception {
        mInstrumentation.removeMonitor(mRulesMonitor);
    }
}
