package com.loconav.lookup;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.databinding.FragmentDeviceIdBinding;
import com.loconav.lookup.model.FastTagResponse;
import com.loconav.lookup.model.LookupResponse;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.utils.AppUtils;

import retrofit2.Call;
import retrofit2.Response;

import static com.loconav.lookup.Constants.DEVICE_ID;
import static com.loconav.lookup.Constants.LOOKUP_RESPONSE;
import static com.loconav.lookup.Constants.MESSENGER_SCANNED_ID;
import static com.loconav.lookup.Constants.USER_ID;

/**
 * Created by sejal on 28-06-2018.
 */

public class DeviceIdFragment extends BaseTitleFragment {
    private FragmentDeviceIdBinding binding;
    private final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    private ProgressDialog progressDialog;
    private SharedPrefHelper sharedPrefHelper;
    private PassingReason passingReason;
    private final FragmentController fragmentController = new FragmentController();
    private boolean fastagSelection;

    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(DEVICE_ID);
            Log.d("receiver", "Got message: " + message);
            binding.etDeviceId.setText(message);
            binding.etDeviceId.setSelection(binding.etDeviceId.getText().length());
        }
    };

    @Override
    public int setViewId() {
        return R.layout.fragment_device_id;
    }

    @Override
    public void onFragmentCreated() {
        passingReason = ((LookupSubActivity) getActivity()).getPassingReason();
        if (passingReason.getReasonResponse().getId() != 37) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Enter device ID");
            enterDeviceId();
        } else {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Fastag Installation");
            fastagInstallation();
        }
        registerBroadcast();
        checkAndShowUserIdDialog();
    }

    private void enterDeviceId() {
        binding.etDeviceId.setHint("Enter device id");
        fastagSelection = false;
        commonFunction();
    }

    private void fastagInstallation() {
        binding.etDeviceId.setHint("Enter truck number");
        fastagSelection = true;
        commonFunction();
    }

    private void commonFunction() {
        initSharedPf();
        setScanner();
        initProgressDialog();
        setInfoButton();
    }

    private void initSharedPf() {
        sharedPrefHelper = SharedPrefHelper.getInstance();
    }

    private void checkAndShowUserIdDialog() {
        if (!isUserIdSet())
            showEnterIdDialog();
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);
    }

    private void setInfoButton() {
        binding.btGetInfo.setOnClickListener(view -> {
            String deviceId = binding.etDeviceId.getText().toString().trim();
            if (!deviceId.isEmpty()) {
                if (AppUtils.isNetworkAvailable()) {
                    progressDialog.show();
                    if (!fastagSelection) {
                        apiService.getDeviceLookup(binding.etDeviceId.getText().toString()).enqueue(new RetrofitCallback<LookupResponse>() {
                            @Override
                            public void handleSuccess(Call<LookupResponse> call, Response<LookupResponse> response) {
                                if (getActivity() != null)
                                    AppUtils.hideKeyboard(getActivity());
                                Log.e("handle ", response.code() + "");
                                DeviceDetailFragment deviceDetailFragment = new DeviceDetailFragment();
                                passingReason.setDeviceid(binding.etDeviceId.getText().toString());
                                ((LookupSubActivity) getActivity()).setPassingReason(passingReason);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable(LOOKUP_RESPONSE, response.body());
                                deviceDetailFragment.setArguments(bundle);
                                fragmentController.loadFragment(deviceDetailFragment, getFragmentManager(), R.id.frameLayout, true);
                                progressDialog.dismiss();
                            }

                            @Override
                            public void handleFailure(Call<LookupResponse> call, Throwable t) {
                                progressDialog.dismiss();
                                Toaster.makeToast(t.getMessage());
                            }
                        });
                    } else {
                         String truckId =binding.etDeviceId.getText().toString().toUpperCase();
                        apiService.validateTruckNumberOrFastagNumber(truckId).enqueue(new RetrofitCallback<FastTagResponse>() {
                            @Override
                            protected void handleSuccess(Call<FastTagResponse> call, Response<FastTagResponse> response) {
                                setFastagPhotoFrag(response);
                            }

                            @Override
                            protected void handleFailure(Call<FastTagResponse> call, Throwable t) {
                                Toaster.makeToast(t.getMessage());
                                progressDialog.dismiss();
                            }
                        });
                    }
                } else
                    Toaster.makeToast(getString(R.string.internet_not_available));
            } else {
                if (fastagSelection) {
                    Toaster.makeToast(getString(R.string.truckno_cant_be_empty));
                } else {
                    Toaster.makeToast(getString(R.string.device_cant_be_empty));
                }
            }

        });
        binding.fastTag.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), FastTagActivity.class);
            startActivity(intent);
        });
    }

    private void setFastagPhotoFrag(Response<FastTagResponse> response) {
        if (getActivity() != null)
            AppUtils.hideKeyboard(getActivity());
        FastTagPhotosFragment fastTagPhotosFragment = new FastTagPhotosFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Truck_No", response.body().getTruckNumber());
        bundle.putString("FastTag_Serial_No", response.body().getFastagSerialNumber());
        bundle.putInt("Installation_Id", response.body().getId());
        fastTagPhotosFragment.setArguments(bundle);
        fragmentController.loadFragment(fastTagPhotosFragment, getFragmentManager(), R.id.frameLayout, true);
        progressDialog.dismiss();
    }

    private void setScanner() {
        binding.ibQrScanner.setOnClickListener(view -> {
            if (getActivity() != null)
                AppUtils.hideKeyboard(getActivity());
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_FOR_QRSCANNER,Constants.MESSENGER_SCANNED_ID);
            QRScannerFragment qrScannerFragment = new QRScannerFragment();
            qrScannerFragment.setArguments(bundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, qrScannerFragment).addToBackStack("qr_scanner");
            transaction.commit();
        });
    }

    private void registerBroadcast() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter(MESSENGER_SCANNED_ID));
    }

    private void unRegisterBroadcastReceiver() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterBroadcastReceiver();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.unbind();
    }

    private void showEnterIdDialog() {
        final EditText input = new EditText(getActivity());
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Your Phone Number");
        builder.setPositiveButton("OK", null);
        builder.setView(input);
        builder.setCancelable(false);
        final AlertDialog mAlertDialog = builder.create();
        mAlertDialog.setOnShowListener(dialog -> {
            Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            b.setTextColor(Color.BLACK);
            b.setOnClickListener(view -> {
                if (!input.getText().toString().trim().equals("")) {
                    sharedPrefHelper.setStringData(USER_ID, input.getText().toString());
                    mAlertDialog.cancel();
                } else
                    Toaster.makeToast(getString(R.string.user_cant_empty));
            });
        });
        mAlertDialog.show();
    }

    private boolean isUserIdSet() {
        return !sharedPrefHelper.getStringData(USER_ID).equals("");
    }

    @Override
    public void bindView(View view) {
        binding = DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {
    }

    @Override
    public String getTitle() {
        return "Enter device ID";
    }

}
