package com.loconav.lookup;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.loconav.lookup.customcamera.ImageUri;
import com.loconav.lookup.databinding.RepairBinding;
import com.loconav.lookup.model.PassingReason;

import java.util.ArrayList;


/**
 * Created by sejal on 12-07-2018.
 */

public class RepairFragment extends BaseTitleFragment {
    private RepairBinding binding;
    private PassingReason passingReason;
    private final FragmentController fragmentController=new FragmentController();
    @Override
    public int setViewId() {
        return R.layout.repair;
    }

    @Override
    public void onFragmentCreated() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Upload repair image");
        passingReason= ((LookupSubActivity)getActivity()).getPassingReason();
        binding.proceed.setOnClickListener(view -> onProceedClicked());
    }

    @Override
    public void bindView(View view) {
        binding = DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {

    }

    private void onProceedClicked(){
        if(!binding.DeviceImage.getimagesList().isEmpty()) {
            ArrayList<String> imagesList = new ArrayList<>();
            for(ImageUri imageUri : binding.DeviceImage.getimagesList()) {
                imagesList.add(imageUri.getUri().toString());
            }
            passingReason.setImagesList(imagesList);
            passingReason.setImagesPreRepair(binding.DeviceImage.getimagesList().size());
            ((LookupSubActivity)getActivity()).setPassingReason(passingReason);
            DeviceIdFragment f1 =new DeviceIdFragment();
            fragmentController.loadFragment(f1,getActivity().getSupportFragmentManager(),R.id.frameLayout,true);
        }else
            Toaster.makeToast(getString(R.string.add_device_image));
    }


    @Override
    public String getTitle() {
        return "Upload repair image";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.unbind();
    }
}
