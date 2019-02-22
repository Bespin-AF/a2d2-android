package com.example.bespinaf.a2d2.controllers;

import android.Manifest;
import android.support.v7.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;

import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.utilities.ActivityUtils;
import com.example.bespinaf.a2d2.utilities.Permissions;

import butterknife.BindView;
import butterknife.OnClick;


public class Rules extends ButterKnifeActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    @BindView(R.id.button_rules_agree)
    Button buttonRulesAgree;
    private AlertDialog.Builder mDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_rules);

        mDialogBuilder = ActivityUtils.newNotifyDialogBuilder(this);
    }

    @OnClick(R.id.button_rules_agree)
    public void btnRulesAgree_Clicked(View sender) {
        if (hasLocationPermissions()) {
            ActivityUtils.navigate(this, RequestRide.class);
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

    /**
     * Called on when the requestPermissions method is called, handles the response
     *
     * @param requestCode  Callback identifier for the initial permissions request
     * @param permissions  The requested permissions, never null
     * @param grantResults Grant result: PERMISSION_GRANTED | PERMISSION_DENIED
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode != MY_PERMISSIONS_REQUEST_LOCATION){ return; }
        if (hasGrantedPermissions(grantResults)) {
            ActivityUtils.navigate(this, RequestRide.class);
        } else {
            ActivityUtils.showDialog(mDialogBuilder, R.string.dialog_title_LocationPermissionDenied, R.string.error_LocationPermissionDenied);
        }
    }

    private boolean hasGrantedPermissions(@NonNull int[] grantResults){
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }
}
