package com.loconav.lookup.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by prateek on 16/02/18.
 */
public class LookUpApplication extends Application {

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
    }
}
