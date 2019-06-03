package com.example.gdivekar.fileapplication;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Toast;


public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        intent = new Intent(context, MyService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context,
                12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
                1*60*60,pendingIntent);



    }

}