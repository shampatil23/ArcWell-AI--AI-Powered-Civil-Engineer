package com.example.app.model;

public class ApiKeyModel {
    private String id;
    private String key;
    private int failCount;
    private long createdAt;

    public ApiKeyModel() {
        // Default constructor required for Firebase calls
    }

    public ApiKeyModel(String id, String key, int failCount, long createdAt) {
        this.id = id;
        this.key = key;
        this.failCount = failCount;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
