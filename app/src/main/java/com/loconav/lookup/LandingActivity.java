package com.loconav.lookup;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.LocationManager;
import android.os.Bundle;


import com.loconav.lookup.base.BaseActivity;
import com.loconav.lookup.databinding.ActivityLookupEntryBinding;
import com.loconav.lookup.location.LocationBroadcastReciever;
import com.loconav.lookup.location.OnGpsDialog;
import com.loconav.lookup.utils.AppUtils;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class LandingActivity extends BaseActivity {
    private ActivityLookupEntryBinding lookupEntryBinding;
    private final FragmentController fragmentController=new FragmentController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lookupEntryBinding = DataBindingUtil.setContentView(this, R.layout.activity_lookup_entry);
        HomeFragment homeFragment=new HomeFragment();
        fragmentController.loadFragment(homeFragment,getSupportFragmentManager(),R.id.landing_fragment_host,false);
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
    private void startBroadcstReceiver()
    {
        Intent intent = new Intent(this, LocationBroadcastReciever.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), TimeUnit.MINUTES.toMillis(30), pendingIntent);
    }



}
