package com.example.bespinaf.a2d2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bespinaf.a2d2.utilities.Messaging;
import com.example.bespinaf.a2d2.utilities.Request;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RideRequestDetailsActivity extends AppCompatActivity {

    Request rideRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_request_details);
        ButterKnife.bind(this);

        rideRequest = getIntent().getParcelableExtra("rideRequest");
    }

    @OnClick(R.id.button_message_driver_ride_request_details)
    protected void messageDriver(){
        if(rideRequest == null){
            return;
        }

        Messaging.openMessagingApplicationWithMessage(this, rideRequest.getPhone(), getString(R.string.all_default_message_from_driver));
    }
}
