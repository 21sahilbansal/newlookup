package com.loconav.lookup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.model.Client;
import com.loconav.lookup.model.ImageUri;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.model.RepairRequirements;
import com.loconav.lookup.sharedetailsfragmants.DeviceChange;
import com.loconav.lookup.sharedetailsfragmants.NewInstallation;
import com.loconav.lookup.sharedetailsfragmants.SimChange;
import com.loconav.lookup.sharedetailsfragmants.VehicleChange;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.loconav.lookup.EncodingDecoding.encodeToBase64;

/**
 * Created by sejal on 12-07-2018.
 */

public class Repair extends BaseFragment {
    @BindView (R.id.proceed) Button proceed;
    @BindView (R.id.DeviceImage) CustomImagePicker deviceImage;
    private List<ImageUri> deviceImageUris = new ArrayList<>();
    private PassingReason passingReason;
    private String userChoice;


    @Override
    public int setViewId() {
        return R.layout.repair;
    }

    @Override
    public void onFragmentCreated() {
        passingReason= (PassingReason)getActivity().getIntent().getSerializableExtra("str");
        Log.e("sd",""+passingReason.getDeviceid());
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProceedClicked();
            }
        });
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, getView());
    }

    @Override
    public void getComponentFactory() {

    }

    private void onProceedClicked(){

        if(deviceImage.GetimagesList().size()>=1){

            deviceImageUris.addAll(deviceImage.GetimagesList());
            userChoice=passingReason.getUserChoice();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            DeviceIdFragment f1 = DeviceIdFragment.newInstance(passingReason,deviceImage.GetimagesList().get(0).getUri());;
            fragmentTransaction.add(R.id.frameLayout, f1);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }else {
            Toast.makeText(getContext(),"Add Device Image",Toast.LENGTH_SHORT).show();
        }
    }
    public static Repair newInstance(PassingReason passingReason1) {
        Repair fragment = new Repair();
        Bundle bundle = new Bundle();
        bundle.putSerializable("str",passingReason1);
        fragment.setArguments(bundle);
        return fragment;
    }
    void openFragment(){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            switch (userChoice) {
                case "SIM Change": {
                    SimChange f1 = SimChange.newInstance(passingReason,deviceImage.GetimagesList().get(0).getUri());;
                    fragmentTransaction.replace(android.R.id.content, f1);
                   // passData("str",passingReason,"str2",repairRequirements);
                    break;
                }
                case "Device Change": {
                    DeviceChange f1 = DeviceChange.newInstance(passingReason,deviceImage.GetimagesList().get(0).getUri());;
                    fragmentTransaction.replace(android.R.id.content, f1);
                 //   passData("str",passingReason,"str2",repairRequirements);
                    break;
                }
                case "vehChange": {
                    VehicleChange f1 = new VehicleChange();
                    fragmentTransaction.replace(android.R.id.content, f1);
                   // passData("str",passingReason,"str2",repairRequirements);
                    break;
                }
                case "Repairs": {
                    RepairForm f1 = RepairForm.newInstance(passingReason,deviceImage.GetimagesList().get(0).getUri());
                    fragmentTransaction.replace(android.R.id.content, f1);
                    break;
                }
            }
            fragmentTransaction.commit();
        }
}
