package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.widget.Button;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    public MainActivity activity;
    public Instrumentation mInstrumentation;
    public Instrumentation.ActivityMonitor mRulesMonitor;

    @Before
    public void setUp(){
        activity = mainActivity.getActivity();
        mInstrumentation = getInstrumentation();
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
        Button btnNavigateToRules = activity.findViewById(0);
        assertNotNull(btnNavigateToRules);
    }

    //Button id is R.id.button_navigate_to_rules ; fails to compile if provided however since it doesn't exist
    @Test
    public void canNavigate(){
        final Button btnNavigateToRules = activity.findViewById(0);
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

    @After
    public void tearDown() throws Exception {
        mInstrumentation.removeMonitor(mRulesMonitor);
    }
}
