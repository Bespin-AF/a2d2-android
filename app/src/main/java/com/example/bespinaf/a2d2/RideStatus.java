package com.example.bespinaf.a2d2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

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
    }

    @OnClick(R.id.button_call_a2d2)
    public void callA2D2(){
        checkForPhonePermission();
    }

    private void checkForPhonePermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            // Permission not yet granted. Use requestPermissions().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }
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
            //if user denies permissions display a prompt explaining why they cannot proceed
            if(requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
                mDialogBuilder.setTitle(R.string.dialog_title_LocationPermissionDenied)
                        .setMessage(R.string.error_LocationPermissionDenied)
                        .show();
            } else {
                mDialogBuilder.setTitle("Error")
                        .setMessage(R.string.error_DefaultError)
                        .show();
            }
        } else {
            //if the user grants permissions start the dialer
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            // Set the data for the intent as the phone number.
            dialIntent.setData(Uri.parse(getString(R.string.test_phone_number)));

            // If package resolves to an app, send intent.
            if (dialIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(dialIntent);
            } else {
                mDialogBuilder.setTitle("Error")
                        .setMessage(R.string.error_DefaultError)
                        .show();
            }
        }
    }
}
