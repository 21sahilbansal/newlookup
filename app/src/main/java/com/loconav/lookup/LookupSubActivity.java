package com.loconav.lookup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.loconav.lookup.base.BaseActivity;
import com.loconav.lookup.model.PassingReason;

import static com.loconav.lookup.Constants.USER_CHOICE;

public class LookupSubActivity extends BaseActivity {

    Repair fragmentDeviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookup_sub_activity);
        Intent intent=getIntent();
        fragmentDeviceId = new Repair();
        passIntentData(intent);
    }

    @Override
    public boolean showBackButton() {
        return true;
    }

    void passIntentData(Intent intent){
        PassingReason passingReason = (PassingReason)intent.getSerializableExtra("str");
        Log.e("sej",""+passingReason.getReasons().get(1).getName());
        Bundle bundle = new Bundle();
        bundle.putSerializable("str", passingReason);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(passingReason.getUserChoice().equals("New Install")){
            DeviceIdFragment f1 = DeviceIdFragment.newInstance(passingReason);
            transaction.add(R.id.frameLayout, f1);
        }else {
            transaction.add(R.id.frameLayout, fragmentDeviceId);
        }
        transaction.commit();
    }
}
