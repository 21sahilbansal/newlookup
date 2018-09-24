package com.loconav.lookup.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loconav.lookup.FileUtility;
import com.loconav.lookup.GalleryEvents;
import com.loconav.lookup.ImagePickerEvent;
import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseDialogFragment;
import com.loconav.lookup.databinding.DialogImagePickerBinding;
import com.loconav.lookup.model.ImageUri;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

/**
 * Created by prateek on 09/07/18.
 */

public class ImagePickerDialog extends BaseDialogFragment {
    private DialogImagePickerBinding binding;
    private int REQUEST_CAMERA = 0,SELECT_FILE = 1;
    private String stringId;
    private int limit;
    String mCurrentPhotoPath;
    private ArrayList<ImageUri> imagesUriArrayList=new ArrayList<>();
    public static ImagePickerDialog newInstance(String id,int limit) {
        ImagePickerDialog imagePickerDialog = new ImagePickerDialog();
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
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    public void cameraIntent()
    {
        setimage();
    }
    private void setimage()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = FileUtility.getfile(getActivity());
            mCurrentPhotoPath = photoFile.getAbsolutePath();
        } catch (Exception ex) {
        }

        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(getActivity(),
                    "com.example.android.fileproviderloconav",
                    photoFile);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_CAMERA);

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE){
                parsingGalleryImage(data);
                Log.e("list size",""+imagesUriArrayList.size());
                EventBus.getDefault().post(new ImagePickerEvent(ImagePickerEvent.IMAGE_SELECTED_FROM_GALLERY+""+stringId, imagesUriArrayList));
            }
            else if (requestCode == REQUEST_CAMERA) {
                parsingCameraImage(data);
                EventBus.getDefault().post(new ImagePickerEvent(ImagePickerEvent.IMAGE_SELECTED_FROM_CAMERA+""+stringId, imagesUriArrayList));

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        dismiss();
    }

    public void parsingGalleryImage(final Intent data)  {
        data.getExtras();
        if(data.getClipData()==null){
            ImageUri imageUri = new ImageUri();
            imageUri.setUri(data.getData());
            imagesUriArrayList.add(imageUri);
        }else {
            if(data.getClipData().getItemCount()<=limit) {
                Log.e("clip3",""+data.getClipData().getItemCount());
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {

                    ImageUri imageUri = new ImageUri();
                    imageUri.setUri(data.getClipData().getItemAt(i).getUri());
                    imagesUriArrayList.add(imageUri);
                }
            }else {
                for (int i = 0; i <limit; i++) {
                    ImageUri imageUri = new ImageUri();
                    imageUri.setUri(data.getClipData().getItemAt(i).getUri());
                    imagesUriArrayList.add(imageUri);
                }
                Toast.makeText(getContext(), "size limit upto "+limit, Toast.LENGTH_SHORT).show();
            }
        }
        Log.e("SIZE", imagesUriArrayList.size() + ""+imagesUriArrayList);
    }


    public void parsingCameraImage(Intent data)
    {

        File destination = new File(mCurrentPhotoPath);
        ImageUri imageUri=new ImageUri();
        imageUri.setUri(( Uri.fromFile(destination)));
        imagesUriArrayList=new ArrayList<>();
        imagesUriArrayList.add(imageUri);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }
}
