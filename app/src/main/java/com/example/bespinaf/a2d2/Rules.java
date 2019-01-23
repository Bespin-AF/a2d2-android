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

import com.example.bespinaf.a2d2.utilities.Permissions;


public class Rules extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private AlertDialog.Builder mDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        mDialogBuilder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);

        mDialogBuilder.setPositiveButton(R.string.dialog_okay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    public void btnRulesAgree_Clicked(View sender) {
        //If permissions are not granted prompts the user to turn them on with a default pop-up
        if(!Permissions.isLocationPermissionGranted(this)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        } else {
            //If the user has permissions turned on, go to the request ride page
            Intent intent = new Intent(this, RequestRide.class);
            startActivity(intent);
        }
    }

    /**
     * Called on when the requestPermissions method is called, handles the response
     * @param requestCode Callback identifier for the initial permissions request
     * @param permissions The requested permissions, never null
     * @param grantResults Grant result: PERMISSION_GRANTED | PERMISSION_DENIED
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            //if user denies permissions display a prompt explaining why they cannot proceed
            if(requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
                mDialogBuilder.setTitle(R.string.dialog_title_LocationPermissionDenied)
                        .setMessage(R.string.error_LocationPermissionDenied)
                        .show();

            } else {
                mDialogBuilder.setTitle("Error")
                        .setMessage(R.string.error_DefaultError)
                        .show();
            }
        } else {
            //If the user grants permissions open the request ride page
            Intent intent = new Intent(this, RequestRide.class);
            startActivity(intent);
        }
    }
}
