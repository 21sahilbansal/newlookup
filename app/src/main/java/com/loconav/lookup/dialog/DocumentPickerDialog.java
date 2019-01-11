package com.loconav.lookup.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loconav.lookup.Constants;
import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseDialogFragment;
import com.loconav.lookup.customcamera.CameraOpenActivity;
import com.loconav.lookup.customcamera.ImagePickerDialog;
import com.loconav.lookup.customcamera.ImagePickerEvent;
import com.loconav.lookup.customcamera.ImageUri;
import com.loconav.lookup.databinding.DialogImagePickerBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;

import static com.loconav.lookup.Constants.REQUEST_CODE;

public class DocumentPickerDialog extends BaseDialogFragment {
    private static final int REQUEST_CODE = 99;
    private DialogImagePickerBinding binding;
    private int SELECT_FILE = 1;
    private String stringId;
    private int limit;
    private ArrayList<ImageUri> imagesUriArrayList=new ArrayList<>();
    public static DocumentPickerDialog newInstance(String id, int limit) {
        DocumentPickerDialog imagePickerDialog = new DocumentPickerDialog();
        Bundle bundle=new Bundle();
        bundle.putString("id",id);
        bundle.putInt("limitImages",limit);
        imagePickerDialog.setArguments(bundle);
        return imagePickerDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View dialogView = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_image_picker, new LinearLayout(getActivity()),
                        false);
        binding= DataBindingUtil.bind(dialogView);

        stringId=getArguments().getString("id");
        limit=getArguments().getInt("limitImages");
        binding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();
            }
        });

        binding.gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryIntent();
            }
        });

        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (builder.getWindow() != null) {
            builder.getWindow()
                    .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        builder.setContentView(dialogView);
        return builder;
    }

    void galleryIntent() {
//        Intent intent= new Intent(getContext(), ScanActivity.class);
//        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, ScanConstants.OPEN_MEDIA);
//        startActivityForResult(intent, Constants.REQUEST_CODE);
    }
    public void cameraIntent()
    {
//        Intent intent= new Intent(getContext(), ScanActivity.class);
//        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, ScanConstants.OPEN_CAMERA);
//        startActivityForResult(intent,Constants.REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
//            EventBus.getDefault().post(uri.toString());
        }
        dismiss();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }
}

