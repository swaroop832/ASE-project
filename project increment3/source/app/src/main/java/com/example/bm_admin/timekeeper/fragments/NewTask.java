package com.example.bm_admin.timekeeper.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bm_admin.timekeeper.MapsActivity;
import com.example.bm_admin.timekeeper.R;
import com.example.bm_admin.timekeeper.TimeZoneActivity;
import com.example.bm_admin.timekeeper.alarm.AlarmReceiver;
import com.example.bm_admin.timekeeper.bean.Converter;
import com.example.bm_admin.timekeeper.bean.ReminderBean;
import com.example.bm_admin.timekeeper.bean.TimeZoneBean;
import com.example.bm_admin.timekeeper.sqlite.DBHandler;
import com.example.bm_admin.timekeeper.utility.AppConfig;
import com.example.bm_admin.timekeeper.utility.PreferenceManager;
import com.example.bm_admin.timekeeper.utility.Utils;
import com.example.bm_admin.timekeeper.utility.Validation;

import java.util.Calendar;

/**
 * Created by bm-admin on 26/3/17.
 */
public class NewTask extends AppCompatActivity implements View.OnClickListener {

    private EditText taskDesc, taskDateTime, taskInterval, taskLocation;
    private Button addButton;
    private DBHandler dbHandler;
    private AppConfig appConfig;
    Calendar calNow;
    Calendar calSet;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Activity activity;
    int selectedYear = 0;
    int selectedMonth = 0;
    int selectedDay = 0, hour = 0, min = 0;
    private String timeSet = "AM";

    public NewTask() {
        dbHandler = DBHandler.getInstance();
        appConfig = AppConfig.getInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtask);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activity = this;
        taskDesc = (EditText) findViewById(R.id.task_desc);
        taskDateTime = (EditText) findViewById(R.id.task_dateTime);
        taskInterval = (EditText) findViewById(R.id.task_interval);
        taskLocation = (EditText) findViewById(R.id.task_location);
        addButton = (Button) findViewById(R.id.addBtn);

        if(!appConfig.locationName.isEmpty()) taskLocation.setText(appConfig.locationName);

        taskDesc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                taskDesc.setFocusable(true);
                taskDesc.setFocusableInTouchMode(true);
                return false;
            }
        });
        taskInterval.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                taskInterval.setFocusable(true);
                taskInterval.setFocusableInTouchMode(true);
                return false;
            }
        });

        taskLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskDesc.setFocusable(false);
                taskInterval.setFocusable(false);
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivityForResult(intent, 155);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(activity);
                if (Validation.isNonEmpty(taskDesc) && Validation.isNonEmpty(taskDateTime)) {
                    insertReminder();
                }
            }
        });

        taskDateTime.setOnClickListener(this);

        calNow = Calendar.getInstance();
        calSet = (Calendar) calNow.clone();


    }

    public int Timepicker() {

        // Get Current Date


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        //    taskDateTime.setText(hourOfDay + ":" + minute);
                        hour = Utils.get12HourTime(hourOfDay);
                        min = minute;

                        if (hourOfDay > 12) {
                            timeSet = "PM";
                        } else if (hourOfDay == 0) {
                            timeSet = "AM";
                        } else if (hour == 12){
                            timeSet = "PM";
                        }else{
                            timeSet = "AM";
                        }


                        calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calSet.set(Calendar.MINUTE, minute);
                        calSet.set(Calendar.SECOND, 0);
                        calSet.set(Calendar.MILLISECOND, 0);

                        if (calSet.compareTo(calNow) <= 0) {
                            //Today Set time passed, count to tomorrow
                            calSet.add(Calendar.DATE, 1);
                        }
                        //settime.setText(hourOfDay+ ":" + minute);
                        taskDateTime.setText(String.format("%02d",selectedMonth) + "/" + String.format("%02d",selectedDay) +
                                "/" + selectedYear + "    " + String.format("%02d",hour) + ":" +  String.format("%02d",min)+" "+timeSet);
                    }
                }, mHour, mMinute, false);

        timePickerDialog.show();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {


                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        //S(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        selectedYear = year;
                        selectedDay = dayOfMonth;
                        selectedMonth = monthOfYear + 1;

                        calSet.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calSet.set(Calendar.MONTH, monthOfYear);
                        calSet.set(Calendar.YEAR, year);

                       /* if (calSet.compareTo(calNow) <= 0) {
                            //Today Set time passed, count to tomorrow
                            calSet.add(Calendar.DATE, 1);
                        }*/

                    }
                }, mYear, mMonth, mDay);


        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

return 0;

    }

    @Override
    public void onClick(View v) {
        if (v == taskDateTime) {
            taskDesc.setFocusable(false);
            taskInterval.setFocusable(false);
            Timepicker();


        }
    }

    private void insertReminder() {
        try {
            int reqCode = (int) System.currentTimeMillis();
            ReminderBean reminderBean = new ReminderBean();
            reminderBean.setDesc(taskDesc.getText().toString());
            System.out.println("Time 0:"+calSet);
            reminderBean.setTime(Utils.getDate(calSet));
            reminderBean.setReqCode(reqCode);
            reminderBean.setInterval(taskInterval.getText().toString());
            reminderBean.setLocation(taskLocation.getText().toString());
            reminderBean.setCreatedDate(Utils.getCurrentDayWithTime());
            reminderBean.setStatus("N");

            reminderBean.setEmail(appConfig.loginMail);
            if (dbHandler.insertHandler(AppConfig.REMINDERTB, Converter.gson.toJson(reminderBean), getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                setAlarm(calSet,reqCode);
                clearFields();
                finish();
            } else {
                System.out.println("Task Insert failure");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAlarm(Calendar calendar, int reqCode) {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("ALARM", true);
        intent.putExtra("CODE", reqCode);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), reqCode, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }
private void clearFields(){
    taskDateTime.setText("");
    taskLocation.setText("");
    taskDesc.setText("");
    taskInterval.setText("");
}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("TST RESULT :" + resultCode);
        if (resultCode == 155) {
            String locationStr = data.getStringExtra("LOCATION");
            Log.d("Location", "" + locationStr);
            String latlng = data.getStringExtra("LATLNG");
            Log.d("latlng", "" + latlng);

            Address address = Converter.gson.fromJson(data.getStringExtra("LOCALE"), Address.class);
            System.out.println("LOCALE 1:" + address.getAdminArea());
            System.out.println("LOCALE 2:" + address.getLocality());
            System.out.println("LOCALE 3:" + address.getFeatureName());
            System.out.println("LOCALE 4:" + address.getSubAdminArea());
            System.out.println("LOCALE 5:" + address.getSubLocality());

            if (address.getSubAdminArea() != null) {
                appConfig.locationName = address.getSubAdminArea();
                if (!appConfig.locationName.isEmpty()) taskLocation.setText(appConfig.locationName);
            }
            if (address.getSubLocality() != null) {
                appConfig.locationName = address.getSubLocality();
                if (!appConfig.locationName.isEmpty()) taskLocation.setText(appConfig.locationName);
            }
            if (address.getLocality() != null) {
                appConfig.locationName = address.getLocality();
                if (!appConfig.locationName.isEmpty()) taskLocation.setText(appConfig.locationName);
            }

            TimeZoneBean timeZoneBean = new TimeZoneBean();
            timeZoneBean.setTitle(appConfig.locationName);
            timeZoneBean.setSubTitle(latlng);
            PreferenceManager.storeIntoSharedPrefrence(getApplicationContext(), appConfig.loginMail, Converter.gson.toJson(timeZoneBean));

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
