package com.example.bespinaf.a2d2;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;
import android.widget.Button;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import static org.junit.Assert.*;

public class RulesTest {
    @Rule
    public ActivityTestRule<Rules> ruleActivity = new ActivityTestRule<>(Rules.class);

    public Rules activity;

    @Before
    public void setUp(){
        activity = ruleActivity.getActivity();
    }


    @Test
    public void canAccessLocation(){

    }
}