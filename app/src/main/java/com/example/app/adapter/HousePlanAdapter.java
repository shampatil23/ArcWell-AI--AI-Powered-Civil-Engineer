package com.example.app.adapter;

import com.example.app.R;
import com.example.app.model.*;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

public class HousePlanAdapter extends RecyclerView.Adapter<HousePlanAdapter.ViewHolder> {

    private Context context;
    private List<HousePlan> housePlans;

    public HousePlanAdapter(Context context, List<HousePlan> housePlans) {
        this.context = context;
        this.housePlans = housePlans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_house_plan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HousePlan housePlan = housePlans.get(position);
        holder.setImageFromAssets(housePlan.getImagePath());

        // Set dynamic title & description from ML inference
        if (holder.titleTextView != null) {
            if (housePlan.getMatchScore() > 0) {
                holder.titleTextView.setText(String.format(Locale.US, "%d BHK Design (%.1f%% Match)", housePlan.getBeds(), housePlan.getMatchScore()));
            } else {
                holder.titleTextView.setText(String.format(Locale.US, "%d BHK Architectural Plan", housePlan.getBeds()));
            }
        }

        if (holder.descriptionTextView != null) {
            holder.descriptionTextView.setText(String.format(Locale.US,
                    "%,d Sq. Ft. • %.1f Baths • %d Garage(s)",
                    housePlan.getSquareFeet(),
                    housePlan.getBaths(),
                    housePlan.getGarages()));
        }

        // Click listeners to open full-screen high-res blueprint
        View.OnClickListener openDetail = v -> {
            Intent intent = new Intent(context, ImageDetailActivity.class);
            intent.putExtra("imagePath", housePlan.getImagePath());
            context.startActivity(intent);
        };

        holder.imageView.setOnClickListener(openDetail);
        if (holder.detailsButton != null) {
            holder.detailsButton.setOnClickListener(openDetail);
        }
    }

    @Override
    public int getItemCount() {
        return housePlans.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView descriptionTextView;
        Button detailsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.house_image);
            titleTextView = itemView.findViewById(R.id.house_title);
            descriptionTextView = itemView.findViewById(R.id.house_description);
            detailsButton = itemView.findViewById(R.id.view_details_button);
        }

        // Load image from assets safely with path normalization
        public void setImageFromAssets(String imagePath) {
            try {
                AssetManager assetManager = itemView.getContext().getAssets();
                String cleanPath = imagePath.trim();
                String fullPath = cleanPath.startsWith("images/") ? cleanPath : "images/" + cleanPath;

                try (InputStream inputStream = assetManager.open(fullPath)) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);
                }

            } catch (IOException e) {
                Log.e("HousePlanAdapter", "Image not found: " + imagePath, e);
                // Set a default placeholder image if the asset is missing
                imageView.setImageResource(R.drawable.img);
            }
        }
    }
}
