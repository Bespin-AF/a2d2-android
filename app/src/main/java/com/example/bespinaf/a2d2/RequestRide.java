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

public class RequestRide extends AppCompatActivity {
    TextInputLayout mPhoneNumberTextLayout;
    TextInputEditText mPhoneNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_ride);

         mPhoneNumberTextLayout = (TextInputLayout) findViewById(R.id.activity_request_ride_phone_number_text_input_layout);

        mPhoneNumberEditText = (TextInputEditText) findViewById(R.id.activity_ride_request_phone_number_text_edit);
        mPhoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mPhoneNumberEditText.getText().length() == 0){
                    mPhoneNumberTextLayout.setError(getString(R.string.a2d2_field_required));
                } else {
                    mPhoneNumberTextLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void btnRequestDriver_Clicked(View view) {

        if(mPhoneNumberEditText.length() == 0){
            mPhoneNumberTextLayout.setError(getString(R.string.a2d2_field_required));
            return;
        }else if(mPhoneNumberEditText.length() < 10){
            mPhoneNumberTextLayout.setError(getString(R.string.error_phone_number));
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
