package com.example.bespinaf.a2d2.utilities;

import android.content.ComponentName;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.Context;
import android.util.Pair;

import com.example.bespinaf.a2d2.R;

import java.io.Serializable;

public class ActivityUtils {

    public static void navigate(Context from, Class to){
        Intent pageIntent = new Intent(from, to);
        from.startActivity(pageIntent);
    }


    //TODO Decide on data format and transfer standards
    public static void navigateWithData(Context from, Class to, Pair<String, Serializable>... data){
        Intent pageIntent = new Intent(from, to);

        for (Pair<String, Serializable> dataPair : data){
            pageIntent.putExtra(dataPair.first, dataPair.second);
        }

        from.startActivity(pageIntent);
    }




    public static void navigateAway(Context from, Uri to){
        Intent exitIntent = new Intent();
        exitIntent.setData(to);
        ComponentName exitIntentTarget = exitIntent.resolveActivity(from.getPackageManager());
        exitIntent.setComponent(exitIntentTarget);
        from.startActivity(exitIntent);
    }

    public static boolean isFieldEmpty(TextInputEditText input){
        return input.getText() == null ? true : input.getText().toString().isEmpty();
    }

/*    public static boolean isFieldValid(Context context, TextInputEditText input, TextInputLayout layout){
        if(input.getText().toString().isEmpty()){
            layout.setError(context.getString(R.string.a2d2_field_required));
            return false;
        }

        layout.setError("");

        return true;
    }*/


    public static Builder newNotifyDialogBuilder(Context context){
        Builder builder = new Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setPositiveButton(R.string.dialog_okay, (dialog, which) -> {});
        return builder;
    }


    public static void showDialog(Builder builder, int titleResourceId, int messageResourceId){
        builder.setTitle(titleResourceId).setMessage(messageResourceId).show();
    }
}
