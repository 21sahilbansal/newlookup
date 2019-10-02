package com.loconav.lookup.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.loconav.lookup.customcamera.Callback;

import java.util.concurrent.TimeUnit;


@SuppressWarnings("deprecation")
class LocationGetter implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private final Context context;
    private final GoogleApiClient googleApiClient;
    private final Callback callback;
    private Location mLocation;
    private final long FASTEST_INTERVAL =  TimeUnit.MINUTES.toMillis(15);
    private Location currentLocation = null;
    private long locationUpdatedAt = Long.MIN_VALUE;

    public LocationGetter(Callback callback,Context context) {
        this.context = context;
        this.callback = callback;
        googleApiClient = getInstance();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    private GoogleApiClient getInstance() {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(context).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        return mGoogleApiClient;
    }

    private void settingsrequest() {
        Log.e("settingsrequest", "Comes");
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        locationRequest.setInterval(TimeUnit.MINUTES.toMillis(15));
        locationRequest.setFastestInterval(TimeUnit.MINUTES.toMillis(15));

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        settingsrequest();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation=location;
        boolean updateLocationandReport = false;
        if(currentLocation == null){
            currentLocation = location;
            locationUpdatedAt = System.currentTimeMillis();
            updateLocationandReport = true;
        } else {
            long secondsElapsed = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - locationUpdatedAt);
            if (secondsElapsed >= TimeUnit.MILLISECONDS.toSeconds(FASTEST_INTERVAL)){
                // check location accuracy here
                currentLocation = location;
                locationUpdatedAt = System.currentTimeMillis();
                updateLocationandReport = true;
            }
        }
        if(updateLocationandReport){
            //  send your location to server
            callback.onEventDone(mLocation);
        }

    }


}