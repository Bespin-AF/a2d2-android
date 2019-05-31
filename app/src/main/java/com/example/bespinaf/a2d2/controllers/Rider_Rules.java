package com.example.bespinaf.a2d2.controllers;

import android.Manifest;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;

import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.models.DataReceiver;
import com.example.bespinaf.a2d2.models.DataSource;
import com.example.bespinaf.a2d2.utilities.ActivityUtils;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;
import com.example.bespinaf.a2d2.utilities.FormatUtils;
import com.example.bespinaf.a2d2.utilities.LocationUtils;
import com.example.bespinaf.a2d2.utilities.Permissions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;


public class Rider_Rules extends ButterKnifeActivity implements ActivityCompat.OnRequestPermissionsResultCallback, DataReceiver {

    private final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    @BindView(R.id.button_rules_agree)
    Button buttonRulesAgree;
    private AlertDialog.Builder mDialogBuilder;
    //TODO: extract into R.string
    private String userOutOfRangeMessageFormat = "You are outside of the 25 mile range defined by the A2D2 program rules. If you still require a ride, please call A2D2 Dispatch at %s";
    private String a2d2PhoneNumber = null;
    private Location currentLocation;

    OnCompleteListener<String> checkCurrentLocationIsWithinA2D2ServiceArea = (task) -> {
        if(!task.isSuccessful()){
            return;
        }

        String baseName = task.getResult();
        Location closestA2D2Location = LocationUtils.a2d2Locations.get(baseName);

        if(!LocationUtils.isInRange(currentLocation, closestA2D2Location)){
            return;
        }

        ActivityUtils.navigate(this, Rider_RequestRide.class);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_rules);

        mDialogBuilder = ActivityUtils.newNotifyDialogBuilder(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        DataSourceUtils.resources.setReciever(this);
        DataSourceUtils.locations.setReciever(this);
        buttonRulesAgree.setEnabled(false);
    }


    @Override
    public void onDataChanged(DataSource dataSource, HashMap<String, Object> data) {
        if(dataSource.databaseRef.getKey() == null){
            return;
        }

        if(dataSource.databaseRef.getKey().equals("base_info")){
            a2d2PhoneNumber = (String) data.get("phone_number");//TODO add to 'R'
        } else if (dataSource.databaseRef.getKey().equals("locations")){
            LocationUtils.updateA2D2Locations(data);
        }

        if(a2d2PhoneNumber != null && !LocationUtils.a2d2Locations.isEmpty()){
            buttonRulesAgree.setEnabled(true);
        }
    }


    @OnClick(R.id.button_rules_agree)
    public void btnRulesAgree_Clicked(View sender) {
        if(!Permissions.hasLocationPermission(this)){
            requestLocationPermissions();
            return;
        }

        navigateToRideRequest();
    }


    private void requestLocationPermissions(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                0);
    }


    //TODO: Find android utility to request GPS permission
    private void navigateToRideRequest(){
        if(!LocationUtils.isGPSEnabled(this)){
            ActivityUtils.showDialog(mDialogBuilder, "GPS Unavailable", "Please enable GPS and try again.");
            return;
        }

        checkUserIsWIthinA2D2PickupRange();
    }

    private void checkUserIsWIthinA2D2PickupRange(){
        LocationUtils.getCurrentGPSLocationAsync(this, (location) -> {
            currentLocation = location; //Used in range check callback

            LocationUtils.getClosestA2D2Location(location)
                    .addOnCompleteListener(checkCurrentLocationIsWithinA2D2ServiceArea);
        });
    }




    private void displayOutOfRangeMessage(){
        String outOfRangeMessage = FormatUtils.formatString(
                userOutOfRangeMessageFormat,
                FormatUtils.formatPhoneNumber(a2d2PhoneNumber)
        );

        ActivityUtils.showDialog(
                mDialogBuilder,"Location out of range!", outOfRangeMessage
        );
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
            navigateToRideRequest();
        } else {
            ActivityUtils.showLocationPermissionDeniedDialog(mDialogBuilder);
        }
    }


    private boolean hasGrantedPermissions(@NonNull int[] grantResults){
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }
}
