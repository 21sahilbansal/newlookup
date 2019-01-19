package com.loconav.lookup.customcamera;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loconav.lookup.R;
import com.loconav.lookup.Toaster;
import com.loconav.lookup.base.BaseDialogFragment;
import com.loconav.lookup.databinding.DialogImagePickerBinding;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;

import static com.loconav.lookup.Constants.IMAGE_LIST;


public class ImagePickerDialog extends BaseDialogFragment {
    private DialogImagePickerBinding binding;
    private int SELECT_FILE = 1;
    private int CAMERA_FILE=2;
    private String stringId; //it is the name of custom image picker
    private int limit;
    String startCompression="started_compression";//for the progress bar to start in the custom image picker
    private ArrayList<ImageUri> imagesUriArrayList=new ArrayList<>();
    public static ImagePickerDialog newInstance(String id, int limit) {
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

    public void cameraIntent() {
        Bundle bundle=new Bundle();
        bundle.putInt("limit",limit);
        Intent i =new Intent(getContext(),CameraOpenActivity.class);
        i.putExtras(bundle);
        startActivityForResult(i,CAMERA_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            EventBus.getDefault().post(startCompression+stringId);
            if (requestCode == SELECT_FILE){
                parsingGalleryImage(data);
                Log.e("list size",""+imagesUriArrayList.size());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            imagesUriArrayList=ImageUtils.compressImageList(imagesUriArrayList,getContext());
                        } catch (IOException e) {
                            Toaster.makeToast(getString(R.string.images_not_compressed));
                        }
                        Log.e("sd",""+stringId);
                        EventBus.getDefault().post(new ImagePickerEvent(ImagePickerEvent.IMAGE_SELECTED_FROM_GALLERY+""+stringId, imagesUriArrayList));
                    }
                }).start();
            }
            if (requestCode == CAMERA_FILE){
                Log.e("list size",""+imagesUriArrayList.size());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //We get the list of string as uri is non seriazable object so then we again convert it into list of uri
                        ArrayList<ImageUri> imageUris=new ArrayList<>();
                        for(String s: (ArrayList<String>)data.getExtras().get(IMAGE_LIST))
                        {
                            ImageUri imageUri=new ImageUri();
                            imageUri.setUri(Uri.parse(s));
                            imageUris.add(imageUri);}
                        try {
                            imagesUriArrayList=ImageUtils.compressImageList(imageUris,getContext());
                        } catch (IOException e) {
                            Toaster.makeToast(getString(R.string.images_not_compressed));
                        }
                        Log.e("sd",""+stringId);
                        EventBus.getDefault().post(new ImagePickerEvent(ImagePickerEvent.IMAGE_SELECTED_FROM_CAMERA+""+stringId, imagesUriArrayList));
                    }
                }).start();
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
                Toaster.makeToast(getString(R.string.size_limit)+limit);
            }
        }
        Log.e("SIZE", imagesUriArrayList.size() + ""+imagesUriArrayList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }

}
