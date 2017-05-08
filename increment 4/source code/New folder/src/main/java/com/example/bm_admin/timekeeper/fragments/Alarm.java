package com.example.bm_admin.timekeeper.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DigitalClock;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bm_admin.timekeeper.R;
import com.example.bm_admin.timekeeper.adapter.NavDrawerItem;
import com.example.bm_admin.timekeeper.adapter.SpinnerAdapter;
import com.example.bm_admin.timekeeper.alarm.AlarmReceiver;
import com.example.bm_admin.timekeeper.bean.AlarmBean;
import com.example.bm_admin.timekeeper.bean.Converter;
import com.example.bm_admin.timekeeper.sqlite.DBHandler;
import com.example.bm_admin.timekeeper.utility.AppConfig;
import com.example.bm_admin.timekeeper.utility.Utils;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by bm-admin on 25/3/17.
 */
public class Alarm extends Fragment implements View.OnClickListener {
    private DigitalClock digital;
    private LinearLayout linearLayout;
    private TextView alarmTime;
    private Spinner alarmSpinner;
    private CheckBox checkBox;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Button saveButton;
    private DBHandler dbHandler;
    private AppConfig appConfig;
    private String[] spinnerValues;
    private SpinnerAdapter spinnerAdapter;
    private Spinner spinner;
    private ArrayList<NavDrawerItem> navDrawerItemsBW = new ArrayList<>();
    Calendar calNow;
    Calendar calSet;

    private String timeSet = "AM";

    public Alarm() {
        appConfig = AppConfig.getInstance();
        dbHandler = DBHandler.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm, container, false);
        Log.d("Events", "fragment" + view);


        // digital = (DigitalClock) view.findViewById(R.id.alarmclock);
        //linearLayout = (LinearLayout)view.findViewById(R.id.digitime);
        alarmTime = (TextView) view.findViewById(R.id.alarmTime);
        alarmSpinner = (Spinner) view.findViewById(R.id.alarmSpinner);
        checkBox = (CheckBox) view.findViewById(R.id.isRepeat);
        spinnerValues = getResources().getStringArray(R.array.spinnerdata);
        saveButton = (Button) view.findViewById(R.id.saveBtn);
        SpinnerRoller(alarmSpinner, spinnerValues, getContext(), navDrawerItemsBW, spinnerAdapter);

        calNow = Calendar.getInstance();
        calSet = (Calendar) calNow.clone();

        alarmTime.setOnClickListener(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertAlarm();
            }
        });
        alarmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int ii, long l) {
                if (alarmSpinner.getSelectedItemPosition() == 1) {

                } else if (alarmSpinner.getSelectedItemPosition() == 2) {

                } else if (alarmSpinner.getSelectedItemPosition() == 0) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }

    @Override
    public void onClick(View v) {

        if (v == alarmTime) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);




            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            mHour = Utils.get12HourTime(hourOfDay);
                            mMinute = minute;

                            Log.w("#TIME",""+hourOfDay);
                            int displayHour = hourOfDay;
                            if (hourOfDay == 12) {
                                timeSet = "PM";
                            }else if (hourOfDay > 12) {
                                displayHour = hourOfDay - 12;
                                timeSet = "PM";
                            } else {
                                timeSet = "AM";
                            }

                            alarmTime.setText(String.format("%02d", displayHour) + ":" + String.format("%02d", minute) + " " + timeSet);
                            alarmTime.setTextSize(50);

                            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calSet.set(Calendar.MINUTE, minute);
                            calSet.set(Calendar.SECOND, 0);
                            calSet.set(Calendar.MILLISECOND, 0);

                            if (calSet.compareTo(calNow) <= 0) {
                                //Today Set time passed, count to tomorrow
                                calSet.add(Calendar.DATE, 1);
                            }
                            //settime.setText(hourOfDay+ ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    private void insertAlarm() {
        try {
            int reqCode = (int) System.currentTimeMillis();
            AlarmBean alarmBean = new AlarmBean();
            alarmBean.setReqCode(reqCode);
            alarmBean.setCreatedDate(Utils.getCurrentDayWithTime());
            alarmBean.setDesc(alarmTime.getText().toString());
            alarmBean.setSnooze(spinnerValues[alarmSpinner.getSelectedItemPosition()]);
            if (checkBox.isChecked()) alarmBean.setRepeat(1);
            else alarmBean.setRepeat(0);
            alarmBean.setInterval("");
            alarmBean.setEmail(appConfig.loginMail);
            if (dbHandler.insertHandler(AppConfig.ALARMTB, Converter.gson.toJson(alarmBean), getActivity())) {
                Toast.makeText(getActivity(), "Alarm set", Toast.LENGTH_SHORT).show();
                setAlarm(calSet, reqCode,alarmBean.getSnooze());
            } else {
                System.out.println("Alarm Insert Failure");
            }
        } catch (Exception e) {
            System.out.println("Alarm Insert Failure");
            e.printStackTrace();
        }
    }

    public void SpinnerRoller(Spinner spinner, String[] local, Context context,
                              ArrayList<NavDrawerItem> navDrawerItem, SpinnerAdapter spinnerAdapter) {
        int i = 0;
        while (i < local.length) {
            navDrawerItem.add(new NavDrawerItem(local[i]));
            i++;
        }
        spinnerAdapter = new SpinnerAdapter(context, navDrawerItem, R.layout.spinner_list_item);
        spinner.setAdapter(spinnerAdapter);
    }

    private void setAlarm(Calendar calendar, int reqCode,String snooze) {
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        intent.putExtra("ALARM", false);
        intent.putExtra("CODE", reqCode);
        intent.putExtra("SNOOZE", snooze);
        if (!checkBox.isChecked()) intent.putExtra("REPEAT", true);
        else intent.putExtra("REPEAT", false);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getActivity(), reqCode, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
        System.out.println("Remind SIze 3:"+calendar.getTimeInMillis());

        if (!checkBox.isChecked())
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        else
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 5 * 60 * 1000, pendingIntent);
    }
}
