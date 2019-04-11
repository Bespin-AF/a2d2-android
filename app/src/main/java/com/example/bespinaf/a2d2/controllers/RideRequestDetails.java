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
import com.example.bespinaf.a2d2.utilities.FormatUtils;

import java.text.Format;
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
    String currentDriver = AuthorizationUtils.getCurrentUser().getUid();
    Builder takeJobConfirmationDialog;
    DialogInterface.OnClickListener jobAction = (dialog, which) -> {
        updateRequestDetails();

        if(request.getStatus().equals("In Progress")){
            startNavigationToRequester();
        } else if (request.getStatus().equals("Completed")){
            this.finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_ride_request_details);
        loadRequestData();
        configureView();

        takeJobConfirmationDialog = ActivityUtils.newNotifyDialogBuilder(this)
                                            .setTitle("Confirm Pickup?")
                                            .setPositiveButton(R.string.dialog_confirm, jobAction)
                                            .setNegativeButton(R.string.cancel, null);
    }


    //Ensures drivers have updated data when they return
    @Override
    protected void onPause(){
        super.onPause();
        populateDetails(getDetails(request));
    }


    @OnClick(R.id.materialbutton_riderequestdetails_contactrider)
    protected void messageRider(){
        Uri phoneNumber = Uri.parse( FormatUtils.formatPhoneNumberToSMSUri(request.getPhone()) );
        Pair<String, String> messageBody = new Pair<>("sms_body", getString(R.string.riderequestdetails_driver_messageToRider));

        ActivityUtils.navigateAway(this, phoneNumber, messageBody);
    }


    @OnClick(R.id.materialbutton_riderequestdetails_jobaction)
    protected void takeJob() {
        String message = getConfirmationMessage(request.getStatus(), isCurrentUserDriver());

        takeJobConfirmationDialog.setMessage(message)
                .show();
    }


    private void loadRequestData(){
        loadIntentData();
        populateDetails(getDetails(request));
    }


    private void loadIntentData(){
        requestId = (String) getIntent().getSerializableExtra("requestId");
        request = (Request) getIntent().getSerializableExtra("request");
    }


    private void populateDetails(List<Pair<String, String>> details){
        RideRequestDetailAdapter detailAdapter = new RideRequestDetailAdapter(details);
        LinearLayoutManager llmRequestManager = new LinearLayoutManager(this);

        detailList.setLayoutManager(llmRequestManager);
        detailList.setHasFixedSize(true);
        detailList.setAdapter(detailAdapter);
    }


    private void configureView(){
        if(request.getStatus().equals("In Progress") && isCurrentUserDriver()){
            jobActionButton.setText(R.string.riderequestdetails_completejob);
        }
        else if(request.getStatus().equals("Completed")){
            contactRiderButton.setVisibility(View.GONE);
            jobActionButton.setVisibility(View.GONE);
        }
    }


    private void updateRequestDetails(){
        String updatedStatus = getUpdatedStatus(request.getStatus());

        request.setStatus(updatedStatus);
        request.setDriver(currentDriver);

        DataSourceUtils.updateRequest(requestId, request);
    }


    private void startNavigationToRequester(){
        String location = FormatUtils.formatLatLonToGoogleMapsUri(request.getLat(), request.getLon());
        Uri destination = Uri.parse(location);

        jobActionButton.setText(R.string.riderequestdetails_completejob);
        ActivityUtils.navigateAway(this, destination);
    }

    /** In page utilities **/

    private Boolean isCurrentUserDriver(){
        if(request.getDriver() == null){ return false; }
        return request.getDriver().equals(currentDriver);
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


    private String getConfirmationMessage(String status, boolean isCurrentUserDriver){
        if(status.equals("Available")){
            return "Are you sure you want to pickup this rider?";
        } else if (isCurrentUserDriver){
            return "Confirm you dropped off the rider?";
        } else {
            return "This job has already been taken by another driver. Are you sure you want to pickup this rider anyway?";
        }
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
}
