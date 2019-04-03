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
import android.util.Log;
import android.widget.TextView;

import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.utilities.ActivityUtils;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;
import com.example.bespinaf.a2d2.utilities.Permissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RideStatus extends ButterKnifeActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.button_call_a2d2)
    MaterialButton buttonCallA2d2;
    @BindView(R.id.button_cancel_ride)
    MaterialButton buttonCancelRide;
    @BindView(R.id.a2d2number_ridestatus)
    TextView a2d2number_textview;

    final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private String a2d2Number;

    AlertDialog.Builder mDialogBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_ride_status);
        DataSourceUtils.getResource("a2d2phonenumber", () -> displayA2D2PhoneNumber());

        mDialogBuilder = ActivityUtils.newNotifyDialogBuilder(this);
    }

    private void displayA2D2PhoneNumber(){
        a2d2Number = DataSourceUtils.getA2D2Number();
        try {
           String displayNumber = format10DigitNumber(a2d2Number);
           String displayMessage = String.format("A2D2 Number: %s", displayNumber);
           a2d2number_textview.setText(displayMessage);
        } catch (Exception error){
            Log.e("RideStatus: phoneError", error.getMessage());
        }
    }

    private String format10DigitNumber(String number) throws IndexOutOfBoundsException{
        String phoneFormat = "(%1$s) %2$s-%3$s";
        return String.format(phoneFormat, number.substring(0,3), number.substring(3,6), number.substring(6));
    }


    @OnClick(R.id.button_call_a2d2)
    public void callA2D2(){
        if(Permissions.isPhoneCallPermissionGranted(this)){
            openDialer();
        } else {
            getPhonePermission();
        }
    }

    @OnClick(R.id.button_cancel_ride)
    public void cancelRideRequest(){
        mDialogBuilder.setTitle("Confirm Cancellation?")
                .setMessage("Are you sure you want to cancel your ride request?")
                .setPositiveButton("Confirm",(dialog, which)->{})
                .setNegativeButton("Cancel",(dialog, which)->{}).show();

    }

    /**
     * Attempts to open the phone's dialler with A2D2's phone number
     */
    public void openDialer(){
        Uri phoneNumber = Uri.parse(String.format("tel:%s", a2d2Number));
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
