package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ApiKeyActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "AppPrefs";
    public static final String KEY_API_KEY = "api_key";

    private EditText apiKeyEditText;
    private MaterialButton saveButton;
    private RecyclerView rvApiKeys;
    private TextView tvEmptyKeys, tvKeysHeader;
    private ProgressBar pbLoading;

    private DatabaseReference apiKeysRef;
    private DatabaseReference legacyKeyRef;
    private SharedPreferences sharedPreferences;

    private final List<ApiKeyModel> keyList = new ArrayList<>();
    private ApiKeyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_key);

        apiKeyEditText = findViewById(R.id.api_key_edit_text);
        saveButton     = findViewById(R.id.save_api_key_button);
        rvApiKeys      = findViewById(R.id.rv_api_keys);
        tvEmptyKeys    = findViewById(R.id.tv_empty_keys);
        tvKeysHeader   = findViewById(R.id.tv_keys_header);
        pbLoading      = findViewById(R.id.pb_loading_keys);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // RecyclerView setup
        rvApiKeys.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ApiKeyAdapter(keyList, model -> deleteKey(model));
        rvApiKeys.setAdapter(adapter);

        // Firebase references
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        apiKeysRef   = database.getReference("config/api_keys");
        legacyKeyRef = database.getReference("config/api_key");

        // Load and listen to keys
        loadApiKeys();

        // Add Key button
        saveButton.setOnClickListener(v -> addApiKey());
    }

    private void loadApiKeys() {
        pbLoading.setVisibility(View.VISIBLE);

        apiKeysRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pbLoading.setVisibility(View.GONE);
                keyList.clear();

                for (DataSnapshot child : snapshot.getChildren()) {
                    ApiKeyModel model = child.getValue(ApiKeyModel.class);
                    if (model != null) {
                        model.setId(child.getKey());
                        keyList.add(model);
                    }
                }

                adapter.notifyDataSetChanged();
                updateEmptyState();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pbLoading.setVisibility(View.GONE);
                Toast.makeText(ApiKeyActivity.this,
                        "Failed to load keys: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addApiKey() {
        String key = apiKeyEditText.getText().toString().trim();
        if (key.isEmpty()) {
            apiKeyEditText.setError("Please enter a valid Stability AI API key");
            apiKeyEditText.requestFocus();
            return;
        }

        if (!key.startsWith("sk-")) {
            apiKeyEditText.setError("Stability AI keys should begin with 'sk-'");
            apiKeyEditText.requestFocus();
            return;
        }

        // Check if key is already added
        for (ApiKeyModel existing : keyList) {
            if (key.equals(existing.getKey())) {
                Toast.makeText(this, "This API key is already registered", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String newId = apiKeysRef.push().getKey();
        if (newId == null) {
            newId = String.valueOf(System.currentTimeMillis());
        }

        ApiKeyModel model = new ApiKeyModel(newId, key, 0, System.currentTimeMillis());

        saveButton.setEnabled(false);
        apiKeysRef.child(newId).setValue(model).addOnCompleteListener(task -> {
            saveButton.setEnabled(true);
            if (task.isSuccessful()) {
                apiKeyEditText.setText("");
                Toast.makeText(ApiKeyActivity.this,
                        "API Key added to fallback pool!", Toast.LENGTH_SHORT).show();

                // Save locally and to legacy node for compatibility
                sharedPreferences.edit().putString(KEY_API_KEY, key).apply();
                legacyKeyRef.setValue(key);
            } else {
                Toast.makeText(ApiKeyActivity.this,
                        "Failed to add key: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteKey(ApiKeyModel model) {
        if (model.getId() != null) {
            apiKeysRef.child(model.getId()).removeValue()
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(ApiKeyActivity.this, "Key removed from database", Toast.LENGTH_SHORT).show()
                    )
                    .addOnFailureListener(e ->
                            Toast.makeText(ApiKeyActivity.this, "Failed to remove key: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        }
    }

    private void updateEmptyState() {
        tvKeysHeader.setText("Configured API Keys (" + keyList.size() + ")");
        if (keyList.isEmpty()) {
            tvEmptyKeys.setVisibility(View.VISIBLE);
            rvApiKeys.setVisibility(View.GONE);
        } else {
            tvEmptyKeys.setVisibility(View.GONE);
            rvApiKeys.setVisibility(View.VISIBLE);
        }
    }
}