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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Spinner;

import com.example.bespinaf.a2d2.utilities.Request;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRequestsReference;

    private AlertDialog.Builder mDialogBuilder;

    private double mLatitude;
    private double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_ride);
        ButterKnife.bind(this);

        mDialogBuilder = new AlertDialog.Builder(this, Theme_AppCompat_Light_Dialog_Alert);

        //set up database connection
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRequestsReference = mFirebaseDatabase.getReference().child("requests");

        mPhoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mPhoneNumberEditText.getText().length() == 0) {
                    mPhoneNumberTextLayout.setError(getString(R.string.a2d2_field_required));
                } else if (mPhoneNumberEditText.length() < 10) {
                    mPhoneNumberTextLayout.setError(getString(R.string.error_phone_number));
                } else {
                    mPhoneNumberTextLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mNameEditText.getText().length() == 0) {
                    mNameTextLayout.setError(getString(R.string.a2d2_field_required));
                } else {
                    mNameTextLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

    }
    @OnClick(R.id.button_request_driver)
    public void btnRequestDriver_Clicked(View view) {
        boolean isInputValid = true;
        String strGroupSize = mGroupSizeSpinner.getSelectedItem().toString();
        int intGroupSize = Integer.parseInt(strGroupSize);

        //Setting the format of the date field
        SimpleDateFormat sdfOurFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss +SSSS");
        String strCurrentDate = sdfOurFormat.format(Calendar.getInstance().getTime());

        //IS THIS OK HERE OR DO WE NEED TO OPEN ON CREATE
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            mDialogBuilder.setTitle(R.string.dialog_title_LocationPermissionDenied)
                    .setMessage(R.string.error_LocationPermissionDenied)
                    .setPositiveButton("OKAY", ((dialog, which) -> {
                    }))
                    .show();
            return;
        }

        LocationManager ourLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ourLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);

        //TODO: MAKE THIS A SEPARATE METHOD
        //checks the phone number field to make sure that the data is valid
        if (mPhoneNumberEditText.length() == 0) {
            mPhoneNumberTextLayout.setError(getString(R.string.a2d2_field_required));
            isInputValid = false;
        } else if (mPhoneNumberEditText.length() < 10) {
            mPhoneNumberTextLayout.setError(getString(R.string.error_phone_number));
            isInputValid = false;
        }

        if (mNameEditText.length() == 0) {
            mNameTextLayout.setError(getString(R.string.a2d2_field_required));
            isInputValid = false;
        }

        if (!isInputValid) {
            return;
        }

        mPhoneNumberTextLayout.setError("");
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this, Theme_AppCompat_Light_Dialog_Alert);

        mDialogBuilder.setTitle(R.string.confirm_driver_request_title)
                .setMessage(R.string.confirm_driver_request_body)
                .setPositiveButton("OKAY", (dialog, which) -> {
                    try {
                        Request rideRequest = new Request("", intGroupSize, mGenderSpinner
                                .getSelectedItem().toString(), mNameEditText.getText().toString(),
                                mPhoneNumberEditText.getText().toString(), mRemarksEditText.getText()
                                .toString(), strCurrentDate, mLatitude, mLongitude);

                        mRequestsReference.push().setValue(rideRequest);

                        Intent intent = new Intent(this, RideStatus.class);
                        startActivity(intent);

                    } catch (Error exception) {

                    }

                }).setNegativeButton(R.string.cancel, (dialog, which) -> {

                })
                .show();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }

    @OnClick(R.id.button_request_driver)
    public void onViewClicked() {
    }

}
