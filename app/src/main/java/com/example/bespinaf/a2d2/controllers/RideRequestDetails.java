package com.example.bespinaf.a2d2.controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AlertDialog.Builder;
import android.util.Log;
import android.util.Pair;

import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.adapters.RideRequestDetailAdapter;
import com.example.bespinaf.a2d2.models.Request;
import com.example.bespinaf.a2d2.utilities.ActivityUtils;
import com.example.bespinaf.a2d2.utilities.AuthorizationUtils;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class RideRequestDetails extends ButterKnifeActivity {
    @BindView(R.id.recyclerview_riderequestdetails_detaillist)
    RecyclerView detailList;
    @BindView(R.id.materialbutton_riderequestdetails_contactrider)
    MaterialButton contactRiderButton;
    @BindView(R.id.materialbutton_riderequestdetails_takejob)
    MaterialButton takeJobButton;
    Request request;
    String requestId;

    Builder takeJobConfirmationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_ride_request_details);
        loadRequestData();

        takeJobConfirmationDialog = ActivityUtils.newNotifyDialogBuilder(this);

        takeJobConfirmationDialog.setTitle("Confirm Pickup?")
                .setPositiveButton("CONFIRM", (dialog, which) -> {
                    request.setDriver( AuthorizationUtils.getCurrentUser().getUid() );
                    request.setStatus("In Progress");

                    DataSourceUtils.updateData(requestId, request);

                    //TODO: Refactor NavigateAway and/or add another method to make navigation to other applications more useable
                    //String mapsLocation = String.format("google.navigation:q=%1$f, %2$f &avoid=tf", request.getLat(), request.getLon());
                    //Uri destination = Uri.parse(mapsLocation);

                    //Placeholder

                    String destination = String.format("google.navigation:q=%1$f, %2$f &avoid=tf", request.getLat(), request.getLon());
                    Uri latLon = Uri.parse(destination);

                    ActivityUtils.navigateAway(this, latLon);
                })
                .setNegativeButton("CANCEL", (dialog, which) -> {});
    }

    @Override
    protected void onPause(){
        super.onPause();
        populateDetails(getDetails(request));

    }

    
    @OnClick(R.id.materialbutton_riderequestdetails_contactrider)
    protected void messageRider(){
        Uri phoneNumber = Uri.parse( String.format("smsto:%s", request.getPhone()) );
        Pair<String, String> messageBody = new Pair<>("sms_body", "This is your A2D2 driver. I'm on my way!");

        ActivityUtils.navigateAway(this, phoneNumber, messageBody);
    }

    
    @OnClick(R.id.materialbutton_riderequestdetails_takejob)
    protected void takeJob() {
        String message = "";

        if(request.getStatus().equals("Available")){
            message = "Are you sure you want to pickup this rider?";
        } else if ( request.getStatus().equals("In Progress")) {
            message = "This job has already been taken by another driver. Are you sure you want to pickup this rider anyway?";
        }

        takeJobConfirmationDialog.setMessage(message)
                                  .show();
    }

    
    private void loadIntentData(){
        requestId = (String) getIntent().getSerializableExtra("requestId");
        request = (Request) getIntent().getSerializableExtra("request");
    }


    private ArrayList<Pair<String, String>> getDetails(Request request){
        ArrayList<Pair<String, String>> details = new ArrayList<>();

        details.add(new Pair<>("Status", request.getStatus()));
        details.add(new Pair<>("Name", request.getName()));
        details.add(new Pair<>("Gender",  request.getGender()));
        details.add(new Pair<>("Group Size", Integer.toString(request.getGroupSize())));
        details.add(new Pair<>("Phone number", request.getPhone()));
        details.add(new Pair<>("Remarks", request.getRemarks()));

        return details;
    }


    private void populateDetails(List<Pair<String, String>> details){
        RideRequestDetailAdapter detailAdapter = new RideRequestDetailAdapter(details);
        LinearLayoutManager llmRequestManager = new LinearLayoutManager(this);

        detailList.setLayoutManager(llmRequestManager);
        detailList.setHasFixedSize(true);
        detailList.setAdapter(detailAdapter);
    }


    private void loadRequestData(){
        loadIntentData();
        ArrayList<Pair<String, String>> details = getDetails(request);
        populateDetails(details);
    }
}
