package com.loconav.lookup.customcamera;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.loconav.lookup.FragmentController;
import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseActivity;
import com.loconav.lookup.databinding.ActivityCameraOpenBinding;

public class CameraOpenActivity extends BaseActivity {
    ActivityCameraOpenBinding cameraOpenBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraOpenBinding=DataBindingUtil.setContentView(this,R.layout.activity_camera_open);
        FragmentController fragmentController=new FragmentController();
        Bundle bundle=new Bundle();
        bundle.putInt("limit",getIntent().getExtras().getInt("limit"));
        bundle.putString("Stringid",getIntent().getExtras().getString("Stringid"));
        CameraPickerFragment cameraPickerFragment=new CameraPickerFragment();
        cameraPickerFragment.setArguments(bundle);
        fragmentController.loadFragment(cameraPickerFragment,getSupportFragmentManager(),R.id.camera,false);
    }

    @Override
    public boolean showBackButton() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraOpenBinding.unbind();
    }
}
