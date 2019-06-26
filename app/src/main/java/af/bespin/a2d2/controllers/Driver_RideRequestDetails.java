package af.bespin.a2d2.controllers;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AlertDialog.Builder;
import android.util.Pair;
import android.view.View;

import af.bespin.a2d2.R;
import af.bespin.a2d2.adapters.RideRequestDetailAdapter;
import af.bespin.a2d2.models.Request;
import af.bespin.a2d2.models.RequestStatus;
import af.bespin.a2d2.utilities.ActivityUtils;
import af.bespin.a2d2.utilities.AuthorizationUtils;
import af.bespin.a2d2.utilities.DataSourceUtils;
import af.bespin.a2d2.utilities.FormatUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class Driver_RideRequestDetails extends ButterKnifeActivity {
    @BindView(R.id.recyclerview_riderequestdetails_detaillist)
    RecyclerView detailList;
    @BindView(R.id.materialbutton_riderequestdetails_contactrider)
    MaterialButton contactRiderButton;
    @BindView(R.id.materialbutton_riderequestdetails_jobaction)
    MaterialButton jobActionButton;

    Request request;
    String currentDriver = AuthorizationUtils.getCurrentUser().getUid();
    Builder jobActionDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_ride_request_details);
        loadRequestData();
        configureView();
        configureJobActionDialog();
    }


    @OnClick(R.id.materialbutton_riderequestdetails_contactrider)
    protected void messageRider(){
        Uri phoneNumber = Uri.parse( FormatUtils.formatPhoneNumberToSMSUri(request.getPhone()));
        Pair<String, String> messageBody = new Pair<>("sms_body", getString(R.string.riderequestdetails_driver_messageToRider));
        ActivityUtils.navigateAway(this, phoneNumber, messageBody);
    }


    @OnClick(R.id.materialbutton_riderequestdetails_jobaction)
    protected void jobActionClicked() {
        jobActionDialog.show();
    }


    private void loadRequestData(){
        loadIntentData();
        populateDetails();
    }


    private void loadIntentData(){
        request = (Request) getIntent().getSerializableExtra("request");
    }


    private void populateDetails(){
        List<Pair<String, String>> details = getDetails(request);
        RideRequestDetailAdapter detailAdapter = new RideRequestDetailAdapter(details);
        LinearLayoutManager llmRequestManager = new LinearLayoutManager(this);

        detailList.setLayoutManager(llmRequestManager);
        detailList.setHasFixedSize(true);
        detailList.setAdapter(detailAdapter);
    }


    private ArrayList<Pair<String, String>> getDetails(Request request){
        ArrayList<Pair<String, String>> details = new ArrayList<>();

        details.add(new Pair<>("Status", request.getStatus().toString()));
        details.add(new Pair<>("Name", request.getName()));
        details.add(new Pair<>("Gender",  request.getGender()));
        details.add(new Pair<>("Group Size", Integer.toString(request.getGroupSize())));
        details.add(new Pair<>("Phone number", request.getPhone()));
        details.add(new Pair<>("Remarks", request.getRemarks()));

        return details;
    }


    private void configureView(){
        if(request.getStatus() == RequestStatus.InProgress && isCurrentUserDriver()){
            jobActionButton.setText(R.string.riderequestdetails_completejob);
        }
        else if(request.getStatus() == RequestStatus.Completed){
            contactRiderButton.setVisibility(View.GONE);
            jobActionButton.setVisibility(View.GONE);
        }
    }


    private void configureJobActionDialog(){
        DialogInterface.OnClickListener jobAction = (dialog, which) -> {
            if(request.getStatus() == RequestStatus.Available){
                takeJobActions();
            } else if (request.getStatus() == RequestStatus.InProgress){
                completeJobActions();
            }
        };

        jobActionDialog = ActivityUtils.newNotifyDialogBuilder(this);
        jobActionDialog.setTitle(getJobActionDialogTitle());
        jobActionDialog.setMessage(getJobActionDialogMessage());
        jobActionDialog.setPositiveButton(R.string.dialog_confirm, jobAction);
        jobActionDialog.setNegativeButton(R.string.cancel, null);
    }


    private String getJobActionDialogTitle(){
        if(request.getStatus() == RequestStatus.Available ||
                (request.getStatus() == RequestStatus.InProgress && !isCurrentUserDriver())){
            return "Confirm Pickup?";
        } else if (request.getStatus() == RequestStatus.InProgress && isCurrentUserDriver()){
            return "Confirm Dropoff?";
        }
        return "Something went wrong!";
    }


    private String getJobActionDialogMessage(){
        if(request.getStatus() == RequestStatus.Available){
            return "Are you sure you want to pickup this rider?";
        } else if (request.getStatus() == RequestStatus.InProgress && !isCurrentUserDriver()) {
            return "This job has already been taken by another driver. Are you sure you want to pickup this rider anyway?";
        } else if (request.getStatus() == RequestStatus.InProgress && isCurrentUserDriver()){
            return "Confirm you dropped off the rider?";
        }
        return "Something went wrong!";
    }


    private void takeJobActions(){
        request.setDriver(currentDriver);
        request.setStatus(RequestStatus.InProgress);
        updateRideRequest();
        configureJobActionDialog();
        startNavigationToRequester();
    }


    private void completeJobActions(){
        request.setStatus(RequestStatus.Completed);
        updateRideRequest();
        this.finish();
    }


    private void startNavigationToRequester(){
        String location = FormatUtils.formatLatLonToGoogleMapsUri(request.getLat(), request.getLon());
        Uri destination = Uri.parse(location);

        jobActionButton.setText(R.string.riderequestdetails_completejob);
        ActivityUtils.navigateAway(this, destination);
    }


    // Updates the displayed information and sends the current request data to the datasource
    private void updateRideRequest(){
        populateDetails(); // Update The screen for instant gratification
        DataSourceUtils.requests.updateData(request.key, request.getData());
    }


    private Boolean isCurrentUserDriver(){
        if(request.getDriver() == null){ return false; }
        return request.getDriver().equals(currentDriver);
    }
}
