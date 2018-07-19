package com.loconav.lookup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import static com.loconav.lookup.Constants.USER_CHOICE;

import com.loconav.lookup.sharedetailsfragmants.NewInstallation;

import java.util.ArrayList;

public class LookupSubActivity extends FragmentController {

//    public LookupSubActivity(FragmentManager fragmentManager, Activity activity, Context context) {
//        super(fragmentManager, activity, context);
//    }
   DeviceIdFragment fragmentDeviceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookup_sub_activity);
        Intent intent=getIntent();
        fragmentDeviceId = new DeviceIdFragment();
        passIntentData(intent);
    }
    void passIntentData(Intent intent){
        USER_CHOICE=intent.getStringExtra("button");
        Bundle bundle = new Bundle();
        bundle.putString("data",USER_CHOICE);
        fragmentDeviceId.setArguments(bundle);
        loadFragment(fragmentDeviceId,getSupportFragmentManager(),R.id.fragmentContainerSub);
    }
}
