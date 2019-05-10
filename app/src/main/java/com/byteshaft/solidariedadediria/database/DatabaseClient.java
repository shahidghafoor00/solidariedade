package com.byteshaft.solidariedadediria.database;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DatabaseClient {
    private Context mContext;
    private static DatabaseClient mInstance;

    //our app database object
    private AppDatabase appDatabase;

    private DatabaseClient(Context context) {
        this.mContext = context;

        //creating the app database with Room database builder
        //EngineDetail is the name of the database
        appDatabase = Room.databaseBuilder(mContext, AppDatabase.class, "maindb").build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
