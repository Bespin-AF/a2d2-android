package af.bespin.a2d2.controllers;


import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

<<<<<<< HEAD:app/src/main/java/com/example/bespinaf/a2d2/controllers/Rider_RequestRide.java
import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.models.RequestStatus;
import com.example.bespinaf.a2d2.utilities.ActivityUtils;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;
import com.example.bespinaf.a2d2.models.Request;
import com.example.bespinaf.a2d2.utilities.FormatUtils;
import com.example.bespinaf.a2d2.utilities.LocationUtils;
import com.example.bespinaf.a2d2.utilities.NetworkUtils;
import com.example.bespinaf.a2d2.utilities.Permissions;
=======
import af.bespin.a2d2.R;
import af.bespin.a2d2.models.RequestStatus;
import af.bespin.a2d2.utilities.ActivityUtils;
import af.bespin.a2d2.utilities.DataSourceUtils;
import af.bespin.a2d2.models.Request;
import af.bespin.a2d2.utilities.FormatUtils;
import af.bespin.a2d2.utilities.LocationUtils;
import af.bespin.a2d2.utilities.Permissions;
>>>>>>> release/1.0:app/src/main/java/af/bespin/a2d2/controllers/Rider_RequestRide.java

import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;


public class Rider_RequestRide extends ButterKnifeActivity {

    @BindView(R.id.activity_ride_request_name_text_edit)
    TextInputEditText mNameEditText;
    @BindView(R.id.activity_request_ride_name_text_input_layout)
    TextInputLayout mNameTextLayout;
    @BindView(R.id.activity_ride_request_phone_number_text_edit)
    TextInputEditText mPhoneNumberEditText;
    @BindView(R.id.activity_request_ride_phone_number_text_input_layout)
    TextInputLayout mPhoneNumberTextLayout;
    @BindView(R.id.gender_spinner)
    Spinner mGenderSpinner;
    @BindView(R.id.group_size_spinner)
    Spinner mGroupSizeSpinner;
    @BindView(R.id.request_ride_remarks_edit_text)
    TextInputEditText mRemarksEditText;
    @BindView(R.id.button_request_driver)
    MaterialButton buttonRequestDriver;
    @BindView(R.id.ride_request_progress_bar)
    ProgressBar rideRequestProgressBar;

