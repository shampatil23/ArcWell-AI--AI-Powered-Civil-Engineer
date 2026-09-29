package com.example.app.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatMessageItem {
    public static final int TYPE_USER = 1;
    public static final int TYPE_BOT = 2;

    private final int type;
    private final String message;
    private final String model;
    private final String timestamp;

    public ChatMessageItem(int type, String message, String model) {
        this.type = type;
        this.message = message;
        this.model = model;
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        this.timestamp = sdf.format(new Date());
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getModel() {
        return model;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
