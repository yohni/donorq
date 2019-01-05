package com.example.yohni.donorq;

class ProfileQ {
    private String name;
    private String email;
    private String noHP;
    private String noPMI;
    private String type;
    private LocationQ location;
    private int points;
    private Status status;

    public ProfileQ(){

    }

    public ProfileQ(String uName, String uEmail, String uNoHP, String uNoPMI, String uType){
        this.name = uName;
        this.email = uEmail;
        this.noHP = uNoHP;
        this.noPMI = uNoPMI;
        this.type = uType;
        this.location = new LocationQ(1000,1000);
    }

    public ProfileQ(String uName, String uEmail, String uNoHP, String uNoPMI, String uType, LocationQ uLocation, int uPoints, Status uStatus){
        this.name = uName;
        this.email = uEmail;
        this.noHP = uNoHP;
        this.noPMI = uNoPMI;
        this.type = uType;
        this.location = uLocation;
        this.points = uPoints;
        this.status = uStatus;
    }

    public int getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getNoHP() {
        return noHP;
    }

    public String getNoPMI() {
        return noPMI;
    }

    public String getType() {
        return type;
    }

    public LocationQ getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNoHP(String noHP) {
        this.noHP = noHP;
    }

    public void setNoPMI(String noPMI) {
        this.noPMI = noPMI;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLocation(LocationQ location) {
        this.location = location;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
