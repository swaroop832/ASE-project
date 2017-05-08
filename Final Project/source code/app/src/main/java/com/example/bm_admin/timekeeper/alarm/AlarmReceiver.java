package com.example.bm_admin.timekeeper.alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

import com.example.bm_admin.timekeeper.bean.AlarmBean;
import com.example.bm_admin.timekeeper.bean.Converter;
import com.example.bm_admin.timekeeper.bean.ReminderBean;
import com.example.bm_admin.timekeeper.bean.SettingsBean;
import com.example.bm_admin.timekeeper.sqlite.DBHandler;
import com.example.bm_admin.timekeeper.utility.AppConfig;
import com.example.bm_admin.timekeeper.utility.Utils;

/**
 * Created by bm-admin on 28/3/17.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    Integer requestCode = 0;
    boolean isReminder = false, isRepeat = false;
    AppConfig appConfig = AppConfig.getInstance();
    public static Ringtone ringtone;
    private SettingsBean settingsBean;

    @Override
    public void onReceive(final Context context, Intent intent) {
        //this will update the UI with message

        try {
            isReminder = intent.getBooleanExtra("ALARM", false);
            isRepeat = intent.getBooleanExtra("REPEAT", false);
            requestCode = intent.getIntExtra("CODE", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //cancel repeat alarm
        if (!isReminder) cancelAlarm(context);

        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
        settingsBean = Utils.getSettings(appConfig, context);

        if (settingsBean != null) {
            if (settingsBean.getSound() != null) {
                if (settingsBean.getSound().equalsIgnoreCase("Y")) {
                  //  System.out.println("STEP 1111");
                    setRingtone(context);
                }
            } else {
               // System.out.println("STEP 222");
                setRingtone(context);
            }
        } else {
           // System.out.println("STEP 33");
            setRingtone(context);
        }

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);

    }

    private void cancelAlarm(Context context) {
        try {
            DBHandler dbHandler = DBHandler.getInstance();
            AlarmBean alarmBean = getAlarm(context);
            int maxCount = alarmBean.getMaxCount();
            if (maxCount < 2) {
                alarmBean.setMaxCount(maxCount + 1);
                if (dbHandler.updateAndDeleteHandler(AppConfig.ALARMTB, Converter.gson.toJson(alarmBean),
                        "reqCode=?", new String[]{Integer.toString(requestCode)}, true, context)) {
                }
            } else {
                Intent intent = new Intent(context, AlarmReceiver.class);
                PendingIntent sender = PendingIntent.getBroadcast(context,
                        requestCode, intent, 0);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                alarmManager.cancel(sender);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AlarmBean getAlarm(Context context) {
        try {
            String dbStr = DBHandler.selectHandler(AppConfig.ALARMTB, null, "reqCode=? and email=?",
                    new String[]{Integer.toString(requestCode), appConfig.loginMail}, null, false, false, false, context);
            return Converter.convertJsonToAlarmBeans(dbStr).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return new AlarmBean();
        }
    }

    private void setRingtone(Context context) {
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();
    }

}