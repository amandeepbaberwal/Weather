package com.ycalm.amandeep.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by amandeep on 28-01-2018.
 */

class MyAdaptor extends RecyclerView.Adapter<MyAdaptor.ViewHolder>{
    private Context context;
    private ArrayList<String> descriptionArrayList;
    private ArrayList<String> minTempArrayList;
    private ArrayList<String> maxTempArrayList;
    private ArrayList<String> dateArrayList;
    private ArrayList<Bitmap> bitmapArrayList;
    public MyAdaptor(Context context1, ArrayList<String> descriptionArrayList, ArrayList<String> minTempArrayList, ArrayList<String> maxTempArrayList, ArrayList<String> dateArrayList, ArrayList<Bitmap> bitmapArrayList) {
        this.context = context1;
        this.descriptionArrayList = descriptionArrayList;
        this.minTempArrayList = minTempArrayList;
        this.maxTempArrayList = maxTempArrayList;
        this.dateArrayList = dateArrayList;
        this.bitmapArrayList = bitmapArrayList;
    }

    @Override
    public MyAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_layout2,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdaptor.ViewHolder holder, int position) {
        holder.description.setText(descriptionArrayList.get(position));
        holder.minTemp.setText(minTempArrayList.get(position));
        holder.maxTemp.setText(maxTempArrayList.get(position));
        holder.date.setText(dateArrayList.get(position));
        holder.icon.setImageBitmap(bitmapArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return descriptionArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView minTemp,date,maxTemp,description;
        public ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.description);
            maxTemp = itemView.findViewById(R.id.maxTemp);
            minTemp = itemView.findViewById(R.id.minTemp);
            date = itemView.findViewById(R.id.date);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}

