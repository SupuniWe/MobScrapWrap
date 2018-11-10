package com.db.cdap.scrapwrap.location;

/**
 * Created by panalk on 9/12/2018.
 */

public class BinInformation {

    private String name;
    private double latitude;
    private double longitude;

    public  BinInformation(){
    }

    public BinInformation(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
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
}
