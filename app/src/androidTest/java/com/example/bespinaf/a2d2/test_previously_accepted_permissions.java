package com.example.bespinaf.a2d2;

import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertNotNull;

public class testUserPreviouslyAcceptedPermissions
{
    @Rule
    public ActivityTestRule<Rules> ruleActivity = new ActivityTestRule<>(Rules.class);

    public Rules activity;
    public UiDevice device;
    public Instrumentation mInstrumentation;
    public Instrumentation.ActivityMonitor mRulesMonitor;

    @Before
    public void setUp(){
        mInstrumentation = getInstrumentation();
        mRulesMonitor = mInstrumentation.addMonitor(RequestRide.class.getName(),null,false);
        activity = ruleActivity.getActivity();
        device = UiDevice.getInstance(getInstrumentation());
    }

    @Test //Checking that the activity has loaded
    public void hasActivity(){
        assertNotNull(activity);
    }



}
