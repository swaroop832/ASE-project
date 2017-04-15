package com.example.bm_admin.timekeeper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bm_admin.timekeeper.R;
import com.example.bm_admin.timekeeper.bean.TimeZoneBean;

import java.util.ArrayList;

/**
 * Created by bm-admin on 9/12/16.
 */
public class TimeZoneAdapter extends RecyclerView.Adapter<TimeZoneAdapter.ViewHolder> {

    private ArrayList<TimeZoneBean> timeZones;
    Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView, subTitle;
        Context contxt;

        public ViewHolder(View itemView, int viewType, Context c) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
            subTitle = (TextView) itemView.findViewById(R.id.subTitle);
        }

    }

    public TimeZoneAdapter(ArrayList<TimeZoneBean> timeZones, Context passedContext) {
        this.timeZones = timeZones;
        this.context = passedContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timezone_adapter, parent, false);
        ViewHolder vhItem = new ViewHolder(v, viewType, context);
        return vhItem;

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(timeZones.get(position).getTitle());
        holder.subTitle.setText(timeZones.get(position).getSubTitle());
    }

    @Override
    public int getItemCount() {
        return timeZones.size();
    }

}
