package com.loconav.lookup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.loconav.lookup.base.BaseActivity;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.model.ReasonResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

import static com.loconav.lookup.EncodingDecoding.encodeToBase64;
import static com.loconav.lookup.EncodingDecoding.getResizedBitmap;

public class LookupSubActivity extends BaseActivity {

    private Repair repairFragment;
    private ArrayList<Input> addtionalFields = new ArrayList<>();
    public PassingReason passingReason;
    private ReasonResponse reasonResponse;
    FragmentController fragmentController=new FragmentController();
    String fasttag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookup_sub_activity);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        passingReason = (PassingReason)bundle.getSerializable("PassingReason");
        reasonResponse = (ReasonResponse)bundle.getSerializable("reasonResponse");
        fasttag= (String) bundle.getSerializable("fastag");
        addOtherFields(passingReason.getUserChoice());
        reasonResponse.setAdditional_fields(addtionalFields);
        passingReason.setReasonResponse(reasonResponse);
        repairFragment = new Repair();
        passIntentData();
    }

    @Override
    public boolean showBackButton() {
        return true;
    }

    void passIntentData(){
        if(passingReason.getUserChoice().equals("New Install")){
            DeviceIdFragment deviceIdFragment = new DeviceIdFragment();
            fragmentController.loadFragment(deviceIdFragment,getSupportFragmentManager(),R.id.frameLayout,false);
        }
        else {
            fragmentController.loadFragment(repairFragment,getSupportFragmentManager(),R.id.frameLayout,false);
        }
    }

    public PassingReason getPassingReason(){
        return passingReason;
    }

    public void setPassingReason(PassingReason passingReason){
        this.passingReason=passingReason;
    }

    private void addOtherFields(String userChoice) {
        Input i1 = new Input("deviceId", "imei", "textView", "Device Id :");
        Input i2 = new Input("remarks", "remarks", "text", "");
        Input i3 = new Input("reasons", "reasons", "spinner", "");
        Input i4 = new Input("addImage", "addImage", "ImagePicker", "");
        addtionalFields.add(i1);
        addtionalFields.add(i2);
        addtionalFields.add(i3);
        addtionalFields.add(i4);
        if (passingReason.getUserChoice().equals(userChoice)) {
            addtionalFields.addAll(reasonResponse.getAdditional_fields());
        }
    }
}