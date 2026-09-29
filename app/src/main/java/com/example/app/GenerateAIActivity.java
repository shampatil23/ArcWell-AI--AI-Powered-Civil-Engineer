package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * ArcWell AI Assistant Bot Activity.
 * High-speed architectural chatbot powered by Groq Compound AI.
 * Handles interactive floor plan planning, Vastu compliance, cost estimation,
 * material queries, and multi-turn follow-up conversations.
 */
public class GenerateAIActivity extends BaseActivity {

    private ImageButton btnBack;
    private ImageButton btnClearChat;
    private TextView tvActiveModel;
    private RecyclerView rvChatMessages;
    private LinearLayout layoutWelcome;
    private LinearLayout layoutTyping;
    private TextView tvTypingStatus;
    private EditText etMessageInput;
    private CardView btnSend;

    // Suggestion chips
    private TextView chipPrompt1, chipPrompt2, chipPrompt3, chipPrompt4, chipPrompt5;

    private ChatMessageAdapter adapter;
    private final List<ChatMessageItem> displayMessages = new ArrayList<>();
    private final List<GroqApiClient.ChatMessage> apiConversation = new ArrayList<>();
    private GroqApiClient groqClient;

    private static final String SYSTEM_PROMPT =
            "You are ArcWell AI, an elite architectural assistant, structural engineer, and construction planner. "
            + "Help the user with residential/commercial floor plans, optimal room dimensions, Vastu Shastra guidelines, "
            + "civil engineering safety, material requirements, and construction budget estimation. "
            + "Keep explanations clear, practical, well-structured, and use emojis and bullet points where helpful.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_aiactivity);

        initViews();
        initGroq();
        setupRecyclerView();
        setupListeners();
        setupSuggestions();

        // Add welcome bot greeting
        addBotMessage("👋 Hello! I am **ArcWell AI**, your architectural assistant.\n\n"
                + "Ask me anything about:\n"
                + "• 📐 Floor plan layouts & room dimensions\n"
                + "• ☀️ Vastu Shastra compliance\n"
                + "• 💰 Construction costs & budget breakdowns\n"
                + "• 🧱 Building materials & structural tips\n\n"
                + "How can I help you design your ideal space today?", "groq/compound");
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        btnClearChat = findViewById(R.id.btn_clear_chat);
        tvActiveModel = findViewById(R.id.tv_active_model);
        rvChatMessages = findViewById(R.id.rv_chat_messages);
        layoutWelcome = findViewById(R.id.layout_welcome);
        layoutTyping = findViewById(R.id.layout_typing);
        tvTypingStatus = findViewById(R.id.tv_typing_status);
        etMessageInput = findViewById(R.id.et_message_input);
        btnSend = findViewById(R.id.btn_send);

