package com.loconav.lookup;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.dialog.ImagePickerDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sejal on 06-07-2018.
 */

public abstract class BaseCameraFragment extends BaseFragment {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    public boolean checkAndRequestPermissions() {

        int write_storage = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read_storage = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int camera = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();

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
            requestPermissions(listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;
    }

    public abstract int setViewId();

    public void onFragmentCreated() {}

    public abstract void bindView(View view);

    public abstract void getComponentFactory();

    public void showImagePickerDialog() {
        ImagePickerDialog imagePickerDialog = ImagePickerDialog.newInstance();
        imagePickerDialog.show(getFragmentManager() ,getClass().getSimpleName());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("tagb ", "onRequestPermissionsResult: ");
        if(requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            boolean allPermissionsGranted = true;
            for(int result : grantResults) {
                if(result != 0) {
                    allPermissionsGranted = false;
                }
            }

            if(!allPermissionsGranted) {
                onAnyPermissionDenied();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void showGoToSettings(){
        new AlertDialog.Builder(getContext())
                .setTitle("Closing application")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("No", null).show();
    }



    public abstract void onAllPermissionsGranted();

    public void onAnyPermissionDenied() {
        showGoToSettings();
    }

}
