package com.example.root.newapplayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SavedDevices extends AppCompatActivity {

    ListView device_list;
    ArrayList savedBeacons = new ArrayList();
    String data1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_devices);

        SQLBeaconInit p = new SQLBeaconInit(this);

        device_list = (ListView)findViewById(R.id.lists);

        final String TABLE_NAME = "device_details";
        String selectQuery = "SELECT SNAME FROM " + TABLE_NAME;
        SQLiteDatabase db  = p.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do
            {
                data1 = cursor.getString(cursor.getColumnIndex("SNAME"));
                savedBeacons.add(data1);
            }
            while (cursor.moveToNext());
        }
        else
            {
                noDevicesFound();
            }
        cursor.close();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,savedBeacons);
        device_list.setAdapter(arrayAdapter);

        device_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String itemClickValue = (String)parent.getItemAtPosition(position);
                Toast.makeText(SavedDevices.this,itemClickValue,Toast.LENGTH_SHORT).show();
                finish();
                Intent dev_details = new Intent(SavedDevices.this, DeviceDetails.class);
                dev_details.putExtra("dev_name",itemClickValue);
                SavedDevices.this.startActivity(dev_details);
            }
        });
    }

    @Override
    protected void onDestroy() {super.onDestroy();}

    public void noDevicesFound()
    {
        new AlertDialog.Builder(SavedDevices.this)
                .setTitle("No Saved Devices Found!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Intent mainActivity = new Intent(SavedDevices.this, MainActivity.class);
                        SavedDevices.this.startActivity(mainActivity);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
