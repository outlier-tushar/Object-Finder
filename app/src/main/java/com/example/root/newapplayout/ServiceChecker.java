package com.example.root.newapplayout;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import org.altbeacon.beacon.BeaconManager;

/**
 * Created by root on 20/4/17.
 */

public class ServiceChecker {
    private Context context;
    private int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    public ServiceChecker(MainActivity mainActivity)
    {
        this.context = mainActivity;
    }

    public void checkServices() {

        final LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        BluetoothAdapter bla = BluetoothAdapter.getDefaultAdapter();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            // Android M Permission check
            if (context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context,"Manifest Permission",Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access in settings and restart this application.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @RequiresApi(23)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        ((MainActivity)context).requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_REQUEST_COARSE_LOCATION);
                        ((MainActivity)context).finish();
                        System.exit(0);
                    }
                });
                builder.show();
            }

            else if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Object Finder requires location access");
                builder.setMessage("Please grant location access in settings and restart this application.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        ((MainActivity)context).finish();
                        //System.exit(0);
                    }

                });
                builder.show();
            }
            else if(!bla.isEnabled())
            {
                try {
                    if (!BeaconManager.getInstanceForApplication(context).checkAvailability()) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Bluetooth not enabled");
                        builder.setMessage("Please enable bluetooth in settings and restart this application.");
                        builder.setPositiveButton(android.R.string.ok, null);
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                ((MainActivity)context).finish();
                                System.exit(0);
                            }
                        });
                        builder.show();
                    }
                }
                catch (RuntimeException e) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Bluetooth LE not available");
                    builder.setMessage("Sorry, this device does not support Bluetooth LE.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            ((MainActivity)context).finish();
                            System.exit(0);
                        }

                    });
                    builder.show();

                }
            }
        }
        else
        {
            if(!bla.isEnabled())
            {
                try {
                    if (!BeaconManager.getInstanceForApplication(context).checkAvailability()) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Bluetooth not enabled");
                        builder.setMessage("Please enable Bluetooth and restart this application.");
                        builder.setPositiveButton(android.R.string.ok, null);
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                ((MainActivity)context).finish();
                                System.exit(0);
                            }
                        });
                        builder.show();
                    }
                }
                catch (RuntimeException e) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Bluetooth LE not available");
                    builder.setMessage("Sorry, this device does not support Bluetooth LE.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            ((MainActivity)context).finish();
                            System.exit(0);
                        }

                    });
                    builder.show();

                }
            }
        }

    }
}
