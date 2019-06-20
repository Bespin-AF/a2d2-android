package af.bespin.a2d2.utilities;

import android.content.ComponentName;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.Context;
import android.util.Pair;

import af.bespin.a2d2.R;

import java.io.Serializable;

public class ActivityUtils {


    public static void navigate(Context from, Class to) {
        Intent pageIntent = new Intent(from, to);
        from.startActivity(pageIntent);
    }


    public static void navigateWithData(Context from, Class to, Pair<String, Serializable>... data) {
        Intent pageIntent = new Intent(from, to);

        for (Pair<String, Serializable> dataPair : data) {
            pageIntent.putExtra(dataPair.first, dataPair.second);
        }

        from.startActivity(pageIntent);
    }


    public static void navigateAway(Context from, Uri to, Pair<String, String>... extras) {
        Intent exitIntent = resolveIntent(to);
        exitIntent.setData(to);

        for (Pair<String, String> extra : extras) {
            exitIntent.putExtra(extra.first, extra.second);
        }

        ComponentName exitIntentTarget = exitIntent.resolveActivity(from.getPackageManager());
        exitIntent.setComponent(exitIntentTarget);
        from.startActivity(exitIntent);
    }



    private static Intent resolveIntent(Uri target){
        String targetString = target.toString();
        if(targetString.contains("sms")){
            return new Intent(Intent.ACTION_SENDTO);
        }
        else if(targetString.contains("navigation")){
            Intent mapIntent = new Intent(Intent.ACTION_VIEW);
            mapIntent.setPackage("com.google.android.apps.maps");
            return mapIntent;
        }
        return new Intent(Intent.ACTION_DEFAULT);
    }


    public static boolean isFieldEmpty(TextInputEditText input) {
        return input.getText() == null || input.getText().toString().isEmpty();
    }

    public static String getFieldText(TextInputEditText input){
        return (input.getText() == null) ? null : input.getText().toString();
    }


    public static Builder newNotifyDialogBuilder(Context context){
        Builder builder = new Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setPositiveButton(R.string.dialog_okay, (dialog, which) -> {});
        return builder;
    }


    public static void showDialog(Builder builder, int titleResourceId, int messageResourceId){
        builder.setTitle(titleResourceId).setMessage(messageResourceId).show();
    }


    public static void showDialog(Builder builder, String title, String message){
        builder.setTitle(title).setMessage(message).show();
    }


    public static void showCallPermissionDeniedDialog(Builder builder){
        showDialog(builder, R.string.dialog_title_CallPermissionDenied, R.string.error_CallPermissionDenied);
    }


    public static void showLocationPermissionDeniedDialog(Builder builder){
        showDialog(builder, R.string.dialog_title_LocationPermissionDenied, R.string.error_LocationPermissionDenied);
    }



}
