package com.example.app.model;

import java.io.Serializable;

public class EngineerModel implements Serializable {
    private int imageResId;
    private String name;
    private String shortDescription;
    private String email;
    private String skills;
    private String experience;

    // No-argument constructor required for Firebase deserialization
    public EngineerModel() {}

    public EngineerModel(int imageResId, String name, String shortDescription, String email, String skills, String experience) {
        this.imageResId = imageResId;
        this.name = name;
        this.shortDescription = shortDescription;
        this.email = email;
        this.skills = skills;
        this.experience = experience;
    }

    // Getters (and setters if needed)
    public int getImageResId() { return imageResId; }
    public String getName() { return name; }
    public String getShortDescription() { return shortDescription; }
    public String getEmail() { return email; }
    public String getSkills() { return skills; }
    public String getExperience() { return experience; }
}