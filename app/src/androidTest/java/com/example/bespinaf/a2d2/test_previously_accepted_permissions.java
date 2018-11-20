package com.example.bespinaf.a2d2;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.widget.Button;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

public class test_previously_accepted_permissions
{
    @Rule
    public ActivityTestRule<Rules> ruleActivity = new ActivityTestRule<>(Rules.class);

    public Rules activity;
    public UiDevice device;
    public Instrumentation mInstrumentation;
    public Instrumentation.ActivityMonitor mRulesMonitor;

    @Rule //Assuming that permissions are granted; turns on the permissions if they are turned off
    public GrantPermissionRule mRuntimePermissionsRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

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

    @Test //Checks that the rules page is displaying text
    public void hasRulesText() {
        Context appContext = activity.getApplicationContext();
        String rulesText = appContext.getResources().getString(R.string.a2d2_rules_text);
        final UiObject rulesTextView = device.findObject(new UiSelector().text(rulesText));
        assertTrue(rulesTextView.exists());
    }

    @Test //Go to the request ride page
    public void hasPermissions(){
        final Button testButtonAgree = activity.findViewById(R.id.button_rules_agree);
        assertNotNull(testButtonAgree);

        activity.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                testButtonAgree.performClick();
            }
        });

        //Checks that the Request ride page appears
        Activity mRequestRide = mInstrumentation.waitForMonitorWithTimeout(mRulesMonitor, 1000);
        Assert.assertNotNull(mRequestRide);
    }

}
