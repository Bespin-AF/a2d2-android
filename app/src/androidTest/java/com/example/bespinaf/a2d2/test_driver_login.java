package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.design.button.MaterialButton;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;

import com.example.bespinaf.a2d2.controllers.DriverLogin;
import com.example.bespinaf.a2d2.controllers.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

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
        mDevice = UiDevice.getInstance(mInstrumentation);
        mDriverLoginMonitor = mInstrumentation.addMonitor(DriverLogin.class.getName(), null, false);
    }

    @Test
    public void hasActivityLaunched() {
        assertNotNull(mActivity);
    }

    @Test
    public void driverLoginButtonExists(){
        mButtonDriverLogin = mActivity.findViewById(R.id.button_main_driver_login);
        assertNotNull(mButtonDriverLogin);
    }

    @Test
    public void buttonDriverLoginClicked_NavigateToDriverLogin(){
        mButtonDriverLogin = mActivity.findViewById(R.id.button_main_driver_login);

        mInstrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mButtonDriverLogin.performClick();
            }
        });

        mDriverLoginActivity = mInstrumentation.waitForMonitorWithTimeout(mDriverLoginMonitor, 1000);
        assertNotNull(mDriverLoginActivity);
    }

}
