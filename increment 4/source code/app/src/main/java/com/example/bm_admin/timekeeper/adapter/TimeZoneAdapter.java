package com.example.bm_admin.timekeeper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bm_admin.timekeeper.R;
import com.example.bm_admin.timekeeper.bean.TimeZoneBean;

import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Created by bm-admin on 9/12/16.
 */
public class TimeZoneAdapter extends RecyclerView.Adapter<TimeZoneAdapter.ViewHolder> implements Filterable {


    private ArrayList<TimeZoneBean> timeZones;
    private ArrayList<TimeZoneBean> timeZonesMatch;
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
        this.timeZonesMatch = timeZones;
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



    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Log.d("publishResults","publishResults");
                timeZonesMatch = (ArrayList<TimeZoneBean> ) results.values;
                TimeZoneAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.d("FilterResults","FilterResults");
                ArrayList<TimeZoneBean> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = timeZones;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    protected ArrayList<TimeZoneBean> getFilteredResults(String constraint) {
        Log.d("getFilteredResults","getFilteredResults");
        ArrayList<TimeZoneBean> results = new ArrayList<>();

        for (TimeZoneBean item : timeZones) {
            if (item.getTitle().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }

}
