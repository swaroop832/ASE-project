package com.example.bm_admin.timekeeper.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.bm_admin.timekeeper.R;
import com.example.bm_admin.timekeeper.bean.ReminderBean;
import com.example.bm_admin.timekeeper.fragments.NewTask;
import com.example.bm_admin.timekeeper.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bm-admin on 27/3/17.
 */
public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<ReminderBean> reminderBeans = new ArrayList<>();
    private Integer color;
    private HashMap<Integer, Boolean> clickedPos = new HashMap<>();

    public CustomExpandableListAdapter(Context context, ArrayList<ReminderBean> reminderBeans) {
        this.context = context;
        this.reminderBeans = reminderBeans;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return 1;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return reminderBeans.size();
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        expandedListTextView.setText(Utils.dateFormat(reminderBeans.get(listPosition).getTime()) + " , " + reminderBeans.get(listPosition).getLocation());

        expandedListTextView.setTextColor(color);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.reminderBeans.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        try {
            if (reminderBeans != null) return this.reminderBeans.size();
            else return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(final int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        Log.d("Expandable", "Liis");
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        final ImageView circleicon = (ImageView) convertView.findViewById(R.id.circleicon);
        final ImageView downarrow = (ImageView) convertView.findViewById(R.id.downarrow);

        listTitleTextView.setText(reminderBeans.get(listPosition).getDesc());
        downarrow.setImageResource(isExpanded ? R.drawable.ic_keyboard_arrowup : R.drawable.ic_keyboard_arrow);
        //  circleicon.setImageResource(isExpanded ? R.mipmap.circle_checked : R.mipmap.circle);
        listTitleTextView.setTag(listPosition);

        circleicon.setTag(listPosition);
        try {
            Log.d("Expandable", "Liis :" + clickedPos.containsKey((Integer) circleicon.getTag()));
            Log.d("Expandable", "Liis :" + clickedPos);
            if (clickedPos.containsKey((Integer) circleicon.getTag())) {
                circleicon.setImageResource(R.mipmap.circle_checked);
            } else {
                circleicon.setImageResource(R.mipmap.circle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        circleicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    View parent = (View) v.getParent();
                    TextView textView = (TextView) parent
                            .findViewById(R.id.listTitle);
                    if (clickedPos.containsKey((Integer) circleicon.getTag())) {
                        clickedPos.remove((Integer) circleicon.getTag());
                        circleicon.setImageResource(R.mipmap.circle);
                        textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    } else {
                        clickedPos.put((Integer) circleicon.getTag(), true);
                        circleicon.setImageResource(R.mipmap.circle_checked);
                        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        listTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(context, NewTask.class);
                    intent.putExtra("TASK","TASK");
                    intent.putExtra("ID",reminderBeans.get(listPosition).getId());
                    intent.putExtra("DESC",reminderBeans.get(listPosition).getDesc());
                    intent.putExtra("INTERVAL",reminderBeans.get(listPosition).getInterval());
                    intent.putExtra("TIME",Utils.dateFormat(reminderBeans.get(listPosition).getTime()));
                    intent.putExtra("LOCATION",reminderBeans.get(listPosition).getLocation());
                    intent.putExtra("REQ",reminderBeans.get(listPosition).getReqCode());
                    intent.putExtra("LAT",reminderBeans.get(listPosition).getLatitude());
                    intent.putExtra("LNG",reminderBeans.get(listPosition).getLongitude());

                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        color = Utils.getColors((int) Utils.compareDates(Utils.getCurrentDayWithTime(),
                reminderBeans.get(listPosition).getTime()));

        listTitleTextView.setTextColor(color);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}