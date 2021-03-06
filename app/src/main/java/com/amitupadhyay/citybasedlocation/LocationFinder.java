package com.amitupadhyay.citybasedlocation;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by aupadhyay on 3/16/17.
 */

public class LocationFinder implements LocationListener {

    private Context context;
    private OnLocationNotifiedListener onLocationNotifiedListener;

    public LocationFinder(Context context) {
        this.context = context;
        onLocationNotifiedListener = (OnLocationNotifiedListener) context;
    }

    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 1;

    private LocationManager locationManager;
    private ProgressDialog progressDialog;

    private boolean checkNetworkConnectivity()
    {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }


    public void getCityByLocation() {

        if (!checkNetworkConnectivity())
        {
            Toast.makeText(context, "No internet found", Toast.LENGTH_SHORT).show();
            return;
        }

        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Explanation not needed, since user requests this himself

            } else {
                ActivityCompat.requestPermissions((Activity)context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_ACCESS_FINE_LOCATION);
            }

        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(context.getString(R.string.getting_location));
            progressDialog.setCancelable(false);
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        locationManager.removeUpdates(LocationFinder.this);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            });
            progressDialog.show();
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        } else {
            showLocationSettingsDialog();
        }
    }




    @Override
    public void onLocationChanged(Location location) {

        progressDialog.hide();
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {
            Log.e("LocationManager", "Error while trying to stop listening for location updates. This is probably a permissions issue", e);
        }
        Log.i("LOCATION (" + location.getProvider().toUpperCase() + ")", location.getLatitude() + ", " + location.getLongitude());
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        getCityDetails(latitude, longitude);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void getCityDetails(double lat, double lon)
    {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null)
        {
            String cityName = addresses.get(0).getAddressLine(0);
            String stateName = addresses.get(0).getAddressLine(1);
            String countryName = addresses.get(0).getAddressLine(2);

            progressDialog.dismiss();
            onLocationNotifiedListener.setOnLocationFoundListener(cityName, stateName, countryName);
        }
        else
        {
            progressDialog.dismiss();
            onLocationNotifiedListener.setOnLocationNotFoundListener();
        }

    }

    private void showLocationSettingsDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.location_settings);
        alertDialog.setMessage(R.string.location_settings_message);
        alertDialog.setPositiveButton(R.string.location_settings_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public interface OnLocationNotifiedListener
    {
        void setOnLocationFoundListener(String cityName, String stateName, String countryName);
        void setOnLocationNotFoundListener();
    }
}
