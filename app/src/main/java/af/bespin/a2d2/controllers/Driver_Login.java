package af.bespin.a2d2.controllers;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import af.bespin.a2d2.R;
import af.bespin.a2d2.utilities.ActivityUtils;
import af.bespin.a2d2.utilities.AuthorizationUtils;

import af.bespin.a2d2.utilities.NetworkUtils;
import butterknife.BindView;
import butterknife.OnClick;


public class Driver_Login extends ButterKnifeActivity {

    @BindView(R.id.textInputEditText_driverLogin_emailInput)
    TextInputEditText mEmailEditText;
    @BindView(R.id.textInputLayout_driverLogin_emailInput)
    TextInputLayout mEmailInputLayout;
    @BindView(R.id.textInputEditText_driverLogin_passwordInput)
    TextInputEditText mPasswordEditText;
    @BindView(R.id.textInputLayout_driverLogin_passwordLayout)
    TextInputLayout mPasswordInputLayout;
    @BindView(R.id.materialButton_driverLogin_loginButton)
    MaterialButton mDriverLoginButton;
    @BindView(R.id.progressBar_driverLogin_loadingIndicator)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_driver_login);
    }


    @OnClick(R.id.materialButton_driverLogin_loginButton)
    public void driverLogin() {
        validateInputs();
        if(!isDataValid()){ return; }

        String  email = ActivityUtils.getFieldText(mEmailEditText),
                password = ActivityUtils.getFieldText(mPasswordEditText);

        tryLogin(email, password);
    }


    private boolean isDataValid(){
        return (mEmailInputLayout.getError() == null &&
                mPasswordInputLayout.getError() == null);
    }


    private void validateInputs(){
        validateField(mEmailEditText, mEmailInputLayout);
        validateField(mPasswordEditText, mPasswordInputLayout);
    }


    private void validateField(TextInputEditText input, TextInputLayout layout){
        if(ActivityUtils.isFieldEmpty(input)) { layout.setError(getString(R.string.a2d2_field_required)); }
        else { layout.setError(""); }
    }


    private void tryLogin(String email, String password){

        mProgressBar.setVisibility(View.VISIBLE);
        if(email == null || password == null){ return; }

        AuthorizationUtils.authorizeUser(email, password, (wasLoginSuccessful) -> {
            if(!(NetworkUtils.checkInternetConnectivity(getApplicationContext()))){
                NetworkUtils.displayNetworkError(this);
            }
            else if(wasLoginSuccessful) {

                ActivityUtils.navigate(this, Driver_RideRequestList.class);
            }
            else {
                Toast.makeText(
                        this,
                        "Invalid username or password",
                        Toast.LENGTH_SHORT
                ).show();
            }
            mProgressBar.setVisibility(View.INVISIBLE);
        });
    }
}