    private AlertDialog.Builder mErrorDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_request_ride);

        mErrorDialog = ActivityUtils.newNotifyDialogBuilder(this);
        initializeTextFieldLiveValidation();
    }


    private void initializeTextFieldLiveValidation() {
        mPhoneNumberEditText.addTextChangedListener(
                getPhoneNumberFormattingTextWatcher(()-> mPhoneNumberTextLayout.setError(getPhoneNumberError()))
        );

        mNameEditText.addTextChangedListener(
                getTextWatcher(()-> mNameTextLayout.setError(getNameError()) )
        );
    }


    @OnClick(R.id.button_request_driver)
    public void btnRequestDriver_Clicked(View view) {
        //rideRequestProgressBar.setVisibility(View.VISIBLE);
        if(!isValidData()){
            //rideRequestProgressBar.setVisibility(View.INVISIBLE);
            return;
        } else if(!Permissions.hasLocationPermission(this)) {
            ActivityUtils.showLocationPermissionDeniedDialog(mErrorDialog);
            //rideRequestProgressBar.setVisibility(View.INVISIBLE);
            return;
        }

        confirmRideRequest();
        rideRequestProgressBar.setVisibility(View.VISIBLE);
    }


    //Checks all fields for errors
    private boolean isValidData() {
        setErrors();
        return mPhoneNumberTextLayout.getError() == null && mNameTextLayout.getError() == null;
    }


    private void confirmRideRequest() {
        buildConfirmationDialog().show();
    }


    private AlertDialog.Builder buildConfirmationDialog(){
        AlertDialog.Builder dialogBuilder = ActivityUtils.newNotifyDialogBuilder(this);

        DialogInterface.OnClickListener confirmEvent = (dialog, which) -> submitRequest();
        DialogInterface.OnClickListener cancelEvent = (dialog, which) -> rideRequestProgressBar.
                setVisibility(View.INVISIBLE);

        if (NetworkUtils.checkInternetConnectivity(getApplicationContext())) {
            dialogBuilder.setTitle(R.string.confirm_driver_request_title)
                    .setMessage(R.string.confirm_driver_request_body)
                    .setPositiveButton(R.string.dialog_okay, confirmEvent)
                    .setNegativeButton(R.string.cancel, cancelEvent);
            return dialogBuilder;
        }
        else {
            dialogBuilder.setTitle(R.string.connection_error_title)
                    .setMessage(R.string.connection_error)
                    .setPositiveButton(R.string.dialog_okay, cancelEvent);
            return dialogBuilder;
        }
    }


    //Performs a location sync in order to maximize accuracy vs getLastKnownLocation
    private void submitRequest() {
        rideRequestProgressBar.setVisibility(View.VISIBLE);
        LocationUtils.getCurrentGPSLocationAsync(this, (location) -> {
            Request rideRequest = buildRideRequest(location);
            String requestId = DataSourceUtils.requests.sendData(rideRequest.getData());
            rideRequest.key = requestId;
        	Pair<String, Serializable> requestData = new Pair<>("request", rideRequest);
        	ActivityUtils.navigateWithData(this, Rider_RideStatus.class, requestData );
        });
    }


    private Request buildRideRequest(Location currentLocation){
        String name = ActivityUtils.getFieldText(mNameEditText);
        String phoneNumber = ActivityUtils.getFieldText(mPhoneNumberEditText);
        String groupSize = mGroupSizeSpinner.getSelectedItem().toString();
        String gender = mGenderSpinner.getSelectedItem().toString();
        String remarks = ActivityUtils.getFieldText(mRemarksEditText);
        String currentDate = DataSourceUtils.getCurrentDateString();

        Request request = new Request();
        request.setName(name);
        request.setPhone(FormatUtils.formatPhoneNumberToE164(phoneNumber));
        request.setGroupSize(Integer.parseInt(groupSize));
        request.setGender(gender);
        request.setRemarks(remarks);
        request.setTimestamp(currentDate);
        request.setLat(currentLocation.getLatitude());
        request.setLon(currentLocation.getLongitude());
        request.setStatus(RequestStatus.Available);

        rideRequestProgressBar.setVisibility(View.INVISIBLE);
        return request;
    }


    private String getNameError(){
        return (ActivityUtils.isFieldEmpty(mNameEditText)) ? getString(R.string.a2d2_field_required) : "";
    }


    /*
        This appears to be work mostly as intended; there is a client error where they can continuously preface with pluses.
        When submitted, it is normalized to conform to E164.
        It is not compared to E164 format because it's more intuitive for some users to see e.g. (123) 456-7890
    */
    private String getPhoneNumberError(){
        String phoneNumber = ActivityUtils.getFieldText(mPhoneNumberEditText);
        if(phoneNumber == null || ActivityUtils.isFieldEmpty(mPhoneNumberEditText)){ return getString(R.string.a2d2_field_required); }

        String phoneDigits = FormatUtils.fetchDigitsFromString(phoneNumber);

        if(!FormatUtils.isValidE164PhoneNumberFormat(phoneDigits)){ return getString(R.string.error_phone_number);  }
        else { return ""; }
    }


    private void setErrors(){
        String phoneNumberError = getPhoneNumberError();
        String nameError = getNameError();

        mPhoneNumberTextLayout.setError(phoneNumberError);
        mNameTextLayout.setError(nameError);
    }


    /**** Text Watcher constructors : these watch for changes to text fields ****/
    private interface onTextChanged { void then(); }


    private PhoneNumberFormattingTextWatcher getPhoneNumberFormattingTextWatcher(onTextChanged textChangedHandler){
        return new PhoneNumberFormattingTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(textChangedHandler != null) { textChangedHandler.then(); }
            }
        };
    }


    private TextWatcher getTextWatcher(onTextChanged textChangedHandler){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(textChangedHandler != null) { textChangedHandler.then(); }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };
    }
}
