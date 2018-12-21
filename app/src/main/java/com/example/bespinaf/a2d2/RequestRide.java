package com.example.bespinaf.a2d2;

import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import org.w3c.dom.Text;

public class RequestRide extends AppCompatActivity {

    TextInputLayout mPhoneNumberTextLayout;
    TextInputEditText mPhoneNumberEditText;
    TextInputLayout mNameTextLayout;
    TextInputEditText mNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_ride);

        mPhoneNumberTextLayout = (TextInputLayout) findViewById(R.id.activity_request_ride_phone_number_text_input_layout);
        mNameTextLayout = findViewById(R.id.activity_request_ride_name_text_input_layout);

        mPhoneNumberEditText = (TextInputEditText) findViewById(R.id.activity_ride_request_phone_number_text_edit);
        mNameEditText = findViewById(R.id.activity_ride_request_name_text_edit);

        mPhoneNumberEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mPhoneNumberEditText.getText().length() == 0){
                    mPhoneNumberTextLayout.setError(getString(R.string.a2d2_field_required));
                }
                else if(mPhoneNumberEditText.length() < 10){
                    mPhoneNumberTextLayout.setError(getString(R.string.error_phone_number));
                }
                else {
                    mPhoneNumberTextLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mNameEditText.getText().length() == 0){
                    mNameTextLayout.setError(getString(R.string.a2d2_field_required));
                } else {
                    mNameTextLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    public void btnRequestDriver_Clicked(View view) {
        boolean isInputValid = true;
        //checks the phone number field to make sure that the data is valid
        if(mPhoneNumberEditText.length() == 0){
            mPhoneNumberTextLayout.setError(getString(R.string.a2d2_field_required));
            isInputValid = false;
        }else if(mPhoneNumberEditText.length() < 10){
            mPhoneNumberTextLayout.setError(getString(R.string.error_phone_number));
            isInputValid = false;
        }

        if(mNameEditText.length() == 0) {
            mNameTextLayout.setError(getString(R.string.a2d2_field_required));
            isInputValid = false;
        }

        if(!isInputValid){
            return;
        }

        mPhoneNumberTextLayout.setError("");
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);

        mDialogBuilder.setTitle(R.string.confirm_driver_request_title)
                .setMessage(R.string.confirm_driver_request_body)
                .setPositiveButton("OKAY", (dialog, which) -> {

                })
                .show();
    }
}
