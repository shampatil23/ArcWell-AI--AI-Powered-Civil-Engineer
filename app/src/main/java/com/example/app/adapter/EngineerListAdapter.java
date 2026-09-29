package com.example.app.adapter;

import com.example.app.R;
import com.example.app.model.*;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class EngineerListAdapter extends RecyclerView.Adapter<EngineerListAdapter.EngineerViewHolder> {

    private List<Engineer> engineerList;

    public EngineerListAdapter(List<Engineer> engineerList) {
        this.engineerList = (engineerList != null) ? engineerList : new ArrayList<>();
    }

    @NonNull
    @Override
    public EngineerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_engineer, parent, false);
        return new EngineerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EngineerViewHolder holder, int position) {
        if (engineerList == null || engineerList.isEmpty() || position >= engineerList.size()) return;

        Engineer engineer = engineerList.get(position);
        if (engineer != null) {
            holder.name.setText(!TextUtils.isEmpty(engineer.getName()) ? engineer.getName() : "N/A");
            holder.email.setText(!TextUtils.isEmpty(engineer.getEmail()) ? engineer.getEmail() : "N/A");
            holder.experience.setText(!TextUtils.isEmpty(engineer.getExperience()) ? engineer.getExperience() : "N/A");
            holder.skills.setText(!TextUtils.isEmpty(engineer.getSkills()) ? engineer.getSkills() : "N/A");
        }
    }

    @Override
    public int getItemCount() {
        return (engineerList != null) ? engineerList.size() : 0;
    }

    // Method to update data and refresh RecyclerView
    public void setData(List<Engineer> newList) {
        if (newList != null) {
            engineerList.clear();
            engineerList.addAll(newList);
            notifyDataSetChanged();
        }
    }

    static class EngineerViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, experience, skills;

        EngineerViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            email = itemView.findViewById(R.id.tvEmail);
            experience = itemView.findViewById(R.id.tvExperience);
            skills = itemView.findViewById(R.id.tvSkills);
        }
    }
}
