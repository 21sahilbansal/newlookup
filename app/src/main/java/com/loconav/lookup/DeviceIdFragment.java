package com.loconav.lookup;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.model.LookupResponse;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static com.loconav.lookup.Constants.DEVICE_ID;
import static com.loconav.lookup.Constants.MESSENGER_SCANNED_ID;
import static com.loconav.lookup.Constants.USER_ID;

/**
 * Created by sejal on 28-06-2018.
 */

public class DeviceIdFragment extends BaseFragment {

    @BindView(R.id.ib_qr_scanner)
    ImageButton ibOpenQrScanner;
    @BindView(R.id.et_device_id) EditText etDeviceId;
    @BindView(R.id.bt_get_info) Button btGetInfo;
    @BindView(R.id.fast_tag) Button fastTag;
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    private ProgressDialog progressDialog;
    private SharedPrefHelper sharedPrefHelper;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(DEVICE_ID);
            Log.d("receiver", "Got message: " + message);
            etDeviceId.setText(message);
            etDeviceId.setSelection(etDeviceId.getText().length());
        }
    };

    @Override
    int setViewId() {
        return R.layout.fragment_device_id;
    }

    @Override
    void onFragmentCreated() {
        ButterKnife.bind(this, getView());
        initSharedPf();
        setScanner();
        initProgressDialog();
        setInfoButton();
        registerBroadcast();
        checkAndShowUserIdDialog();
    }
        private void initSharedPf() {
        sharedPrefHelper = SharedPrefHelper.getInstance(getContext());
    }

    private void checkAndShowUserIdDialog() {
        if(!isUserIdSet()) {
            showEnterIdDialog();
        }
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
    }

    private void setInfoButton() {
        btGetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deviceId = etDeviceId.getText().toString().trim();
                if(!deviceId.isEmpty()) {
                    progressDialog.show();
                    apiService.getDeviceLookup(etDeviceId.getText().toString()).enqueue(new RetrofitCallback<LookupResponse>() {
                        @Override
                        public void handleSuccess(Call<LookupResponse> call, Response<LookupResponse> response) {
                            Log.e("handle ", response.code() +"");
                            Intent intent  = new Intent(getContext(), MainActivity3.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(DEVICE_ID, etDeviceId.getText().toString());
                            bundle.putSerializable("lookup_response", response.body());
                            intent.putExtras(bundle);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }

                        @Override
                        public void handleFailure(Call<LookupResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else
                    Toast.makeText(getContext(), "Device id can't be empty", Toast.LENGTH_LONG).show();

            }
        });
        fastTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TODO : Open new activity ...
                Intent intent = new Intent(getContext(), FastTagActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setScanner() {
        ibOpenQrScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideKeyboard(getActivity());
                QRScannerFragment1 qrScannerFragment = new QRScannerFragment1();
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

    private void showEnterIdDialog() {
        final EditText input = new EditText(getActivity());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
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
                           // editor.putString(Constants.USER_ID ,input.getText().toString());
                            sharedPrefHelper.setStringData(USER_ID ,input.getText().toString());
                            //editor.commit();
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
       // return !sharedPreferences.getString(Constants.USER_ID, "").equals("");
        return !sharedPrefHelper.getStringData(USER_ID).equals("");
    }

    @Override
    void bindView(View view) {

    }

    @Override
    void getComponentFactory() {

    }
}
