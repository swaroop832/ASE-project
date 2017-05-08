package com.example.bm_admin.timekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.bm_admin.timekeeper.adapter.TimeZoneAdapter;
import com.example.bm_admin.timekeeper.bean.Converter;
import com.example.bm_admin.timekeeper.bean.TimeZoneBean;
import com.example.bm_admin.timekeeper.recyclerview.RecyclerItemClickListner;
import com.example.bm_admin.timekeeper.sqlite.DBHandler;
import com.example.bm_admin.timekeeper.utility.AppConfig;
import com.example.bm_admin.timekeeper.utility.PreferenceManager;
import com.google.android.gms.common.data.DataHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeZoneActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TimeZoneAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<TimeZoneBean> timeZoneList = new ArrayList<>();
    private ArrayList<TimeZoneBean> allTimeZones = new ArrayList<>();
    private AppConfig appConfig;
    private DBHandler dbHandler;
    private SearchView inputSearch;

    public TimeZoneActivity() {
        appConfig = AppConfig.getInstance();
        dbHandler = DBHandler.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_zone);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inputSearch = (SearchView)findViewById(R.id.inputSearch);

        loadTimeZones();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new TimeZoneAdapter(timeZoneList, getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


        inputSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                searchTimeZones(text);
                //Log.d("onQueryTextChange","onQueryTextChange");
                mAdapter.notifyDataSetChanged();
                return true;
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListner(getApplicationContext(), recyclerView,
                new RecyclerItemClickListner.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        try {
                            String timeZoneJson = Converter.gson.toJson(timeZoneList.get(position));
                            appConfig.defaultTimeZone = timeZoneList.get(position).getTimeZone();

                            PreferenceManager.storeIntoSharedPrefrence(getApplicationContext(), appConfig.loginMail, timeZoneJson);
                            String[] parts = timeZoneList.get(position).getTitle().split("/");
                            //   Calendar.set(TimeZone.getTimeZone(timeZoneList.get(position).getTitle()));
                            appConfig.baselocationName = parts[parts.length - 1];
                            Intent data = new Intent();
                            setResult(144, data);
                            //---close the activity---
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
    }

    private void loadTimeZones() {
        try {
            String[] ids = TimeZone.getAvailableIDs();
            for (String id : ids) {
                timeZoneList.add(new TimeZoneBean(TimeZone.getTimeZone(id),displayTimeZone(TimeZone.getTimeZone(id)), TimeZone.getTimeZone(id).getDisplayName()));
                allTimeZones.add(new TimeZoneBean(TimeZone.getTimeZone(id),displayTimeZone(TimeZone.getTimeZone(id)), TimeZone.getTimeZone(id).getDisplayName()));
                System.out.println(displayTimeZone(TimeZone.getTimeZone(id)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchTimeZones(String searchText) {
        try {
            timeZoneList.clear();
            String[] ids = TimeZone.getAvailableIDs();
            for (String id : ids) {
                        if(TimeZone.getTimeZone(id).getDisplayName().toLowerCase().contains(searchText.toLowerCase())) {
                            timeZoneList.add(new TimeZoneBean(TimeZone.getTimeZone(id),displayTimeZone(TimeZone.getTimeZone(id)),TimeZone.getTimeZone(id).getDisplayName()));
                        }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String displayTimeZone(TimeZone tz) {
        String result = "";
        try {
            long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset())
                    - TimeUnit.HOURS.toMinutes(hours);
            // avoid -4:-30 issue
            minutes = Math.abs(minutes);

            if (hours > 0) {
                result = String.format("(GMT+%d:%02d) %s", hours, minutes, tz.getID());
            } else {
                result = String.format("(GMT%d:%02d) %s", hours, minutes, tz.getID());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

  /*  void filter(String text) {
        List<DataHolder> temp = new ArrayList();
        for (DataHolder d : displayedList) {
            //or use .contains(text)
            if (d.getEnglish().equal(text)) {
                temp.add(d);
            }
        }
        //update recyclerview
        disp_adapter.updateList(temp);
    }

    public void updateList(List<DataHolder> list) {
        displayedList = list;
        notifyDataSetChanged();
    }*/
}
