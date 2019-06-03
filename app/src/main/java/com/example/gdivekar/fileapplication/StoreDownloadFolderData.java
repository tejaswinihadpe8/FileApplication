package com.example.gdivekar.fileapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class StoreDownloadFolderData {
    String state = Environment.getExternalStorageState();
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<String> downloadFiles;


    public StoreDownloadFolderData(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("MyRef", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void insertDoawnloadFolderData() {
        if (isMediaAvailable()) {
            String path = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DOWNLOADS;
            File file = new File(path);
            downloadFiles = new ArrayList<>();

            File filesInDirectory[] = file.listFiles();
            if (filesInDirectory != null) {
                for (int i = 0; i < filesInDirectory.length; i++) {
                    Log.e("show", "download files==" + filesInDirectory[i].getName());
                    downloadFiles.add(filesInDirectory[i].getName());
                }
                storeInSharedPreferences(downloadFiles);

            }

        } else {
            Toast.makeText(context, "Media not available", Toast.LENGTH_SHORT).show();
        }
    }



    //************check media available method********
    private boolean isMediaAvailable() {

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else {
            return false;
        }

    }

    /*public ArrayList<String> getDownloadFiles() {
        return downloadFiles;
    }*/

    private void storeInSharedPreferences(ArrayList<String> downloadFiles) {
        Set<String> set = new HashSet<String>();
        set.addAll(downloadFiles);
        editor.putStringSet("file_list", set);
        editor.apply();

        Log.e("show", "" + set);
    }



}
