package com.example.bespinaf.a2d2.controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
                    DataSourceUtils.updateRequestStatus(requestId, "In Progress");
                    DataSourceUtils.updateDriver(requestId, AuthorizationUtils.getCurrentUser().getUid());
                    DataSourceUtils.updateData(requestId, request);
                    ActivityUtils.openMaps(this, request.getLat(), request.getLon());
                })
                .setNegativeButton("CANCEL", (dialog, which) -> {});
    }

    
    @OnClick(R.id.materialbutton_riderequestdetails_contactrider)
    protected void messageRider(){
        Uri telephoneNumber = Uri.parse( String.format("smsto:%s", request.getPhone()) );
        Pair<String, String>[] extraData = new Pair[]{
                new Pair<>("sms_body","This is your A2D2 driver. I'm on my way!"),
        };
        
        ActivityUtils.navigateAwayWithData(this, telephoneNumber, Intent.ACTION_SENDTO, extraData);
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

        //ActivityUtils.openMaps(this, request.getLat(), request.getLon());
    }

    
    private String retrieveRequestFromIntent(){
        return (String) getIntent().getSerializableExtra("request");
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
        requestId = retrieveRequestFromIntent();
        request = DataSourceUtils.getRequestById(requestId);
        ArrayList<Pair<String, String>> details = getDetails(request);

        populateDetails(details);
    }
}
