package com.db.cdap.scrapwrap.location;

/**
 * Created by panalk on 9/16/2018.
 */

public class ObjectInformation {

    private String name;
    private double latitude;
    private double longitude;
    private String status;

    public ObjectInformation(){
    }

    public ObjectInformation(String name, double latitude, double longitude, String status) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
