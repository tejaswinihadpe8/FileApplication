package com.example.gdivekar.fileapplication;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Entity(tableName = "file_table")
public class FileData {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;


    @NonNull
    @ColumnInfo(name = "fileType")
    private String fileType;

    @Nullable
    @ColumnInfo(name = "filePath")
    private String filePath;

    public FileData(@NonNull String fileType, @NonNull String filePath) {

        this.fileType = fileType;
        this.filePath = filePath;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    @NonNull
    public String getFileType() {
        return fileType;
    }

    public void setFileType(@NonNull String fileType) {
        this.fileType = fileType;
    }

    @Nullable
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(@Nullable String filePath) {
        this.filePath = filePath;
    }


}
