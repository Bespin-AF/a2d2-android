package com.example.bespinaf.a2d2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DriverLogin extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;
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

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);
        ButterKnife.bind(this);

        //TODO: Remove testing code
        activityDriverLoginEmailTextEdit.setText(R.string.TEST_DRIVER_EMAIL);
        activityDriverLoginPasswordTextEdit.setText(R.string.TEST_DRIVER_PASSWORD);

        //Checks if
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    //handles on click for both buttons
    @OnClick(R.id.button_driver_login)
    public void driverLogin() {
        //attempt to sign user in with given info
        mAuth.signInWithEmailAndPassword(activityDriverLoginEmailTextEdit.getText().toString(), activityDriverLoginPasswordTextEdit.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(DriverLogin.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            Toast.makeText(DriverLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
                            updateUI(null);
                        }
                        // ...
                    }
                });
    }

    @OnClick(R.id.button_become_a_driver)
    public void becomeDriver() {
        //attempt to create a new user
        mAuth.createUserWithEmailAndPassword(activityDriverLoginEmailTextEdit.getText().toString(), activityDriverLoginPasswordTextEdit.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(DriverLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        // ...
                    }
                });
    }

    //TODO: update the UI to have visible and invisible elements and implement this method
    //navigate to the ride requests page and populate with data from the database
    public void updateUI(FirebaseUser user) {
        if (user != null) {
            //update UI to allow for user to logout
        } else {
            //update UI to exclude the logout functions
        }
    }
}
