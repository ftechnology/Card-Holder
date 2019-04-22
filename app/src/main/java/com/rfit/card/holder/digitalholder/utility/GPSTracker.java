package com.rfit.card.holder.digitalholder.utility;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.rfit.card.holder.digitalholder.listener.LocationChangeListener;
import com.rfit.card.holder.digitalholder.listener.SettingsDialogCancelListener;

public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location = null; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0; // 0 sec

    private static final String TAG = "GPSTracker";

    // Declaring a Location Manager
    protected LocationManager locationManager;
    private static LocationChangeListener mLocationChangeListener = null;
    private SettingsDialogCancelListener digCancelListener = null;

    private long mInterval = MIN_TIME_BW_UPDATES;
    private long mDistance = MIN_DISTANCE_CHANGE_FOR_UPDATES;

    public GPSTracker(Context context, long distance, long interval) {
        this.mContext = context;
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        setDistance(distance);
        setInterval(interval);
    }

    public GPSTracker(Context context) {
        this.mContext = context;
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        getLocation();
    }

    public void setLocChangeListener(LocationChangeListener listener) {
        mLocationChangeListener = listener;
    }

    public void setDistance(long mDistance) {
        this.mDistance = mDistance;
    }

    public void setInterval(long mInterval) {
        this.mInterval = mInterval;
    }

    public Location getLocation() {
        // Toast.makeText(mContext,
        // "Get location called",Toast.LENGTH_SHORT).show();
        try {
            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                // showSettingsAlert();
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            mInterval, mDistance, this);
                    Log.d("Network", "Network Enabled");

                    // Toast.makeText(mContext,
                    // "Network Enabled",Toast.LENGTH_SHORT).show();
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                mInterval, mDistance, this);
                        Log.d("GPS", "GPS Enabled");

                        // Toast.makeText(mContext,
                        // "gps Enabled",Toast.LENGTH_SHORT).show();
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    public void getLocation(String provider) {
        stopUsingGPS();
        boolean isReqProviderEnabled = locationManager.isProviderEnabled(provider);
        if (isReqProviderEnabled) {
            locationManager.requestLocationUpdates(provider,
                    mInterval, mDistance, this);
        }

    }

    public boolean isEnabled(String provider) {
        return locationManager.isProviderEnabled(provider);
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     * */
    public void stopUsingGPS() {
        if (locationManager != null) {
            Log.d(TAG, "Removing updates");

            // Toast.makeText(mContext,
            // "Removing updates",Toast.LENGTH_SHORT).show();
            locationManager.removeUpdates(GPSTracker.this);
            // mLocationChangeListener = null;
        }
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     * 
     * @return boolean
     * */
    public boolean canGetLocation() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    AlertDialog alertDialog;

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     * */
    public void showSettingsAlert(String title, String message) {
        removeDialog();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialogBuilder.setTitle(title);

        // Setting Dialog Message
        alertDialogBuilder.setMessage(message);

        // On pressing Settings button
        alertDialogBuilder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (digCancelListener != null) {
                    digCancelListener.onCancelDialog();
                }
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog = alertDialogBuilder.show();
    }

    public void removeDialog() {
        try {
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        } catch (Exception e) {

        }
    }

    public void setDialogCancelListener(SettingsDialogCancelListener listener) {
        digCancelListener = listener;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("GPSTracker", "OnLocationChanged, listener = " + mLocationChangeListener);
        this.location = location;
        if (mLocationChangeListener != null) {
            mLocationChangeListener.onLocationChange(location);
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public void cleanUp() {
        mLocationChangeListener = null;
    }
}