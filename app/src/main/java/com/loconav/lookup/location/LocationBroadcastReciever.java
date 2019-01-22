package com.loconav.lookup.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.loconav.lookup.customcamera.Callback;
import com.loconav.lookup.model.CoordinateRequest;
import com.loconav.lookup.model.Coordinates;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class LocationBroadcastReciever extends BroadcastReceiver implements Callback {
    private Context context;
    private final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        LocationGetter locationGetter=new LocationGetter(this,context);
    }

    @Override
    public void onEventDone(Object object) {
        Location location=(Location) object;
        if(location!=null) {
            long time = System.currentTimeMillis() / 1000;
            String latitude = String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());
            CoordinateRequest coordinateRequest = new CoordinateRequest();
            List<Coordinates> coordinatesList = new ArrayList<>();
            Coordinates coordinates = new Coordinates();
            coordinates.setLatitude(latitude);
            coordinates.setLongitude(longitude);
            coordinates.setRecordedAt(time);
            coordinatesList.add(coordinates);
            coordinateRequest.setCoordinates(coordinatesList);
            sendCoordinates(coordinateRequest);
            Log.e("location", "onLocationChanged: " + location);
        }
    }

    private void sendCoordinates(CoordinateRequest coordinateRequest)
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
}
