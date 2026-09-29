package com.example.app.model;

public class ButtonModel {
    private String buttonText;
    private int imageResId;
    private String description;
    private Class<?> targetActivity;  // Renamed for consistency

    // Constructor
    public ButtonModel(String buttonText, int imageResId, String description, Class<?> targetActivity) {
        this.buttonText = buttonText;
        this.imageResId = imageResId;
        this.description = description;
        this.targetActivity = targetActivity;
    }

    // Getter methods
    public String getButtonText() { return buttonText; }
    public int getImageResId() { return imageResId; }
    public String getDescription() { return description; }
    public Class<?> getTargetActivity() { return targetActivity; }  // Updated method name

    // Alternative names for clarity (if needed)
    public String getTitle() { return buttonText; }
    public int getIcon() { return imageResId; }
}
