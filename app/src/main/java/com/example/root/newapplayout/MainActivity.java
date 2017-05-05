package com.example.root.newapplayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BeaconConsumer {

    private ArrayList scannedBeacons = new ArrayList();
    private ListView scannedBeaconsList;
    private String itemClickValue;
    private boolean search=false,existsDb = true;
    private ProgressDialog dialog;
    private Timer t = new Timer();
    BeaconManager beaconManager;
    private SQLBeaconDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ServiceChecker sc = new ServiceChecker(MainActivity.this);
        sc.checkServices();

        scannedBeaconsList = (ListView)findViewById(R.id.beaconsList);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        Toast.makeText(this,"Preferences Loaded",Toast.LENGTH_SHORT).show();

        dialog = new ProgressDialog(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Search Initiated", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                dialog.setIcon(R.drawable.ic_search_black_18px);
                dialog.setTitle("Searching");
                dialog.setMessage("Please wait...");
                dialog.setButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this,"Search Cancelled",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
                search=true;
                beaconManager.bind(MainActivity.this);
                scannedBeacons.clear();
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1);
                arrayAdapter.clear();
                scannedBeaconsList.setAdapter(arrayAdapter);
            }
        });

 /////////////////////////////////////////DRAWER///////////////////////////////////////////
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
 /////////////////////////////////////////DRAWER///////////////////////////////////////////
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dao = new SQLBeaconDAO(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_devices) {
            // Open Saved Devices Activity
            Intent saved_devices = new Intent(MainActivity.this, SavedDevices.class);
            saved_devices.putExtra("beaconName",itemClickValue);
            MainActivity.this.startActivity(saved_devices);

        } else if (id == R.id.nav_project_info) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBeaconServiceConnect() {
        final BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beaconCollection, Region region) {
                if(beaconCollection.size()>0 && search==true)
                {
                    Log.i("MainActivity","Search = True");

                    if(dialog.isShowing())
                        dialog.dismiss();

                    for (final Beacon details : beaconCollection)
                    {
                        final String rec_atname = details.getBluetoothName();
                        final int uuid = details.getServiceUuid();
                        final String mac = details.getBluetoothAddress();
                        final int rssi = details.getRssi();
                        final Double distance = details.getDistance();


                        if(scannedBeacons.isEmpty())
                        {
                            checkAdder(rec_atname);
                        }
                        else
                        {
                            checkAdder(rec_atname);
                        }



////////////////////////////////////////////////////////////MainActivity.this.runOnUiThread///////////////////////////////////////////////////////////////
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,scannedBeacons);
                                scannedBeaconsList.setAdapter(arrayAdapter);
                                scannedBeaconsList.setOnItemClickListener(new AdapterView.OnItemClickListener()
                                {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
                                    {
                                        itemClickValue = (String)adapter.getItemAtPosition(position);
                                        if(!existsInDb(itemClickValue))
                                        {
                                            beaconConfirmation(itemClickValue, uuid, mac, rssi);
                                        }
                                        else
                                            {
                                                Toast.makeText(MainActivity.this,itemClickValue,Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(MainActivity.this, ObjectRanging.class);
                                                Bundle extras = new Bundle();
                                                extras.putString("ATNAME",rec_atname);
                                                extras.putString("KEY",itemClickValue);
                                                intent.putExtras(extras);
                                                startActivity(intent);
                                                beaconManager.unbind(MainActivity.this);
                                            }
                                    }
                                });

                            }
                        });
////////////////////////////////////////////////////////////MainActivity.this.runOnUiThread///////////////////////////////////////////////////////////////
                        Log.i("MainActivity", rec_atname+" Distance: "+String.format(Locale.US,"%.2f",distance)+" Power: "+details.getTxPower());
                    }
                }
                else if(beaconCollection.size()<=0 && dialog.isShowing())
                {
                    if (search==true) {
                        timer();
                        search = false;
                    }
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            //Toast.makeText(MainActivity.this, "Ranging", Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }



    public void timer()//TIMER TO DISMISS SEARCH DIALOG AFTER 10 SECONDS
    {
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"No Objects Found",Toast.LENGTH_LONG).show();
                        beaconManager.unbind(MainActivity.this);
                    }
                });
                this.cancel();
            }
        }, 10000L);
    }
    //ASK USER TO SAVE CURRENT DEVICE OR NOT
    public void beaconConfirmation(final String atname, final int uuid, final String mac, final int rssi)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Save this device?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SQLBeaconBean sbb = new SQLBeaconBean();
                        sbb.setATName(atname);
                        sbb.setUuid(uuid);
                        sbb.setMac(mac);
                        sbb.setRssi(rssi);
                        sbb.setSName(atname);
                        dao.saveDevice(sbb);
                        Toast.makeText(MainActivity.this,"Successfully Added. You can edit saved devices in My Devices",Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        beaconManager.unbind(MainActivity.this);
                        Intent intent = new Intent(MainActivity.this, ObjectRanging.class);
                        Bundle extras = new Bundle();
                        extras.putString("ATNAME",atname);
                        extras.putString("KEY",itemClickValue);
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean existsInDb(String atname)//TO CHECK IF DEVICE EXISTS IN DATABASE
    {
        String TABLE_NAME = "device_details";
        SQLBeaconInit p = new SQLBeaconInit(this);
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE ATNAME = '" +atname+ "'"+ " OR SNAME = '" +atname+ "'";
        SQLiteDatabase db  = p.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);

        if (cursor.getCount()>0){existsDb=true;}
        else{ existsDb = false;}
        cursor.close();

        return existsDb;
    }
    public String sName(String rec_atname)//TO GET SAVED NAME OF DEVICE
    {
        String TABLE_NAME = "device_details";
        String snameDB = null;
        SQLBeaconInit p = new SQLBeaconInit(this);

        String selectQuery = "SELECT SNAME FROM " + TABLE_NAME + " WHERE ATNAME = '" +rec_atname+ "'";
        SQLiteDatabase db  = p.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);
        //int i=0;
        if (cursor.moveToFirst())
        {snameDB = cursor.getString(cursor.getColumnIndex("SNAME"));}
        else{ existsDb = false;}
        cursor.close();

        return snameDB;
    }

    public String checkAdder(String rec_atname)//TO ADD ONLY DISTINCT VALUES IN LIST
    {
        String s_nameca = null;
        if(existsInDb(rec_atname))
        {
            s_nameca = sName(rec_atname);
            if (rec_atname.equals(s_nameca))
            {
                if(!scannedBeacons.contains(s_nameca))
                    scannedBeacons.add(s_nameca);

            }
            else {
                if(!scannedBeacons.contains(s_nameca))
                    scannedBeacons.add(s_nameca);
            }
        }
        else
        if(!scannedBeacons.contains(rec_atname))
            scannedBeacons.add(rec_atname);

        return s_nameca;
    }
}
