package af.bespin.a2d2.controllers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import af.bespin.a2d2.R;
import af.bespin.a2d2.models.DataReceiver;
import af.bespin.a2d2.models.DataSource;
import af.bespin.a2d2.models.Request;
import af.bespin.a2d2.models.RequestStatus;
import af.bespin.a2d2.utilities.ActivityUtils;
import af.bespin.a2d2.utilities.DataSourceUtils;
import af.bespin.a2d2.utilities.FormatUtils;
import af.bespin.a2d2.utilities.Permissions;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Rider_RideStatus extends ButterKnifeActivity implements ActivityCompat.OnRequestPermissionsResultCallback, DataReceiver {

    @BindView(R.id.button_call_a2d2)
    MaterialButton buttonCallA2d2;
    @BindView(R.id.button_cancel_ride)
    MaterialButton buttonCancelRide;
    @BindView(R.id.a2d2number_ridestatus)
    TextView a2d2number_textview;

    final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private String a2d2Number;
    private Request request;

    AlertDialog.Builder mDialogBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_ride_status);
        loadIntentData();
        mDialogBuilder = ActivityUtils.newNotifyDialogBuilder(this);
        buttonCallA2d2.setEnabled(false);
    }


    @Override
    protected void onStart() {
        super.onStart();
        DataSourceUtils.resources.setReciever(this);
    }


    private void loadIntentData(){
        request = (Request) getIntent().getSerializableExtra("request");
    }


    @Override
    public void onDataChanged(DataSource dataSource, HashMap<String, Object> data) {
        a2d2Number = (String)data.get("phone_number");//TODO Put this key somewhere, maybe in 'R'
        displayA2D2PhoneNumber();
        buttonCallA2d2.setEnabled(true);
    }


    private void displayA2D2PhoneNumber(){
        a2d2number_textview.setText(
                FormatUtils.formatString("A2D2 Number: %s", a2d2Number)
        );
    }


    @OnClick(R.id.button_call_a2d2)
    public void callA2D2(){
        if(Permissions.hasPhoneCallPermission(this)){
            openDialer();
        } else {
            getPhonePermission();
        }
    }

    @OnClick(R.id.button_cancel_ride)
    public void cancelButtonClicked_ConfirmCancellation(){
        if(request == null){
            String message = FormatUtils.formatString(
                    "An error occurred trying to cancel your request. Please try again later or contact %s for further assistance.", a2d2Number
            );
            ActivityUtils.showDialog( mDialogBuilder, "Missing request data", message );

            return;
        }

        mDialogBuilder.setTitle("Confirm Cancellation?")
                .setMessage("Are you sure you want to cancel your ride request?")
                .setPositiveButton("Confirm",(dialog, which)-> cancelRideRequest())
                .setNegativeButton("Cancel",null)
                .show();
    }

    private void cancelRideRequest(){
        request.setStatus(RequestStatus.Cancelled);
        DataSourceUtils.testRequests.updateData(request.key, request.getData());

        AlertDialog.Builder redirectDialog = ActivityUtils.newNotifyDialogBuilder(this);

        redirectDialog.setTitle("Ride Request Cancelled!")
                .setMessage("Your request has been successfully cancelled. You will now be taken back to the home screen.")
                .setPositiveButton("OKAY", (dialog, which) -> navigateToHomePage())
                .show();
    }

    private void navigateToHomePage(){
        Intent homeNavigationIntent = new Intent(this, MainActivity.class);
        homeNavigationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeNavigationIntent);
    }

    /**
     * Attempts to open the phone's dialler with A2D2's phone number
     */
    public void openDialer(){
        Uri phoneNumber = Uri.parse(String.format("tel:%s", a2d2Number));
        ActivityUtils.navigateAway(this, phoneNumber);
    }


    private void getPhonePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CALL_PHONE},
                MY_PERMISSIONS_REQUEST_CALL_PHONE);
    }


    /**
     * Called on when the requestPermissions method is called, handles the response
     * @param requestCode Callback identifier for the initial permissions request
     * @param permissions The requested permissions, never null
     * @param grantResults Grant result: PERMISSION_GRANTED | PERMISSION_DENIED
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode != MY_PERMISSIONS_REQUEST_CALL_PHONE) { return; }
        if(hasGrantedPermissions(grantResults)){
            openDialer();
        } else {
            ActivityUtils.showCallPermissionDeniedDialog(mDialogBuilder);
        }
    }


    private boolean hasGrantedPermissions(int[] grantedPermissions){
       return grantedPermissions.length > 0 && grantedPermissions[0] == PackageManager.PERMISSION_GRANTED;
    }
}
