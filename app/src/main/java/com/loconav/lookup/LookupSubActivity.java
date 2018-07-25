package com.loconav.lookup;

import android.content.Intent;
import android.os.Bundle;

import static com.loconav.lookup.Constants.USER_CHOICE;

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
       // loadFragment(fragmentDeviceId,getSupportFragmentManager(),R.id.fragmentContainerSub);
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
