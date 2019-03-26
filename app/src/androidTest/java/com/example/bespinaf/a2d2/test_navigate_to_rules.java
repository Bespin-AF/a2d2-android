package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.view.View;
import android.widget.Button;

import com.example.bespinaf.a2d2.controllers.MainActivity;
import com.example.bespinaf.a2d2.controllers.Rules;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.action.ViewActions.click;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import static android.support.test.espresso.Espresso.onView;

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
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mRulesMonitor;

    /**
     *  Creates monitor for Rules page and creates Main Activity
     */
    @Before
    public void setUp(){
        mActivity = mMainActivity.getActivity();
        mInstrumentation = getInstrumentation();
        mRulesMonitor = mInstrumentation.addMonitor(Rules.class.getName(),null,false);
    }

    @After
    public void tearDown() throws Exception {
        mInstrumentation.removeMonitor(mRulesMonitor);
    }

    @Test
    public void hasActivityLaunched(){
        assertNotNull(mActivity);
    }

    @Test
    public void doesNavigationButtonExist(){
        onView(ViewMatchers.withId(R.id.button_navigate_to_rules));
    }

    @Test
    public void canUserNavigateToRules(){
        onView(ViewMatchers.withId(R.id.button_navigate_to_rules)).perform(click());

        Activity mRulesActivity = mInstrumentation.waitForMonitorWithTimeout(mRulesMonitor, 1000);
        assertNotNull(mRulesActivity);
    }

    @Test
    public void doesRulesPageContainRulesText(){
        onView(ViewMatchers.withId(R.id.button_navigate_to_rules)).perform(click());
        onView(ViewMatchers.withText(R.string.a2d2_rules_text));
    }
}
