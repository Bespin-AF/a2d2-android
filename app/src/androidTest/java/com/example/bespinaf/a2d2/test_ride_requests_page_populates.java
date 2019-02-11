package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.widget.ListView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static org.hamcrest.core.AllOf.allOf;

public class test_ride_requests_page_populates {
    @Rule
    public ActivityTestRule<RideRequests> mRideRequestsActivity = new
            ActivityTestRule<>(RideRequests.class);

    private Instrumentation mInstrumentation;

    @Before
    public void setUp(){
        mInstrumentation = getInstrumentation();
    }

    @Test
    public void activityDidLoad(){
        Activity activity = mRideRequestsActivity.getActivity();
        Assert.assertNotNull(activity);
    }

    @Test
    public void recyclerViewsHaveTitles(){
        onView(allOf(ViewMatchers.withText(R.string.request_ride_available),
                ViewMatchers.withId(R.string.request_ride_in_progress),
                ViewMatchers.withId(R.string.request_ride_completed)));
    }

    @Test
    public void recyclerViewsDoExist(){
        onView(allOf(ViewMatchers.withId(R.id.ride_requests_available_recycler_view),
                ViewMatchers.withId(R.id.ride_requests_in_progress_recycler_view),
                ViewMatchers.withId(R.id.ride_requests_completed_recycler_view)));
    }

    @Test
    public void requestsDidPopulate(){

    }
}
