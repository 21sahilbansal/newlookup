package com.loconav.lookup;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.login.SplashActivity;
import com.loconav.lookup.base.BaseActivity;
import com.loconav.lookup.databinding.ActivityUserBinding;
import com.loconav.lookup.model.Repairs;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.network.rest.LoginApiClient;
import com.loconav.lookup.network.rest.StagingApiClient;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.loconav.lookup.Constants.IS_LOGGED_IN;
import static com.loconav.lookup.Constants.USER_ID;
import static com.loconav.lookup.UserPrefs.authenticationToken;
import static com.loconav.lookup.UserPrefs.code;
import static com.loconav.lookup.UserPrefs.location;
import static com.loconav.lookup.UserPrefs.name;
import static com.loconav.lookup.UserPrefs.phoneNumber;

public class UserActivity extends BaseActivity implements View.OnClickListener{
    private SharedPrefHelper sharedPrefHelper ;
    private ActivityUserBinding binding;
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        initSharedPf();
        attachClickListener();
        fillUserId();
    }

    @Override
    public boolean showBackButton() {
        return true;
    }

    private void initSharedPf() {
      sharedPrefHelper = SharedPrefHelper.getInstance(getBaseContext());
    }

    private void attachClickListener() {
        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPrefHelper.removeStringData(code);
                sharedPrefHelper.removeStringData(USER_ID);
                sharedPrefHelper.removeStringData(authenticationToken);
                sharedPrefHelper.removeStringData(phoneNumber);
                sharedPrefHelper.removeStringData(location);
                sharedPrefHelper.removeStringData(name);
                sharedPrefHelper.setBooleanData(IS_LOGGED_IN,false);
                Intent intent=new Intent(getBaseContext(),SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    private void fillUserId() {
      binding.userId.setText(SharedPrefHelper.getInstance(getBaseContext()).getStringData(code));
        binding.userName.setText(SharedPrefHelper.getInstance(getBaseContext()).getStringData(name));
        binding.userPhone.setText(SharedPrefHelper.getInstance(getBaseContext()).getStringData(phoneNumber));
    }

    public void RepairLogs(View view)
    {
        if(Utility.isNetworkAvailable(this)) {
            Bundle bundle = new Bundle();
            bundle.putString("task", "RepairLogs");
            Intent intent = new Intent(this, UserActivitySubActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Internet not available", Toast.LENGTH_SHORT).show();
        }
    }
    public void NewInstall(View view)
    {
        if(Utility.isNetworkAvailable(this)) {
            Bundle bundle = new Bundle();
            bundle.putString("task", "InstallLogs");
            Intent intent = new Intent(this, UserActivitySubActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Internet not available", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View view) {
            finish();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }
}
