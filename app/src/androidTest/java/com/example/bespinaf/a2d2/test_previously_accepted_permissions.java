package com.example.bespinaf.a2d2;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

import com.example.bespinaf.a2d2.controllers.RequestRide;
import com.example.bespinaf.a2d2.controllers.Rules;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.action.ViewActions.click;
import static junit.framework.TestCase.assertNotNull;
import static android.support.test.espresso.Espresso.onView;

public class test_previously_accepted_permissions
{
    @Rule
    public ActivityTestRule<Rules> mRuleActivity = new ActivityTestRule<>(Rules.class);
    @Rule //Assuming that permissions are granted; turns on the permissions if they are turned off
    public GrantPermissionRule mRuntimePermissionsRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    private Rules mActivity;
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mRulesMonitor;


    /**
     *  Creates monitor for RequestRide page and creates Rule Activity
     */
    @Before
    public void setUp(){
        mInstrumentation = getInstrumentation();
        mRulesMonitor = mInstrumentation.addMonitor(RequestRide.class.getName(),null,false);
        mActivity = mRuleActivity.getActivity();
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
    public void doesAgreeButtonExist(){
        onView(ViewMatchers.withId(R.id.button_rules_agree));
    }


    /*
     *  Go to the request ride page
     */
    @Test
    public void userClicksAgree_RedirectsToRequestPickup(){
        //Validates the agree button exists
        onView(ViewMatchers.withId(R.id.button_rules_agree)).perform(click());

        //Checks that the Request ride page appears
        Activity mRequestRide = mInstrumentation.waitForMonitorWithTimeout(mRulesMonitor, 1000);
        Assert.assertNotNull(mRequestRide);
    }
}
