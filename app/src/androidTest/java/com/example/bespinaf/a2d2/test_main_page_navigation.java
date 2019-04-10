package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.example.bespinaf.a2d2.controllers.DriverLogin;
import com.example.bespinaf.a2d2.controllers.MainActivity;
import com.example.bespinaf.a2d2.controllers.Rules;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static org.junit.Assert.assertNotNull;

public class test_main_page_navigation {
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private MainActivity mActivity;
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mRulesMonitor;
    private Instrumentation.ActivityMonitor mLoginMonitor;

    /**
     *  Creates monitor for Rules page and creates Main Activity
     */
    @Before
    public void setUp(){
        mActivity = mMainActivityTestRule.getActivity();
        mInstrumentation = getInstrumentation();
        mRulesMonitor = mInstrumentation.addMonitor(Rules.class.getName(),null,false);
        mLoginMonitor = mInstrumentation.addMonitor(DriverLogin.class.getName(), null, false);
    }


    @After
    public void tearDown(){
        mActivity = null;
        mInstrumentation.removeMonitor(mRulesMonitor);
        mInstrumentation = null;
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
        onView(ViewMatchers.withText(mActivity.getString(R.string.a2d2_rules_disclaimer)));
    }


    @Test
    public void canUserNavigateToUserLogin(){
        onView(ViewMatchers.withId(R.id.button_main_driver_login)).perform(click());

        Activity mLoginActivity = mInstrumentation.waitForMonitorWithTimeout(mLoginMonitor, 1000);
        assertNotNull(mLoginActivity);
    }
}
