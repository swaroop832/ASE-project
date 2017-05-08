package com.example.bm_admin.timekeeper.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.bm_admin.timekeeper.R;
import com.example.bm_admin.timekeeper.TimeZoneActivity;
import com.example.bm_admin.timekeeper.alarm.AlarmReceiver;
import com.example.bm_admin.timekeeper.bean.Converter;
import com.example.bm_admin.timekeeper.bean.ReminderBean;
import com.example.bm_admin.timekeeper.bean.SettingsBean;
import com.example.bm_admin.timekeeper.bean.TimeZoneBean;
import com.example.bm_admin.timekeeper.sqlite.DBHandler;
import com.example.bm_admin.timekeeper.utility.AppConfig;
import com.example.bm_admin.timekeeper.utility.PreferenceManager;
import com.example.bm_admin.timekeeper.utility.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by bm-admin on 25/3/17.
 */
public class Settings extends Fragment implements View.OnClickListener {

    private ToggleButton fbToggle, calendarToggle, soundToggle, notificationToggle, vibrateToggle;
    private int a = 0;
    private DBHandler dbHandler;
    private AppConfig appConfig;
    private TextView viewLocation, setLocation;
    private Calendar calSet, calendar, calNow;

    public Settings() {
        dbHandler = DBHandler.getInstance();
        appConfig = AppConfig.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting, container, false);
        Log.d("Events", "fragment" + view);

        fbToggle = (ToggleButton) view.findViewById(R.id.fbToggle);
        calendarToggle = (ToggleButton) view.findViewById(R.id.calendarToggle);
        soundToggle = (ToggleButton) view.findViewById(R.id.soundToggle);
        notificationToggle = (ToggleButton) view.findViewById(R.id.notifyToggle);
        vibrateToggle = (ToggleButton) view.findViewById(R.id.vibrateToggle);
        viewLocation = (TextView) view.findViewById(R.id.viewlocation);
        setLocation = (TextView) view.findViewById(R.id.setlocation);

        calNow = Calendar.getInstance();
        calSet = (Calendar) calNow.clone();


