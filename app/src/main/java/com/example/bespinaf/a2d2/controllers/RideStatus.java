package com.example.bespinaf.a2d2.controllers;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.utilities.ActivityUtils;
import com.example.bespinaf.a2d2.utilities.Permissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RideStatus extends ButterKnifeActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.button_call_a2d2)
    MaterialButton buttonCallA2d2;
    @BindView(R.id.button_cancel_ride)
    MaterialButton buttonCancelRide;

    final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    AlertDialog.Builder mDialogBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_ride_status);
        mDialogBuilder = ActivityUtils.newNotifyDialogBuilder(this);
    }


    @OnClick(R.id.button_call_a2d2)
    public void callA2D2(){
        if(Permissions.isPhoneCallPermissionGranted(this)){
            openDialer();
        } else {
            getPhonePermission();
        }
    }


    /**
     * Attempts to open the phone's dialler with A2D2's phone number
     */
    public void openDialer(){
        Uri phoneNumber = Uri.parse("tel:" + getString(R.string.test_phone_number)); //TODO: CHANGE THE RESOURCE FOR THE PHONE NUMBER TO A2D2
        ActivityUtils.navigateAway(this, phoneNumber);
    }


    private void getPhonePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CALL_PHONE},
                MY_PERMISSIONS_REQUEST_CALL_PHONE);
    }


    /**
     * Called on when the requestPermissions method is called, handles the response
     * @param requestCode Callback identifier for the initial permissions request
     * @param permissions The requested permissions, never null
     * @param grantResults Grant result: PERMISSION_GRANTED | PERMISSION_DENIED
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode != MY_PERMISSIONS_REQUEST_CALL_PHONE) { return; }
        if(hasGrantedPermissions(grantResults)){
            openDialer();
        } else {
            notifyUserDeniedPermissions();
        }
    }


    private void notifyUserDeniedPermissions(){
        ActivityUtils.showDialog(mDialogBuilder,
                R.string.dialog_title_CallPermissionDenied,
                R.string.error_CallPermissionDenied);
    }


    private boolean hasGrantedPermissions(int[] grantedPermissions){
       return grantedPermissions.length > 0 && grantedPermissions[0] == PackageManager.PERMISSION_GRANTED;
    }
}
