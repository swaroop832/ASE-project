package com.example.bm_admin.timekeeper.tabFragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bm_admin.timekeeper.adapter.CustomExpandableListAdapter;
import com.example.bm_admin.timekeeper.adapter.ExpandableListDataPump;
import com.example.bm_admin.timekeeper.bean.Converter;
import com.example.bm_admin.timekeeper.bean.ReminderBean;
import com.example.bm_admin.timekeeper.bean.TimeZoneBean;
import com.example.bm_admin.timekeeper.fragments.NewTask;
import com.example.bm_admin.timekeeper.fragments.RemindFragement;
import com.example.bm_admin.timekeeper.R;
import com.example.bm_admin.timekeeper.sqlite.DBHandler;
import com.example.bm_admin.timekeeper.utility.AppConfig;
import com.example.bm_admin.timekeeper.utility.PreferenceManager;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by bm-admin on 26/3/17.
 */
public class MyTaskFragment extends Fragment {

    private Fragment fragment = null;
    private ImageView new_task, pending_task, completed_task;
    private ExpandableListView expandableListView;
    private CustomExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;
    private ArrayList<ReminderBean> reminderBeans;
    private AppConfig appConfig;
    private LinearLayout visibletask1, visibletask2;
    private Button add_task;

    private FloatingActionButton addtask;

    public MyTaskFragment() {
        reminderBeans = new ArrayList<>();
        appConfig = AppConfig.getInstance();
    }

    public static MyTaskFragment newInstance() {
        MyTaskFragment fragment = new MyTaskFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.mytask, container, false);
        new_task = (ImageView) view.findViewById(R.id.new_task);
        pending_task = (ImageView) view.findViewById(R.id.pending_task);
        completed_task = (ImageView) view.findViewById(R.id.completed_task);
        addtask = (FloatingActionButton) view.findViewById(R.id.add_task1);
        visibletask1 = (LinearLayout) view.findViewById(R.id.visibletask);
        visibletask2 = (LinearLayout) view.findViewById(R.id.visibletask2);
        add_task = (Button) view.findViewById(R.id.addtask1);


        add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewTask.class);
                intent.putExtra("TASK","ADDTASK");
                startActivityForResult(intent, 101);
                //startActivity(intent);
            }
        });
        addtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewTask.class);
                intent.putExtra("TASK","ADDTASK");
                startActivityForResult(intent, 101);
                //startActivity(intent);
            }
        });
        new_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragment = new RemindFragement();
                fragmentManager.beginTransaction().replace(R.id.contentfragment, fragment).commit();
            }
        });
        pending_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragment = new MyTaskFragment();
                fragmentManager.beginTransaction().replace(R.id.contentfragment, fragment).commit();
            }
        });
        completed_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragment = new CompletedTaskFragment();
                fragmentManager.beginTransaction().replace(R.id.contentfragment, fragment).commit();
            }
        });
        DisplayMetrics metrics = new DisplayMetrics();
        int width = metrics.widthPixels;

        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(getContext(), getReminders());
        expandableListView.setAdapter(expandableListAdapter);
        // expandableListView.setIndicatorBounds(width-GetPixelFromDips(35), width-GetPixelFromDips(5));

        if (getReminders().size() == 0) {
            Log.d("Firsttime", "");
            visibletask1.setVisibility(View.VISIBLE);
            visibletask2.setVisibility(View.GONE);
        } else {
            Log.d("Secondtime", "");
            visibletask1.setVisibility(View.GONE);
            visibletask2.setVisibility(View.VISIBLE);
        }
        return view;
    }
 /*   public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }*/

    private ArrayList<ReminderBean> getReminders() {
        try {
            String dbStr = DBHandler.selectHandler(AppConfig.REMINDERTB, null, "status=? and email=?",
                    new String[]{"N", appConfig.loginMail}, null, false, false, false, getActivity());
            System.out.println("Step usrrst:" + dbStr);

            return Converter.convertJsonToReminderBeans(dbStr);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        System.out.println("RESUME RESULT :");
        expandableListAdapter = new CustomExpandableListAdapter(getContext(), getReminders());
        expandableListView.setAdapter(expandableListAdapter);
        expandableListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("TST RESULT :" + resultCode);
        if (resultCode == 101) {
            String returnedResult = data.getData().toString();
            expandableListAdapter = new CustomExpandableListAdapter(getContext(), getReminders());
            expandableListView.setAdapter(expandableListAdapter);
            expandableListAdapter.notifyDataSetChanged();
        }
    }
}