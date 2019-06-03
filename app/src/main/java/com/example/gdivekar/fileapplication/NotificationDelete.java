package com.example.gdivekar.fileapplication;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;

public class NotificationDelete extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId=intent.getIntExtra("notificationId",-1);
        Log.e("show","Delete receiver notificationId=="+notificationId);
        if(notificationId!=-1){
            NotificationRemove( context,notificationId);
        }

        //This is used to close the notification tray
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }

    public void NotificationRemove(Context context,int notificationId) {

        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
        new StoreDownloadFolderData(context).insertDoawnloadFolderData();

    }

}
