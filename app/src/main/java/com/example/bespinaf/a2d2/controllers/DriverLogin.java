package com.example.bespinaf.a2d2.controllers;

import android.app.Instrumentation;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.widget.Toast;

import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.utilities.ActivityUtils;
import com.example.bespinaf.a2d2.utilities.AuthorizationUtils;

import butterknife.BindView;
import butterknife.OnClick;


public class DriverLogin extends ButterKnifeActivity {

    @BindView(R.id.activity_driver_login_email_text_edit)
    TextInputEditText activityDriverLoginEmailTextEdit;
    @BindView(R.id.activity_driver_login_email_input_layout)
    TextInputLayout activityDriverLoginEmailInputLayout;
    @BindView(R.id.activity_driver_login_password_text_edit)
    TextInputEditText activityDriverLoginPasswordTextEdit;
    @BindView(R.id.activity_driver_login_password_input_layout)
    TextInputLayout activityDriverLoginPasswordInputLayout;
    @BindView(R.id.button_driver_login)
    MaterialButton buttonDriverLogin;
    @BindView(R.id.button_become_a_driver)
    MaterialButton buttonBecomeADriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_driver_login);
    }


    @OnClick(R.id.button_driver_login)
    public void driverLogin() {
        validateInputs();
        if(!isDataValid()){ return; }

        String  mEmail = ActivityUtils.getFieldText(activityDriverLoginEmailTextEdit),
                mPassword = ActivityUtils.getFieldText(activityDriverLoginPasswordTextEdit);

        if(mEmail == null || mPassword == null){ return; }

            


        if(isDataValid()){
            AuthorizationUtils.authorizeUser(mEmail, mPassword, (wasLoginSuccessful)->{
                if(wasLoginSuccessful){ ActivityUtils.navigate(this, RideRequests.class); }
                else { Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show(); }
            });
        }
    }


    private boolean isDataValid(){
        return (activityDriverLoginEmailInputLayout.getError() == null &&
                activityDriverLoginPasswordInputLayout.getError() == null);
    }


    private void validateInputs(){
        validateField(activityDriverLoginEmailTextEdit, activityDriverLoginEmailInputLayout);
        validateField(activityDriverLoginPasswordTextEdit, activityDriverLoginPasswordInputLayout);
    }


    private void validateField(TextInputEditText input, TextInputLayout layout){
        if(ActivityUtils.isFieldEmpty(input)) { layout.setError(getString(R.string.a2d2_field_required)); }
        else { layout.setError(""); }
    }
}
