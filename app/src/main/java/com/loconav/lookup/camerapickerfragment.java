package com.loconav.lookup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.LinearLayout;

import com.loconav.lookup.adapter.ImageSetterAdapter;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.databinding.FragmentCamerapickerBinding;
import com.loconav.lookup.model.ImageUri;
import com.loconav.lookup.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class camerapickerfragment extends BaseFragment{
    private Camera mCamera;
    private CameraPreview mPreview;
    FragmentCamerapickerBinding fragmentCamerapickerBinding;
    String mPhoto;
    Context context;
    Uri photoURI;
    List<String> uriList=new ArrayList<>();
    List<ImageUri> imageUris=new ArrayList<>();
    ImageSetterAdapter imageSetterAdapter;
    private boolean cameraFront = false;
    int camerId=0;
    Camera.Parameters parameters;
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d("tag", "Error creating media file, check storage permissions");
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("tag", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("tag", "Error accessing file: " + e.getMessage());
            }
        }
    };
    @Override
    public int setViewId() {
        return R.layout.fragment_camerapicker;
    }
    @Override
    public void onFragmentCreated() {
        context=getContext();
        mCamera = getCameraInstance();
        setCameraDisplayOrientation(getActivity(),camerId);
        parameters =getParameters(mCamera);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayout.HORIZONTAL);
        imageSetterAdapter = new ImageSetterAdapter(getContext(),uriList);
        fragmentCamerapickerBinding.rvImages.setLayoutManager(layoutManager);
        fragmentCamerapickerBinding.rvImages.setAdapter(imageSetterAdapter);

        mCamera.setParameters(parameters);
        // Create our Preview view and set it as the content of our activity.
        fragmentCamerapickerBinding.capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null,null,mPicture);
                fragmentCamerapickerBinding.capture.setVisibility(View.INVISIBLE);
                fragmentCamerapickerBinding.flash.setVisibility(View.INVISIBLE);
                fragmentCamerapickerBinding.totalcorrect.setVisibility(View.INVISIBLE);
                fragmentCamerapickerBinding.switchcamera.setVisibility(View.INVISIBLE);
                fragmentCamerapickerBinding.correct.setVisibility(View.VISIBLE);
                fragmentCamerapickerBinding.cancel.setVisibility(View.VISIBLE);
            }
        });
        fragmentCamerapickerBinding.correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUri uri=new ImageUri();
                uri.setUri(photoURI);
                imageUris.add(uri);
                uriList.add(photoURI.toString());
                imageSetterAdapter.images=uriList;
                imageSetterAdapter.notifyDataSetChanged();
                fragmentCamerapickerBinding.capture.setVisibility(View.VISIBLE);
                fragmentCamerapickerBinding.totalcorrect.setVisibility(View.VISIBLE);
                fragmentCamerapickerBinding.flash.setVisibility(View.VISIBLE);
                fragmentCamerapickerBinding.switchcamera.setVisibility(View.VISIBLE);
                fragmentCamerapickerBinding.correct.setVisibility(View.INVISIBLE);
                fragmentCamerapickerBinding.cancel.setVisibility(View.INVISIBLE);
                mCamera.startPreview();
            }
        });
        fragmentCamerapickerBinding.switchcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseCamera();
                if(camerId==0)
                { camerId=1;
                    mCamera=Camera.open(camerId);
                    Camera.Parameters parameters=getParameters(mCamera);
                    mCamera.setParameters(parameters);
                    mPreview = new CameraPreview(getContext(),mCamera);
                    fragmentCamerapickerBinding.cameraPreview.addView(mPreview);
                }
            }
        });
        fragmentCamerapickerBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCamerapickerBinding.capture.setVisibility(View.VISIBLE);
                fragmentCamerapickerBinding.flash.setVisibility(View.VISIBLE);
                fragmentCamerapickerBinding.totalcorrect.setVisibility(View.VISIBLE);
                fragmentCamerapickerBinding.switchcamera.setVisibility(View.VISIBLE);
                fragmentCamerapickerBinding.correct.setVisibility(View.INVISIBLE);
                fragmentCamerapickerBinding.cancel.setVisibility(View.INVISIBLE);
                mCamera.startPreview();
            }
        });

        mPreview = new CameraPreview(getContext(),mCamera);
        fragmentCamerapickerBinding.cameraPreview.addView(mPreview);
    }
    @Override
    public void bindView(View view) {
        fragmentCamerapickerBinding = DataBindingUtil.bind(view);
    }
    private  File getOutputMediaFile(int type){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = FileUtils.getImagefile(context);
            mPhoto = photoFile.getAbsolutePath();
        } catch (Exception ex) {
        }

        if (photoFile != null) {
             photoURI = FileProvider.getUriForFile(context,
                    "com.lookuploconav.lookup",
                    photoFile);
        }
        return photoFile;
    }
    @Override
    public void getComponentFactory() {
    }
    protected Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(0);
        } catch (Exception e){
        }
        return c;
    }
    public  void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        mCamera.setDisplayOrientation(result);
    }
    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
    public Camera.Parameters getParameters(Camera mCamera)
    {
        Camera.Parameters params = mCamera.getParameters();
        params.set("orientation", "portrait");
        params.set("rotation",90);
        List<Camera.Size> allSizes = params.getSupportedPictureSizes();
        Camera.Size size = allSizes.get(0); // get top size
        for (int i = 0; i < allSizes.size(); i++) {
            if (allSizes.get(i).width > size.width)
                size = allSizes.get(i);
        }
        params.setPictureSize(size.width, size.height);
        return  params;
    }



}
