package com.loconav.lookup;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.databinding.FragmentDeviceIdBinding;
import com.loconav.lookup.model.LookupResponse;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.utils.AppUtils;

import retrofit2.Call;
import retrofit2.Response;

import static com.loconav.lookup.Constants.DEVICE_ID;
import static com.loconav.lookup.Constants.MESSENGER_SCANNED_ID;
import static com.loconav.lookup.Constants.USER_ID;

/**
 * Created by sejal on 28-06-2018.
 */

public class DeviceIdFragment extends BaseTitleFragment {


    private FragmentDeviceIdBinding binding;
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    private ProgressDialog progressDialog;
    private SharedPrefHelper sharedPrefHelper;
    private PassingReason passingReason;
    FragmentController fragmentController = new FragmentController();
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
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
        passingReason= ((LookupSubActivity)getActivity()).getPassingReason();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Enter device ID");
        initSharedPf();
        setScanner();
        initProgressDialog();
        setInfoButton();
        registerBroadcast();
        checkAndShowUserIdDialog();
    }

    private void initSharedPf() {
        sharedPrefHelper = SharedPrefHelper.getInstance();
    }

    private void checkAndShowUserIdDialog() {
        if(!isUserIdSet())
            showEnterIdDialog();
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
    }

    private void setInfoButton() {
        binding.btGetInfo.setOnClickListener(view -> {
            String deviceId = binding.etDeviceId.getText().toString().trim();
            if(!deviceId.isEmpty()) {
                if (AppUtils.isNetworkAvailable()) {
                    progressDialog.show();
                    apiService.getDeviceLookup(binding.etDeviceId.getText().toString()).enqueue(new RetrofitCallback<LookupResponse>() {
                        @Override
                        public void handleSuccess(Call<LookupResponse> call, Response<LookupResponse> response) {
                            if(getActivity()!=null)
                                AppUtils.hideKeyboard(getActivity());
                            Log.e("handle ", response.code() + "");
                            DeviceDetailFragment deviceDetailFragment = new DeviceDetailFragment();
                            passingReason.setDeviceid(binding.etDeviceId.getText().toString());
                            ((LookupSubActivity) getActivity()).setPassingReason(passingReason);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("lookup_response", response.body());
                            deviceDetailFragment.setArguments(bundle);
                            fragmentController.loadFragment(deviceDetailFragment, getFragmentManager(), R.id.frameLayout, true);
                            progressDialog.dismiss();
                        }

                        @Override
                        public void handleFailure(Call<LookupResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else
                    Toast.makeText(getContext(), "Internet not available", Toast.LENGTH_SHORT).show();
            }
                else
                    Toast.makeText(getContext(), "Device id can't be empty", Toast.LENGTH_LONG).show();

        });
        binding.fastTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TODO : Open new activity ...
                Intent intent = new Intent(getContext(), FastTagActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setScanner() {
        binding.ibQrScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null)
                    AppUtils.hideKeyboard(getActivity());
                QRScannerFragment qrScannerFragment = new QRScannerFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container,qrScannerFragment).addToBackStack("qr_scanner");
                transaction.commit();
            }
        });
    }


    public void registerBroadcast() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter(MESSENGER_SCANNED_ID));
    }


    public void unRegisterBroadcastReceiver() {
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
// Specify the type of Input expected; this, for example, sets the Input as repair password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Your Phone Number");
        builder.setPositiveButton("OK", null);
        builder.setView(input);
        builder.setCancelable(false);

        final AlertDialog mAlertDialog = builder.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setTextColor(Color.BLACK);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        if(!input.getText().toString().trim().equals("")) {
                            sharedPrefHelper.setStringData(USER_ID ,input.getText().toString());
                            mAlertDialog.cancel();
                        } else
                            Toast.makeText(getContext(), "User Id can't be Empty", Toast.LENGTH_LONG).show();
                    }
                });
            }
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
    public void getComponentFactory() {}

    @Override
    public String getTitle() {
        return "Enter device ID";
    }

}
