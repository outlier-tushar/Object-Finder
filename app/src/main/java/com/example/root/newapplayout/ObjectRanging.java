package com.example.root.newapplayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.Locale;

public class ObjectRanging extends AppCompatActivity implements BeaconConsumer{

    TextView distanceTv;
    BeaconManager beaconManager;
    String fetchedATName;
    String fetchedSName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_ranging);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);



        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        fetchedATName = extras.getString("ATNAME");
        fetchedSName = extras.getString("KEY");

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);

        TextView beaconTv = (TextView)findViewById(R.id.beaconName);
        beaconTv.setTextColor(Color.WHITE);
        beaconTv.setText(fetchedSName);

        distanceTv = (TextView)findViewById(R.id.beaconDistance);

    }

    @Override
    public void onBackPressed()
    {
        beaconManager.unbind(this);
        finish();
        /*Intent returnIntent = new Intent(ObjectRanging.this, MainActivity.class);
        ObjectRanging.this.startActivity(returnIntent);*/
    }

    @Override
    protected void onDestroy() {
        beaconManager.unbind(this);
        super.onDestroy();
    }

    @Override
    public void onBeaconServiceConnect() {

        final BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beaconCollection, Region region)
            {
                for (final Beacon details : beaconCollection)
                {
                    String name = details.getBluetoothName();
                    final Double distance = details.getDistance();
                    if(name.equals(fetchedATName))
                    {
                        Log.i("ObjectRanging",name);
                        Log.i("ObjectRanging", String.valueOf(distance));
                        ObjectRanging.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                if(distance>0){distanceTv.setText(String.format(Locale.US,"%.2f",distance));}
                            }
                        });
                    }
                }
            }
        });
        try{
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            Toast.makeText(this, "Ranging", Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
        }
    }
}
