package com.loconav.lookup.customcamera;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.loconav.lookup.FragmentController;
import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseActivity;
import com.loconav.lookup.databinding.ActivityCameraOpenBinding;

import java.util.Objects;

public class CameraOpenActivity extends BaseActivity {
    public static final String STRINGID = "Stringid";
    private ActivityCameraOpenBinding cameraOpenBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraOpenBinding = DataBindingUtil.setContentView(this, R.layout.activity_camera_open);
        FragmentController fragmentController = new FragmentController();
        Bundle bundle = new Bundle();
        bundle.putInt(ImagePickerDialog.LIMIT, Objects.requireNonNull(getIntent().getExtras()).getInt(ImagePickerDialog.LIMIT));
        bundle.putInt(ImagePickerDialog.ALREADY_TAKEN_PHOTOS, Objects.requireNonNull(getIntent().getExtras()).getInt(ImagePickerDialog.ALREADY_TAKEN_PHOTOS));
        bundle.putString(STRINGID, getIntent().getExtras().getString(STRINGID));
        CameraPickerFragment cameraPickerFragment = new CameraPickerFragment();
        cameraPickerFragment.setArguments(bundle);
        fragmentController.loadFragment(cameraPickerFragment, getSupportFragmentManager(), R.id.camera, false);
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
