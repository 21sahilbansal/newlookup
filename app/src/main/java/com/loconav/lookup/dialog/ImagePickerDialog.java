package com.loconav.lookup.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loconav.lookup.GalleryEvents;
import com.loconav.lookup.ImagePickerEvent;
import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseDialogFragment;
import com.loconav.lookup.model.ImageUri;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by prateek on 09/07/18.
 */

public class ImagePickerDialog extends BaseDialogFragment {

    @BindView(R.id.camera) LinearLayout camera;
    @BindView(R.id.gallery) LinearLayout gallery;
    int REQUEST_CAMERA = 0,SELECT_FILE = 1;
    static String stringId;
    static int limit1;
    ArrayList<ImageUri> imagesUriArrayList=new ArrayList<>(); ;
    public static ImagePickerDialog newInstance(String id,int limit) {
        ImagePickerDialog imagePickerDialog = new ImagePickerDialog();
//        Bundle bundle=imagePickerDialog.getArguments();
//        stringId=bundle.getString("id");
//        limit1= bundle.getInt("limitImages");
        limit1=limit;
        stringId=id;
        return imagePickerDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View dialogView = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_image_picker, new LinearLayout(getActivity()),
                        false);
        ButterKnife.bind(this, dialogView);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
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
    public void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
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

    public void parsingGalleryImage(Intent data)  {
        if(data.getClipData()==null){
            ImageUri imageUri = new ImageUri();
            imageUri.setUri(data.getData());
            imagesUriArrayList.add(imageUri);
        }else {
            if(data.getClipData().getItemCount()<=limit1) {
                Log.e("clip3",""+data.getClipData().getItemCount());
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {

                    ImageUri imageUri = new ImageUri();
                    imageUri.setUri(data.getClipData().getItemAt(i).getUri());
                    imagesUriArrayList.add(imageUri);
                }
            }else {
                for (int i = 0; i <limit1; i++) {

                    ImageUri imageUri = new ImageUri();
                    imageUri.setUri(data.getClipData().getItemAt(i).getUri());
                    imagesUriArrayList.add(imageUri);
                }
                Toast.makeText(getContext(), "size limit upto "+limit1, Toast.LENGTH_SHORT).show();
            }
        }
        Log.e("SIZE", imagesUriArrayList.size() + ""+imagesUriArrayList);
    }

    public void parsingCameraImage(Intent data){
        Bitmap thumbnail = (Bitmap)data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        thumbnail=Bitmap.createScaledBitmap(thumbnail,thumbnail.getWidth()*3 , thumbnail.getHeight()*3, true);
      //  Log.e("w",""+thumbnail.getWidth()+"h"+thumbnail.getHeight());
        String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),thumbnail, "Title", null);
        ImageUri imageUri=new ImageUri();
        imageUri.setUri(Uri.parse(path));
        imagesUriArrayList=new ArrayList<>();
        imagesUriArrayList.add(imageUri);
    }
// Log.e("SIZE", imagesUriArrayList.size() + ""+imagesUriArrayList+"ssd"+siz);
//        if(data.getClipData()==null){
//        if(imagesUriArrayList.size()<limit1) {
//            ImageUri imageUri = new ImageUri();
//            imageUri.setUri(data.getData());
//            imagesUriArrayList.add(imageUri);
//        }
//    }else {
//        if((data.getClipData().getItemCount()+imagesUriArrayList.size())<=limit1) {
//            for (int i = 0; i < data.getClipData().getItemCount(); i++) {
//                ImageUri imageUri = new ImageUri();
//                imageUri.setUri(data.getClipData().getItemAt(i).getUri());
//                imagesUriArrayList.add(imageUri);
//            }
//        }else {
//            if(imagesUriArrayList.size()>0) {
//                for (int i = 0; i < (limit1 - imagesUriArrayList.size()); i++) {
//
//                    ImageUri imageUri = new ImageUri();
//                    imageUri.setUri(data.getClipData().getItemAt(i).getUri());
//                    imagesUriArrayList.add(imageUri);
//                }
//            }else {
//                for (int i = 0; i<limit1; i++) {
//
//                    ImageUri imageUri = new ImageUri();
//                    imageUri.setUri(data.getClipData().getItemAt(i).getUri());
//                    imagesUriArrayList.add(imageUri);
//                }
//            }
//            Toast.makeText(getContext(), "size limit upto "+limit1, Toast.LENGTH_SHORT).show();
//        }
//    }
//    siz=imagesUriArrayList.size();
//        Log.e("SIZE", imagesUriArrayList.size() + ""+imagesUriArrayList+"ssd"+siz);
//


}
