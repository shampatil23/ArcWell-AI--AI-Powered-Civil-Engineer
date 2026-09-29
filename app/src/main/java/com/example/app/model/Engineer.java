package com.example.app.model;

public class Engineer {
    private String engineerId;
    private String name;
    private String email;
    private String experience;
    private String skills; // Using 'skills' instead of 'specialization'

    // Empty constructor required for Firebase
    public Engineer() {
    }

    // Constructor with all fields
    public Engineer(String engineerId, String name, String email, String experience, String skills) {
        this.engineerId = (engineerId != null) ? engineerId : ""; // Avoid null values
        this.name = name;
        this.email = email;
        this.experience = experience;
        this.skills = skills;
    }

    // Getters
    public String getEngineerId() {
        return engineerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getExperience() {
        return experience;
    }

    public String getSkills() { // Getter for skills
        return skills;
    }

    // Setters
    public void setEngineerId(String engineerId) {
        this.engineerId = (engineerId != null) ? engineerId : "";
    }

    public void setName(String name) {
        this.name = (name != null) ? name : "Unknown";
    }

    public void setEmail(String email) {
        this.email = (email != null) ? email : "No Email";
    }

    public void setExperience(String experience) {
        this.experience = (experience != null) ? experience : "Not Provided";
    }

    public void setSkills(String skills) { // Setter for skills
        this.skills = (skills != null) ? skills : "No Skills Listed";
    }

    // toString() method for debugging
    @Override
    public String toString() {
        return "Engineer {" +
                "\n  Engineer ID: '" + engineerId + '\'' +
                "\n  Name: '" + name + '\'' +
                "\n  Email: '" + email + '\'' +
                "\n  Experience: '" + experience + '\'' +
                "\n  Skills: '" + skills + '\'' +
                "\n}";
    }
}
