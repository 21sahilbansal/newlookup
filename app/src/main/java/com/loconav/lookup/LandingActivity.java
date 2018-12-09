package com.loconav.lookup;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
    private PassingReason passingReason = new PassingReason();
    WhatToDoAdapter adapter;
    ReasonResponse reasonResponse;
    Toolbar toolbar;
    ArrayList<ReasonResponse> jsonLog = new ArrayList<>();
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Check if the gps is ON or not and if ON then continue and start the BroadcastReceiver if not then onGpsDialog appers and ask the user to ON the GPS
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            new OnGpsDialog(this);
        }
        else
        {
            startBroadcstReceiver();
            firstTimeTutorial();
        }

        lookupEntryBinding = DataBindingUtil.setContentView(this, R.layout.activity_lookup_entry);
        toolbar = lookupEntryBinding.toolbar;
        toolbar.inflateMenu(R.menu.user);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_lookup_app_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        String reasonsResponse = SharedPrefHelper.getInstance().getStringData(REASONS_RESPONSE);
        Gson gson = new Gson();
        jsonLog = gson.fromJson(reasonsResponse, new TypeToken<List<ReasonResponse>>() {
        }.getType());
        if (jsonLog != null) {
            setPhotoAdapter();
        } else {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_LONG).show();
        }
        showAppUpdateDialog();
    }

    private void showAppUpdateDialog() {
        new AppUpdateController(getSupportFragmentManager(), AppUtils.getVersionCode(getBaseContext()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean showBackButton() {
        return false;
    }

    private void setPhotoAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        lookupEntryBinding.rvTasks.setLayoutManager(layoutManager);
        adapter = new WhatToDoAdapter(jsonLog, new Callback() {
            @Override
            public void onEventDone(Object object) {
                reasonResponse = (ReasonResponse) object;
                reasonResponse.setName("Repairs");
                passingReason.setUserChoice(reasonResponse.getName());
                passIntent();
            }
        });
        lookupEntryBinding.rvTasks.setAdapter(adapter);
        lookupEntryBinding.rvTasks.setNestedScrollingEnabled(false);
        LayoutAnimationController layoutAnimationController=AnimationUtils.loadLayoutAnimation(lookupEntryBinding.rvTasks.getContext(),R.anim.layout_animation);
        lookupEntryBinding.rvTasks.setLayoutAnimation(layoutAnimationController);
    }

    public void newInstall(View view) {
        List<ReasonTypeResponse> reasons = new ArrayList<>();
        ArrayList<Input> additionalFields = new ArrayList<>();
        ReasonResponse reasonResponse = new ReasonResponse(1, "New Install", reasons, additionalFields, "abc");
        this.reasonResponse = reasonResponse;
        passingReason.setUserChoice(reasonResponse.getName());
        passIntent();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_user:
                Intent intent = new Intent(getBaseContext(), UserActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void passIntent() {
        Intent intent = new Intent(this, LookupSubActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("PassingReason", passingReason);
        bundle.putSerializable("reasonResponse", reasonResponse);
        intent.putExtras(bundle);
        startActivity(intent);
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
                firstTimeTutorial();
                startBroadcstReceiver();
            }
        }
    }

    //These 3 function(firTimeTutorial,showTutorial1,showTutorial2) these are used to show the tutorial and they no much use
    public void firstTimeTutorial() {
        Boolean firstTime = SharedPrefHelper.getInstance().getBooleanData(TUTORIAL_KEY);//first time it will be false
        if(!firstTime)
        {
            showTutorial1();
            SharedPrefHelper.getInstance().setBooleanData(TUTORIAL_KEY,true); // next time it will always be true
        }
    }
    public void showTutorial1() {
        view =toolbar.findViewById(R.id.action_user);
        new GuideView.Builder(this)
                .setTitle(getString(R.string.user_profile))
                .setContentText("1. "+getString(R.string.check_profile)+"\n\n"+
                        "2. "+getString(R.string.check_repairs_logs)+"\n\n"+
                        "3. "+getString(R.string.check_install_logs))
                .setGravity(Gravity.auto) //optional
                .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                .setTargetView(view)
                .setGuideListener(new GuideListener() {
                    @Override
                    public void onDismiss(View view) {
                        showTutorial2();
                    }
                })
                .setContentTextSize(12)//optional
                .setTitleTextSize(14)//optional
                .build()
                .show();
    }
    public void showTutorial2() {
        view =findViewById(R.id.newInstall);
        new GuideView.Builder(this)
                .setTitle(getString(R.string.create_new_install))
                .setContentText(getString(R.string.creation_installation))
                .setGravity(Gravity.auto) //optional
                .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                .setTargetView(view)
                .setContentTextSize(12)//optional
                .setTitleTextSize(14)//optional
                .build()
                .show();
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
