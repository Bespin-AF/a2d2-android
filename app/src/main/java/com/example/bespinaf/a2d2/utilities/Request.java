package com.example.bespinaf.a2d2.utilities;

import java.util.Date;

public class Request {

    private String status;
    private int groupSize;
    private String gender;
    private String name;
    private String phone;
    private String remarks;
    private String timeStamp;
    private double lat;
    private double lon;

    public Request(){

    }

    public Request(String status, int groupSize, String gender, String name, String phone, String remarks, String timeStamp, double lat, double lon){
        this.status = status;
        this.groupSize = groupSize;
        this.gender = gender;
        this.name = name;
        this.phone = phone;
        this.remarks = remarks;
        this.timeStamp = timeStamp;
        this.lat = lat;
        this.lon = lon;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public int getGroupSize() {
        return groupSize;
    }
    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }
}
