package com.loconav.lookup.customcamera;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.hardware.Camera;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.loconav.lookup.R;
import com.loconav.lookup.Toaster;
import com.loconav.lookup.adapter.RecycleCustomImageAdapter;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.databinding.FragmentCamerapickerBinding;
import com.loconav.lookup.dialog.FullImageDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static com.loconav.lookup.Constants.FILE_PROVIDER_AUTHORITY;
import static com.loconav.lookup.Constants.IMAGE_LIST;


public class CameraPickerFragment extends BaseFragment {
    private FragmentCamerapickerBinding binding;
    private Camera mCamera;
    private Camera.Parameters parameters;
    private boolean isFlashOn=false;
    private final ArrayList<ImageUri> imageList=new ArrayList<>();
    private RecycleCustomImageAdapter recycleCustomImageAdapter;
    private int limit;
    @Override
    public int setViewId() {
        return R.layout.fragment_camerapicker;
    }

    @Override
    public void onFragmentCreated() {
        limit= Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getInt("limit");
        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        CameraPreview mPreview = new CameraPreview(getContext(), mCamera);
        FrameLayout preview = binding.cameraPreview;
        preview.addView(mPreview);

        //set recyclerview adapter
        setImageAdapter();

        //Capture the photo and save it
        binding.capture.setOnClickListener(view -> {
            binding.capture.setClickable(false);
            if(imageList.size()<limit) {
                mCamera.takePicture(null, null, (bytes, camera) -> {
                    File pictureFile = null;
                    try {
                        pictureFile = ImageUtils.getImagefile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (pictureFile == null) {
                        Log.d("hey", "Error creating media file, check storage permissions");
                        return;
                    }

                    try {
                        FileOutputStream fos = new FileOutputStream(pictureFile);
                        fos.write(bytes);
                        fos.close();
                        mCamera.startPreview();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Settting ImageView List for original images list
                    ImageUri imageUri = new ImageUri();
                    imageUri.setUri(FileProvider.getUriForFile(getContext(), FILE_PROVIDER_AUTHORITY, pictureFile));
                    imageList.add(imageUri);
                    recycleCustomImageAdapter.notifyDataSetChanged();
                    binding.capture.setClickable(true);
                });
            }
            else
            {
                Toaster.makeToast(getString(R.string.size_limit)+limit);
            }
        });

        //It is for auto focus when user touches the screen and it is only enable for rear camera
        binding.cameraPreview.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mCamera.autoFocus((b, camera) -> {
                });
            }
            return true;
        });

        //for turn ON or OFF the Flash in camera
        binding.flash.setOnClickListener(v -> {
            if(mCamera!=null) {
                parameters = mCamera.getParameters();
                if (parameters!=null &&parameters.getSupportedFlashModes() != null) {
                    if (isFlashOn) {
                        isFlashOn = false;
                        binding.flash.setImageResource(R.drawable.noflash);
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    } else {
                        isFlashOn = true;
                        binding.flash.setImageResource(R.drawable.flash);
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                    }
                    mCamera.setParameters(parameters);
                }
            }
        });

        //When the Images are Final
        binding.totalcorrect.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            //As Uri is not parceble/Seriazable we have to convert it into string list
            ArrayList<String> imageListinString=new ArrayList<>();
            for(ImageUri uri:imageList)
                imageListinString.add(uri.getUri().toString());
            returnIntent.putExtra(IMAGE_LIST,imageListinString);
            getActivity().setResult(Activity.RESULT_OK,returnIntent);
            getActivity().finish();
        });
    }

    private void setImageAdapter()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleCustomImageAdapter=new RecycleCustomImageAdapter(imageList, new Callback() {
            @Override
            public void onEventDone(Object object) {
                ImageUri uri=(ImageUri) object;
                FullImageDialog fullImageDialog = FullImageDialog.newInstance(uri.getUri().toString());
                fullImageDialog.show(getActivity().getSupportFragmentManager(),getClass().getSimpleName());
            }
        },getContext());
        binding.rvImages.setLayoutManager(linearLayoutManager);
        binding.rvImages.setAdapter(recycleCustomImageAdapter);
    }


    private Camera getCameraInstance(){
        Camera camera = null;
        try {
            camera = Camera.open();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return camera;
    }

    @Override
    public void bindView(View view) {
        binding= DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.unbind();
        if(mCamera!=null) {
            mCamera.stopPreview();
            mCamera.release();
        }
    }

}
