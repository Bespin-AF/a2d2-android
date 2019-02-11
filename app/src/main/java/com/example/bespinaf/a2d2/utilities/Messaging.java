package com.example.bespinaf.a2d2.utilities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import static java.net.Proxy.Type.HTTP;

public class Messaging {
    public static void openMessagingApplicationWithMessage(Context context, String recipientPhoneNumber, String message){
        Intent intent = new Intent(Intent.ACTION_SENDTO);

        Uri recipientURI = Uri.parse( String.format("smsto:%s", recipientPhoneNumber) );
        intent.setData(recipientURI);

        intent.putExtra("sms_body",message);

        Intent.createChooser(intent,"Select an application to use");
        if(intent.resolveActivity(context.getPackageManager()) != null){
            context.startActivity(intent);
        }
    }
}
