package com.loconav.lookup.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.loconav.lookup.BaseCameraActivity;
import com.loconav.lookup.LookUpEntry;
import com.loconav.lookup.login.SplashActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prateek on 09/07/18.
 */

public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       if(!checkPermissions()){
           Intent intent=new Intent(getBaseContext(),SplashActivity.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
           startActivity(intent);
       }
        if(showBackButton())
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public  boolean checkPermissions() {
        List<String> listPermissionsNeeded = new ArrayList<>();
        int write_storage = ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read_storage = ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int camera = ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA);

        if (read_storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (write_storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            return false;
        }
        return true;
    }

    public abstract boolean showBackButton();

//    @Override
//    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        if (menuItem.getItemId() == android.R.id.home) {
//            onBackPressed();
//        }
//        return super.onOptionsItemSelected(menuItem);
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
