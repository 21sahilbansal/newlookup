package com.loconav.lookup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.loconav.lookup.sharedetailsfragmants.NewInstallation;

public class LookupSubActivity extends FragmentController {

//    public LookupSubActivity(FragmentManager fragmentManager, Activity activity, Context context) {
//        super(fragmentManager, activity, context);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookup_sub_activity);
        Intent intent=getIntent();
        String str=intent.getStringExtra("buttonN");
        Bundle data = new Bundle();//Use bundle to pass data
        data.putString("data", "This is Argument Fragment");//put string, int, etc in bundle with a key value
        //DeviceIdFragment.setArguments(data);//Finally set argument bundle to fragment

        DeviceIdFragment fragmentDeviceId = new DeviceIdFragment();
        loadFragment(fragmentDeviceId,getSupportFragmentManager(),R.id.fragmentContainerSub);
      //  whichFragmentOpen(intent);
    }
    void whichFragmentOpen(Intent intent){
        if(intent.getStringExtra("buttonN").equals("newInstall")){
            NewInstallation fragmentNewInstallation = new NewInstallation();
          loadFragment(fragmentNewInstallation,getSupportFragmentManager(),R.id.fragmentContainerSub);
        }
    }
}
