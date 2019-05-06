package com.example.bespinaf.a2d2.utilities;

import android.location.Location;
import android.util.Log;

import com.example.bespinaf.a2d2.models.DataSource;
import com.example.bespinaf.a2d2.models.DataSourceType;
import com.example.bespinaf.a2d2.models.Request;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataSourceUtils {
    public static DataSource requests = new DataSource(DataSourceType.Requests);
    public static DataSource resources = new DataSource(DataSourceType.BaseInfo);

    public static String getCurrentDateString(){
        Date currentDate = Calendar.getInstance().getTime();
        return FormatUtils.dateToDatabaseFormat(currentDate);
    }


    public static Request[] requestsFromData(HashMap<String, Object> data){
        Request[] requests = new Request[data.size()];
        int index = 0;
        for (Map.Entry<String, Object> row : data.entrySet()) {
            Object requestData = row.getValue();
            Request newRequest = new Request(row.getKey(), (Map<String, Object>) requestData);
            requests[index++] = newRequest;
        }
        return requests;
    }


    public static Location getLocationFromString(String locationString){
        //Matches ±Latitude,±Longitude ; does not take into account potential spaces
        Pattern locationPattern = Pattern.compile("(-?\\d+\\.\\d*),(-?\\d+\\.\\d*)");
        Matcher coordinateMatcher = locationPattern.matcher(locationString);

        double latitude = 0.0d;
        double longitude = 0.0d;

        try {
            if(coordinateMatcher.find()){
                latitude = Double.parseDouble(coordinateMatcher.group(1));
                longitude = Double.parseDouble(coordinateMatcher.group(2));
            }
        } catch (NullPointerException nullError) {
            Log.e("NullLocationError", "Database contained null location for A2D2 base");
        } catch (NumberFormatException formatError) {
            Log.e("NumberFormatError", "Database location was improperly formatted: " + locationString);
        } catch (Exception error) {
            Log.e("LocationParseError", "Database location format changed or pattern needs updating");
        }

        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        return location;
    }
}
