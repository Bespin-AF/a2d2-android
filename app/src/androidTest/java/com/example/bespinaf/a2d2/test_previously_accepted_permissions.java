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
    public ActivityTestRule<Rules> mRuleActivity = new ActivityTestRule<>(Rules.class);

    private Rules mActivity;
    private UiDevice mDevice;
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mRulesMonitor;

    @Rule //Assuming that permissions are granted; turns on the permissions if they are turned off
    public GrantPermissionRule mRuntimePermissionsRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    /**
     *  Creates monitor for RequestRide page and creates Rule Activity
     */
    @Before
    public void setUp(){
        mInstrumentation = getInstrumentation();
        mRulesMonitor = mInstrumentation.addMonitor(RequestRide.class.getName(),null,false);
        mActivity = mRuleActivity.getActivity();
        mDevice = UiDevice.getInstance(getInstrumentation());
    }

    @Test
    public void hasActivityLaunched(){
        assertNotNull(mActivity);
    }

    @Test
    public void doesAgreeButtonExist(){
        Button mButtonAgree = mActivity.findViewById(R.id.button_rules_agree);
        Assert.assertNotNull(mButtonAgree);
    }

    /*
     *  Go to the request ride page
     */
    @Test
    public void userClicksAgree_RedirectsToRequestPickup(){
        //Validates the agree button exists
        final Button mButtonAgree = mActivity.findViewById(R.id.button_rules_agree);
        assertNotNull(mButtonAgree);

        mActivity.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                mButtonAgree.performClick();
            }
        });

        //Checks that the Request ride page appears
        Activity mRequestRide = mInstrumentation.waitForMonitorWithTimeout(mRulesMonitor, 1000);
        Assert.assertNotNull(mRequestRide);
    }

}
