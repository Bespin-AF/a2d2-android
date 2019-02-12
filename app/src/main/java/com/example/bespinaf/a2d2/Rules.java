package com.example.bespinaf.a2d2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.bespinaf.a2d2.utilities.Permissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Rules extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    @BindView(R.id.button_rules_agree)
    Button buttonRulesAgree;
    private AlertDialog.Builder mDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        ButterKnife.bind(this);

        mDialogBuilder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);

        mDialogBuilder.setPositiveButton(R.string.dialog_okay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
    }

    @OnClick(R.id.button_rules_agree)
    public void btnRulesAgree_Clicked(View sender) {
        if (hasLocationPermissions()) {
            goToRideRequestPage();
        } else {
            requestLocationPermissions();
        }
    }

    private boolean hasLocationPermissions(){
        return Permissions.isLocationPermissionGranted(this);
    }

    private void requestLocationPermissions(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                0);
    }

    private void goToRideRequestPage(){
        Intent intent = new Intent(this, RequestRide.class);
        startActivity(intent);
    }

    /**
     * Called on when the requestPermissions method is called, handles the response
     *
     * @param requestCode  Callback identifier for the initial permissions request
     * @param permissions  The requested permissions, never null
     * @param grantResults Grant result: PERMISSION_GRANTED | PERMISSION_DENIED
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean hasGrantedPemissions = !(grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED);
        if (hasGrantedPemissions) {
            goToRideRequestPage();
        } else {
            alertUserOfRequiredPermissions(requestCode);
        }
    }

    private void alertUserOfRequiredPermissions(int requestCode){
        //if user denies permissions display a prompt explaining why they cannot proceed
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            showDialog(R.string.dialog_title_LocationPermissionDenied, R.string.error_LocationPermissionDenied);
        } else {
            showDialog(R.string.error_dialog_title, R.string.error_DefaultError);
        }
    }

    private void showDialog(int title, int message){
        mDialogBuilder.setTitle(title)
                .setMessage(message)
                .show();
    }
}
