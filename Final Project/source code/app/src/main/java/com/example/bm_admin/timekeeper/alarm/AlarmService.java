package com.example.bm_admin.timekeeper.alarm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.bm_admin.timekeeper.DialogActivity;
import com.example.bm_admin.timekeeper.R;
import com.example.bm_admin.timekeeper.bean.AlarmBean;
import com.example.bm_admin.timekeeper.bean.Converter;
import com.example.bm_admin.timekeeper.bean.ReminderBean;
import com.example.bm_admin.timekeeper.bean.SettingsBean;
import com.example.bm_admin.timekeeper.fragments.Alarm;
import com.example.bm_admin.timekeeper.sqlite.DBHandler;
import com.example.bm_admin.timekeeper.utility.AppConfig;
import com.example.bm_admin.timekeeper.utility.PreferenceManager;
import com.example.bm_admin.timekeeper.utility.Utils;


import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * Created by bm-admin on 28/3/17.
 */
public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;
    private Integer requestCode = 0;
    private DBHandler dbHandler = DBHandler.getInstance();
    private AppConfig appConfig = AppConfig.getInstance();
    private boolean isReminder = false;
    private String snooze;
    NotificationCompat.Builder alamNotificationBuilder;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        requestCode = intent.getIntExtra("CODE", 0);
        isReminder = intent.getBooleanExtra("ALARM", false);
        snooze = intent.getStringExtra("SNOOZE");
        appConfig.loginMail = PreferenceManager.retrieveFromSharedPrefrence(getApplicationContext(), appConfig.MAIL);

        if (isReminder) {
            ReminderBean reminderBean = getReminder();
            if (isNotify())
                sendNotification(reminderBean.getDesc() + ", " + reminderBean.getTime() + ", " + reminderBean.getLocation());
            updateReminder();
            openLayout(getApplicationContext(), reminderBean.getDesc() + " , " + reminderBean.getLocation());

        } else {
            if (isNotify()) sendNotification("Right/Left swipe to cancel alarm");
            openLayout(getApplicationContext(), "");
        }

    }

    private void sendNotification(String msg) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, AlarmActivity.class), 0);

        Log.d("Vibration", "" + isViberate());
        if (isViberate()) {

            alamNotificationBuilder = new NotificationCompat.Builder(
                    this).setContentTitle("Time Keeper").setSmallIcon(R.mipmap.ic_launcher)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                    .setDeleteIntent(createOnDismissedIntent(getApplicationContext(), requestCode))
                    .setVibrate(new long[]{1000, 1000})
                    .setContentText(msg);
        } else {
            alamNotificationBuilder = new NotificationCompat.Builder(
                    this).setContentTitle("Time Keeper").setSmallIcon(R.mipmap.ic_launcher)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                    .setDeleteIntent(createOnDismissedIntent(getApplicationContext(), requestCode))
                    .setContentText(msg);
        }


        alamNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");
    }

    private PendingIntent createOnDismissedIntent(Context context, int notificationId) {
        Intent intent = new Intent(context, NotificationDismissedReceiver.class);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("ISALARM", false);
        intent.putExtra("ALARM", false);
        intent.putExtra("SNOOZE", snooze);
        intent.putExtra("CODE", notificationId);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context.getApplicationContext(),
                        notificationId, intent, 0);
        return pendingIntent;
    }

    private ReminderBean getReminder() {
        try {
            String dbStr = DBHandler.selectHandler(AppConfig.REMINDERTB, null, "reqCode=? and email=?",
                    new String[]{Integer.toString(requestCode), appConfig.loginMail}, null, false, false, false, getApplicationContext());
            System.out.println("Step usrrst:" + dbStr);
            return Converter.convertJsonToReminderBeans(dbStr).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return new ReminderBean();
        }
    }


    private AlarmBean getAlarm() {
        try {
            String dbStr = DBHandler.selectHandler(AppConfig.ALARMTB, null, "reqCode=? and email=?",
                    new String[]{Integer.toString(requestCode), appConfig.loginMail}, null, false, false, false, getApplicationContext());
            System.out.println("Step usrrst:" + dbStr);
            return Converter.convertJsonToAlarmBeans(dbStr).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return new AlarmBean();
        }
    }

    private void updateReminder() {
        ReminderBean reminderBean = new ReminderBean();
        reminderBean.setStatus("Y");
        dbHandler.updateAndDeleteHandler(AppConfig.REMINDERTB, Converter.gson.toJson(reminderBean),
                "reqCode=?", new String[]{Integer.toString(requestCode)}, true, getApplicationContext());
    }

    private void openLayout(Context context, String msg) {
        try {
            Intent intent = new Intent(context, DialogActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("ACTION", isReminder);
            intent.putExtra("REMIND", msg);
            intent.putExtra("CODE", requestCode);
            intent.putExtra("SNOOZE", snooze);
            if (!isReminder) {
                AlarmBean alarmBean = getAlarm();
                if (alarmBean != null)
                    if (alarmBean.getSnooze() != null)
                        if (alarmBean.getSnooze().equalsIgnoreCase("Puzzle"))
                            intent.putExtra("ALARM", true);
                        else intent.putExtra("ALARM", false);
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isViberate() {
        SettingsBean settingsBean = Utils.getSettings(appConfig, getApplicationContext());
        if (settingsBean != null) {
            if (settingsBean.getVibration() != null) {
                if (settingsBean.getVibration().equalsIgnoreCase("Y"))
                    return true;
                else return false;
            } else {
                return true;
            }
        } else
            return true;
    }

    private boolean isNotify() {
        SettingsBean settingsBean = Utils.getSettings(appConfig, getApplicationContext());
        if (settingsBean != null) {
            if (settingsBean.getNotification() != null) {
                if (settingsBean.getNotification().equalsIgnoreCase("Y"))
                    return true;
                else return false;
            } else {
                return true;
            }
        } else
            return true;
    }

}

