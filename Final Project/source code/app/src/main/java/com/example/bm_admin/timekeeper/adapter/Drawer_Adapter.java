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

/**
 * Created by bm-admin on 9/12/16.
 */
public class Drawer_Adapter extends RecyclerView.Adapter<Drawer_Adapter.ViewHolder> {

    private static final int type_header = 0;
    private static final int type_Item = 1;
    private String mNavTitles[];
    private int mIcons[];
    private String mName;
    private int mProfile;
    private String mEmail;
    Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        int Holderid;

        TextView textView;
        ImageView imageView;
        ImageView profile;
        TextView name;
        TextView email;
        Context contxt;

        public ViewHolder(View itemView, int viewType, Context c) {
            super(itemView);

            contxt = c;


            if (viewType == type_Item) {
                Log.d("type_item", "head" + type_Item);
                textView = (TextView) itemView.findViewById(R.id.rowtext);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                Holderid = 1;
            } else {
                Log.d("type_item", "row" + type_Item);
                name = (TextView) itemView.findViewById(R.id.name);
                Holderid = 0;
            }

            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getPosition();
                    Log.w("position", "" + position);
                    Toast.makeText(contxt, "The Item Clicked is: " + position, Toast.LENGTH_SHORT).show();


                }
            });
        }


    }

    public Drawer_Adapter(String title[], int icons[], String name, String email, int profile, Context passedContext) {
        mNavTitles = title;
        mIcons = icons;
        mName = name;
        mEmail = email;
        mProfile = profile;
        this.context = passedContext;
        Log.d("type_item", "item" + mName);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        Log.d("type_item11", "item" + type_Item);
        Log.d("type_item11", "item" + viewType);
        View v;
        ViewHolder vhItem = null;
        if (viewType == type_Item) {
            Log.d("type_item12", "item" + type_Item);
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_rows, parent, false);
            vhItem = new ViewHolder(v, viewType, context);
        } else if (viewType == type_header) {
            Log.d("type_header", "  " + type_header);
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header, parent, false);
            vhItem = new ViewHolder(v, viewType, context);

        }
        return vhItem;

    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (holder.Holderid == 1) {
            Log.d("position", " " + position);
            Log.d("position", " " + mNavTitles[0]);
            holder.textView.setText(mNavTitles[position - 1]);
            holder.imageView.setImageResource(mIcons[position - 1]);
        } else {
            Log.d("position2", "item" + (position));
            holder.name.setText(mName);

        }
    }

    @Override
    public int getItemCount() {
        return mNavTitles.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return type_header;

        return type_Item;
    }

    private Boolean isPositionHeader(int position) {
        return position == 0;
    }

   /* public Fragment onTouchDrawer(final int position){

        Log.w("Position",""+position);
        if (position == 1) {
            Log.w("Position_1",""+position);
            fragment = new HomeFragement();
            *//*Intent intent = new Intent(getApplicationContext(), HomeFragement.class);
            startActivity(intent);*//*
        } else if (position == 2) {

            fragment = new RemindFragement();
        }

    }*/

}
