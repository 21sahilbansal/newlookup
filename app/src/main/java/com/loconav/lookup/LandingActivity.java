package com.loconav.lookup;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;



import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loconav.lookup.adapter.WhatToDoAdapter;
import com.loconav.lookup.application.SharedPrefHelper;


import com.loconav.lookup.base.BaseActivity;
import com.loconav.lookup.databinding.ActivityLookupEntryBinding;
import com.loconav.lookup.location.LocationBroadcastReciever;
import com.loconav.lookup.location.LocationGetter;
import com.loconav.lookup.location.OnGpsDialog;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.model.ReasonResponse;
import com.loconav.lookup.model.ReasonTypeResponse;
import com.loconav.lookup.utils.AppUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

import static com.loconav.lookup.Constants.REASONS_RESPONSE;
import static com.loconav.lookup.Constants.TUTORIAL_KEY;

public class LandingActivity extends BaseActivity {
    private ActivityLookupEntryBinding lookupEntryBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lookupEntryBinding = DataBindingUtil.setContentView(this, R.layout.activity_lookup_entry);
        //Check if the gps is ON or not and if ON then continue and start the BroadcastReceiver if not then onGpsDialog appers and ask the user to ON the GPS
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            new OnGpsDialog(this);
        }
        else
        {
            startBroadcstReceiver();
        }

        showAppUpdateDialog();
    }

    private void showAppUpdateDialog() {
        new AppUpdateController(getSupportFragmentManager(), AppUtils.getVersionCode(getBaseContext()));
    }

    @Override
    public boolean showBackButton() {
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        lookupEntryBinding.unbind();
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== OnGpsDialog.REQUEST_CHECK_SETTINGS) {
            if (resultCode==RESULT_CANCELED)
                finish();
            else {
                startBroadcstReceiver();
            }
        }
    }
    //This is used to start the receiver in background after the app is killed
    public void startBroadcstReceiver()
    {
        Intent intent = new Intent(this, LocationBroadcastReciever.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), TimeUnit.MINUTES.toMillis(30), pendingIntent);
    }



}
