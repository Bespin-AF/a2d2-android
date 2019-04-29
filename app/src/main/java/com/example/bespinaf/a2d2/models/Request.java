package com.example.bespinaf.a2d2.models;

import java.io.Serializable;

public class Request implements Comparable<Request>, Serializable {

    public String key;
    private RequestData data;


    public Request(){
        key = null;
        data = new RequestData();
    }


    public Request(String key, RequestData data) {
        this.key = key;
        this.data = data;
    }


    public RequestData getData() {
        return data;
    }

    public String getDriver() {
        return data.driver;
    }

    public void setDriver(String driver) {
        data.driver = driver;
    }

    public RequestStatus getStatus() {
        return RequestStatus.getStatusFromString(data.status);
    }

    public void setStatus(RequestStatus status) {
        data.status = RequestStatus.getStringFromStatus(status);
    }

    public int getGroupSize() {
        return data.groupSize;
    }
    
    public void setGroupSize(int groupSize) {
        data.groupSize = groupSize;
    }

    public String getGender() {
        return data.gender;
    }

    public void setGender(String gender) {
        data.gender = gender;
    }

    public String getName() {
        return data.name;
    }

    public void setName(String name) {
        data.name = name;
    }

    public String getPhone() {
        return data.phone;
    }

    public void setPhone(String phone) {
        data.phone = phone;
    }

    public String getRemarks() {
        return data.remarks;
    }

    public void setRemarks(String remarks) {
        data.remarks = remarks;
    }

    public String getTimestamp() {
        return data.timestamp;
    }

    public void setTimestamp(String timestamp) {
        data.timestamp = timestamp;
    }

    public double getLat() {
        return data.lat;
    }

    public void setLat(double lat) {
        data.lat = lat;
    }

    public double getLon() {
        return data.lon;
    }

    public void setLon(double lon) {
        data.lon = lon;
    }

    @Override
    public int compareTo(Request o) {
        return this.key.compareTo(o.key);
    }
}
