package com.example.bespinaf.a2d2;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.bespinaf.a2d2.utilities.Permissions;


public class Rules extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private final int MY_PERMISSIONS_REQUEST_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
    }
    @TargetApi(Build.VERSION_CODES.M)
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
                Toast.makeText(this, R.string.error_LocationPermissionDenied, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.error_DefaultError, Toast.LENGTH_LONG).show();
            }
        } else {
            //If the user grnats permissions open the request ride page
            Intent intent = new Intent(this, RequestRide.class);
            startActivity(intent);
        }
    }
}
