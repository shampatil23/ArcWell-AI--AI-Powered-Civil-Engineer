package com.example.app.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * High-performance Groq AI API Client with multi-model fallback.
 * Supported models: groq/compound, groq/compound-mini, llama-3.3-70b-versatile, qwen/qwen3.6-27b
 */
public class GroqApiClient {

    private static final String TAG = "GroqApiClient";
    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";
    public static final String API_KEY = "YOUR_GROQ_API_KEY_HERE";

    // Candidate models in preference order
    public static final List<String> MODELS = Arrays.asList(
            "groq/compound",
            "groq/compound-mini",
            "qwen/qwen3.6-27b",
            "openai/gpt-oss-120b",
            "llama-3.3-70b-versatile",
            "llama-3.1-8b-instant"
    );

    private final OkHttpClient client;
    private final Handler mainHandler;

    public interface GroqCallback {
        void onSuccess(String response, String modelUsed);
        void onError(String errorMessage);
    }

    public static class ChatMessage {
        public String role; // "system", "user", "assistant"
        public String content;

        public ChatMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    public GroqApiClient() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Send a single user prompt with an optional system prompt.
     */
    public void sendPrompt(String systemPrompt, String userPrompt, GroqCallback callback) {
        List<ChatMessage> messages = new ArrayList<>();
        if (systemPrompt != null && !systemPrompt.isEmpty()) {
            messages.add(new ChatMessage("system", systemPrompt));
        }
        messages.add(new ChatMessage("user", userPrompt));
        sendConversation(messages, 0, callback);
    }

    /**
     * Send full multi-turn conversation history for follow-up questions.
     */
    public void sendConversation(List<ChatMessage> conversationHistory, GroqCallback callback) {
        sendConversation(conversationHistory, 0, callback);
    }

    private void sendConversation(List<ChatMessage> conversationHistory, int modelIndex, GroqCallback callback) {
        if (modelIndex >= MODELS.size()) {
            mainHandler.post(() -> callback.onError("All Groq models failed to respond. Please check connection."));
            return;
        }

        String modelToUse = MODELS.get(modelIndex);

        try {
            JSONObject root = new JSONObject();
            root.put("model", modelToUse);
            root.put("temperature", 0.6);
            root.put("max_tokens", 1024);

            JSONArray messagesArray = new JSONArray();
            for (ChatMessage msg : conversationHistory) {
                JSONObject msgObj = new JSONObject();
                msgObj.put("role", msg.role);
                msgObj.put("content", msg.content);
                messagesArray.put(msgObj);
            }
            root.put("messages", messagesArray);

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, root.toString());

            Request request = new Request.Builder()
                    .url(GROQ_API_URL)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.w(TAG, "Model " + modelToUse + " network failure: " + e.getMessage());
                    // Fallback to next model
                    sendConversation(conversationHistory, modelIndex + 1, callback);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            String responseBody = response.body().string();
                            JSONObject json = new JSONObject(responseBody);
                            JSONArray choices = json.getJSONArray("choices");
                            if (choices.length() > 0) {
                                String reply = choices.getJSONObject(0)
                                        .getJSONObject("message")
                                        .getString("content")
                                        .trim();
                                mainHandler.post(() -> callback.onSuccess(reply, modelToUse));
                                return;
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing Groq response: " + e.getMessage());
                        }
                    }

                    // If HTTP 400 (e.g. model not found/deprecated) or 503, fallback to next model
                    Log.w(TAG, "Model " + modelToUse + " returned code " + response.code() + ". Trying fallback model...");
                    sendConversation(conversationHistory, modelIndex + 1, callback);
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error building Groq payload: " + e.getMessage());
            sendConversation(conversationHistory, modelIndex + 1, callback);
        }
    }
}
