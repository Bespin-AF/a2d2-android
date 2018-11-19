package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
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
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class testUserDeniesPermissions {
    @Rule
    public ActivityTestRule<Rules> ruleActivity = new ActivityTestRule<>(Rules.class);

    public Rules activity;
    public UiDevice device;
    public Instrumentation mInstrumentation;
    public Instrumentation.ActivityMonitor mRulesMonitor;

    //Indexes are language agnostic
    private static final int DENY_PERMISSION = 0;
    private static final int GRANT_PERMISSION = 1;

    @Before
    public void setUp(){
        mInstrumentation = getInstrumentation();
        mRulesMonitor = mInstrumentation.addMonitor(RequestRide.class.getName(),null,false);
        activity = ruleActivity.getActivity();
        device = UiDevice.getInstance(getInstrumentation());
    }

    @Test
    public void hasActivity(){
        assertNotNull(activity);
    }

    @Test
    public void hasRulesText(){
        Context appContext = activity.getApplicationContext();
        String rulesText = appContext.getResources().getString(R.string.a2d2_rules_text);
        final UiObject rulesTextView = device.findObject(new UiSelector().text(rulesText));
        assertTrue(rulesTextView.exists());
    }

    @Test
    public void hasButton(){
        Button testButtonAgree = activity.findViewById(R.id.button_rules_agree);
        assertNotNull(testButtonAgree);
    }

    @Test
    public void canUserAgree() {
        final Button testButtonAgree = activity.findViewById(R.id.button_rules_agree);
        assertNotNull(testButtonAgree);

        activity.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                testButtonAgree.performClick();
            }
        });

        final UiObject buttonDenyLocationPermission = device.findObject(
                new UiSelector()
                        .clickable(true)
                        .checkable(false)
                        .index(DENY_PERMISSION)
        );

        assertTrue(buttonDenyLocationPermission.exists());

        //If the button doesn't exist I'm not sure how it would've gotten past the above point. However, the click is run in the ui thread so some weird scoping stuff is happening

        activity.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                try {
                    buttonDenyLocationPermission.click();
                } catch (UiObjectNotFoundException e){
                    //What do I do here?
                }
            }
        });

        Espresso.onView(ViewMatchers.withText(R.string.error_LocationPermissionDenied))
                .inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }
}
