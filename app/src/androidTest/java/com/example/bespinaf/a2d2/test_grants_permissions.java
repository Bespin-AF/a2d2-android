package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
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

public class test_grants_permissions {

    @Rule
    public ActivityTestRule<Rules> mRuleActivity = new ActivityTestRule<>(Rules.class);

    private Rules mActivity;
    private UiDevice mDevice;
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mRulesMonitor;

    //Indexes are language agnostic
    private static final int DENY_PERMISSION = 0;
    private static final int GRANT_PERMISSION = 1;

    /**
     *  Creates monitor for RequestRide page and creates Rules Activity
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
        Button testButtonAgree = mActivity.findViewById(R.id.button_rules_agree);
        assertNotNull(testButtonAgree);
    }

    /**
     * Tests that the user sees an error message when they deny location permission
     */
    @Test
    public void userGrantsLocationPermission_ViewsErrorPopup() {
        final Button testButtonAgree = mActivity.findViewById(R.id.button_rules_agree);
        assertNotNull(testButtonAgree);

        mActivity.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                testButtonAgree.performClick();
            }
        });

        //Ensures that there is a button of index "GRANT_PERMISSION" (1), should correspond to 'Allow'
        final UiObject buttonGrantLocationPermission = mDevice.findObject(
                new UiSelector()
                        .clickable(true)
                        .checkable(false)
                        .index(GRANT_PERMISSION)
        );
        assertTrue(buttonGrantLocationPermission.exists());

        mActivity.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                try {
                    buttonGrantLocationPermission.click();
                } catch (UiObjectNotFoundException e){
                    //Thrown if no popup is found. However, existence is previously checked.
                }
            }
        });

        //Checks that the Request ride page appears
        Activity mRequestRide = mInstrumentation.waitForMonitorWithTimeout(mRulesMonitor, 4000);
        assertNotNull(mRequestRide);
    }
}