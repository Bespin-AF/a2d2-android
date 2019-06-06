package com.example.bespinaf.a2d2;

import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;

import com.example.bespinaf.a2d2.controllers.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class loading_bar_test {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private MainActivity mActivity;
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mRulesMonitor;
    private Instrumentation.ActivityMonitor mLoginMonitor;

    @Before
    public void setup(){
        mActivity = mMainActivityTestRule.getActivity();
    }

    @After
    public void teardown(){

    }

    @Test
    public void test(){
        assert(true);
    }
}
