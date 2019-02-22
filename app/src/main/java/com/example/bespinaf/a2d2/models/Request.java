package com.example.bespinaf.a2d2.models;

public class Request {

    private String driver;
    private String status;
    private int groupSize;
    private String gender;
    private String name;
    private String phone;
    private String remarks;
    private String timestamp;
    private double lat;
    private double lon;

    public Request(){

    }

    public String getDriver() { return driver; }
    public void setDriver(String driver) { this.driver = driver; }

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

    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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