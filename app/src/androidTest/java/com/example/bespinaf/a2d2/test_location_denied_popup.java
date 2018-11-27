package com.example.bespinaf.a2d2;

import android.support.test.espresso.Espresso;
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
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class test_location_denied_popup {
    @Rule
    public ActivityTestRule<Rules> mRuleActivity = new ActivityTestRule<>(Rules.class);

    private Rules mActivity;
    private UiDevice mDevice;

    //Indexes are language agnostic
    private static final int DENY_PERMISSION = 0;
    private static final int GRANT_PERMISSION = 1;

    @Before
    public void setUp(){
        mActivity = mRuleActivity.getActivity();
        mDevice = UiDevice.getInstance(getInstrumentation());
    }

    /**
     * Clicks the agree button, denies location permission in the ensuing prompt and verifies that
     * the Toast indicating that permission must be enabled appears
     */
    @Test
    public void userDeniesLocationPermission_ErrorToastAppears() {
        //Ensures the agree button exists and clicks on it to prompt for location permission
        final Button buttonAgreeToRules = mActivity.findViewById(R.id.button_rules_agree);
        assertNotNull(buttonAgreeToRules);

        final String mOkayButtonText = mActivity.getResources().getString(R.string.dialog_okay);

        mActivity.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                buttonAgreeToRules.performClick();
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

        mActivity.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                try {
                    buttonDenyLocationPermission.click();
                } catch (UiObjectNotFoundException e){
                    //Deny permission button not found
                }
            }
        });
        //Makes sure the the error Toast message appears
        Espresso.onView(withText(R.string.error_LocationPermissionDenied))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        //Clicks on the Okay button in the popup and then checks that the popup went away
        Espresso.onView(withText(R.string.dialog_okay)).inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        Espresso.onView(withText(R.string.error_LocationPermissionDenied))
                .check(doesNotExist());
    }
}
