package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.design.button.MaterialButton;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.view.View;

import com.example.bespinaf.a2d2.controllers.DriverLogin;
import com.example.bespinaf.a2d2.controllers.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.action.ViewActions.click;
import static org.junit.Assert.assertNotNull;
import static android.support.test.espresso.Espresso.onView;

public class test_driver_login {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivity = new ActivityTestRule<>(MainActivity.class);

    private MainActivity mActivity;
    private Activity mDriverLoginActivity;
    private UiDevice mDevice;
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mDriverLoginMonitor;
    private MaterialButton mButtonDriverLogin;

    /**
     * Creates monitor for Rules page and creates Main Activity
     */
    @Before
    public void setUp() {
        mActivity = mMainActivity.getActivity();
        mInstrumentation = getInstrumentation();
        mDriverLoginMonitor = mInstrumentation.addMonitor(DriverLogin.class.getName(), null, false);
    }

    @Test
    public void hasActivityLaunched() {
        assertNotNull(mActivity);
    }

    @Test
    public void driverLoginButtonExists(){
        onView(ViewMatchers.withId(R.id.button_main_driver_login));
    }

    @Test
    public void buttonDriverLoginClicked_NavigateToDriverLogin(){
        onView(ViewMatchers.withId(R.id.button_main_driver_login)).perform(click());

        mDriverLoginActivity = mInstrumentation.waitForMonitorWithTimeout(mDriverLoginMonitor, 1000);
        assertNotNull(mDriverLoginActivity);
    }

}
