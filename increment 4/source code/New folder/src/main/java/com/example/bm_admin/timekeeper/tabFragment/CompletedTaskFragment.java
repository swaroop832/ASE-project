package com.example.bm_admin.timekeeper.tabFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bm_admin.timekeeper.adapter.CustomExpandableListAdapter;
import com.example.bm_admin.timekeeper.adapter.ExpandableListDataPump;
import com.example.bm_admin.timekeeper.bean.Converter;
import com.example.bm_admin.timekeeper.bean.ReminderBean;
import com.example.bm_admin.timekeeper.fragments.NewTask;
import com.example.bm_admin.timekeeper.fragments.RemindFragement;
import com.example.bm_admin.timekeeper.R;
import com.example.bm_admin.timekeeper.sqlite.DBHandler;
import com.example.bm_admin.timekeeper.utility.AppConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bm-admin on 26/3/17.
 */
public class CompletedTaskFragment extends Fragment {

    Fragment fragment = null;
    ImageView new_task, pending_task, completed_task, add_task;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    AppConfig appConfig=AppConfig.getInstance();

    public static CompletedTaskFragment newInstance() {
        CompletedTaskFragment fragment = new CompletedTaskFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.completedtask, container, false);
        new_task = (ImageView) view.findViewById(R.id.new_task1);
        pending_task = (ImageView) view.findViewById(R.id.pending_task1);
        completed_task = (ImageView) view.findViewById(R.id.completed_task1);

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

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });


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
                    new String[]{"Y",appConfig.loginMail}, null, false, false, false, getActivity());
            System.out.println("Step usrrst:" + dbStr);
            return Converter.convertJsonToReminderBeans(dbStr);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }
}