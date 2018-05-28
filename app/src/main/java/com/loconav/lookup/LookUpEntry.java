package com.loconav.lookup;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.loconav.lookup.model.LookupResponse;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.loconav.lookup.Constants.DEVICE_ID;
import static com.loconav.lookup.Constants.MESSENGER_SCANNED_ID;
import static com.loconav.lookup.application.LookUpApplication.editor;
import static com.loconav.lookup.application.LookUpApplication.sharedPreferences;

public class LookUpEntry extends AppCompatActivity {
    @BindView(R.id.ib_qr_scanner) ImageButton ibOpenQrScanner;
    @BindView(R.id.et_device_id) EditText etDeviceId;
    @BindView(R.id.bt_get_info) Button btGetInfo;
    @BindView(R.id.fast_tag) Button fastTag;
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    private ProgressDialog progressDialog;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lookup);
        ButterKnife.bind(this);
        setScanner();
        initProgressDialog();
        setInfoButton();
        registerBroadcast();
        checkAndShowUserIdDialog();
    }

    private void checkAndShowUserIdDialog() {
        if(!isUserIdSet()) {
            showEnterIdDialog();
        }
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(LookUpEntry.this);
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
                            Intent intent  = new Intent(LookUpEntry.this, MainActivity3.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.DEVICE_ID, etDeviceId.getText().toString());
                            bundle.putSerializable("lookup_response", response.body());
                            intent.putExtras(bundle);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }

                        @Override
                        public void handleFailure(Call<LookupResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else
                    Toast.makeText(getBaseContext(), "Device id can't be empty", Toast.LENGTH_LONG).show();

            }
        });
        fastTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TODO : Open new activity ...
                Intent intent = new Intent(LookUpEntry.this, FastTagActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setScanner() {
        ibOpenQrScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideKeyboard(LookUpEntry.this);
                QRScannerFragment1 qrScannerFragment = new QRScannerFragment1();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container,qrScannerFragment).addToBackStack("qr_scanner");
                transaction.commit();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_user:
                Intent intent = new Intent(this, UserActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void registerBroadcast() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(MESSENGER_SCANNED_ID));
    }


    public void unRegisterBroadcastReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBroadcastReceiver();
    }


    private void showEnterIdDialog() {
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                            editor.putString(Constants.USER_ID ,input.getText().toString());
                            editor.commit();
                            mAlertDialog.cancel();
                        } else
                            Toast.makeText(getBaseContext(), "User Id can't be Empty", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        mAlertDialog.show();
    }

    private boolean isUserIdSet() {
        return !sharedPreferences.getString(Constants.USER_ID, "").equals("");
    }
}
