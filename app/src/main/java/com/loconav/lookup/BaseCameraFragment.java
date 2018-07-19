package com.loconav.lookup;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sejal on 06-07-2018.
 */

public abstract class BaseCameraFragment extends BaseFragment {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    boolean checkResult=true;

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
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public abstract int setViewId();

    public void onFragmentCreated() {
    }

    public abstract void bindView(View view);

    public abstract void getComponentFactory();

        public void showImagePickerDialog() {
        ImagePickerDialog imagePickerDialog = ImagePickerDialog.newInstance();
        imagePickerDialog.show(getFragmentManager() ,getClass().getSimpleName());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("tagb ", "onRequestPermissionsResult: " + grantResults[1]);
        for (int grantResult : grantResults) {
            if (grantResult != 0) {
                checkResult=false;
            }
        }
        if(!checkResult){
            showRequestDenied();
        }else {
            showRequestAccepted();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected abstract void showRequestAccepted();

    private void showRequestDenied() {
        showdialogbox();
    }

    void showdialogbox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
        builder.setTitle("Permission necessary");
        builder.setMessage("Accessing gallery and camera permissions are necessary");
        builder.setPositiveButton(R.string.permissions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getActivity().getPackageName()));
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}