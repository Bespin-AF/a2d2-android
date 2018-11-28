package com.example.bespinaf.a2d2;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class test_enter_group_size {
    @Rule
    public ActivityTestRule<RequestRide> mRequestRideActivity= new ActivityTestRule<>(RequestRide.class);

    private RequestRide mActivity;
    private int groupSizeId = 0;

    @Before
    public void setUp(){
        mActivity = mRequestRideActivity.getActivity();
    }

    @Test
    public void hasActivityLoaded(){ assertNotNull(mActivity);  }

    @Test
    public void hasGroupLabel(){
        onView(withText(R.string.label_group_size));
    }

    @Test
    public void hasDropDownList(){
        onView(ViewMatchers.withId(groupSizeId));
    }

    @Test
    public void groupSizeHasMinOneAndMaxOfFour(){
        for(int i = 1; i < 5; i++){
            onView(ViewMatchers.withId(R.id.spinner_group_size)).perform(click());
            Espresso.onData(
                    allOf(
                            is(instanceOf(String.class)),
                            is( Integer.toString(i) )
                    )
            ).perform(click());

            onView(ViewMatchers
                    .withId(R.id.spinner_group_size))
                    .check(matches(withSpinnerText(containsString(Integer.toString(i)))));
        }
    }
}
