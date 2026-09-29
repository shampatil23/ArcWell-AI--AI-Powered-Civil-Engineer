package com.example.app.adapter;

import com.example.app.R;
import com.example.app.model.*;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<ChatMessageItem> messages;

    public ChatMessageAdapter(Context context, List<ChatMessageItem> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ChatMessageItem.TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_user, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_bot, parent, false);
            return new BotViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessageItem item = messages.get(position);
        if (holder instanceof UserViewHolder) {
            UserViewHolder userHolder = (UserViewHolder) holder;
            userHolder.tvMessage.setText(item.getMessage());
            userHolder.tvTime.setText(item.getTimestamp());
        } else if (holder instanceof BotViewHolder) {
            BotViewHolder botHolder = (BotViewHolder) holder;
            botHolder.tvMessage.setText(item.getMessage());
            botHolder.tvTime.setText(item.getTimestamp());
            if (item.getModel() != null && !item.getModel().isEmpty()) {
                botHolder.tvModel.setVisibility(View.VISIBLE);
                botHolder.tvModel.setText("✦ " + item.getModel());
            } else {
                botHolder.tvModel.setVisibility(View.GONE);
            }

            botHolder.btnCopy.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard != null) {
                    ClipData clip = ClipData.newPlainText("AI Architecture Plan", item.getMessage());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, "Copied recommendation to clipboard", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_user_message);
            tvTime = itemView.findViewById(R.id.tv_user_time);
        }
    }

    static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvModel, tvTime;
        ImageView btnCopy;

        BotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_bot_message);
            tvModel = itemView.findViewById(R.id.tv_bot_model);
            tvTime = itemView.findViewById(R.id.tv_bot_time);
            btnCopy = itemView.findViewById(R.id.btn_copy_bot);
        }
    }
}
