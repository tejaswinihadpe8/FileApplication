package com.example.gdivekar.fileapplication;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {FileData.class}, version = 3)
public abstract class FileRoomDatabase extends RoomDatabase {

    public abstract FileDao fileDao();

    private static FileRoomDatabase INSTANCE;

    static FileRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FileRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FileRoomDatabase.class, "file_database").allowMainThreadQueries()
//                            .fallbackToDestructiveMigration()
//                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
