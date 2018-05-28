package com.loconav.lookup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import butterknife.ButterKnife;
import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;

import static com.loconav.lookup.Constants.DEVICE_ID;
import static com.loconav.lookup.Constants.MESSENGER_SCANNED_ID;

/**
 * Created by prateek on 15/05/18.
 */

public class QRScannerFragment1 extends BaseFragment implements BarcodeRetriever {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    int setViewId() {
        return R.layout.fargment_qr_scanner1;
    }

    @Override
    void onFragmentCreated() {
        BarcodeCapture barcodeCapture = (BarcodeCapture) getChildFragmentManager().findFragmentById(R.id.barcode);
        barcodeCapture.setRetrieval(this);
    }

    @Override
    void bindView(View view) {
        ButterKnife.bind(this, view);
        Log.e("time ", "bindView: "+ System.currentTimeMillis());
    }

    @Override
    void getComponentFactory() {}

    private void sendMessage(String scannedDeviceId) {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent(MESSENGER_SCANNED_ID);
        // You can also include some extra data.
        intent.putExtra(DEVICE_ID, scannedDeviceId);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    @Override
    public void onRetrieved(Barcode barcode) {
        sendMessage(barcode.displayValue);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(getActivity()!=null) {
                    getActivity().onBackPressed();
                }
            }
        });
    }

    @Override
    public void onRetrievedMultiple(Barcode barcode, List<BarcodeGraphic> list) {}

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {}

    @Override
    public void onRetrievedFailed(String s) {}

    @Override
    public void onPermissionRequestDenied() {
        if(getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                    getActivity().onBackPressed();
            }});
    }

}