package com.example.bm_admin.timekeeper.tabFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bm_admin.timekeeper.R;

/**
 * Created by bm-admin on 26/3/17.
 */
public class TaskCreationFragment extends Fragment {
    public static TaskCreationFragment newInstance() {
        TaskCreationFragment fragment = new TaskCreationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.remind, container, false);
        return view;
    }
}