package com.loconav.lookup.customcamera;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.loconav.lookup.R;
import com.loconav.lookup.Toaster;
import com.loconav.lookup.base.BaseDialogFragment;
import com.loconav.lookup.databinding.DialogImagePickerBinding;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;

import static com.loconav.lookup.Constants.ALREADY_TAKEN_IMAGES;
import static com.loconav.lookup.Constants.ID;
import static com.loconav.lookup.Constants.IMAGE_LIST;
import static com.loconav.lookup.Constants.LIMIT_IMAGES;
import static com.loconav.lookup.Constants.STARTED_COMPRESSION;


public class ImagePickerDialog extends BaseDialogFragment {
    public static final String ALREADY_TAKEN_PHOTOS = "alreadyTakenPhotos";
    public static final String LIMIT = "limit";
    private DialogImagePickerBinding binding;
    private final int SELECT_FILE = 1;
    private final int CAMERA_FILE=2;
    private String stringId; //it is the name of custom image picker
    private int limit;
    private int alreadyTakenPhotos;
    private final String startCompression=STARTED_COMPRESSION;//for the progress bar to start in the custom image picker
    private ArrayList<ImageUri> imagesUriArrayList=new ArrayList<>();
    public static ImagePickerDialog newInstance(String id, int limit,int alreadyTakenPhotos) {
        ImagePickerDialog imagePickerDialog = new ImagePickerDialog();
        Bundle bundle=new Bundle();
        bundle.putString(ID,id);
        bundle.putInt(LIMIT_IMAGES,limit);
        bundle.putInt(ALREADY_TAKEN_IMAGES,alreadyTakenPhotos);
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

        stringId=getArguments().getString(ID);
        limit=getArguments().getInt(LIMIT_IMAGES);
        alreadyTakenPhotos=getArguments().getInt(ALREADY_TAKEN_IMAGES);

        binding.camera.setOnClickListener(v -> cameraIntent());

        binding.gallery.setOnClickListener(v -> galleryIntent());

        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (builder.getWindow() != null) {
            builder.getWindow()
                    .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        builder.setContentView(dialogView);
        return builder;
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent() {
        Bundle bundle=new Bundle();
        bundle.putInt(LIMIT,limit);
        bundle.putInt(ALREADY_TAKEN_PHOTOS,alreadyTakenPhotos);
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
                new Thread(() -> {
                    try {
                        imagesUriArrayList=ImageUtils.compressImageList(imagesUriArrayList);
                    } catch (IOException e) {
                        Toaster.makeToast(getString(R.string.images_not_compressed));
                    }
                    Log.e("sd",""+stringId);
                    EventBus.getDefault().post(new ImagePickerEvent(ImagePickerEvent.IMAGE_SELECTED_FROM_GALLERY+""+stringId, imagesUriArrayList));
                }).start();
            }
            if (requestCode == CAMERA_FILE){
                Log.e("list size",""+imagesUriArrayList.size());
                new Thread(() -> {
                    //We get the list of string as uri is non seriazable object so then we again convert it into list of uri
                    ArrayList<ImageUri> imageUris=new ArrayList<>();
                    for(String s: (ArrayList<String>)data.getExtras().get(IMAGE_LIST))
                    {
                        ImageUri imageUri=new ImageUri();
                        imageUri.setUri(Uri.parse(s));
                        imageUris.add(imageUri);}
                    try {
                        imagesUriArrayList=ImageUtils.compressImageList(imageUris);
                    } catch (IOException e) {
                        Toaster.makeToast(getString(R.string.images_not_compressed));
                    }
                    Log.e("sd",""+stringId);
                    EventBus.getDefault().post(new ImagePickerEvent(ImagePickerEvent.IMAGE_SELECTED_FROM_CAMERA+""+stringId, imagesUriArrayList));
                }).start();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        dismiss();
    }

    private void parsingGalleryImage(final Intent data)  {
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
