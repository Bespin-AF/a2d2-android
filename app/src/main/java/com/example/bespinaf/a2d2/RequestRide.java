package com.example.bespinaf.a2d2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.bespinaf.a2d2.utilities.Request;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.bespinaf.a2d2.R.style.Theme_AppCompat_Light_Dialog_Alert;

public class RequestRide extends AppCompatActivity implements LocationListener {

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
        setContentView(R.layout.activity_request_ride);
        ButterKnife.bind(this);

        mDialogBuilder = new AlertDialog.Builder(this, Theme_AppCompat_Light_Dialog_Alert);

        initTextFieldLiveValidation();
        startLocationSync();
    }

    private void initTextFieldLiveValidation() {
        mPhoneNumberEditText.addTextChangedListener(getPhoneFieldWatcher());
        mNameEditText.addTextChangedListener(getNameFieldWatcher());
    }

    @OnClick(R.id.button_request_driver)
    public void btnRequestDriver_Clicked(View view) {
        if (isValidData()) {
            confirmRideRequest();
        }
    }

    // check all fields and permissions
    private boolean isValidData() {
        return (hasLocationPermission() && isTextViewNotEmpty(mNameEditText) && isTextViewNotEmpty(mPhoneNumberEditText));
    }

    private boolean hasLocationPermission() {
        // Permission NOT Granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            String title = getString(R.string.dialog_title_LocationPermissionDenied);
            String message = getString(R.string.error_LocationPermissionDenied);
            notify(title, message);
            return false;
        }
        return true;
    }

    private void confirmRideRequest() {
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this, Theme_AppCompat_Light_Dialog_Alert);

        mDialogBuilder.setTitle(R.string.confirm_driver_request_title)
                .setMessage(R.string.confirm_driver_request_body)
                .setPositiveButton("OKAY", (dialog, which) -> {
                    submitRequest();
                }).setNegativeButton(R.string.cancel, (dialog, which) -> {
            //Some kinda user feedback
        }).show();
    }

    private void submitRequest() {
        Request rideRequest = buildRideRequest();
        DataSourceUtils.sendData(rideRequest);
        goToStatusPage();
    }

    private boolean isTextViewNotEmpty(EditText editText) {
        return (editText.getText().length() > 0);
    }

    private Request buildRideRequest() {
        Request rideRequest = new Request();

        rideRequest.setGroupSize(Integer.parseInt(mGroupSizeSpinner.getSelectedItem().toString()));
        rideRequest.setTimestamp(getDateString());
        rideRequest.setGender(mGenderSpinner.getSelectedItem().toString());
        rideRequest.setName(mNameEditText.getText().toString());
        rideRequest.setPhone(mPhoneNumberEditText.getText().toString());
        rideRequest.setRemarks(mRemarksEditText.getText().toString());
        rideRequest.setStatus("Available");
        rideRequest.setLat(mLatitude);
        rideRequest.setLon(mLongitude);

        return rideRequest;
    }

    private String getDateString(){
        //Setting the format of the date field -- can be abstracted
        SimpleDateFormat sdfOurFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss +SSSS");
        return sdfOurFormat.format(Calendar.getInstance().getTime());
    }

    private void startLocationSync() {
        LocationManager ourLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        ourLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    private void goToStatusPage(){
        Intent intent = new Intent(this, RideStatus.class);
        startActivity(intent);
    }

    //Methods handling location listener feedback
    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
    }


    private void notify(String title, String message){
        mDialogBuilder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OKAY", ((dialog, which) -> { }))
                .show();
    }

    private void setNameError(){
        if (!isTextViewNotEmpty(mNameEditText)) {
            mNameTextLayout.setError(getString(R.string.a2d2_field_required));
        } else {
            mNameTextLayout.setError("");
        }
    }

    private void setPhoneNumberError(){
        if (!isTextViewNotEmpty(mPhoneNumberEditText)) {
            mPhoneNumberTextLayout.setError(getString(R.string.a2d2_field_required));
        } else if (mPhoneNumberEditText.length() < 10) {
            mPhoneNumberTextLayout.setError(getString(R.string.error_phone_number));
        } else {
            mPhoneNumberTextLayout.setError("");
        }
    }


/*** These are ugly and I'm putting it in the basement ****/
    private TextWatcher getNameFieldWatcher(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setNameError();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };
    }

    private TextWatcher getPhoneFieldWatcher(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setPhoneNumberError();
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
