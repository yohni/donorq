package com.example.yohni.donorq;

public class DataPmi {
    private String name;
    private String address;
    private String noTelp;
    private Stok stok;
    private LocationQ location;
    private String imageName;
    private String nameOnMap;

    public DataPmi(){

    }

    public DataPmi(String name, String address, String noTelp, Stok stok, LocationQ location, String imageName, String nameOnMap){
        this.name = name;
        this.address = address;
        this.noTelp = noTelp;
        this.stok = stok;
        this.location = location;
        this.imageName = imageName;
        this.nameOnMap = nameOnMap;
    }

    public String getNameOnMap() {
        return nameOnMap;
    }

    public void setNameOnMap(String nameOnMap) {
        this.nameOnMap = nameOnMap;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public Stok getStok() {
        return stok;
    }

    public LocationQ getLocation() {
        return location;
    }

    public String getImageName() {
        return imageName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public void setStok(Stok stok) {
        this.stok = stok;
    }

    public void setLocation(LocationQ location) {
        this.location = location;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
