package com.loconav.lookup;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.login.SplashActivity;
import com.loconav.lookup.base.BaseActivity;
import com.loconav.lookup.databinding.ActivityUserBinding;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.utils.AppUtils;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import static com.loconav.lookup.Constants.IS_LOGGED_IN;
import static com.loconav.lookup.Constants.USER_ID;
import static com.loconav.lookup.UserPrefs.authenticationToken;
import static com.loconav.lookup.UserPrefs.code;
import static com.loconav.lookup.UserPrefs.location;
import static com.loconav.lookup.UserPrefs.name;
import static com.loconav.lookup.UserPrefs.phoneNumber;

public class UserActivity extends BaseActivity implements View.OnClickListener{
    private ActivityUserBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user);
    }

    @Override
    public boolean showBackButton() {
        return true;
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
