package com.example.gdivekar.fileapplication;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class NotificationReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.e("show","broadcast receiver");
        //Toast.makeText(context,"recieved",Toast.LENGTH_SHORT).show();
        String sourcePath, destinationPath;
        sourcePath=intent.getStringExtra("sourcePath");
//        Log.e("show","receiver sourcePath=="+sourcePath);
        destinationPath=intent.getStringExtra("destinationPath");
//        Log.e("show","receiver destinationPath=="+destinationPath);
        int notificationId=intent.getIntExtra("notificationId",-1);
//        Log.e("show","receiver notificationId=="+notificationId);
        if(sourcePath.length()!=0 && destinationPath.length()!=0){
            fileMove( context,notificationId,sourcePath, destinationPath);
        }

        //This is used to close the notification tray
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }

    public void fileMove(Context context,int notificationId,String sourcePath,String destinationPath) {
        File from = new File(sourcePath);
        File to = new File(destinationPath);
        from.renameTo(to);
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
        new StoreDownloadFolderData(context).insertDoawnloadFolderData();

    }

}
