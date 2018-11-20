package com.example.bespinaf.a2d2;

import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class test_denies_permissions {
    @Rule
    private ActivityTestRule<Rules> mRuleActivity = new ActivityTestRule<>(Rules.class);

    private Rules mActivity;
    private UiDevice mDevice;
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mRulesMonitor;

    //Indexes are language agnostic
    private static final int DENY_PERMISSION = 0;
    private static final int GRANT_PERMISSION = 1;

    @Before
    public void setUp(){
        mInstrumentation = getInstrumentation();
        mRulesMonitor = mInstrumentation.addMonitor(RequestRide.class.getName(),null,false);
        mActivity = mRuleActivity.getActivity();
        mDevice = UiDevice.getInstance(getInstrumentation());
    }

    @Test // Checking that the mActivity has loaded
    public void hasActivity(){
        assertNotNull(mActivity);
    }

    @Test // Makes sure that the Agree button is on the screen
    public void hasButton(){
        Button testButtonAgree = mActivity.findViewById(R.id.button_rules_agree);
        assertNotNull(testButtonAgree);
    }

    @Test //Tests the user can see an error pop-up when they deny permissions
    public void canUserAgree() {
        //Clicks the agree button
        final Button testButtonAgree = mActivity.findViewById(R.id.button_rules_agree);
        assertNotNull(testButtonAgree);

        mActivity.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                testButtonAgree.performClick();
            }
        });
        //makes sure that there is a button with an index of "DENY_PERMISSION"
        final UiObject buttonDenyLocationPermission = mDevice.findObject(
                new UiSelector()
                        .clickable(true)
                        .checkable(false)
                        .index(DENY_PERMISSION)
        );

        assertTrue(buttonDenyLocationPermission.exists());

        //In case the button does not exist [shouldn't trigger]
        mActivity.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                try {
                    buttonDenyLocationPermission.click();
                } catch (UiObjectNotFoundException e){

                }
            }
        });
        //Makes sure the the error Toast message appears
        Espresso.onView(ViewMatchers.withText(R.string.error_LocationPermissionDenied))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }
}
