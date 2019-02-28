package com.example.bespinaf.a2d2.controllers;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Spinner;

import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.utilities.ActivityUtils;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;
import com.example.bespinaf.a2d2.models.Request;

import butterknife.BindView;
import butterknife.OnClick;


public class RequestRide extends ButterKnifeActivity implements LocationListener {

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

    private double mLatitude;
    private double mLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_request_ride);

        mDialogBuilder = ActivityUtils.newNotifyDialogBuilder(this);
        initTextFieldLiveValidation();
        startLocationSync();
    }

    private void initTextFieldLiveValidation() {
        mPhoneNumberEditText.addTextChangedListener(getTextWatcher());
        mNameEditText.addTextChangedListener(getTextWatcher());
    }

    @OnClick(R.id.button_request_driver)
    public void btnRequestDriver_Clicked(View view) {
        if(!isValidData()){
            return;
        } else if(!hasLocationPermission()) {
            ActivityUtils.showDialog(mDialogBuilder,
                    R.string.dialog_title_LocationPermissionDenied,
                    R.string.error_LocationPermissionDenied);
            return;
        }

        confirmRideRequest();
    }

    // check all fields and permissions
    private boolean isValidData() {
        setErrors();
        return mPhoneNumberTextLayout.getError() == null && mNameTextLayout.getError() == null;
    }

    private boolean hasLocationPermission() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private void confirmRideRequest() {
        AlertDialog.Builder dialogBuilder = ActivityUtils.newNotifyDialogBuilder(this);

        DialogInterface.OnClickListener confirmEvent = (dialog, which) -> { submitRequest(); };
        DialogInterface.OnClickListener denyEvent = (dialog, which) -> {};

        dialogBuilder.setTitle(R.string.confirm_driver_request_title)
                .setMessage(R.string.confirm_driver_request_body)
                .setPositiveButton(R.string.dialog_okay, confirmEvent)
                .setNegativeButton(R.string.cancel, denyEvent)
                .show();
    }

    private void submitRequest() {
        Request rideRequest = buildRideRequest();
        DataSourceUtils.sendData(rideRequest);
        ActivityUtils.navigate(this, RideStatus.class);
    }

    private Request buildRideRequest() {
        Request rideRequest = new Request();

        rideRequest.setGroupSize(Integer.parseInt(mGroupSizeSpinner.getSelectedItem().toString()));
        rideRequest.setTimestamp(DataSourceUtils.getCurrentDateString());
        rideRequest.setGender(mGenderSpinner.getSelectedItem().toString());
        rideRequest.setName(mNameEditText.getText().toString());
        rideRequest.setPhone(mPhoneNumberEditText.getText().toString());
        rideRequest.setRemarks(mRemarksEditText.getText().toString());
        rideRequest.setStatus(getString(R.string.request_ride_available));
        rideRequest.setLat(mLatitude);
        rideRequest.setLon(mLongitude);

        return rideRequest;
    }


    private void startLocationSync() {
        LocationManager ourLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        ourLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    //Methods handling location listener feedback
    @Override
    public void onLocationChanged(@NonNull Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
    }

    private String getNameError(){
        return (ActivityUtils.isFieldEmpty(mNameEditText)) ? getString(R.string.a2d2_field_required) : "";
    }

    private String getPhoneNumberError(){
        if (ActivityUtils.isFieldEmpty(mPhoneNumberEditText)) {
            return getString(R.string.a2d2_field_required);
        } else if (mPhoneNumberEditText.length() < 10) {
            return getString(R.string.error_phone_number);
        } else {
            return "";
        }
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

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }
}