        try {
            SettingsBean bean = Utils.getSettings(appConfig, getActivity());

            if (bean.getFb_event() != null)
                if (bean.getFb_event().equalsIgnoreCase("Y")) {
                    fbToggle.setBackgroundResource(R.mipmap.toogle_on);
                    fbToggle.setChecked(true);
                } else {
                    fbToggle.setBackgroundResource(R.mipmap.toogle_off);
                }
            if (bean.getCalendar_event() != null)
                if (bean.getCalendar_event().equalsIgnoreCase("Y")) {
                    calendarToggle.setBackgroundResource(R.mipmap.toogle_on);
                    calendarToggle.setChecked(true);
                } else {
                    calendarToggle.setBackgroundResource(R.mipmap.toogle_off);
                }
            if (bean.getSound() != null)
                if (bean.getSound().equalsIgnoreCase("Y")) {
                    soundToggle.setBackgroundResource(R.mipmap.toogle_on);
                    soundToggle.setChecked(true);
                } else {
                    soundToggle.setBackgroundResource(R.mipmap.toogle_off);
                }
            if (bean.getNotification() != null)
                if (bean.getNotification().equalsIgnoreCase("Y")) {
                    notificationToggle.setBackgroundResource(R.mipmap.toogle_on);
                    notificationToggle.setChecked(true);
                } else {
                    notificationToggle.setBackgroundResource(R.mipmap.toogle_off);
                }
            if (bean.getVibration() != null)
                if (bean.getVibration().equalsIgnoreCase("Y")) {
                    vibrateToggle.setBackgroundResource(R.mipmap.toogle_on);
                    vibrateToggle.setChecked(true);
                } else {
                    vibrateToggle.setBackgroundResource(R.mipmap.toogle_off);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!checkSettingsExists()) {
            soundToggle.setChecked(true);
            soundToggle.setBackgroundResource(R.mipmap.toogle_on);
            vibrateToggle.setChecked(true);
            vibrateToggle.setBackgroundResource(R.mipmap.toogle_on);
            notificationToggle.setChecked(true);
            notificationToggle.setBackgroundResource(R.mipmap.toogle_on);
        }

        fbToggle.setOnClickListener(this);
        calendarToggle.setOnClickListener(this);
        soundToggle.setOnClickListener(this);
        notificationToggle.setOnClickListener(this);
        vibrateToggle.setOnClickListener(this);

        if (!appConfig.baselocationName.isEmpty()) setLocation.setText(appConfig.baselocationName);

        viewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TimeZoneActivity.class);
                startActivityForResult(intent, 144);
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("TST RESULT :" + resultCode);
        if (resultCode == 144) {
            String latlng = data.getStringExtra("LATLNG");
            if (!appConfig.locationName.isEmpty()) setLocation.setText(appConfig.locationName);
            TimeZoneBean timeZoneBean = new TimeZoneBean();
            timeZoneBean.setTitle(appConfig.locationName);
            timeZoneBean.setSubTitle(latlng);
            PreferenceManager.storeIntoSharedPrefrence(getActivity(), appConfig.loginMail, Converter.gson.toJson(timeZoneBean));

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fbToggle:
                System.out.println("CHECK 1:" + fbToggle.isChecked());
                SettingsBean settingsBean = new SettingsBean();
                if (fbToggle.isChecked()) {
                    fbToggle.setBackgroundResource(R.mipmap.toogle_on);
                    settingsBean.setFb_event("Y");
                } else {
                    fbToggle.setBackgroundResource(R.mipmap.toogle_off);
                    settingsBean.setFb_event("N");
                }
                System.out.println("CHECK 21:" + Converter.gson.toJson(settingsBean));
                InsertOrUpdate(settingsBean);
                break;
            case R.id.calendarToggle:
                SettingsBean settingsBean1 = new SettingsBean();
                if (calendarToggle.isChecked()) {
                    readCalendarEvent(getActivity());
                    calendarToggle.setBackgroundResource(R.mipmap.toogle_on);
                    settingsBean1.setCalendar_event("Y");
                } else {
                    calendarToggle.setBackgroundResource(R.mipmap.toogle_off);
                    settingsBean1.setCalendar_event("N");
                }
                InsertOrUpdate(settingsBean1);
                break;
            case R.id.soundToggle:
                SettingsBean settingsBean2 = new SettingsBean();
                if (soundToggle.isChecked()) {
                    soundToggle.setBackgroundResource(R.mipmap.toogle_on);
                    settingsBean2.setSound("Y");
                } else {
                    soundToggle.setBackgroundResource(R.mipmap.toogle_off);
                    settingsBean2.setSound("N");
                }
                InsertOrUpdate(settingsBean2);
                break;
            case R.id.notifyToggle:
                SettingsBean settingsBean3 = new SettingsBean();
                if (notificationToggle.isChecked()) {
                    notificationToggle.setBackgroundResource(R.mipmap.toogle_on);
                    settingsBean3.setNotification("Y");
                } else {
                    notificationToggle.setBackgroundResource(R.mipmap.toogle_off);
                    settingsBean3.setNotification("N");
                }
                InsertOrUpdate(settingsBean3);
                break;
            case R.id.vibrateToggle:
                SettingsBean settingsBean4 = new SettingsBean();
                if (vibrateToggle.isChecked()) {
                    vibrateToggle.setBackgroundResource(R.mipmap.toogle_on);
                    settingsBean4.setVibration("Y");
                } else {
                    vibrateToggle.setBackgroundResource(R.mipmap.toogle_off);
                    settingsBean4.setVibration("N");
                }
                InsertOrUpdate(settingsBean4);
                break;
            default:
                break;
        }
    }

    private void InsertOrUpdate(SettingsBean bean) {
        System.out.println("CHECK 3:" + Converter.gson.toJson(bean));
        if (!checkSettingsExists()) {
            insertSettings(bean);
        } else {
            updateSettings(bean);
        }
    }

    private void insertSettings(SettingsBean settingsBean) {
        try {
            settingsBean.setEmail(appConfig.loginMail);
            if (dbHandler.insertHandler(AppConfig.SETTINGSTB, Converter.gson.toJson(settingsBean), getActivity())) {
            } else {
                System.out.println("Settings Insert failure");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSettings(SettingsBean settingsBean) {
        try {
            if (dbHandler.updateAndDeleteHandler(AppConfig.SETTINGSTB, Converter.gson.toJson(settingsBean),
                    "email=?", new String[]{appConfig.loginMail}, true, getActivity())) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* private Integer checkSettingsExists() {
        try {
            String dbStr = DBHandler.selectHandler(AppConfig.SETTINGSTB, null, "email= ? ", new String[]{appConfig.loginMail},
                    appConfig.RawQuery, false, true, false, getActivity());
            System.out.println("TST 01 :"+dbStr);
            return Integer.parseInt(dbStr);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }*/


    private boolean checkSettingsExists() {
        try {
            String dbStr = DBHandler.selectHandler(AppConfig.SETTINGSTB, null, "email=?",
                    new String[]{appConfig.loginMail}, null, false, false, false, getActivity());
            for (SettingsBean settingsBean : Converter.convertJsonToSettingsBeans(dbStr)) {
                if (settingsBean.getEmail().equalsIgnoreCase(appConfig.loginMail)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;

    }

    public void readCalendarEvent(Context context) {
        try {
            //cancel alarm and remove old calendar events
            cancelAlarm("CAL");
            deleteExistingEvents("CAL");

            ArrayList<String> nameOfEvent = new ArrayList<String>();
            ArrayList<String> startDates = new ArrayList<String>();
            ArrayList<String> endDates = new ArrayList<String>();
            ArrayList<String> descriptions = new ArrayList<String>();
            ArrayList<String> locations = new ArrayList<String>();

            Cursor cursor = context.getContentResolver()
                    .query(
                            Uri.parse("content://com.android.calendar/events"),
                            new String[]{"calendar_id", "title", "description",
                                    "dtstart", "dtend", "eventLocation"}, null,
                            null, null);
            cursor.moveToFirst();
            // fetching calendars name
            String CNames[] = new String[cursor.getCount()];

            // fetching calendars id
            nameOfEvent.clear();
            startDates.clear();
            endDates.clear();
            descriptions.clear();
            for (int i = 0; i < CNames.length; i++) {
                if (cursor.getString(1) != null) nameOfEvent.add(cursor.getString(1));
                if (cursor.getString(3) != null)
                    startDates.add(getDateEvent(Long.parseLong(cursor.getString(3))));
                if (cursor.getString(4) != null)
                    endDates.add(getDateEvent(Long.parseLong(cursor.getString(4))));
                if (cursor.getString(5) != null) locations.add(cursor.getString(5));
                if (cursor.getString(2) != null) descriptions.add(cursor.getString(2));
                if (cursor.getString(1) != null) CNames[i] = cursor.getString(1);
                cursor.moveToNext();
            }

            System.out.println("Remind SIze:" + nameOfEvent.size());
            int count = 0;
            while (count < nameOfEvent.size()) {
                int reqCode = (int) System.currentTimeMillis();
                ReminderBean reminderBean = new ReminderBean();
                reminderBean.setDesc(nameOfEvent.get(count));
                try {
                    calendar = Utils.parseReturnDate(startDates.get(count));
                    reminderBean.setTime(Utils.getDate(Utils.parseReturnDate(startDates.get(count))));
                    //   reminderBean.setLocation(locations.get(count));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                reminderBean.setReqCode(reqCode);
                reminderBean.setInterval("1");
                reminderBean.setCreatedDate(Utils.getCurrentDayWithTime());
                reminderBean.setStatus("N");
                reminderBean.setFlag("CAL");
                reminderBean.setLocation("");
                insertReminder(reminderBean);
                count++;
            }

            System.out.println("Event Name :" + nameOfEvent);
            System.out.println("Event stDate :" + startDates);
            System.out.println("Event endDate :" + endDates);
            System.out.println("Event desc :" + descriptions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDateEvent(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy hh:mm:ss a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private void insertReminder(ReminderBean reminderBean) {
        try {
            reminderBean.setEmail(appConfig.loginMail);
            System.out.println("EMAIL :" + appConfig.loginMail);

            if (dbHandler.insertHandler(AppConfig.REMINDERTB, Converter.gson.toJson(reminderBean), getActivity())) {
                System.out.println("Remind SIze 2:" + calendar.getTime());
                setAlarm(calendar, reminderBean.getReqCode());
            } else {
                System.out.println("Task Insert failure");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelAlarm(String flag) {
        try {
            String dbStr = DBHandler.selectHandler(AppConfig.REMINDERTB, null, "flag = ? and email = ? ",
                    new String[]{flag, appConfig.loginMail}, null, false, false, false, getActivity());
            System.out.println("Step usrrst:" + dbStr);

            for (ReminderBean reminderBean : Converter.convertJsonToReminderBeans(dbStr)) {
                Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                PendingIntent sender = PendingIntent.getBroadcast(getActivity(),
                        reminderBean.getReqCode(), intent, 0);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
                alarmManager.cancel(sender);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteExistingEvents(String flag) {
        try {
            dbHandler.updateAndDeleteHandler(AppConfig.REMINDERTB, null, "flag= ? ",
                    new String[]{flag}, false, getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAlarm(Calendar calendar, int reqCode) {
        try {
            Intent intent = new Intent(getActivity(), AlarmReceiver.class);
            intent.putExtra("ALARM", true);
            intent.putExtra("CODE", reqCode);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this.getActivity(), reqCode, intent, 0);

            int diff = (int) Utils.compareDates(calSet.getInstance().getTime(), calendar.getTime());
            System.out.println("Remind SIze 3:" + diff);
            if (diff > 0) {
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                System.out.println("Remind SIze 5:" + calendar.toString());
                ReminderBean reminderBean = new ReminderBean();
                reminderBean.setStatus("Y");
                if (dbHandler.updateAndDeleteHandler(AppConfig.REMINDERTB, Converter.gson.toJson(reminderBean),
                        "reqCode=?", new String[]{Integer.toString(reqCode)}, true, getActivity())) {
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
