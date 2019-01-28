package com.example.bespinaf.a2d2;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.bespinaf.a2d2.utilities.Permissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RideStatus extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.button_call_a2d2)
    MaterialButton buttonCallA2d2;
    @BindView(R.id.button_cancel_ride)
    MaterialButton buttonCancelRide;

    final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    AlertDialog.Builder mDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_status);
        ButterKnife.bind(this);

        mDialogBuilder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);

        mDialogBuilder.setPositiveButton(R.string.dialog_okay, (dialog, which) -> {});
    }

    @OnClick(R.id.button_call_a2d2)
    public void callA2D2(){
        if(!Permissions.isPhoneCallPermissionGranted(this)){
            getPhonePermission();
        } else {
            openDialer();
        }
    }

    /**
     * Attempts to open the phone's dialler with A2D2's phone number
     */
    //TODO: CHANGE THE RESOURCE FOR THE PHONE NUMBER TO A2D2
    public void openDialer(){

        Uri phoneNumber = Uri.parse("tel:" + getString(R.string.test_phone_number));

        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(phoneNumber);

        //Why do we get the package manager?
        if (dialIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(dialIntent);
        } else {
            showMessage(R.string.error_dialog_title, R.string.bacon_ipsum);
        }
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
        if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            //if user denies call permissions display a prompt explaining why they cannot proceed
            if(requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
                showMessage(R.string.dialog_title_CallPermissionDenied,
                            R.string.error_CallPermissionDenied);
            } else {
                showMessage(R.string.error_dialog_title, R.string.error_DefaultError);
            }
        } else {
            openDialer();
        }
    }

    private void showMessage(int titleResourceId, int messageResourceId){
        mDialogBuilder.setTitle(titleResourceId)
                .setMessage(messageResourceId)
                .show();
    }
}
