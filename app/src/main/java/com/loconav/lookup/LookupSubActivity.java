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
import static com.loconav.lookup.FragmentController.loadFragment;

public class LookupSubActivity extends BaseActivity {

    private Repair repairFragment;
    private ArrayList<Input> addtionalFields = new ArrayList<>();
    public PassingReason passingReason;
    private ReasonResponse reasonResponse;
    String mCurrentPhotoPath;
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
    }

    public PassingReason getPassingReason(){
        return passingReason;
    }

    public void setPassingReason(PassingReason passingReason){
        this.passingReason=passingReason;
    }


    public String reduceBititmap(Bitmap bitmap) throws Exception
    {
        File f=new FileUtility().getfile(getApplicationContext());
        FileOutputStream fout=new FileOutputStream(f);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
        Bitmap compressedImageBitmap = new Compressor(this).setQuality(70).compressToBitmap(f);
        int height=compressedImageBitmap.getHeight();
        int width=compressedImageBitmap.getWidth();
        compressedImageBitmap=Bitmap.createScaledBitmap(compressedImageBitmap,(width*90)/100,(height*90)/100,true);
        String str= "data:image/png;base64,"+encodeToBase64(compressedImageBitmap, Bitmap.CompressFormat.JPEG,50);
        Log.e("SIZE OF",""+str.length());
        return str;
    }
    private File getfile() throws IOException {
        File storageDir;
        String imageFileName = "JPEG_" + "sourav" + "_";
        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
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