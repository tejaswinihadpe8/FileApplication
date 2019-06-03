package com.example.gdivekar.fileapplication;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FileDao {
    @Query("select * from file_table")
    public List<FileData> getFileData();


    @Insert
    void insertFileData(FileData data);

   /* @Query("SELECT download_file FROM downloadFile_table")
    public List<String> getDownloadedFile();*/

  /*  @Insert
    void insertDownloadFolderData(DownloadFolderData downloadFolderData);
*/
    @Query("SELECT fileType FROM file_table")
    public List<String> getFileType();

    @Query("SELECT filePath FROM file_table where fileType =:fileType")
    String getFilePath(String fileType);
//    @Update
//    void update(Word word);

    @Query("update file_table set filePath = :filePath where fileType =:fileType")
    void updateFilePath(String filePath, String fileType);

    @Query("delete from file_table")
    void deleteAll();

    @Query("DELETE FROM file_table WHERE fileType = :fileType")
    int deleteFileType(String fileType);
}
