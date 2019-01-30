package com.example.bespinaf.a2d2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bespinaf.a2d2.utilities.Request;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.OnClick;

public class DriverLogin extends AppCompatActivity {

    @BindView(R.id.activity_driver_login_email_input_layout)
    TextInputLayout mEmailTextLayout;
    @BindView(R.id.activity_driver_login_email_text_edit)
    TextInputEditText mEmailTextEdit;
    @BindView(R.id.activity_driver_login_password_input_layout)
    TextInputLayout mPasswordTextLayout;
    @BindView(R.id.activity_driver_login_password_text_edit)
    TextInputEditText mPasswordTextEdit;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mTableRideRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);
        mDatabase = FirebaseDatabase.getInstance();
    }

    @OnClick(R.id.button_driver_login)
    public void buttonLoginClicked_NavigateToRideRequests() {
        String email = "",
                password = "";

        //Do auth stuff

        mTableRideRequests = mDatabase.getReference("riderequests");

        Intent rideRequestIntent = new Intent();

        startActivity(rideRequestIntent);
    }
}