        chipPrompt1 = findViewById(R.id.chip_prompt_1);
        chipPrompt2 = findViewById(R.id.chip_prompt_2);
        chipPrompt3 = findViewById(R.id.chip_prompt_3);
        chipPrompt4 = findViewById(R.id.chip_prompt_4);
        chipPrompt5 = findViewById(R.id.chip_prompt_5);
    }

    private void initGroq() {
        groqClient = new GroqApiClient();
        apiConversation.clear();
        apiConversation.add(new GroqApiClient.ChatMessage("system", SYSTEM_PROMPT));
    }

    private void setupRecyclerView() {
        adapter = new ChatMessageAdapter(this, displayMessages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvChatMessages.setLayoutManager(layoutManager);
        rvChatMessages.setAdapter(adapter);
    }

    private void setupListeners() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnClearChat != null) {
            btnClearChat.setOnClickListener(v -> resetChat());
        }

        if (btnSend != null) {
            btnSend.setOnClickListener(v -> handleSendMessage());
        }
    }

    private void setupSuggestions() {
        if (chipPrompt1 != null) {
            chipPrompt1.setOnClickListener(v -> sendUserPrompt("Generate an optimal 30x40 East-facing house plan layout with complete room dimensions and Vastu guidelines."));
        }
        if (chipPrompt2 != null) {
            chipPrompt2.setOnClickListener(v -> sendUserPrompt("Plan a modern 1500 sq ft 3 BHK layout with attached bathrooms, pooja room, and open concept modular kitchen."));
        }
        if (chipPrompt3 != null) {
            chipPrompt3.setOnClickListener(v -> sendUserPrompt("What are the most crucial Vastu Shastra rules for kitchen, master bedroom, and main entrance placements?"));
        }
        if (chipPrompt4 != null) {
            chipPrompt4.setOnClickListener(v -> sendUserPrompt("Provide an itemized construction cost and material estimate (cement, steel, sand, bricks) for a 1500 sq ft duplex house."));
        }
        if (chipPrompt5 != null) {
            chipPrompt5.setOnClickListener(v -> sendUserPrompt("What are the top eco-friendly and cost-effective construction materials suitable for modern home building?"));
        }
    }

    private void handleSendMessage() {
        if (etMessageInput == null) return;
        String message = etMessageInput.getText().toString().trim();
        if (message.isEmpty()) {
            Toast.makeText(this, "Please type a question or prompt", Toast.LENGTH_SHORT).show();
            return;
        }
        etMessageInput.setText("");
        sendUserPrompt(message);
    }

    private void sendUserPrompt(String prompt) {
        addUserMessage(prompt);
        apiConversation.add(new GroqApiClient.ChatMessage("user", prompt));

        showTypingIndicator(true, "ArcWell AI is formulating recommendations...");

        groqClient.sendConversation(apiConversation, new GroqApiClient.GroqCallback() {
            @Override
            public void onSuccess(String response, String modelUsed) {
                showTypingIndicator(false, null);
                addBotMessage(response, modelUsed);
                apiConversation.add(new GroqApiClient.ChatMessage("assistant", response));
                if (tvActiveModel != null) {
                    tvActiveModel.setText("Online • " + modelUsed);
                }
            }

            @Override
            public void onError(String errorMessage) {
                showTypingIndicator(false, null);
                addBotMessage("⚠️ Could not retrieve response at this moment: " + errorMessage + "\n\nPlease check your internet connection and try again.", "offline");
            }
        });
    }

    private void addUserMessage(String message) {
        if (layoutWelcome != null) layoutWelcome.setVisibility(View.GONE);
        displayMessages.add(new ChatMessageItem(ChatMessageItem.TYPE_USER, message, null));
        adapter.notifyItemInserted(displayMessages.size() - 1);
        rvChatMessages.scrollToPosition(displayMessages.size() - 1);
    }

    private void addBotMessage(String message, String model) {
        if (layoutWelcome != null) layoutWelcome.setVisibility(View.GONE);
        displayMessages.add(new ChatMessageItem(ChatMessageItem.TYPE_BOT, message, model));
        adapter.notifyItemInserted(displayMessages.size() - 1);
        rvChatMessages.scrollToPosition(displayMessages.size() - 1);
    }

    private void showTypingIndicator(boolean show, String statusText) {
        if (layoutTyping != null) {
            layoutTyping.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        if (tvTypingStatus != null && statusText != null) {
            tvTypingStatus.setText(statusText);
        }
        if (show && displayMessages.size() > 0) {
            rvChatMessages.scrollToPosition(displayMessages.size() - 1);
        }
    }

    private void resetChat() {
        displayMessages.clear();
        adapter.notifyDataSetChanged();
        initGroq();
        if (tvActiveModel != null) {
            tvActiveModel.setText("Online • Groq Compound");
        }
        addBotMessage("Chat reset! Ask me any question about floor plans, architectural layouts, or construction advice.", "groq/compound");
        Toast.makeText(this, "Chat history cleared", Toast.LENGTH_SHORT).show();
    }
}
