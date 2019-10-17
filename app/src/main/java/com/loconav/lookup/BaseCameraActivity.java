package com.loconav.lookup;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sejal on 06-07-2018.
 */

public abstract class BaseCameraActivity extends AppCompatActivity {
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    protected  final int REQUEST_CHECK_SETTINGS = 0x1;
    private final List<String> listPermissionsNeeded = new ArrayList<>();
    protected final String TAG = getClass().getSimpleName();
    private static GoogleApiClient mGoogleApiClient;


    protected boolean checkAndRequestPermissions(Context context) {
        int write_storage = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read_storage = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        int camera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        int location2=ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION);

        if (read_storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (write_storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (location2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this ,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("tagb ", "onRequestPermissionsResult: ");
        if(requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            boolean allPermissionsGranted = true;
            Log.e("tab",""+permissions.length+grantResults.length);
            for(int result : grantResults) {
                if(result != 0) {
                    allPermissionsGranted = false;
                }
            }

            if(!allPermissionsGranted) {
                onAnyPermissionDenied();
            }else{
                onAllPermissionsGranted();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showGoToSettings(){
        new AlertDialog.Builder(BaseCameraActivity.this)
                .setTitle(getString(R.string.closing_application))
                .setMessage(getString(R.string.want_to_exit))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    Activity a=BaseCameraActivity.this;
                    a.finish();
                }).setNegativeButton(getString(R.string.no), (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }).show();
    }

    protected abstract void onAllPermissionsGranted();

    private void onAnyPermissionDenied() {
        showGoToSettings();
    }


}
