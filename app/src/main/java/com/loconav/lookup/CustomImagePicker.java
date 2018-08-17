package com.loconav.lookup;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.loconav.lookup.adapter.RecycleGridAdapter;
import com.loconav.lookup.dialog.ImagePickerDialog;
import com.loconav.lookup.model.ImageUri;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sejal on 12-07-2018.
 */

public class CustomImagePicker extends LinearLayout{
    private ArrayList<ImageUri> imageUris = new ArrayList<>();
    List<ImageUri>ll=new ArrayList<>();
    private ImageView iv;
    private String idText;
    public int limit;
    RecycleGridAdapter adapter;
    TypedArray a;

    public CustomImagePicker(Context context) {
        super(context, null);
    }
    public CustomImagePicker(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

         a= context.obtainStyledAttributes(attrs,
                R.styleable.Options, 0, 0);
        String titleText = a.getString(R.styleable.Options_titleText);
        idText = a.getString(R.styleable.Options_id);
        limit = a.getInt(R.styleable.Options_limitImages,4);
            if(limit==0) {
              throw new RuntimeException();
            }
        a.recycle();
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_image_picker, this, true);

        TextView title=findViewById(R.id.devText1);
        title.setText(titleText);
        iv=findViewById(R.id.devImage1);
        android.support.v4.app.FragmentActivity fragmentActivity = (android.support.v4.app.FragmentActivity) getContext();
        final FragmentManager fm = fragmentActivity.getSupportFragmentManager();
         iv.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View view) {
                 ImagePickerDialog imagePickerDialog=ImagePickerDialog.newInstance(idText,limit);
                 imagePickerDialog.show(fm,getClass().getSimpleName());
             }
         });

        EventBus.getDefault().register(this);
        setPhotoAdapter();

    }

    public CustomImagePicker(final Context context, String titleText, final int limit1, final String idText1) {
        super(context);
        limit=limit1;
        idText=idText1;
        if(limit==0) {
            throw new RuntimeException();
        }
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_image_picker, this, true);

        TextView title=findViewById(R.id.devText1);
        title.setText(titleText);
        iv=findViewById(R.id.devImage1);
        android.support.v4.app.FragmentActivity fragmentActivity = (android.support.v4.app.FragmentActivity) getContext();
        final FragmentManager fm = fragmentActivity.getSupportFragmentManager();
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePickerDialog imagePickerDialog=ImagePickerDialog.newInstance(idText,limit);
                imagePickerDialog.show(fm,getClass().getSimpleName());
            }
        });

        EventBus.getDefault().register(this);
        setPhotoAdapter();
    }

    private void setPhotoAdapter() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3,GridLayoutManager.VERTICAL,false);
        layoutManager.setInitialPrefetchItemCount(4);
        RecyclerView recyclerImages=findViewById(R.id.rvImages);
        recyclerImages.setLayoutManager(layoutManager);
        adapter = new RecycleGridAdapter(imageUris,iv,limit, new Callback() {
            @Override
            public void onEventDone(Object object) {
            }
        });
        recyclerImages.setAdapter(adapter);
        recyclerImages.setNestedScrollingEnabled(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getImagePickingEvents(ImagePickerEvent event) {
        String message = event.getMessage();
        String str4=ImagePickerEvent.IMAGE_SELECTED_FROM_CAMERA+idText;
        String str2=ImagePickerEvent.IMAGE_SELECTED_FROM_GALLERY+idText;
        Log.e("gdyfg",""+message+" mes str4"+str4);
        if (message.equals(str4)) {
            ll.clear();
            ll.addAll((List<ImageUri>) event.getObject());
            Log.e("size34", "" + ll.size());
            for (int i = 0; i < ll.size(); i++) {
                if (imageUris.size() < limit) {
                    this.imageUris.add(i, ll.get(i));
                }
            }
            checkLimit(imageUris);
            adapter.notifyDataSetChanged();
            Log.e("size", "" + imageUris);
        } else if (message.equals(str2)) {
            ll.clear();
            ll.addAll((List<ImageUri>) event.getObject());
            Log.e("size34", "" + ll.size());
            for (int i = 0; i < ll.size(); i++) {
                if (imageUris.size() < limit) {
                    this.imageUris.add(i, ll.get(i));
                }
            }
            checkLimit(imageUris);
            adapter.notifyDataSetChanged();
            Log.e("size", "" + imageUris);
        }
    }
    public void checkLimit(List<ImageUri> imageUri){
        if(imageUri.size()>=limit){
            iv.setVisibility(GONE);
        }else {
            iv.setVisibility(VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("vdgv","unregister");
        EventBus.getDefault().unregister(this);

    }
    public  ArrayList<ImageUri> getimagesList(){
        return imageUris;
    }


}
