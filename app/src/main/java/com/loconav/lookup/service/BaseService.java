package com.loconav.lookup.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

public abstract class BaseService extends Service {
    public static void startForegroundLocationService(Context appContext,Intent serviceIntent){
        ContextCompat.startForegroundService(appContext,serviceIntent);
    }
}
