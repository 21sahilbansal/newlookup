package com.loconav.lookup.customcamera;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseDialogFragment;
import com.loconav.lookup.databinding.DialogImagePickerBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by prateek on 09/07/18.
 */

public class ImagePickerDialog extends BaseDialogFragment {
    private DialogImagePickerBinding binding;
    private int SELECT_FILE = 1;
    private String stringId;
    private int limit;
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
        EventBus.getDefault().register(this);
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
        Bundle bundle=new Bundle();
        bundle.putInt("limit",limit);
        bundle.putString("Stringid",stringId);
        Intent i =new Intent(getContext(),CameraOpenActivity.class);
        i.putExtras(bundle);
        startActivity(i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE){
                parsingGalleryImage(data);
                Log.e("list size",""+imagesUriArrayList.size());
                EventBus.getDefault().post(new ImagePickerEvent(ImagePickerEvent.IMAGE_SELECTED_FROM_GALLERY+""+stringId, imagesUriArrayList));
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getToDismiss(String dismiss) {
       if(dismiss.equals("true")) {
           Log.e("the dialog ","the dialog should dismiss");
           getDialog().dismiss();
       }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        binding.unbind();
    }
}
