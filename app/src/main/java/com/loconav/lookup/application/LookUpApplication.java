package com.loconav.lookup.application;

import android.app.Application;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.loconav.lookup.base.MyDataBindingComponent;
import com.squareup.leakcanary.LeakCanary;

import io.fabric.sdk.android.Fabric;


/**
 * Created by prateek on 16/02/18.
 */
public class LookUpApplication extends Application {

    private static LookUpApplication instance = null;

    public static LookUpApplication getInstance(){
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
        instance = this;
        Fabric.with(this, new Crashlytics());
        DataBindingUtil.setDefaultComponent(new MyDataBindingComponent());
    }
}
