package com.example.gdivekar.fileapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import lib.folderpicker.FolderPicker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner sp_fileType;
    AutoCompleteTextView et_fileType;
    Button bt_filePath, bt_filetype;
    FileRoomDatabase fileRoomDatabase;
    FileData fileData;
    List<String> fileList;
    List<FileData> fileDataList;
    List<String> fileTypeList;
    List<String> filePathList;
    RecyclerView recyclerview;
    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.VIBRATE,
            Manifest.permission.RECORD_AUDIO,
    };
    private String m_chosenDir = "";
    private boolean m_newFolderEnabled = true;
    int FOLDERPICKER_CODE = 123;
    String state = Environment.getExternalStorageState();
    String destinationPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        checkPermissions();
        initViews();


        setFileType();
        displayFileData();

        sp_fileType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_fileType.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


                    /*if (!filePathList.get(0).isEmpty()){
                        fileMove("/storage/emulated/0/" + filesInDirectory[i].getName(), filePathList.get(0));

                }*/

                    int n=9;
        int[]ar=new int[]{10,20,20,10,10,30,50,10,20};
        int count=sockMerchant(n,ar);
        Log.e("show","count=="+count);

    }
    static int sockMerchant(int n, int[] ar) {
        HashSet<Integer> hashMap = new HashSet<>();
        for(int i=0;i<n;i++){
            for(int j=i+1;j<n;j++){
                if(ar[i]==ar[j]){
                    hashMap.add(ar[i]);
                }
            }
        }

        return hashMap.size();
    }
    public void getDestinationpath(String extension){

    }

    public void setFileType() {
        fileList = fileRoomDatabase.fileDao().getFileType();
//        Log.e("show", "fileList size==" + fileList.size());

        if (fileList.size() != 0) {
            ArrayAdapter<String> fileAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, fileList);
//            fileAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            sp_fileType.setAdapter(fileAdapter);
        }
    }

    public void initViews() {
        sp_fileType = findViewById(R.id.sp_fileType);

        bt_filePath = findViewById(R.id.bt_filePath);
        et_fileType = findViewById(R.id.et_fileType);
        bt_filetype = findViewById(R.id.bt_filetype);
        bt_filetype.setOnClickListener(this);
        bt_filePath.setOnClickListener(this);
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        fileRoomDatabase = FileRoomDatabase.getDatabase(MainActivity.this);
        fileList = new ArrayList<>();
        fileDataList = new ArrayList<>();
        fileTypeList = new ArrayList<>();


    }

    @Override
    public void onClick(View v) {
        if (v == bt_filetype) {
            addFileData();
        } else if (v == bt_filePath) {
            Intent intent = new Intent(this, FolderPicker.class);
            startActivityForResult(intent, FOLDERPICKER_CODE);

//            notification();


        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FOLDERPICKER_CODE && resultCode == Activity.RESULT_OK) {

            String folderLocation = intent.getExtras().getString("data");
            Log.e("show", "folderLocation==" + folderLocation);
            fileRoomDatabase.fileDao().updateFilePath(folderLocation, sp_fileType.getSelectedItem().toString());
            displayFileData();

        }
    }

    public void selectFilepath() {


        // Create DirectoryChooserDialog and register a callback
        DirectoryChooserDialog directoryChooserDialog =
                new DirectoryChooserDialog(MainActivity.this,
                        new DirectoryChooserDialog.ChosenDirectoryListener() {
                            @Override
                            public void onChosenDir(String chosenDir) {
                                m_chosenDir = chosenDir;

                                Toast.makeText(
                                        MainActivity.this, "Chosen directory: " +
                                                chosenDir, Toast.LENGTH_LONG).show();
                            }
                        });
        // Toggle new folder button enabling
        directoryChooserDialog.setNewFolderEnabled(m_newFolderEnabled);
        // Load directory chooser dialog for initial 'm_chosenDir' directory.
        // The registered callback will be called upon final directory selection.
        directoryChooserDialog.chooseDirectory(m_chosenDir);
        m_newFolderEnabled = !m_newFolderEnabled;
    }

    public void addFileData() {
        fileList = fileRoomDatabase.fileDao().getFileType();
        Log.e("show", "fileList size==" + fileList.size());
        if (et_fileType.getText().toString().length() != 0) {
            if (fileList.size() != 0) {
                Log.e("show", "if returns==" + fileList.contains(et_fileType.getText().toString().toLowerCase()));
                if (!fileList.contains(et_fileType.getText().toString().toLowerCase())) {
                    fileData = new FileData(et_fileType.getText().toString().toLowerCase(), "");
                    fileRoomDatabase.fileDao().insertFileData(fileData);
                    addFileTypeList(et_fileType.getText().toString().toLowerCase());
                }

            } else {

                Log.e("show", "else file unique==" + et_fileType.getText().toString());
                fileData = new FileData(et_fileType.getText().toString().toLowerCase(), "");
                fileRoomDatabase.fileDao().insertFileData(fileData);
                addFileTypeList(et_fileType.getText().toString().toLowerCase());
            }

        }


        setFileType();
        displayFileData();
        et_fileType.setText("");

    }

    public void addFileTypeList(String fileType) {
        if (fileTypeList.size() != 0) {
            for (int i = 0; i < fileTypeList.size(); i++) {
                if (!fileType.contains(fileTypeList.get(i))) {
                    fileTypeList.add(fileType);

                }
            }
        } else {
            fileTypeList.add(fileType);
        }
        if (fileTypeList.size() != 0) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, fileTypeList);
            et_fileType.setAdapter(arrayAdapter);
        }
        Log.e("show", "fileTypeList size==" + fileTypeList.size());
    }

    public void displayFileData() {

        fileDataList = fileRoomDatabase.fileDao().getFileData();
        if (fileDataList.size() != 0) {
            FileListAdapter fileListAdapter = new FileListAdapter(this, fileDataList);
            recyclerview.setAdapter(fileListAdapter);
        }
    }



    public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileViewHolder> {

        class FileViewHolder extends RecyclerView.ViewHolder {
            private final TextView tv_fileType, tv_filePath, tv_fileMove, tv_fileDelete;


            private FileViewHolder(final View itemView) {
                super(itemView);
                tv_fileType = itemView.findViewById(R.id.tv_fileType);
                tv_filePath = itemView.findViewById(R.id.tv_filePath);
                tv_fileMove = itemView.findViewById(R.id.tv_fileMove);
                tv_fileDelete = itemView.findViewById(R.id.tv_fileDelete);

            }
        }

        private final LayoutInflater mInflater;
        private Context context;
        private List<FileData> fileDataList;

        FileListAdapter(Context context, List<FileData> fileDataList) {
            this.context = context;
            this.fileDataList = fileDataList;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = mInflater.inflate(R.layout.file_row, parent, false);
            return new FileViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FileViewHolder holder, final int position) {
            final FileData fileData = fileDataList.get(position);
            holder.tv_fileType.setText(fileData.getFileType());
            holder.tv_filePath.setText(fileData.getFilePath());
            holder.tv_fileMove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveButton(position);
                }
            });
            holder.tv_fileDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FileRoomDatabase fileRoomDatabase = FileRoomDatabase.getDatabase(context);
                    fileRoomDatabase.fileDao().deleteFileType(fileData.getFileType());
