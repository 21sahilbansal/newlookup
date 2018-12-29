package com.loconav.lookup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class camera_open extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_open);
        FragmentController fragmentController=new FragmentController();
        camerapickerfragment Camerapickerfragment=new camerapickerfragment();
        fragmentController.loadFragment(Camerapickerfragment,getSupportFragmentManager(),R.id.camera,false);
    }
}
