package com.example.app.adapter;

import com.example.app.R;
import com.example.app.model.*;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EngineerAdapter extends RecyclerView.Adapter<EngineerAdapter.EngineerViewHolder> {
    private Context context;
    private List<EngineerModel> engineerList;

    public EngineerAdapter(Context context, List<EngineerModel> engineerList) {
        this.context = context;
        this.engineerList = engineerList;
    }

    @NonNull
    @Override
    public EngineerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_engineer, parent, false);
        return new EngineerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EngineerViewHolder holder, int position) {
        EngineerModel engineer = engineerList.get(position);
        holder.nameTextView.setText("Name: "+engineer.getName());
        holder.shortDescriptionTextView.setText(engineer.getShortDescription());
        holder.profileImageView.setImageResource(R.drawable.enggg);

        // On click, open the detail activity with full engineer info
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EngineerDetailActivity.class);
            intent.putExtra("engineer", engineer);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return engineerList.size();
    }

    public static class EngineerViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView nameTextView, shortDescriptionTextView;

        public EngineerViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.engineer_image);
            nameTextView = itemView.findViewById(R.id.engineer_name);
            shortDescriptionTextView = itemView.findViewById(R.id.engineer_short_description);
        }
    }
}
