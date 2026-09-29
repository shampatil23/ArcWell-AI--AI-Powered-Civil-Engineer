package com.example.app.model;

public class HousePlan {
    private int squareFeet, beds, garages;
    private float baths;
    private String imagePath;

    private double matchScore = 0.0;

    // Constructor
    public HousePlan(int squareFeet, int beds, float baths, int garages, String imagePath) {
        this.squareFeet = squareFeet;
        this.beds = beds;
        this.baths = baths;
        this.garages = garages;
        this.imagePath = imagePath;
    }

    // Getters for better encapsulation
    public int getSquareFeet() {
        return squareFeet;
    }

    public int getBeds() {
        return beds;
    }

    public float getBaths() {
        return baths;
    }

    public int getGarages() {
        return garages;
    }

    public String getImagePath() {
        return imagePath;
    }

    public double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(double matchScore) {
        this.matchScore = matchScore;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "HousePlan{" +
                "squareFeet=" + squareFeet +
                ", beds=" + beds +
                ", baths=" + baths +
                ", garages=" + garages +
                ", imagePath='" + imagePath + '\'' +
                ", matchScore=" + matchScore +
                '}';
    }
}
