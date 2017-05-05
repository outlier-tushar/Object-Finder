package com.example.root.newapplayout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 22/4/17.
 */

public class SQLBeaconDAO {

    private SQLiteDatabase database;
    private SQLBeaconInit sqlBeaconInit;
    String TABLE_NAME = "device_details";
    public SQLBeaconDAO(Context context)
    {
        sqlBeaconInit = new SQLBeaconInit(context);
        database = sqlBeaconInit.getWritableDatabase();
    }
    public void close(){database.close();}

    public void saveDevice(SQLBeaconBean details)
    {
        ContentValues values = new ContentValues();
        values.put("ATNAME", details.getATName());
        values.put("UUID", details.getUuid());
        values.put("MAC", details.getMac());
        values.put("RSSI", details.getRssi());
        values.put("SNAME", details.getSName());
        database.insert("device_details",null,values);
    }
    public String update(SQLBeaconBean details, String atname)
    {
        /*ContentValues values = new ContentValues();
        values.put(TABLE_NAME,details.getSName());
        database.update(TABLE_NAME,values,"ATNAME = ?", new String[]{details.getATName()});*/
        database.execSQL("UPDATE device_details SET SNAME = '"+details.getSName()+"' WHERE ATNAME = '"+atname+"'");
        return details.getSName();

    }
    public void deleteDevice(String dev_name){database.delete("device_details", "NAME="+dev_name,null);}

    public List getDetails(){
        List deviceDetails = new ArrayList();
        String[] columns = new String[]{"atname","uuid","mac","rssi","sname"};
        Cursor cursor = database.query("device_details",columns,null,null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            SQLBeaconBean setter = new SQLBeaconBean();
            setter.setATName(cursor.getString(0));
            setter.setUuid(cursor.getInt(1));
            setter.setMac(cursor.getString(2));
            setter.setRssi(cursor.getInt(3));
            /*setter.setSName(cursor.getString(4));*/
            deviceDetails.add(setter);
            cursor.moveToNext();
        }
        return deviceDetails;
    }
}
