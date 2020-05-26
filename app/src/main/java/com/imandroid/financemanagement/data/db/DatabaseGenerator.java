package com.imandroid.financemanagement.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;



@Database(entities = {ExpenditureEntity.class}, version = DatabaseGenerator.DATABASE_VERSION, exportSchema = false)
public abstract class DatabaseGenerator extends RoomDatabase {
    static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "finance_db";

    private  static volatile DatabaseGenerator INSTANCE = null;
    private static final Object lock = new Object();

    public abstract ExpenditureDao expenditureDao();

    public static DatabaseGenerator getInstance(Context context) {

        DatabaseGenerator instance = INSTANCE;

        synchronized(lock) {
            if (INSTANCE == null){
                instance = Room.databaseBuilder(context,DatabaseGenerator.class,DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();

                INSTANCE = instance;
            }


        }
        return instance;
    }




}
