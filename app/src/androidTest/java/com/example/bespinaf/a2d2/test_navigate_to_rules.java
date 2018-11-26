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
 * Instrumented test, which will execute on an Android mDevice.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class test_navigate_to_rules {
    @Rule
    public ActivityTestRule<MainActivity> mMainActivity = new ActivityTestRule<>(MainActivity.class);

    private MainActivity mActivity;
    private UiDevice mDevice;
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mRulesMonitor;

    /**
     *  Creates monitor for Rules page and creates Main Activity
     */
    @Before
    public void setUp(){
        mActivity = mMainActivity.getActivity();
        mInstrumentation = getInstrumentation();
        mDevice = UiDevice.getInstance(mInstrumentation);
        mRulesMonitor = mInstrumentation.addMonitor(Rules.class.getName(),null,false);
    }

    @Test
    public void hasActivityLaunched(){
        assertNotNull(mActivity);
    }

    @Test
    public void doesNavigationButtonExist(){
        Button mButtonNavigateToRules = mActivity.findViewById(R.id.button_navigate_to_rules);
        assertNotNull(mButtonNavigateToRules);
    }

    @Test
    public void canUserNavigateToRules(){
        final Button mButtonNavigateToRules = mActivity.findViewById(R.id.button_navigate_to_rules);
        assertNotNull(mButtonNavigateToRules);

        mActivity.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                mButtonNavigateToRules.performClick();
            }
        });

        Activity mRulesActivity = mInstrumentation.waitForMonitorWithTimeout(mRulesMonitor, 1000);
        assertNotNull(mRulesActivity);
    }

    @Test
    public void doesRulesPageContainRulesText(){
        //Tests navigation and ensures that the rules page appears
        canUserNavigateToRules();

        //Gets app context in order to retrieve rules text
        Context mApplicationContext = mActivity.getApplicationContext();
        String mRulesText = mApplicationContext
                .getResources()
                .getString(R.string.a2d2_rules_text);
        final UiObject mRulesTextView = mDevice
                .findObject(new UiSelector().text(mRulesText));

        assertTrue(mRulesTextView.exists());
    }

    @After
    public void tearDown() throws Exception {
        mInstrumentation.removeMonitor(mRulesMonitor);
    }
}
