package com.loconav.lookup.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

public class BaseService {
    public void startForegroundLocationService(Context appContext,Intent serviceIntent){
        ContextCompat.startForegroundService(appContext,serviceIntent);
    }
}
