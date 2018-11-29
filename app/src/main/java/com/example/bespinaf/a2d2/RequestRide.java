package com.example.bespinaf.a2d2;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RequestRide extends AppCompatActivity {

    private AlertDialog.Builder mDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_ride);
    }

    public void btnRequestDriver_Clicked(View view){
        mDialogBuilder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);

        mDialogBuilder.setTitle(R.string.confirm_driver_request_title)
                .setMessage(R.string.confirm_driver_request_body)
                .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
        .show();

    }
}
