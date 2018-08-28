package com.loconav.lookup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.loconav.lookup.base.BaseActivity;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.model.ReasonResponse;

import java.util.ArrayList;

import static com.loconav.lookup.EncodingDecoding.encodeToBase64;
import static com.loconav.lookup.EncodingDecoding.getResizedBitmap;
import static com.loconav.lookup.FragmentController.loadFragment;

public class LookupSubActivity extends BaseActivity {

    Repair repairFragment;
    Uri uri;
    ArrayList<Input> addtionalFields = new ArrayList<>();
    public PassingReason passingReason;
    ReasonResponse reasonResponse;

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
            loadFragment(deviceIdFragment,getSupportFragmentManager(),R.id.frameLayout,false);
        }else {
            loadFragment(repairFragment,getSupportFragmentManager(),R.id.frameLayout,false);
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