//                context.startActivity(new Intent(context,ListActivity.class));

                    if (fileDataList.size() != 0) {
                        fileDataList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, fileDataList.size());
                        setFileType();
                        new StoreDownloadFolderData(MainActivity.this).insertDoawnloadFolderData();

                    }
                }
            });


        }

        void setFileDataList(List<FileData> fileDataList) {
            fileDataList = fileDataList;
            notifyDataSetChanged();
        }


        @Override
        public int getItemCount() {
            if (fileDataList != null)
                return fileDataList.size();
            else return 0;
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
            return;
        }
    }

    public void moveButton(int position) {
        if (isMediaAvailable()) {
            String path = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DOWNLOADS;
            File file = new File(path);
            Log.e("show", "download path==" +path);
            File filesInDirectory[] = file.listFiles();
            //iuyuuyuyu
            if (filesInDirectory != null) {
                for (int i = 0; i < filesInDirectory.length; i++)

                {

//                    Log.e("show", "download files==" + filesInDirectory[i].getName());
                    String filename = filesInDirectory[i].getName();
                    String filenameArray[] = filename.split("\\.");
                    String extension = filenameArray[filenameArray.length - 1];
//                    Log.e("show", "download extension==" + extension);
                    if(extension.equalsIgnoreCase(fileDataList.get(position).getFileType())) {
                        destinationPath = fileRoomDatabase.fileDao().getFilePath(extension.toLowerCase());
                        if (destinationPath != null) {
                            Log.e("show", "sourcePath==" + "/storage/emulated/0/" + filesInDirectory[i].getName());
                            Log.e("show", "********destinationPath==" + destinationPath);
                            fileMove("/storage/emulated/0/Download/" + filesInDirectory[i].getName(), destinationPath + "/" + filesInDirectory[i].getName());
                        }
                    }

                }
            } else {
                Toast.makeText(this, "Media not available", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void fileMove(String sourcePath,String destinationPath) {
        File from = new File(sourcePath);
        File to = new File(destinationPath);
        from.renameTo(to);


    }

    //************check media available method********
    private boolean isMediaAvailable() {

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else {
            return false;
        }
    }

}
