package com.example.bespinaf.a2d2.controllers;


import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Pair;
import android.view.View;
import android.widget.Spinner;

import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.utilities.ActivityUtils;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;
import com.example.bespinaf.a2d2.models.Request;
import com.example.bespinaf.a2d2.utilities.FormatUtils;
import com.example.bespinaf.a2d2.utilities.LocationUtils;
import com.example.bespinaf.a2d2.utilities.Permissions;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;


public class RequestRide extends ButterKnifeActivity {

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


    private AlertDialog.Builder mDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_request_ride);

        mDialogBuilder = ActivityUtils.newNotifyDialogBuilder(this);
        initTextFieldLiveValidation();
    }


    private void initTextFieldLiveValidation() {
        mPhoneNumberEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setErrors();
            }
        });
        mNameEditText.addTextChangedListener(getTextWatcher());
    }


    @OnClick(R.id.button_request_driver)
    public void btnRequestDriver_Clicked(View view) {
        if(!isValidData()){
            return;
        } else if(!Permissions.hasLocationPermission(this)) {
            ActivityUtils.showLocationPermissionDeniedDialog(mDialogBuilder);
            return;
        }

        confirmRideRequest();
    }


    // check all fields and permissions
    private boolean isValidData() {
        setErrors();
        return mPhoneNumberTextLayout.getError() == null && mNameTextLayout.getError() == null;
    }


    private void confirmRideRequest() {
        buildConfirmationDialog().show();
    }


    private AlertDialog.Builder buildConfirmationDialog(){
        AlertDialog.Builder dialogBuilder = ActivityUtils.newNotifyDialogBuilder(this);

        DialogInterface.OnClickListener confirmEvent = (dialog, which) -> { submitRequest(); };
        DialogInterface.OnClickListener denyEvent = (dialog, which) -> {};

        dialogBuilder.setTitle(R.string.confirm_driver_request_title)
                .setMessage(R.string.confirm_driver_request_body)
                .setPositiveButton(R.string.dialog_okay, confirmEvent)
                .setNegativeButton(R.string.cancel, denyEvent);
        return dialogBuilder;
    }


    private void submitRequest() {
        //While it would be great if we could use getLastKnownLocation, we need a high degree of accuracy
        //to decrease the chances of the rider driving to an outdated location
        LocationUtils.getCurrentGPSLocationAsync(this, (location) -> {
            Request rideRequest = buildRideRequest();
            String requestId = DataSourceUtils.addRequest(rideRequest);

            Pair<String, Serializable> requestIdData = new Pair<>("requestId", requestId);
        	Pair<String, Serializable> requestData = new Pair<>("request", rideRequest);

        	ActivityUtils.navigateWithData(this, RideStatus.class, requestIdData, requestData );
        });
    }


    private Request buildRideRequest() {
        Request rideRequest = new Request();
        //Should get the latest possible location given that it's called after getting location
        Location currentLocation = LocationUtils.getLastKnownGPSLocation(this);
        //Takes care of edge case where additional pluses can be prepended ad infinitum
        String phoneNumberInput = mPhoneNumberEditText.getText().toString();
        String phoneNumber = formatNumberToE164(phoneNumberInput);

        rideRequest.setGroupSize(Integer.parseInt(mGroupSizeSpinner.getSelectedItem().toString()));
        rideRequest.setTimestamp(DataSourceUtils.getCurrentDateString());
        rideRequest.setGender(mGenderSpinner.getSelectedItem().toString());
        rideRequest.setName(mNameEditText.getText().toString());
        rideRequest.setPhone(phoneNumber);
        rideRequest.setRemarks(mRemarksEditText.getText().toString());
        rideRequest.setStatus(getString(R.string.request_ride_available));
        rideRequest.setLat(currentLocation.getLatitude());
        rideRequest.setLon(currentLocation.getLongitude());

        return rideRequest;
    }


    private String getNameError(){
        return (ActivityUtils.isFieldEmpty(mNameEditText)) ? getString(R.string.a2d2_field_required) : "";
    }


    private String getPhoneNumberError(){
        if(ActivityUtils.isFieldEmpty(mPhoneNumberEditText)){ return getString(R.string.a2d2_field_required); }

        String phoneNumber = mPhoneNumberEditText.getText() != null ? mPhoneNumberEditText.getText().toString() : "";
        String phoneDigits = FormatUtils.fetchDigitsFromString(phoneNumber);

        //Note: This appears to be work mostly as intended there is a client error where they can continuously preface with pluses. This does not affect actual data

        if(!isValidE164Number(phoneDigits)){
            return getString(R.string.error_phone_number);
        } else {
            return "";
        }
    }

    private boolean isValidE164Number(String phoneDigits){
        String DEFAULT_COUNTRY_CODE = "US";
        return PhoneNumberUtils.formatNumberToE164(phoneDigits, DEFAULT_COUNTRY_CODE) != null;
    }

    private String formatNumberToE164(String phoneDigits){
        String DEFAULT_COUNTRY_CODE = "US";
        return PhoneNumberUtils.formatNumberToE164(phoneDigits, DEFAULT_COUNTRY_CODE);
    }

    private void setErrors(){
        String phoneNumberError = getPhoneNumberError();
        String nameError = getNameError();

        mPhoneNumberTextLayout.setError(phoneNumberError);
        mNameTextLayout.setError(nameError);
    }


/**** These are ugly and I'm putting them in the basement ****/
    private TextWatcher getTextWatcher(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setErrors();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };
    }
}
