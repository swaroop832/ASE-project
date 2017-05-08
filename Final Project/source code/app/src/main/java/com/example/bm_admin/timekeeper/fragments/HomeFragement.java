package com.example.bm_admin.timekeeper.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AnalogClock;
import android.widget.DigitalClock;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

;import com.example.bm_admin.timekeeper.MapsActivity;
import com.example.bm_admin.timekeeper.R;
import com.example.bm_admin.timekeeper.TimeZoneActivity;
import com.example.bm_admin.timekeeper.bean.Converter;
import com.example.bm_admin.timekeeper.bean.TimeZoneBean;
import com.example.bm_admin.timekeeper.utility.AppConfig;
import com.example.bm_admin.timekeeper.utility.PreferenceManager;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by bm-admin on 19/12/16.
 */
public class HomeFragement extends Fragment {

    TextView location;
    TextView textView2;

    TextClock textClock;
    AnalogClock analog;
    LinearLayout linearLayout;
    LocationManager mLocationManager;
    private AppConfig appConfig;
    private LinearLayout locationLayout;

    public HomeFragement() {
        appConfig = AppConfig.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
        Log.d("Home", "fragment" + view);
         analog = (AnalogClock) view.findViewById(R.id.analog_clock);
        //analog clock
        //  DigitalClock digital = (DigitalClock) view.findViewById(R.id.digital_clock);
        location = (TextView) view.findViewById(R.id.location);
        textView2 = (TextView) view.findViewById(R.id.textView2);


        locationLayout = (LinearLayout) view.findViewById(R.id.locationLayout);
        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        textView2.setText(currentDateTimeString);
        textClock = (TextClock) view.findViewById(R.id.digital_clock);
/*
        Intent intent = getActivity().getIntent();
        String id = intent.getStringExtra("ID");
        Log.d("LocationID", "" +id);
        if(id.equalsIgnoreCase("2")) {
            String location = intent.getStringExtra("LOCATION");
            Log.d("Location", "" + location);
            String latlng = intent.getStringExtra("LATLNG");
            Log.d("latlng", "" + latlng);
        }*/

        /*
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        Log.d("Time zone","="+tz.getDisplayName());
        location.setText(tz.getDisplayName());
        */
        appConfig.baselocationName = PreferenceManager.retrieveFromSharedPrefrence(getContext(),"TIMEZONE");
        if (!appConfig.baselocationName.isEmpty())
            location.setText(appConfig.baselocationName);
        else
            location.setText("Set Location");



        //if (!appConfig.baselocationName.isEmpty()) location.setText(appConfig.baselocationName);

        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TimeZoneActivity.class);
                startActivityForResult(intent,144);
                /*Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivityForResult(intent, 155);*/

            }
        });

        return view;
    }

  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("TST RESULT :" + resultCode);
        if (resultCode == 155) {
            String locationStr = data.getStringExtra("LOCATION");
            Log.d("Location", "" + location);

            Address address = Converter.gson.fromJson(data.getStringExtra("LOCALE"), Address.class);
            System.out.println("LOCALE 1:" + address.getAdminArea());
            System.out.println("LOCALE 2:" + address.getLocality());
            System.out.println("LOCALE 3:" + address.getFeatureName());
            System.out.println("LOCALE 4:" + address.getSubAdminArea());
            System.out.println("LOCALE 5:" + address.getSubLocality());

            if (address.getSubAdminArea() != null) {
                appConfig.locationName =address.getSubAdminArea();
                if (!appConfig.locationName.isEmpty()) location.setText(appConfig.locationName);
            }
            if (address.getSubLocality() != null) {
                appConfig.locationName = address.getSubLocality();
                if (!appConfig.locationName.isEmpty()) location.setText(appConfig.locationName);
            }
            if (address.getLocality() != null) {
                appConfig.locationName = address.getLocality();
                if (!appConfig.locationName.isEmpty()) location.setText(appConfig.locationName);
            }

            System.out.println("appConfigloginMail" + appConfig.loginMail);
            TimeZoneBean timeZoneBean=new TimeZoneBean();
            timeZoneBean.setTitle(appConfig.locationName);
            String latlng = data.getStringExtra("LATLNG");
            timeZoneBean.setSubTitle(latlng);
            PreferenceManager.storeIntoSharedPrefrence(getActivity(), appConfig.loginMail, Converter.gson.toJson(timeZoneBean));
        }
    }*/
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      System.out.println("TST RESULT :" + resultCode);
      if (resultCode == 144) {
          //String latlng = data.getStringExtra("LATLNG");
          if (!appConfig.baselocationName.isEmpty())
          {
              PreferenceManager.storeIntoSharedPrefrence(getContext(),"TIMEZONE",appConfig.baselocationName);
              location.setText(appConfig.baselocationName);
          }


          TimeZoneBean timeZoneBean = new TimeZoneBean();
          timeZoneBean.setTitle(appConfig.locationName);
          //timeZoneBean.setSubTitle(latlng);

          TimeZone.setDefault(appConfig.defaultTimeZone);
          java.util.Calendar c = java.util.Calendar.getInstance(appConfig.defaultTimeZone);
          String currentDateTimeString = DateFormat.getDateInstance().format(c.getTime());

          textView2.setText(currentDateTimeString);
          Log.w("PROPER :",appConfig.defaultTimeZone.getDisplayName());
          Log.w("PROPER :",appConfig.defaultTimeZone.getID());
          if(appConfig.defaultTimeZone.getID() != null) {
              textClock.setTimeZone(appConfig.defaultTimeZone.getID());
          }
          //textClock.
          //PreferenceManager.storeIntoSharedPrefrence(getActivity(), appConfig.loginMail, Converter.gson.toJson(timeZoneBean));
      }
  }
}

