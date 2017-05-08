package com.example.bm_admin.timekeeper.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bm_admin.timekeeper.R;

import java.util.ArrayList;

/**
 * Created by vtomoro on 2/2/17.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {
    private ArrayList<NavDrawerItem> navDrawerItems;
    private LayoutInflater mInflater;

    public SpinnerAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems, int resource) {
        super(context, resource);
        this.navDrawerItems = navDrawerItems;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
        return getCustomView(position, cnvtView, prnt);
    }

    @Override
    public View getView(int pos, View cnvtView, ViewGroup prnt) {
        return getCustomView(pos, cnvtView, prnt);
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {
        View view = mInflater.inflate(R.layout.spinner_list_item, parent,
                false);
        TextView store_name = (TextView) view.findViewById(R.id.store_name);
        store_name.setText(navDrawerItems.get(position).getYear());
        return view;
    }
}
