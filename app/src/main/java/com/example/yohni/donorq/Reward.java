package com.example.yohni.donorq;

public class Reward {
    private String name;
    private int points;
    private int image;

    public Reward(){}

    public void setName(String name) {
        this.name = name;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public int getImage() {
        return image;
    }
}
