package com.loconav.lookup.customcamera;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

import static com.loconav.lookup.Constants.ORIENTATION;

@SuppressWarnings("deprecation")
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String PORTRAIT = "portrait";
    private static final String LANDSCAPE = "landscape";
    private final SurfaceHolder mHolder;
    private final Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setFixedSize(100,100);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if( mCamera!=null && mCamera.getParameters()!=null) {
            Camera.Parameters parameters = mCamera.getParameters();
            //display resolution
            List<Camera.Size> allSizes = parameters.getSupportedPictureSizes();
            Camera.Size size = allSizes.get(0); // get top size
            for (int i = 0; i < allSizes.size(); i++) {
                if (allSizes.get(i).width > size.width)
                    size = allSizes.get(i);
                }

            //Change the Orientation
            if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                parameters.set(ORIENTATION, PORTRAIT);
                mCamera.setDisplayOrientation(90);
                parameters.setRotation(90);
            } else {
                parameters.set(ORIENTATION, LANDSCAPE);
                mCamera.setDisplayOrientation(0);
                parameters.setRotation(0);
            }
            parameters.setPictureSize(size.width, size.height);
            mCamera.setParameters(parameters);
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.startPreview();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
        //never release your mCamera instance here otherwise when the activity is resumed then there will be a crash
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d("ds", "Error starting camera preview: " + e);
        }
    }
}