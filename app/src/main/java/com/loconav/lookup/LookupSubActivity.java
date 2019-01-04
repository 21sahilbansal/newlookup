package com.loconav.lookup;

import android.app.Fragment;
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
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import id.zelory.compressor.Compressor;

import static com.loconav.lookup.EncodingDecoding.encodeToBase64;
import static com.loconav.lookup.EncodingDecoding.getResizedBitmap;

public class LookupSubActivity extends BaseActivity {

    private ArrayList<Input> addtionalFields = new ArrayList<>();
    NavController navController;
//    It gathers info from all fragmnets attached to  this activity to post later on to server
    public PassingReason passingReason;

//    Defines the type of fragment it will be like new install or repairs.
    private ReasonResponse reasonResponse;

    private FragmentController fragmentController = new FragmentController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookup_sub_activity);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        passingReason = (PassingReason)bundle.getSerializable("PassingReason");
        reasonResponse = (ReasonResponse)bundle.getSerializable("reasonResponse");
        addOtherFields(passingReason.getUserChoice());
        reasonResponse.setAdditional_fields(addtionalFields);
        passingReason.setReasonResponse(reasonResponse);
        passIntentData();
    }

    @Override
    public boolean showBackButton() {
        return true;
    }

    void passIntentData(){
        if(passingReason.getUserChoice().equals("New Install")){
//            navController.navigate(R.id.action_blankFragment2_to_deviceIdFragment);
            DeviceIdFragment deviceIdFragment = new DeviceIdFragment();
            fragmentController.loadFragment(deviceIdFragment,getSupportFragmentManager(),R.id.frameLayout,false);
        }
        else {
//            navController.navigate(R.id.action_blankFragment2_to_repairFragment);
            RepairFragment repairFragment = new RepairFragment();;
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
        Input i4 = new Input("addImage", "addImage", "TruckImages", "");
        Input i5 = new Input("addImage", "addImage", "DeviceImages", "");
        Input i6 = new Input("addImage", "addImage", "WireConnectionImages", "");
        Input i7 = new Input("addImage", "addImage", "Accessories", "");
        Input i8 = new Input("addImage", "addImage", "EarthWireConnectionImages", "");
        Input i9 = new Input("addImage", "addImage", "DeviceFitting", "");
        addtionalFields.add(i1);
        addtionalFields.add(i2);
        addtionalFields.add(i3);
        addtionalFields.add(i4);
        addtionalFields.add(i5);
        addtionalFields.add(i6);
        addtionalFields.add(i7);
        addtionalFields.add(i8);
        addtionalFields.add(i9);
        if (passingReason.getUserChoice().equals(userChoice)) {
            addtionalFields.addAll(reasonResponse.getAdditional_fields());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}