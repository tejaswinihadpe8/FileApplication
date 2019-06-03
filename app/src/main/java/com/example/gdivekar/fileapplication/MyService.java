package com.example.gdivekar.fileapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MyService extends Service {


    String state = Environment.getExternalStorageState();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String[] downloadFilesArray, downloadFilesUpdateArray;

    ArrayList<String> downloadFiles;
    ArrayList<String> downloadFilesUpdate;
    FileRoomDatabase fileRoomDatabase;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    @Override
    public void onCreate() {

        sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);

        Toast.makeText(this, " MyService Created ", Toast.LENGTH_LONG).show();
        Log.e("show", " MyService Created");
//        firstSave();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        Toast.makeText(this, " MyService Started", Toast.LENGTH_LONG).show();
        Log.e("show", " MyService Started");

        secondSave();


    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Servics Stopped", Toast.LENGTH_SHORT).show();
        Log.e("show", " MyService Stopped");
        super.onDestroy();

    }


    public void secondSave() {
//        sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        downloadFiles = new ArrayList<>();
        downloadFiles = fetchFromSharedPreferences();

        if (downloadFiles.size() != 0) {
            if (isMediaAvailable()) {
                String path = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DOWNLOADS;
                File file = new File(path);
                File filesInDirectory[] = file.listFiles();
                downloadFilesUpdate = new ArrayList<>();

                if (filesInDirectory != null) {
                    for (int i = 0; i < filesInDirectory.length; i++)

                    {
//                        Log.e("show", "download files==" + filesInDirectory[i].getName());
                        downloadFilesUpdate.add(filesInDirectory[i].getName());

                    }

//                    Log.e("show", "downloadFiles size==" + downloadFilesUpdate.size());
                    compareSavedFile(downloadFiles, downloadFilesUpdate);

//                    editor.clear();
//                    editor.commit();
                }

            }

        } else {
            new StoreDownloadFolderData(this).insertDoawnloadFolderData();
        }


    }


    public void compareSavedFile(ArrayList<String> downloadFiles, ArrayList<String> downloadFilesUpdate) {
        //****compare arrays strnew and str and seperate string elemrnt*****


        downloadFilesArray = downloadFiles.toArray(new String[0]);
        downloadFilesUpdateArray = downloadFilesUpdate.toArray(new String[0]);

        ArrayList<String> ar = new ArrayList<String>();
        for (int i = 0; i < downloadFilesUpdateArray.length; i++) {
            if (!Arrays.asList(downloadFilesArray).contains(downloadFilesUpdateArray[i])) {
                ar.add(downloadFilesUpdateArray[i]);
                for (int j = 0; j < ar.size(); j++) {
//                    Log.e("show", "myservice odd file==" + ar.get(j));
                    String filename = ar.get(j);
                    String filenameArray[] = filename.split("\\.");
                    String extension = filenameArray[filenameArray.length - 1];
//                    Log.e("show", "myservice extension==" + extension);
                    fileRoomDatabase = FileRoomDatabase.getDatabase(this);

                    String filePath = fileRoomDatabase.fileDao().getFilePath(extension.toLowerCase());

                    createNotif(ar.get(j), this, "/storage/emulated/0/Download/" + ar.get(j), filePath + "/" + ar.get(j), j);
                    Toast.makeText(this, ar.get(j), Toast.LENGTH_SHORT).show();

                }
            }

        }
        //*************************************

    }


    //************check media available method********
    private boolean isMediaAvailable() {

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else {
            return false;
        }
    }


    public void createNotif(String file, Context context, String sourcePath, String destinationPath, int j) {

        new StoreDownloadFolderData(context).insertDoawnloadFolderData();
        //This is the intent of PendingIntent
        Intent intentAction = new Intent(context, NotificationReceiver.class);

        //This is optional if you have more than one buttons and want to differentiate between two
        intentAction.putExtra("sourcePath", sourcePath);
        intentAction.putExtra("destinationPath", destinationPath);
        intentAction.putExtra("notificationId", j);


        Intent deleteAction=new Intent(context,NotificationDelete.class);
        deleteAction.putExtra("notificationId", j);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intentAction, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent deleteIntent = PendingIntent.getActivity(this, 0, deleteAction, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Notification Ticker")
                .setContentTitle("Do you want to move ")
                .setContentText(file + " to your specified path")
                .addAction(R.drawable.move_file, "Move", pendingIntent)
                .addAction(R.drawable.delete_file, "Cancel", deleteIntent)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(j, builder.build());

    }

    private ArrayList<String> fetchFromSharedPreferences() {

        downloadFiles = new ArrayList<>();
        Set<String> set = sharedPreferences.getStringSet("file_list", null);
        if (set != null) {
//            Log.e("show", "set==" + set.size());
            if (set.size() != 0) {

                downloadFiles.addAll(set);
            }
        }

        return downloadFiles;
    }

}
    // Complete the sockMerchant function below.
