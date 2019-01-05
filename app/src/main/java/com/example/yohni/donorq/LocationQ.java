package com.example.yohni.donorq;

public class LocationQ {
    private double latitude = 1000;
    private double longitude = 1000;

    public LocationQ(){}

    public LocationQ(double uLatitude,double uLongitude){
        this.latitude = uLatitude;
        this.longitude = uLongitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
