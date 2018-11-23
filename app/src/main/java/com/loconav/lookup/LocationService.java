package com.loconav.lookup;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.loconav.lookup.login.SplashActivity;
import com.loconav.lookup.model.CoordinateRequest;
import com.loconav.lookup.model.Coordinates;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class LocationService extends Service {
    private static final String TAG = LocationService.class.getSimpleName();
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = (int) TimeUnit.MINUTES.toMillis(28);
//private static final int LOCATION_INTERVAL = 200;
    private static final float LOCATION_DISTANCE = 0f;
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onCreate()
    {
        if(Build.VERSION.SDK_INT>=26)
        {
            Intent notificationIntent=new Intent(this,SplashActivity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,0);
            NotificationChannel servicechannel=new NotificationChannel("locationservicechannel","Location Service",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(servicechannel);
            Notification notification=new NotificationCompat.Builder(this,"locationservicechannel")
                    .setContentTitle("Lookup")
                    .setContentText("Lookup app is getting your device location")
                    .setSmallIcon(R.drawable.lookup_icon).setContentIntent(pendingIntent).build();
            startForeground(1,notification);
        }
        else
        {Intent notificationIntent=new Intent(this,SplashActivity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,0);
            Notification notification=new NotificationCompat.Builder(this,"locationservicechannel")
                    .setContentTitle("Lookup")
                    .setContentText("Lookup app is getting your device location")
                    .setSmallIcon(R.drawable.lookup_icon).setContentIntent(pendingIntent).build();
            startForeground(1,notification);
        }
        Log.e(TAG, "onCreate");

        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true); //true will remove notification
        }
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        }
    }

    private class LocationListener implements android.location.LocationListener   {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            long time=System.currentTimeMillis()/1000;
            String latitude=String.valueOf(location.getLatitude());
            String longitude=String.valueOf(location.getLongitude());
            CoordinateRequest coordinateRequest = new CoordinateRequest();
            List<Coordinates> coordinatesList = new ArrayList<>();
            Coordinates coordinates = new Coordinates();
            coordinates.setLatitude(latitude);
            coordinates.setLongitude(longitude);
            coordinates.setRecordedAt(time);
            coordinatesList.add(coordinates);
            coordinateRequest.setCoordinates(coordinatesList);
            sendCoordinates(coordinateRequest);
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
        }
        public void sendCoordinates(CoordinateRequest coordinateRequest)
        {
            apiService.addCoordinates(coordinateRequest).enqueue(new RetrofitCallback<ResponseBody>() {
                @Override
                public void handleSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.e("coordinates","posted");
                }

                @Override
                public void handleFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("Not posted","not sent"+t.toString());
                }
            });
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("ss","on unbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e("ss","sfdsfdf");
    }
}