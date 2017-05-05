package com.example.root.newapplayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by root on 23/4/17.
 */

public class DeviceDetails extends AppCompatActivity {

        ArrayList ids = new ArrayList();
        ArrayList details = new ArrayList();
        AlertDialog.Builder alert;
        String atname, sname, uuid, mac,rssi, backData;
        ListView lv;
        SQLBeaconDAO dao;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.device_details_show);

            alert = new AlertDialog.Builder(this);

            Intent intent = getIntent();
            String rec_name = intent.getStringExtra("dev_name");

            dao = new SQLBeaconDAO(this);

            lv = (ListView)findViewById(R.id.device_details1);

            ids.add("Name");
            ids.add("UUID");
            ids.add("MAC");
            ids.add("RSSI");

            if(!rec_name.isEmpty())
                populateDetails(rec_name);

            lv.setAdapter(new DeviceDetailsPopulator(DeviceDetails.this, ids, details, sname));

            Button edit_button = (Button)findViewById(R.id.edit_button);

            edit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deviceNameEditor();
                }
            });
        }

         @Override
        public void onBackPressed()
         {
             finish();
             Intent returnIntent = new Intent(DeviceDetails.this, SavedDevices.class);
             DeviceDetails.this.startActivity(returnIntent);
         }

    public void deviceNameEditor()
        {
            final EditText edittext = new EditText(DeviceDetails.this);
            alert.setTitle("Change Device Name?");
            alert.setMessage("Enter Device Name :");


            alert.setView(edittext);

            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String editedName = edittext.getText().toString();
                    SQLBeaconBean sbb = new SQLBeaconBean();
                    sbb.setSName(editedName);
                    //dao.update(sbb,atname);
                    String rec_sname = dao.update(sbb, atname);
                    backData = rec_sname;
                    populateDetails(rec_sname);
                    lv.setAdapter(new DeviceDetailsPopulator(DeviceDetails.this, ids, details, rec_sname));
                    /*if(!rec_sname.isEmpty())
                    {
                        lv.setAdapter(new DeviceDetailsPopulator(DeviceDetails.this, ids, details, sname));
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }*/
                    //populateDetails(dao.update(sbb,atname));
                    Toast.makeText(DeviceDetails.this,editedName,Toast.LENGTH_SHORT).show();
                }
            });

            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                    dialog.dismiss();
                }
            });
            alert.show();
        }

        public void populateDetails(String rec_name)
        {
            details.clear();
            SQLBeaconInit p = new SQLBeaconInit(this);
            final String TABLE_NAME = "device_details";
            String selectQuery = "SELECT * FROM " + TABLE_NAME +" WHERE SNAME = '"+rec_name+"'";
            SQLiteDatabase db  = p.getReadableDatabase();
            Cursor cursor      = db.rawQuery(selectQuery, null);
            //int i=0;
            if (cursor.moveToFirst()) {
                Toast.makeText(this,"Chaching",Toast.LENGTH_LONG).show();
                do {
                    sname = cursor.getString(cursor.getColumnIndex("SNAME"));
                    details.add(sname);
                    uuid = cursor.getString(cursor.getColumnIndex("UUID"));
                    details.add(uuid);
                    mac = cursor.getString(cursor.getColumnIndex("MAC"));
                    details.add(mac);
                    rssi = cursor.getString(cursor.getColumnIndex("RSSI"));
                    details.add(rssi);
                    atname = cursor.getString(cursor.getColumnIndex("ATNAME"));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

    }


