package com.example.bm_admin.timekeeper.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.bm_admin.timekeeper.R;
import com.example.bm_admin.timekeeper.tabFragment.MyTaskFragment;
import com.example.bm_admin.timekeeper.tabFragment.CompletedTaskFragment;


/**
 * Created by bm-admin on 19/12/16.
 */
public class RemindFragement extends Fragment  {

    Button addtask;

    Fragment fragment = null;
    ImageView new_task,pending_task,completed_task;
    FloatingActionButton add_task;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.remind, container, false);
        Log.d("Events","fragment"+view);

        new_task = (ImageView) view.findViewById(R.id.new_task);
        pending_task = (ImageView) view.findViewById(R.id.pending_task);
        completed_task = (ImageView) view.findViewById(R.id.completed_task);
        add_task = (FloatingActionButton) view.findViewById(R.id.add_task);
        addtask = (Button)view.findViewById(R.id.addtask);

     //   new_task.setBackgroundColor(Color.WHITE);

        new_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragment = new RemindFragement();
                fragmentManager.beginTransaction().replace(R.id.contentfragment,fragment).commit();
            }
        });
        pending_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragment = new MyTaskFragment();
                fragmentManager.beginTransaction().replace(R.id.contentfragment,fragment).commit();
            }
        });
        completed_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragment = new CompletedTaskFragment();
                fragmentManager.beginTransaction().replace(R.id.contentfragment,fragment).commit();
            }
        });

        addtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewTask.class);
                intent.putExtra("TASK","AddTASK");
                startActivity(intent);
            }
        });
        add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewTask.class);
                intent.putExtra("TASK","AddTASK");
                startActivity(intent);
            }
        });



        return view;


    }
}
