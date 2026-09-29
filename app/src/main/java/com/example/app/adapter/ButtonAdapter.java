package com.example.app.adapter;

import com.example.app.R;
import com.example.app.model.*;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder> {
    private final List<ButtonModel> buttonList;

    public ButtonAdapter(List<ButtonModel> buttonList) {
        this.buttonList = buttonList;
    }

    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_item, parent, false);
        return new ButtonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int position) {
        ButtonModel model = buttonList.get(position);

        holder.itemTitle.setText(model.getButtonText());
        holder.itemImage.setImageResource(model.getImageResId());
        holder.itemDescription.setText(model.getDescription());

        // Ensure correct visibility during binding to prevent unwanted UI issues
        holder.itemDescription.setVisibility(View.GONE);
        holder.btnNext.setVisibility(View.GONE);

        // Toggle description and next button visibility
        holder.itemView.setOnClickListener(v -> {
            boolean isVisible = holder.itemDescription.getVisibility() == View.VISIBLE;
            holder.itemDescription.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            holder.btnNext.setVisibility(isVisible ? View.GONE : View.VISIBLE);
        });

        // Open new activity when "Next" button is clicked
        holder.btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), model.getTargetActivity());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return buttonList.size();
    }

    public static class ButtonViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemTitle, itemDescription;
        Button btnNext;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemDescription = itemView.findViewById(R.id.item_description);
            btnNext = itemView.findViewById(R.id.btn_next);
        }
    }
}
