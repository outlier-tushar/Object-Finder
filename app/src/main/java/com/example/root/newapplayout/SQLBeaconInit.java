package com.example.root.newapplayout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 22/4/17.
 */

public class SQLBeaconInit extends SQLiteOpenHelper {

    public SQLBeaconInit(Context context){
        super(context,"device_db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE device_details(ATNAME VARCHAR(20) PRIMARY KEY NOT NULL, UUID VARCHAR(30), MAC VARCHAR(30), RSSI INT, SNAME VARCHAR(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS device_details");
        onCreate(db);
    }
}
