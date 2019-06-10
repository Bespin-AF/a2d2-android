package com.example.bespinaf.a2d2.controllers;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.utilities.ActivityUtils;
import com.example.bespinaf.a2d2.utilities.AuthorizationUtils;

import butterknife.BindView;
import butterknife.OnClick;


public class Driver_Login extends ButterKnifeActivity {

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
    @BindView(R.id.driver_login_progress_bar)
    ProgressBar driverLoginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_driver_login);
    }


    @OnClick(R.id.button_driver_login)
    public void driverLogin() {
        validateInputs();
        if(!isDataValid()){ return; }

        String  email = ActivityUtils.getFieldText(activityDriverLoginEmailTextEdit),
                password = ActivityUtils.getFieldText(activityDriverLoginPasswordTextEdit);

        tryLogin(email, password);
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


    private void tryLogin(String email, String password){

        driverLoginProgressBar.setVisibility(View.VISIBLE);
        if(email == null || password == null){ return; }

        AuthorizationUtils.authorizeUser(email, password, (wasLoginSuccessful) -> {
            if(wasLoginSuccessful) {

                ActivityUtils.navigate(this, Driver_RideRequestList.class);
            }
            else {
                Toast.makeText(
                        this,
                        "Invalid username or password",
                        Toast.LENGTH_SHORT
                ).show();
            }
            driverLoginProgressBar.setVisibility(View.INVISIBLE);
        });
    }
}
