package com.example.bespinaf.a2d2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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

import com.example.bespinaf.a2d2.utilities.Request;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.sql.Driver;

import butterknife.BindView;
import butterknife.OnClick;

public class DriverLogin extends AppCompatActivity {

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

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    //handles on click for both buttons
    @OnClick(R.id.button_driver_login)
    public void driverLogin() {
        //getResources().getString(R.string.TEST_DRIVER_EMAIL), getResources().getString(R.string.TEST_DRIVER_PASSWORD);
        String  mEmail = activityDriverLoginEmailTextEdit.getText().toString(),
                mPassword = activityDriverLoginPasswordTextEdit.getText().toString();

        //Returns if either email or password field is empty
        if(mEmail.isEmpty() || mPassword.isEmpty()){
            if(mEmail.isEmpty()){
                activityDriverLoginEmailInputLayout.setError("Required");
            } else {
                activityDriverLoginEmailInputLayout.setError("");
            }

            if(mPassword.isEmpty()){
                activityDriverLoginPasswordInputLayout.setError("Required");
            } else {
                activityDriverLoginPasswordInputLayout.setError("");
            }

            return;
        }


        //Attempt to sign in
        mAuth.signInWithEmailAndPassword(mEmail, mPassword)
            .addOnCompleteListener((@NonNull Task<AuthResult> task)->{
                if(task.isSuccessful()){
                    login();
                } else {
                    //alert, failed to auth. may be failed password or username
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void login(){
        Intent mRideRequestIntent = new Intent(this,RideRequests.class);
        startActivity(mRideRequestIntent);
    }

    @OnClick(R.id.button_become_a_driver)
    public void becomeDriver() {
        //attempt to create a new user
        //We don't have a story yet for registration. While the following code does work,
        //I don't know whether how the user/pm/designer wants registration to work.
        /*
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
                });*/
    }
}
