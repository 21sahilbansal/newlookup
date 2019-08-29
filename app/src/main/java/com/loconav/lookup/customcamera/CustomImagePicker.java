package com.loconav.lookup.customcamera;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loconav.lookup.R;
import com.loconav.lookup.Toaster;
import com.loconav.lookup.adapter.RecycleCustomImageAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.loconav.lookup.Constants.STARTED_COMPRESSION;

/**
 * Created by sejal on 12-07-2018.
 */

public class CustomImagePicker extends LinearLayout {
    private final ArrayList<ImageUri> originalImageUris = new ArrayList<>();
    private ProgressBar progressBar;
    //needed to differentiate different imagepickers that where inflated through custominflater so did it by textID
    public String textID;
    private String titleText;
    private int limit;
    private RecycleCustomImageAdapter recycleCustomImageAdapter;
    private TypedArray typedArrayCustom;
    private LinearLayout linearLayout;

    public CustomImagePicker(Context context) {
        super(context, null);
    }

    public CustomImagePicker(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        typedArrayCustom = context.obtainStyledAttributes(attrs,
                R.styleable.Options, 0, 0);
        titleText = typedArrayCustom.getString(R.styleable.Options_titleText);
        textID = typedArrayCustom.getString(R.styleable.Options_id);
        limit = typedArrayCustom.getInt(R.styleable.Options_limitImages, 4);
        if (limit == 0) {
            throw new RuntimeException();
        }
        typedArrayCustom.recycle();
        setItem(context);
    }

    private void setItem(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Objects.requireNonNull(inflater).inflate(R.layout.custom_image_picker, this, true);
        progressBar = findViewById(R.id.progress_circular);
        TextView title = findViewById(R.id.devText1);
        title.setText(titleText);
        linearLayout = findViewById(R.id.devImage1);
        android.support.v4.app.FragmentActivity fragmentActivity = (android.support.v4.app.FragmentActivity) getContext();
        final FragmentManager fm = fragmentActivity.getSupportFragmentManager();
        linearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (originalImageUris.size() == limit) {
                    Toaster.makeToast(getResources().getString(R.string.size_limit) + " "+limit + getResources().getString(R.string.images));

                } else {
                    ImagePickerDialog imagePickerDialog = ImagePickerDialog.newInstance(textID, limit, recycleCustomImageAdapter.getItemCount());
                    imagePickerDialog.show(fm, getClass().getSimpleName());
                }
            }
        });
        EventBus.getDefault().register(this);
        setPhotoAdapter();
    }

    public CustomImagePicker(final Context context, String titleText, final int limit1, final String idText1) {
        super(context);
        limit = limit1;
        textID = idText1;
        this.titleText = titleText;
        if (limit == 0) {
            throw new RuntimeException();
        }
        setItem(context);
    }

    private void setPhotoAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager.setInitialPrefetchItemCount(4);
        RecyclerView recyclerImages = findViewById(R.id.rvImages);
        recyclerImages.setLayoutManager(linearLayoutManager);
        recycleCustomImageAdapter = new RecycleCustomImageAdapter(originalImageUris, object -> {
        },this);
        recyclerImages.setAdapter(recycleCustomImageAdapter);
        recyclerImages.setNestedScrollingEnabled(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getImagePickingEvents(ImagePickerEvent event) {
        progressBar.setVisibility(GONE);
        String message = event.getMessage();
        String imageEventCamera = ImagePickerEvent.IMAGE_SELECTED_FROM_CAMERA + textID;
        String imageEventGallery = ImagePickerEvent.IMAGE_SELECTED_FROM_GALLERY + textID;
        String imageRemoved = ImagePickerEvent.IMAGE_REMOVED_AFTER_CAPTURING;
        List<ImageUri> resultLinkedList = new ArrayList<>();
        if (message.equals(imageEventCamera)) {
            resultLinkedList.clear();
            resultLinkedList.addAll((List<ImageUri>) event.getObject());
            if (originalImageUris.size() + resultLinkedList.size() > limit)
                Toaster.makeToast(getResources().getString(R.string.not_more_than) + limit + getResources().getString(R.string.images));
            if (resultLinkedList.size() + originalImageUris.size() >= limit) {
                linearLayout.setVisibility(View.GONE);
            }

            for (int i = 0; i < resultLinkedList.size(); i++) {

                if (originalImageUris.size() < limit) {
                    this.originalImageUris.add(i, resultLinkedList.get(i));
                }
            }
            recycleCustomImageAdapter.notifyDataSetChanged();
        }
        else if (message.equals(imageEventGallery)) {
            resultLinkedList.clear();
            resultLinkedList.addAll((List<ImageUri>) event.getObject());
            if (originalImageUris.size() + resultLinkedList.size() > limit)
                Toaster.makeToast(getResources().getString(R.string.not_more_than) + limit + getResources().getString(R.string.images));

            if (resultLinkedList.size() + originalImageUris.size() >= limit) {
                linearLayout.setVisibility(View.GONE);
            }
            for (int i = 0; i < resultLinkedList.size(); i++) {
                if (originalImageUris.size() < limit) {
                    this.originalImageUris.add(i, resultLinkedList.get(i));
                }
            }
            recycleCustomImageAdapter.notifyDataSetChanged();
        }
        else if(message.equals(imageRemoved) && event.getObject().equals(this)){

              this.linearLayout.setVisibility(VISIBLE);


        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isCompressionStarted(String startCompression) {
        if (startCompression.equals(STARTED_COMPRESSION + textID)) {
            progressBar.setVisibility(VISIBLE);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    public ArrayList<ImageUri> getimagesList() {
        return originalImageUris;
    }
}
