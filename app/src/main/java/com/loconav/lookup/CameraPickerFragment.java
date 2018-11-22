package com.loconav.lookup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loconav.lookup.adapter.ImageSetterAdapter;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.databinding.FragmentCamerapickerBinding;
import com.loconav.lookup.databinding.NewinstallationBinding;
import com.loconav.lookup.model.ImageUri;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class CameraPickerFragment extends BaseFragment{
    private Camera mCamera;
    private CameraPreview mPreview;
    FragmentCamerapickerBinding fragmentCamerapickerBinding;
    String mPhoto;
    Context context;
    Uri photoURI;
    boolean isFlashOn=false;
    int position=0;
    /**
     * uriList is the list of string of uris of images
     */
    /**
     * imageUris is the list of ImageUris
     */
    ArrayList<String> uriList=new ArrayList<>();
    ArrayList<ImageUri> imageUris=new ArrayList<>();
    ImageSetterAdapter imageSetterAdapter;
    int camerId=0;
    Camera.Parameters parameters;
    int size=0;
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
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
        Bundle bundle = this.getArguments();
        int limit = bundle.getInt("limit");
        String stringId=bundle.getString("Stringid");
        context=getContext();
        mCamera = getCameraInstance();
        setCameraDisplayOrientation(getActivity(),camerId);
        parameters =getParameters(mCamera,camerId);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
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

                if(size<limit) {
                    mCamera.takePicture(null, null, mPicture);
                    fragmentCamerapickerBinding.capture.setVisibility(View.INVISIBLE);
                    fragmentCamerapickerBinding.flash.setVisibility(View.INVISIBLE);
                    fragmentCamerapickerBinding.totalcorrect.setVisibility(View.INVISIBLE);
                    fragmentCamerapickerBinding.switchcamera.setVisibility(View.INVISIBLE);
                    fragmentCamerapickerBinding.correct.setVisibility(View.VISIBLE);
                    fragmentCamerapickerBinding.cancel.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(getContext(), "Sorry you cannot put more than "+limit+" images", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fragmentCamerapickerBinding.correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=uriList.size();
                ImageUri uri=new ImageUri();
                uri.setUri(photoURI);
                imageUris.add(uri);
                uriList.add(photoURI.toString());
                imageSetterAdapter.images=uriList;
                imageSetterAdapter.notifyItemChanged(position);
                fragmentCamerapickerBinding.capture.setVisibility(View.VISIBLE);
                fragmentCamerapickerBinding.totalcorrect.setVisibility(View.VISIBLE);
                fragmentCamerapickerBinding.flash.setVisibility(View.VISIBLE);
                fragmentCamerapickerBinding.switchcamera.setVisibility(View.VISIBLE);
                fragmentCamerapickerBinding.correct.setVisibility(View.INVISIBLE);
                fragmentCamerapickerBinding.cancel.setVisibility(View.INVISIBLE);
                size=uriList.size();
                mCamera.startPreview();
            }
        });
        fragmentCamerapickerBinding.flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFlashOn)
                {
                    isFlashOn=false;
                    fragmentCamerapickerBinding.flash.setImageResource(R.drawable.noflash);
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                }
                else
                {
                    isFlashOn=true;
                    fragmentCamerapickerBinding.flash.setImageResource(R.drawable.flash);
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                }
                mCamera.setParameters(parameters);
            }
        });
        fragmentCamerapickerBinding.switchcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseCamera();
                if(camerId==0)
                { camerId=1;
                }
                else
                if(camerId==1)
                {
                    camerId=0;
                }
                mCamera = Camera.open(camerId);
                mPreview = new CameraPreview(getContext(), mCamera);
                setCameraDisplayOrientation(getActivity(),camerId);
                parameters =getParameters(mCamera,camerId);
                mCamera.setParameters(parameters);
                fragmentCamerapickerBinding.cameraPreview.addView(mPreview);
                try {
                    mCamera.setPreviewDisplay(mPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mCamera.startPreview();
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
        fragmentCamerapickerBinding.totalcorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post( true);
                EventBus.getDefault().post(new ImagePickerEvent(ImagePickerEvent.IMAGE_SELECTED_FROM_CAMERA+""+stringId, imageUris));
                getActivity().finish();
            }
        });

        mPreview = new CameraPreview(getContext(),mCamera);
        fragmentCamerapickerBinding.cameraPreview.addView(mPreview);
    }
    @Override
    public void bindView(View view) {
        fragmentCamerapickerBinding = DataBindingUtil.bind(view);
    }
    private  File getOutputMediaFile(){
        File photoFile = null;
        try {
            photoFile = FileUtility.getImagefile(context);
            mPhoto = photoFile.getAbsolutePath();
        }
        catch (Exception ex)
        {
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
            mPreview.surfaceDestroyed(mPreview.getHolder());
            mPreview.getHolder().removeCallback(mPreview);
            mPreview.destroyDrawingCache();
            fragmentCamerapickerBinding.cameraPreview.removeView(mPreview);
            mCamera.stopPreview();
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
        }
    }
    public Camera.Parameters getParameters(Camera mCamera,int camerId)
    {
        Camera.Parameters params = mCamera.getParameters();
        params.set("orientation", "portrait");
        if(camerId==0)
        params.set("rotation",90);
        else if(camerId==1)
            params.set("rotation",270);
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
