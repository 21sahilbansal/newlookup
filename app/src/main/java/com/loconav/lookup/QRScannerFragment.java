package com.loconav.lookup;


import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.databinding.DataBindingUtil;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.databinding.FargmentQrScannerBinding;
import com.loconav.lookup.newfastag.model.NewFastagEvent;

import org.greenrobot.eventbus.EventBus;

import java.security.Policy;
import java.util.List;

import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;

import static android.arch.lifecycle.Lifecycle.Event.ON_RESUME;
import static android.hardware.Camera.Parameters.FLASH_MODE_ON;
import static com.loconav.lookup.Constants.DEVICE_ID;

/**
 * Created by prateek on 15/05/18.
 */

public class QRScannerFragment extends BaseFragment implements BarcodeRetriever {
    private FargmentQrScannerBinding binding;
    private String messageForQrScanner;
    private FloatingActionButton flashButton;
    private Camera mCamera;
    private Camera.Parameters parameters;
    private Boolean isFlashOn = false;
    private BarcodeCapture barcodeCapture;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int setViewId() {
        return R.layout.fargment_qr_scanner;
    }

    @Override
    public void onFragmentCreated() {
        this.messageForQrScanner = getArguments().getString(Constants.KEY_FOR_QRSCANNER);
        barcodeCapture = (BarcodeCapture) getChildFragmentManager().findFragmentById(R.id.barcode);
        barcodeCapture.setRetrieval(this);
        flashButton = binding.flashButton;
        flashButton.setOnClickListener(v -> {
            barcodeCapture.setShowFlash(true);
            mCamera = barcodeCapture.retrieveCamera();
            if(mCamera != null){
            parameters = mCamera.getParameters();}
            if (parameters != null && parameters.getSupportedFlashModes() != null) {
                if (isFlashOn) {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    flashButton.setImageResource(R.drawable.flash);
                    isFlashOn = false;
                } else {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    flashButton.setImageResource(R.drawable.noflash);
                    isFlashOn = true;
                }
                mCamera.setParameters(parameters);
            }
        });

    }

    @Override
    public void bindView(View view) {
        binding = DataBindingUtil.bind(view);
        Log.e("time ", "bindView: " + System.currentTimeMillis());
    }

    @Override
    public void getComponentFactory() {
    }

    private void sendMessage(String scannedDeviceId) {
        Log.d("testLog", "Broadcasting message");
        Intent intent = new Intent(messageForQrScanner);
        // You can also include some extra data.
        intent.putExtra(DEVICE_ID, scannedDeviceId);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onRetrieved(Barcode barcode) {
        if (messageForQrScanner.equals(Constants.NEW_SCANNED_FASTAG)) {
            Log.d("testLog", "sending event");
            EventBus.getDefault().post(new NewFastagEvent(NewFastagEvent.SCANNED_FASTAG, barcode.displayValue));
        }
        sendMessage(barcode.displayValue);
        getActivity().runOnUiThread(() -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onRetrievedMultiple(Barcode barcode, List<BarcodeGraphic> list) {
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {
    }

    @Override
    public void onRetrievedFailed(String s) {
    }

    @Override
    public void onPermissionRequestDenied() {
        if (getActivity() != null)
            getActivity().runOnUiThread(() -> getActivity().onBackPressed());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.unbind();

    }

}
