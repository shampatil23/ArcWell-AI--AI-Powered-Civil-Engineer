package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ai extends BaseActivity {

    private static final String TAG = "AI_FALLBACK_ENGINE";
    private static final String DEFAULT_API_KEY = "YOUR_STABILITY_API_KEY_HERE";

    private EditText editText;
    private MaterialButton generate, downloadButton;
    private ProgressBar progressBar;
    private CardView progressCard;
    private ImageView imageView;
    private Bitmap generatedBitmap;

    private CardView cardAiSuggestions;
    private ProgressBar progressGroq;
    private android.widget.TextView tvPlanSuggestions, tvFollowUpChat, tvGroqBadge;
    private EditText etFollowUp;
    private MaterialButton btnSendFollowUp;

    private GroqApiClient groqClient;
    private final List<GroqApiClient.ChatMessage> groqChatHistory = new ArrayList<>();

    // Active key pool loaded from Firebase
    private final List<ApiKeyModel> activeKeys = new ArrayList<>();
    private DatabaseReference apiKeysRef;
    private SharedPreferences sharedPreferences;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai);

        // Initialize UI components
        editText       = findViewById(R.id.edit_text);
        generate       = findViewById(R.id.generate);
        downloadButton = findViewById(R.id.download_button);
        progressBar    = findViewById(R.id.progressbar);
        progressCard   = findViewById(R.id.progress_card);
        imageView      = findViewById(R.id.imageview);

        cardAiSuggestions = findViewById(R.id.card_ai_suggestions);
        progressGroq      = findViewById(R.id.progress_groq);
        tvPlanSuggestions = findViewById(R.id.tv_plan_suggestions);
        tvFollowUpChat    = findViewById(R.id.tv_follow_up_chat);
        tvGroqBadge       = findViewById(R.id.tv_groq_badge);
        etFollowUp        = findViewById(R.id.et_follow_up);
        btnSendFollowUp   = findViewById(R.id.btn_send_follow_up);

        groqClient = new GroqApiClient();

        if (btnSendFollowUp != null) {
            btnSendFollowUp.setOnClickListener(v -> sendFollowUpQuestion());
        }

        sharedPreferences = getSharedPreferences(ApiKeyActivity.PREFS_NAME, Context.MODE_PRIVATE);

        // Sync with Firebase realtime database
        setupFirebaseKeySync();

        // Generate button
        generate.setOnClickListener(view -> {
            String text = editText.getText().toString().trim();
            if (text.isEmpty()) {
                editText.setError("Please describe the floor plan you want");
                return;
            }
            startGenerationPipeline(text);
            requestGroqPlanSuggestions(text);
        });

        // Download button
        downloadButton.setOnClickListener(v -> {
            if (generatedBitmap != null) {
                try {
                    String savedImageURL = MediaStore.Images.Media.insertImage(
                            getContentResolver(),
                            generatedBitmap,
                            "FloorPlan_" + System.currentTimeMillis(),
                            "Generated Home Plan"
                    );
                    if (savedImageURL != null) {
                        Toast.makeText(getApplicationContext(),
                                "Image saved to gallery!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Failed to save image", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "Error saving image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        "No plan generated yet to download", Toast.LENGTH_SHORT).show();
            }
        });

        // Fullscreen view
        imageView.setOnClickListener(v -> {
            if (generatedBitmap != null) {
                Intent intent = new Intent(ai.this, FullScreenImageActivity.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                generatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("image", byteArray);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(),
                        "No image to display", Toast.LENGTH_SHORT).show();
            }
        });

        // Alternate model button
        MaterialButton errorButton = findViewById(R.id.error_button);
        if (errorButton != null) {
            errorButton.setOnClickListener(v ->
                    startActivity(new Intent(ai.this, GenerateAIActivity.class))
            );
        }
    }

    private void setupFirebaseKeySync() {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            apiKeysRef = database.getReference("config/api_keys");

            apiKeysRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    activeKeys.clear();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        ApiKeyModel model = child.getValue(ApiKeyModel.class);
                        if (model != null && model.getKey() != null && !model.getKey().trim().isEmpty()) {
                            model.setId(child.getKey());
                            activeKeys.add(model);
                        }
                    }
                    Log.d(TAG, "Loaded " + activeKeys.size() + " API keys from Firebase");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "Failed to sync keys: " + error.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Firebase init error: " + e.getMessage());
        }
    }

    /**
     * Prepares the key candidate pool (Firebase keys -> SharedPreferences key -> default key)
     */
    private List<ApiKeyModel> getExecutionKeyPool() {
        List<ApiKeyModel> pool = new ArrayList<>(activeKeys);

        if (pool.isEmpty()) {
            // Check SharedPreferences
            String savedKey = sharedPreferences.getString(ApiKeyActivity.KEY_API_KEY, "");
            if (savedKey != null && savedKey.startsWith("sk-")) {
                pool.add(new ApiKeyModel("local_pref", savedKey.trim(), 0, System.currentTimeMillis()));
            } else {
                pool.add(new ApiKeyModel("default", DEFAULT_API_KEY, 0, System.currentTimeMillis()));
            }
        }
        return pool;
    }

    private void startGenerationPipeline(String userPrompt) {
        setInProgress(true);
        List<ApiKeyModel> keyPool = getExecutionKeyPool();
        attemptGenerationWithKey(userPrompt, keyPool, 0);
    }

    private void attemptGenerationWithKey(String userPrompt, List<ApiKeyModel> keyPool, int index) {
        if (index >= keyPool.size()) {
            setInProgress(false);
            Toast.makeText(this,
                    "All available API keys failed or are out of credits. Please add a new key in the Admin Dashboard.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        ApiKeyModel currentModel = keyPool.get(index);
        String apiKey = currentModel.getKey();

        String prompt = "Architectural 2D floor plan blueprint, bird eye top-down view, clean room layout, walls, doors, windows, professional CAD drawing: " + userPrompt;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("prompt", prompt)
                .addFormDataPart("output_format", "jpeg")
                .build();

        Request request = new Request.Builder()
                .url("https://api.stability.ai/v2beta/stable-image/generate/sd3")
                .header("Authorization", "Bearer " + apiKey)
                .header("Accept", "image/*")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "Key index " + index + " network failed: " + e.getMessage());
                handleKeyFailure(userPrompt, keyPool, index, "Network connection failed");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                int code = response.code();
                if (!response.isSuccessful()) {
                    String errBody = response.body() != null ? response.body().string() : "";
                    Log.e(TAG, "Key index " + index + " failed with HTTP " + code + ": " + errBody);
                    handleKeyFailure(userPrompt, keyPool, index, "HTTP " + code);
                    return;
                }

                // Success!
                try {
                    byte[] imageBytes = response.body().bytes();
                    generatedBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    // Reset fail count on success
                    resetKeyFailure(currentModel);

                    runOnUiThread(() -> {
                        setInProgress(false);
                        if (generatedBitmap != null) {
                            imageView.setImageBitmap(generatedBitmap);
                            Toast.makeText(ai.this,
                                    "Floor plan generated successfully!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ai.this,
                                    "Failed to decode image",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Image decode failed: " + e.getMessage());
                    runOnUiThread(() -> {
                        setInProgress(false);
                        Toast.makeText(ai.this, "Error processing image", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    private void handleKeyFailure(String userPrompt, List<ApiKeyModel> keyPool, int index, String reason) {
        ApiKeyModel failedModel = keyPool.get(index);
        int newFailCount = failedModel.getFailCount() + 1;
        failedModel.setFailCount(newFailCount);

        Log.w(TAG, "Key " + failedModel.getId() + " failure #" + newFailCount + " (" + reason + ")");

        // Update fail count or prune from Firebase if >= 2
        if (apiKeysRef != null && failedModel.getId() != null && !failedModel.getId().equals("default") && !failedModel.getId().equals("local_pref")) {
            if (newFailCount >= 2) {
                // Auto-prune from database after 2 consecutive failures
                apiKeysRef.child(failedModel.getId()).removeValue();
                Log.w(TAG, "Key " + failedModel.getId() + " pruned from DB after 2 failures");
                runOnUiThread(() ->
                        Toast.makeText(ai.this,
                                "An API key failed twice and was removed from pool.",
                                Toast.LENGTH_SHORT).show()
                );
            } else {
                // Increment fail count in DB
                apiKeysRef.child(failedModel.getId()).child("failCount").setValue(newFailCount);
            }
        }

        // Try next key in pool
        runOnUiThread(() -> {
            if (index + 1 < keyPool.size()) {
                Toast.makeText(ai.this,
                        "API key issue detected. Switching to backup key...",
                        Toast.LENGTH_SHORT).show();
            }
            attemptGenerationWithKey(userPrompt, keyPool, index + 1);
        });
    }

    private void resetKeyFailure(ApiKeyModel model) {
        if (apiKeysRef != null && model.getId() != null && model.getFailCount() > 0 &&
                !model.getId().equals("default") && !model.getId().equals("local_pref")) {
            apiKeysRef.child(model.getId()).child("failCount").setValue(0);
        }
    }

    private void setInProgress(boolean inProgress) {
        runOnUiThread(() -> {
            progressCard.setVisibility(inProgress ? View.VISIBLE : View.GONE);
            generate.setVisibility(inProgress ? View.GONE : View.VISIBLE);
        });
    }

    private void requestGroqPlanSuggestions(String prompt) {
        if (cardAiSuggestions != null) {
            cardAiSuggestions.setVisibility(View.VISIBLE);
        }
        if (progressGroq != null) {
            progressGroq.setVisibility(View.VISIBLE);
        }
        if (tvPlanSuggestions != null) {
            tvPlanSuggestions.setText("Analyzing architectural requirements with Groq AI...");
        }

        groqChatHistory.clear();
        String systemPrompt = "You are ArcWell AI, a master architectural consultant and civil engineer. "
                + "The user wants a home floor plan with this description: \"" + prompt + "\". "
                + "Provide an executive, highly detailed architectural analysis with:\n"
                + "1. 📐 Recommended Room Dimensions (Living, Bedrooms, Kitchen, Bathrooms, Utility)\n"
                + "2. ☀️ Ventilation, Natural Lighting & Vastu Shastra Guidelines\n"
                + "3. 🏗️ Structural Material & Foundation Recommendations\n"
                + "4. 💰 Estimated Construction Cost & Timeline Range\n"
                + "Format with clear emojis and concise bullet points.";

        groqChatHistory.add(new GroqApiClient.ChatMessage("system", systemPrompt));
        groqChatHistory.add(new GroqApiClient.ChatMessage("user", "Please analyze this floor plan design: " + prompt));

        groqClient.sendConversation(groqChatHistory, new GroqApiClient.GroqCallback() {
            @Override
            public void onSuccess(String response, String modelUsed) {
                if (progressGroq != null) progressGroq.setVisibility(View.GONE);
                if (tvPlanSuggestions != null) tvPlanSuggestions.setText(response);
                if (tvGroqBadge != null) tvGroqBadge.setText("✦ " + modelUsed);
                groqChatHistory.add(new GroqApiClient.ChatMessage("assistant", response));
            }

            @Override
            public void onError(String errorMessage) {
                if (progressGroq != null) progressGroq.setVisibility(View.GONE);
                if (tvPlanSuggestions != null) {
                    tvPlanSuggestions.setText("AI Architect Analysis: Standard residential layout recommended. Ensure cross-ventilation in bedrooms and North-East placement for living areas.");
                }
            }
        });
    }

    private void sendFollowUpQuestion() {
        if (etFollowUp == null) return;
        String question = etFollowUp.getText().toString().trim();
        if (question.isEmpty()) return;

        etFollowUp.setText("");
        if (tvFollowUpChat != null) {
            tvFollowUpChat.setVisibility(View.VISIBLE);
            String current = tvFollowUpChat.getText().toString();
            tvFollowUpChat.setText(current + (current.isEmpty() ? "" : "\n\n") + "👤 You: " + question + "\n🤖 AI Architect: Thinking...");
        }

        groqChatHistory.add(new GroqApiClient.ChatMessage("user", question));
        groqClient.sendConversation(groqChatHistory, new GroqApiClient.GroqCallback() {
            @Override
            public void onSuccess(String response, String modelUsed) {
                groqChatHistory.add(new GroqApiClient.ChatMessage("assistant", response));
                if (tvFollowUpChat != null) {
                    String current = tvFollowUpChat.getText().toString();
                    int lastThinking = current.lastIndexOf("🤖 AI Architect: Thinking...");
                    if (lastThinking != -1) {
                        current = current.substring(0, lastThinking);
                    }
                    tvFollowUpChat.setText(current + "🤖 AI Architect (" + modelUsed + "):\n" + response);
                }
            }

            @Override
            public void onError(String errorMessage) {
                if (tvFollowUpChat != null) {
                    String current = tvFollowUpChat.getText().toString();
                    tvFollowUpChat.setText(current + "\n[Could not fetch answer: " + errorMessage + "]");
                }
            }
        });
    }
}