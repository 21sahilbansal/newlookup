package com.loconav.lookup;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.model.ImageUri;
import com.loconav.lookup.model.PassingReason;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.loconav.lookup.FragmentController.loadFragment;

/**
 * Created by sejal on 12-07-2018.
 */

public class Repair extends BaseTitleFragment {
    @BindView (R.id.proceed) Button proceed;
    @BindView (R.id.DeviceImage) CustomImagePicker deviceImage;
    private PassingReason passingReason;


    @Override
    public int setViewId() {
        return R.layout.repair;
    }

    @Override
    public void onFragmentCreated() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Upload repair image");
        passingReason= ((LookupSubActivity)getActivity()).getPassingReason();
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

        if(!deviceImage.getimagesList().isEmpty()) {
            ArrayList<String> imagesList = new ArrayList<>();
            for(ImageUri imageUri : deviceImage.getimagesList()) {
                imagesList.add(imageUri.getUri().toString());
            }
            passingReason.setImagesList(imagesList);
            passingReason.setImagesPreRepair(deviceImage.getimagesList().size());
            ((LookupSubActivity)getActivity()).setPassingReason(passingReason);
            DeviceIdFragment f1 =new DeviceIdFragment();
            loadFragment(f1,getFragmentManager(),R.id.frameLayout,true);
        }else
            Toast.makeText(getContext(),"Add Device Image",Toast.LENGTH_SHORT).show();
    }
    public static Repair newInstance(PassingReason passingReason1) {
        Repair fragment = new Repair();
        Bundle bundle = new Bundle();
        bundle.putSerializable("str",passingReason1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public String getTitle() {
        return "Upload repair image";
    }
}
