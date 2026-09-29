package com.example.app.adapter;

import com.example.app.R;
import com.example.app.model.*;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlanAdapter extends BaseAdapter {

    private final Context context;
    private final String[] planNames;
    private final int[] planImages;
    private final LayoutInflater inflater;

    // Constructor
    public PlanAdapter(Context context, String[] planNames, int[] planImages) {
        this.context = context;
        this.planNames = planNames;
        this.planImages = planImages;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return planNames.length;
    }

    @Override
    public Object getItem(int position) {
        return planNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.gridImage);
            holder.textView = convertView.findViewById(R.id.gridText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageResource(planImages[position]);
        holder.textView.setText(planNames[position]);

        return convertView;
    }

    // ViewHolder class to optimize performance
    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
