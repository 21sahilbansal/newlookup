package com.loconav.lookup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.loconav.lookup.base.BaseActivity;
import com.loconav.lookup.model.Client;
import com.loconav.lookup.model.PassingReason;

import static com.loconav.lookup.Constants.USER_CHOICE;
import static com.loconav.lookup.EncodingDecoding.encodeToBase64;
import static com.loconav.lookup.EncodingDecoding.getResizedBitmap;
import static com.loconav.lookup.FragmentController.loadFragment;

public class LookupSubActivity extends BaseActivity {

    Repair fragmentDeviceId;
    Uri uri;
    public PassingReason passingReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookup_sub_activity);
        Intent intent=getIntent();
        passingReason = (PassingReason)intent.getSerializableExtra("str");
        fragmentDeviceId = new Repair();
        passIntentData();
    }

    @Override
    public boolean showBackButton() {
        return true;
    }

    void passIntentData(){
        if(passingReason.getUserChoice().equals("New Install")){
            DeviceIdFragment f1 = new DeviceIdFragment();
            loadFragment(f1,getSupportFragmentManager(),R.id.frameLayout,false);
        }else {
            loadFragment(fragmentDeviceId,getSupportFragmentManager(),R.id.frameLayout,false);
        }
        FragmentController fragmentController=new FragmentController(getSupportFragmentManager(),LookupSubActivity.this);

    }

    public PassingReason getPassingReason(){
        return passingReason;
    }

    public void setPassingReason(PassingReason passingReason){
        this.passingReason=passingReason;
    }

    public String reduceBititmap(Bitmap bitmap){
        Log.e("sizeof",""+encodeToBase64(bitmap, Bitmap.CompressFormat.PNG,0).length());
        Bitmap bm1 = getResizedBitmap(bitmap,500);
        String str= "data:image/png;base64,"+encodeToBase64(bm1, Bitmap.CompressFormat.PNG,0);
        Log.e("SIZE OF",""+str.length());
        return str;

    }
}
