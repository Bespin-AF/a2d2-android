package com.example.bespinaf.a2d2;

import com.example.bespinaf.a2d2.utilities.Permissions;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.Toast;

import java.util.UUID;


public class Rules extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private final int MY_PERMISSIONS_REQUEST_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
    }
    @TargetApi(Build.VERSION_CODES.M)
    public void btnRulesAgree_Clicked(View sender)
    {
        if(!Permissions.isLocationPermissionGranted(this))
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
        }
        else
        {
            Intent intent = new Intent(this, RequestRide.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED)
        {
            if(requestCode == MY_PERMISSIONS_REQUEST_LOCATION)
            {
                Toast.makeText(this, R.string.error_LocationPermissionDenied, Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, R.string.error_DefaultError, Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Intent intent = new Intent(this, RequestRide.class);
            startActivity(intent);
        }
    }
}
