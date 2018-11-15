package com.example.bespinaf.a2d2;

import com.example.bespinaf.a2d2.utilities.Permissions;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.Manifest;


public class Rules extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
    }

    public void btnRulesAgree_Clicked(View sender){
        if(!Permissions.isLocationPermissionGranted(this)){
            return;
        }

        Intent intent = new Intent(this, RequestRide.class);
        startActivity(intent);
    }
    public boolean checkLocationPermission(){
        boolean isLocationEnabled = Permissions.isLocationPermissionGranted(this);


        return false;
    }
    public void requestLocationPermission(){

    }
}
