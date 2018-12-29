package com.loconav.lookup.customcamera;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loconav.lookup.Callback;
import com.loconav.lookup.FragmentController;
import com.loconav.lookup.R;
import com.loconav.lookup.adapter.ImageSetterAdapter;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.databinding.FragmentCamerapickerBinding;

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
    private FragmentCamerapickerBinding fragmentCamerapickerBinding;
    private Context context;
    private Uri photoURI;
    private boolean isFlashOn=false;
    private String dimiss="true";
    private ArrayList<String> uriList=new ArrayList<>();
    private ArrayList<ImageUri> imageUris=new ArrayList<>();
    private ImageSetterAdapter imageSetterAdapter;
    private int camerId=0;
    private Camera.Parameters parameters;
    private int size=0;
    private boolean safeToTakePhoto=false;
    private FragmentController fragmentController=new FragmentController();
    //It is callback for focus
    private Camera.AutoFocusCallback mAutoFocusTakePictureCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                Log.i("tap_to_focus","success!");
            } else {
                Log.i("tap_to_focus","fail!");
            }
        }
    };
    // It is the callback which is called when the photo is clicked
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            int position=0;
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
            size=uriList.size();
            mCamera.startPreview();
            safeToTakePhoto=true;
        }
    };

    @Override
    public int setViewId() {
        return R.layout.fragment_camerapicker;
    }

    @Override
    public void onFragmentCreated() {
        EventBus.getDefault().register(this);
        Bundle bundle = this.getArguments();
        int limit = bundle.getInt("limit");
        String stringId=bundle.getString("Stringid");
        context=getContext();
        mCamera = getCameraInstance();
        setCameraDisplayOrientation(getActivity(),camerId);
        parameters =getPhotoParameters(mCamera,camerId);
        mCamera.setParameters(parameters);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
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

        //It is for auto focus when user touches the screen and it is only enable for rear camera
        fragmentCamerapickerBinding.cameraPreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(camerId==0)
                        mCamera.autoFocus(mAutoFocusTakePictureCallback);
                }
                return true;
            }
        });

        //It is used for capturing the images
        fragmentCamerapickerBinding.capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(safeToTakePhoto) {
                    safeToTakePhoto=false;
                    if (size < limit) {
                        mCamera.takePicture(null, null, mPicture);
                    } else {
                        Toast.makeText(context, "Sorry you cannot put more than " + limit + " images", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //for turn ON or OFF the Flash in camera
        fragmentCamerapickerBinding.flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCamera.getParameters().getSupportedFlashModes()!=null) {
                    if (isFlashOn) {
                        isFlashOn = false;
                        fragmentCamerapickerBinding.flash.setImageResource(R.drawable.noflash);
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    } else {
                        isFlashOn = true;
                        fragmentCamerapickerBinding.flash.setImageResource(R.drawable.flash);
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                    }
                    mCamera.setParameters(parameters);
                }
            }
        });

        //for switching the camera in front and back
        fragmentCamerapickerBinding.switchcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseCamera();
                safeToTakePhoto=false;
                if(camerId==0)
                    camerId=1;
                else if(camerId==1)
                    camerId=0;
                mCamera = Camera.open(camerId);
                mPreview = new CameraPreview(context, mCamera);
                setCameraDisplayOrientation(getActivity(),camerId);
                parameters =getPhotoParameters(mCamera,camerId);
                mCamera.setParameters(parameters);
                fragmentCamerapickerBinding.cameraPreview.addView(mPreview);
                try {
                    mCamera.setPreviewDisplay(mPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mCamera.startPreview();
                safeToTakePhoto=true;
            }
        });

        //it when the images are final and click the 'tick' button to proceed forward
        fragmentCamerapickerBinding.totalcorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(dimiss);
                EventBus.getDefault().post(new ImagePickerEvent(ImagePickerEvent.IMAGE_SELECTED_FROM_CAMERA+""+stringId, imageUris));
                getActivity().finish();
            }
        });

        mPreview = new CameraPreview(context,mCamera);
        fragmentCamerapickerBinding.cameraPreview.addView(mPreview);
        mCamera.startPreview();
        safeToTakePhoto=true;
    }

    @Override
    public void bindView(View view) {
        fragmentCamerapickerBinding = DataBindingUtil.bind(view);
    }

    private  File getOutputMediaFile(){
        File photoFile = null;
        try {
            photoFile = FileUtils.getImagefile(context);
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

    public  void setCameraDisplayOrientation(Activity activity,int cameraId) {
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
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        mCamera.setDisplayOrientation(result);
    }

    private void releaseCamera(){
        if (mCamera != null){
            fragmentCamerapickerBinding.cameraPreview.removeView(mPreview);
            mPreview.surfaceDestroyed(mPreview.getHolder());
            mPreview.getHolder().removeCallback(mPreview);
            mPreview.destroyDrawingCache();
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
        }
    }

    public Camera.Parameters getPhotoParameters(Camera mCamera,int camerId) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getImageUrl(String imageurl) {
        safeToTakePhoto=true;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCamera.release();
        EventBus.getDefault().unregister(this);
        fragmentCamerapickerBinding.unbind();
    }


}
