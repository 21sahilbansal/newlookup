package com.loconav.lookup;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loconav.lookup.adapter.ImageSetterAdapter;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.databinding.FragmentCamerapickerBinding;
import com.loconav.lookup.model.ImageUri;
import com.loconav.lookup.utils.FileUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CameraPickerFragment extends BaseFragment{
    private Camera mCamera;
    private CameraPreview mPreview;
    FragmentCamerapickerBinding fragmentCamerapickerBinding;
    String mPhoto;
    Context context;
    Uri photoURI;
    boolean isFlashOn=false;
    int position=0;
    String dimiss="true";
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
    FragmentController fragmentController=new FragmentController();
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
            position=uriList.size();
            ImageUri uri=new ImageUri();
            uri.setUri(photoURI);
            imageUris.add(uri);
            uriList.add(photoURI.toString());
            imageSetterAdapter.notifyItemChanged(position);
            fragmentCamerapickerBinding.capture.setVisibility(View.VISIBLE);
            fragmentCamerapickerBinding.totalcorrect.setVisibility(View.VISIBLE);
            fragmentCamerapickerBinding.flash.setVisibility(View.VISIBLE);
            fragmentCamerapickerBinding.switchcamera.setVisibility(View.VISIBLE);
            size=uriList.size();
            mCamera.startPreview();
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
        EventBus.getDefault().register(this);
        context=getContext();
        mCamera = getCameraInstance();
        setCameraDisplayOrientation(getActivity(),camerId);
        parameters =getParameters(mCamera,camerId);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayout.HORIZONTAL);
        imageSetterAdapter = new ImageSetterAdapter(uriList, new Callback() {
            @Override
            public void onEventDone(Object object) {
                FullImageFragment full_imageFragment =new FullImageFragment();
                Bundle bundle=new Bundle();
                bundle.putString("imageurl",(String)object);
                full_imageFragment.setArguments(bundle);
                fragmentController.loadFragment(full_imageFragment,getFragmentManager(),R.id.camera,true);
            }
        });
        fragmentCamerapickerBinding.rvImages.setLayoutManager(layoutManager);
        fragmentCamerapickerBinding.rvImages.setAdapter(imageSetterAdapter);
        mCamera.setParameters(parameters);
        // Create our Preview view and set it as the content of our activity.
        fragmentCamerapickerBinding.capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(size<limit) {
                    mCamera.takePicture(null, null, mPicture);
                }
                else
                {
                    Toast.makeText(getContext(), "Sorry you cannot put more than "+limit+" images", Toast.LENGTH_SHORT).show();
                }
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

        fragmentCamerapickerBinding.totalcorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventBus.getDefault().post(dimiss);
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
            photoFile = FileUtils.getImagefile(context);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCamera.release();
        EventBus.getDefault().unregister(this);
        fragmentCamerapickerBinding.unbind();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getImageUrl(String imageurl) {
        for(ImageUri imageUri:imageUris)
        {
            if(imageUri.getUri().toString().equals(imageurl))
            {
                imageUris.remove(imageUri);
                Log.e("remove","removed true");
                break;
            }
        }
        int index=uriList.indexOf(imageurl);
        uriList.remove(imageurl);
        Log.e("the index is ","the index is "+uriList.indexOf(imageurl));
        size--;
        imageSetterAdapter.notifyItemRemoved(index);
    }

}
