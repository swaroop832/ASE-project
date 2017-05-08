package com.example.bm_admin.timekeeper.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.bm_admin.timekeeper.AddNotes;
import com.example.bm_admin.timekeeper.NoteViewerActivity;
import com.example.bm_admin.timekeeper.R;
import com.example.bm_admin.timekeeper.adapter.NotesAdapter;
import com.example.bm_admin.timekeeper.bean.Converter;
import com.example.bm_admin.timekeeper.bean.NoteBean;
import com.example.bm_admin.timekeeper.bean.ReminderBean;
import com.example.bm_admin.timekeeper.recyclerview.RecyclerItemClickListner;
import com.example.bm_admin.timekeeper.sqlite.DBHandler;
import com.example.bm_admin.timekeeper.utility.AppConfig;
import com.example.bm_admin.timekeeper.utility.PreferenceManager;
import com.example.bm_admin.timekeeper.utility.SpacesItemDecoration;

import java.util.ArrayList;

/**
 * Created by bm-admin on 25/3/17.
 */
public class Notes extends Fragment {

    private ImageView addNotes;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "RecyclerViewActivity";
    private AppConfig appConfig;
    private ArrayList<NoteBean> noteBeans;
    private LinearLayout emptyLayout;

    public Notes() {
        appConfig = AppConfig.getInstance();
        noteBeans = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes, container, false);
        Log.d("Events", "fragment" + view);

        addNotes = (ImageView) view.findViewById(R.id.addnotes);
        emptyLayout = (LinearLayout) view.findViewById(R.id.emptyView);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        addNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddNotes.class);
                startActivityForResult(intent, 144);
            }
        });

        getNotes();

        if (noteBeans != null)
            if (noteBeans.size() == 0) {
                mRecyclerView.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
            }
        else {
            mRecyclerView.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }

        mAdapter = new NotesAdapter(noteBeans);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(2, 5, true));

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListner(getActivity(), mRecyclerView,
                new RecyclerItemClickListner.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        try {
                            Intent intent = new Intent(getActivity(), NoteViewerActivity.class);
                            intent.putExtra("NOTE", Converter.gson.toJson(noteBeans.get(position)));
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((NotesAdapter) mAdapter).setOnItemClickListener(new
                                                                 NotesAdapter.MyClickListener() {
                                                                     @Override
                                                                     public void onItemClick(int position, View v) {
                                                                         Log.i(LOG_TAG, " Clicked on Item " + position);
                                                                     }
                                                                 });
    }

    private void getNotes() {
        try {
            noteBeans.clear();
            String dbStr = DBHandler.selectHandler(AppConfig.NOTESTB, null, "email=?",
                    new String[]{appConfig.loginMail}, null, false, false, false, getActivity());
            System.out.println("Step START :" + dbStr);
            if (dbStr != null)
                noteBeans.addAll(Converter.convertJsonToNoteBeans(dbStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mRecyclerView.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);

        if (resultCode == 100) {
            try {
                getNotes();
                if (mAdapter == null) {
                    mAdapter = new NotesAdapter(noteBeans);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setNestedScrollingEnabled(false);
                    mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    mRecyclerView.addItemDecoration(new SpacesItemDecoration(2, 5, true));
                    mAdapter.notifyDataSetChanged();
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}