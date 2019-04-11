package com.example.bespinaf.a2d2.utilities;

import android.telephony.PhoneNumberUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FormatUtils {
    public static Locale DEFAULT_LOCALE = Locale.ENGLISH;
    public static String DEFAULT_COUNTRY = "US";
    private static String MAP_URI_FORMAT = "google.navigation:q=%1$f, %2$f &avoid=tf";
    private static String SMS_FORMAT = "smsto:%s";
    private static final String DISPLAY_DATE_FORMAT = "MMM dd, HH:mm";
    private static final String DATABASE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";//+0000 is UTCs
    private static final TimeZone DISPLAY_TIMEZONE = TimeZone.getDefault();
    private static final TimeZone DATABASE_TIMEZONE = TimeZone.getTimeZone("UTC");

    private static String ONLY_DIGITS_REGEX = "[^\\d]*";

    private static SimpleDateFormat displayDateFormatter;
    private static SimpleDateFormat databaseDateFormatter;

    private static String formatString(String format, Object... args){
        return String.format(DEFAULT_LOCALE, format, args);
    }

    public static void initializeDateFormatters(){
        displayDateFormatter =  new SimpleDateFormat(DISPLAY_DATE_FORMAT, DEFAULT_LOCALE){{
            setTimeZone(DISPLAY_TIMEZONE);
        }};
        databaseDateFormatter = new SimpleDateFormat(DATABASE_DATE_FORMAT, DEFAULT_LOCALE){{
            setTimeZone(DATABASE_TIMEZONE);
        }};
    }

    public static String formatLatLonToGoogleMapsUri(double latitude, double longitude){
        return formatString(MAP_URI_FORMAT, latitude, longitude );
    }

    public static String formatPhoneNumberToSMSUri(String phoneNumber){
        return formatString(SMS_FORMAT, phoneNumber);
    }

    public static String formatPhoneNumber(String phoneNumber){
        return PhoneNumberUtils.formatNumber(phoneNumber, DEFAULT_COUNTRY);
    }

    public static String fetchDigitsFromString(String input){
        return input.replaceAll(ONLY_DIGITS_REGEX, "");
    }

    private static String convertDateFormat(String inputDate, SimpleDateFormat from, SimpleDateFormat to){
        try {
            Date date = from.parse(inputDate);
            return to.format(date);
        } catch (ParseException error) {
            return "";
        }
    }

    public static String dateToDisplayFormat(String databaseDate){
        return convertDateFormat(databaseDate, databaseDateFormatter, displayDateFormatter);
    }

    public static String dateToDisplayFormat(Date databaseDate){
        return dateToDisplayFormat(databaseDate.toString());
    }

    public static String dateToDatabaseFormat(String displayDate){
        return convertDateFormat(displayDate, displayDateFormatter, databaseDateFormatter);
    }

    public static String dateToDatabaseFormat(Date displayDate){
        return dateToDatabaseFormat(displayDate.toString());
    }
}
