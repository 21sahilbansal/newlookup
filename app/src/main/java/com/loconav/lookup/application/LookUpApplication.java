package com.loconav.lookup.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.loconav.lookup.Constants;

import static com.loconav.lookup.Constants.LOOK_UP_PREFERENCES;

/**
 * Created by prateek on 16/02/18.
 */
public class LookUpApplication extends Application {

    public static SharedPreferences sharedPreferences;

    public static SharedPreferences.Editor editor;

    private static LookUpApplication instance = null;

    public static Context getInstance(){
        if (null == instance) {
            instance = new LookUpApplication();
        }
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(LOOK_UP_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
}
