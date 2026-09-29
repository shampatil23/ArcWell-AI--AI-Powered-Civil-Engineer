package com.example.app.adapter;

import com.example.app.R;
import com.example.app.model.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ApiKeyAdapter extends RecyclerView.Adapter<ApiKeyAdapter.ViewHolder> {

    public interface OnDeleteClickListener {
        void onDelete(ApiKeyModel model);
    }

    private final List<ApiKeyModel> keyList;
    private final OnDeleteClickListener deleteListener;

    public ApiKeyAdapter(List<ApiKeyModel> keyList, OnDeleteClickListener deleteListener) {
        this.keyList = keyList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_api_key, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApiKeyModel model = keyList.get(position);
        String rawKey = model.getKey();

        // Obfuscate key for display
        if (rawKey != null && rawKey.length() > 14) {
            String start = rawKey.substring(0, 8);
            String end = rawKey.substring(rawKey.length() - 5);
            holder.tvKeyPreview.setText(start + "..." + end);
        } else {
            holder.tvKeyPreview.setText(rawKey != null ? rawKey : "Invalid Key");
        }

        int fails = model.getFailCount();
        holder.tvFailBadge.setText(fails + " / 2 Failures");

        if (fails >= 1) {
            holder.tvKeyStatus.setText("Warning");
            holder.tvKeyStatus.setTextColor(0xFFF59E0B); // Amber
        } else {
            holder.tvKeyStatus.setText("Active");
            holder.tvKeyStatus.setTextColor(0xFF10B981); // Emerald Green
        }

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return keyList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvKeyPreview, tvFailBadge, tvKeyStatus;
        ImageButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKeyPreview = itemView.findViewById(R.id.tv_key_preview);
            tvFailBadge  = itemView.findViewById(R.id.tv_fail_badge);
            tvKeyStatus  = itemView.findViewById(R.id.tv_key_status);
            btnDelete    = itemView.findViewById(R.id.btn_delete_key);
        }
    }
}
