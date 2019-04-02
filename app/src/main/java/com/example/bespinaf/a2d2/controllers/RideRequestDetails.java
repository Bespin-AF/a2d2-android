package com.example.bespinaf.a2d2.controllers;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AlertDialog.Builder;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.adapters.RideRequestDetailAdapter;
import com.example.bespinaf.a2d2.models.Request;
import com.example.bespinaf.a2d2.utilities.ActivityUtils;
import com.example.bespinaf.a2d2.utilities.AuthorizationUtils;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RideRequestDetails extends ButterKnifeActivity {
    @BindView(R.id.recyclerview_riderequestdetails_detaillist)
    RecyclerView detailList;
    @BindView(R.id.materialbutton_riderequestdetails_contactrider)
    MaterialButton contactRiderButton;
    @BindView(R.id.materialbutton_riderequestdetails_jobaction)
    MaterialButton jobActionButton;
    Request request;
    String requestId;
    String MAPS_URI_FORMAT = "google.navigation:q=%1$f, %2$f &avoid=tf";

    Builder takeJobConfirmationDialog;
    DialogInterface.OnClickListener confirmPickup = (dialog, which) -> {
        String updatedStatus = getUpdatedStatus(request.getStatus());

        request.setStatus(updatedStatus);
        request.setDriver(AuthorizationUtils.getCurrentUser().getUid());

        DataSourceUtils.updateRequest(requestId, request);

        if(updatedStatus.equals("In Progress")){
            String destination = String.format(MAPS_URI_FORMAT, request.getLat(), request.getLon());
            Uri latLon = Uri.parse(destination);

            jobActionButton.setText(R.string.riderequestdetails_completejob);
            ActivityUtils.navigateAway(this, latLon);
        } else if (updatedStatus.equals("Completed")){
            this.finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_ride_request_details);
        loadRequestData();
        setupView();

        takeJobConfirmationDialog = ActivityUtils.newNotifyDialogBuilder(this);

        takeJobConfirmationDialog.setTitle("Confirm Pickup?")
                .setPositiveButton("CONFIRM", confirmPickup)
                .setNegativeButton("CANCEL", (dialog, which) -> {});
    }


    @Override
    protected void onPause(){
        super.onPause();
        populateDetails(getDetails(request));
    }


    private Boolean isCurrentUserDriver(){
        if(request.getDriver() == null){ return false; }
        return request.getDriver().equals(AuthorizationUtils.getCurrentUser().getUid());
    }


    private String getUpdatedStatus(String currentStatus){
        if(currentStatus.equals("Available")){
            return "In Progress";
        } else if (isCurrentUserDriver()){
            return "Completed";
        } else {
            return currentStatus;
        }
    }

    @OnClick(R.id.materialbutton_riderequestdetails_contactrider)
    protected void messageRider(){
        Uri phoneNumber = Uri.parse( String.format("smsto:%s", request.getPhone()) );
        Pair<String, String> messageBody = new Pair<>("sms_body", "This is your A2D2 driver. I'm on my way!");

        ActivityUtils.navigateAway(this, phoneNumber, messageBody);
    }

    
    @OnClick(R.id.materialbutton_riderequestdetails_jobaction)
    protected void takeJob() {
        String status = request.getStatus();

        String message = getConfirmationMessage(status, isCurrentUserDriver());

        takeJobConfirmationDialog.setMessage(message)
                                  .show();
    }


    private String getConfirmationMessage(String status, boolean isCurrentUserDriver){
        if(status.equals("Available")){
            return "Are you sure you want to pickup this rider?";
        } else if (isCurrentUserDriver){
            return "Confirm you dropped off the rider?";
        } else {
            return "This job has already been taken by another driver. Are you sure you want to pickup this rider anyway?";
        }
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


    private void setupView(){
        if(request.getStatus().equals("In Progress") && isCurrentUserDriver()){
            jobActionButton.setText(R.string.riderequestdetails_completejob);
        }
        else if(request.getStatus().equals("Completed")){
            contactRiderButton.setVisibility(View.GONE);
            jobActionButton.setVisibility(View.GONE);
        }
    }
}
