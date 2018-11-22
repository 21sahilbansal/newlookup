package com.loconav.lookup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class camera_open extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_open);
        FragmentController fragmentController=new FragmentController();
        Bundle bundle=new Bundle();
        bundle.putInt("limit",getIntent().getExtras().getInt("limit"));
        bundle.putString("Stringid",getIntent().getExtras().getString("Stringid"));
        CameraPickerFragment cameraPickerFragment=new CameraPickerFragment();
        cameraPickerFragment.setArguments(bundle);
        fragmentController.loadFragment(cameraPickerFragment,getSupportFragmentManager(),R.id.camera,false);
    }
}
