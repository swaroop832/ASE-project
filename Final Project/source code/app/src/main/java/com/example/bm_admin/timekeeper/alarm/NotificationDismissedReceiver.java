package com.example.bm_admin.timekeeper.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.bm_admin.timekeeper.alarm.AlarmReceiver;
import com.example.bm_admin.timekeeper.alarm.AlarmService;

/**
 * Created by suresh on 31/3/17.
 */

public class NotificationDismissedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getExtras().getInt("notificationId");
      /* Your code to handle the event here */
     //   Toast.makeText(context, "Notification dismiss", Toast.LENGTH_SHORT).show();
        cancelAlarm(notificationId, context);

    }

    private void cancelAlarm(int notificationId, Context context) {

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pintent = PendingIntent.getService(context, notificationId, intent, 0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pintent);

        if (AlarmReceiver.ringtone != null)
            if (AlarmReceiver.ringtone.isPlaying()) AlarmReceiver.ringtone.stop();
    }

}
