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
    private String ERROR_MESSAGE_REQUIRED;

    TextInputEditText mNameTextEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_ride);

        ERROR_MESSAGE_REQUIRED = getResources().getString(R.string.a2d2_field_required);

        mNameTextEdit = (TextInputEditText) findViewById(R.id.activity_ride_request_name_text_edit);
        mNameTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mNameTextEdit.getText().length() == 0){
                    setNameTextLayoutErrorMessage(ERROR_MESSAGE_REQUIRED);
                } else {
                    setNameTextLayoutErrorMessage("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void btnRequestDriver_Clicked(View view) {
        if(mNameTextEdit.length() == 0){
            setNameTextLayoutErrorMessage(ERROR_MESSAGE_REQUIRED);
            return;
        }

        setNameTextLayoutErrorMessage("");

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        mDialogBuilder.setTitle(R.string.confirm_driver_request_title)
                .setMessage(R.string.confirm_driver_request_body)
                .setPositiveButton("OKAY", (dialog, which) -> {

                })
                .show();
    }

    public void setNameTextLayoutErrorMessage(String message){
        TextInputLayout mNameTextLayout  = (TextInputLayout) findViewById(R.id.activity_request_ride_name_text_input_layout);
        mNameTextLayout.setError(message);
    }
}
