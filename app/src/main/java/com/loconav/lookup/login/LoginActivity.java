package com.loconav.lookup.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.loconav.lookup.R;
import com.loconav.lookup.Toaster;
import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.databinding.ActivityLoginBinding;
import com.loconav.lookup.login.model.Creds;
import com.loconav.lookup.login.model.LoginResponse;
import com.loconav.lookup.utils.AppUtils;

import static com.loconav.lookup.Constants.IS_LOGGED_IN;
import static com.loconav.lookup.Constants.USER_ID;
import static com.loconav.lookup.UserPrefs.authenticationToken;
import static com.loconav.lookup.UserPrefs.code;
import static com.loconav.lookup.UserPrefs.location;
import static com.loconav.lookup.UserPrefs.name;
import static com.loconav.lookup.UserPrefs.phoneNumber;

public class LoginActivity extends AppCompatActivity implements LoginView{
    private ProgressDialog dialog;
    private SharedPrefHelper sharedPrefHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        sharedPrefHelper=SharedPrefHelper.getInstance();
        initProgressDialog();
        Creds creds = new Creds();
        binding.setCred(creds);
        binding.setPresenter(new LoginPresenterImpl(this));
    }

    @Override
    public void showToast(int resId) {
        Toaster.makeToast(String.valueOf(resId));
    }

    @Override
    public void showToast(String message) {
        Toaster.makeToast(message);
    }

    @Override
    public void onLoginSuccess(LoginResponse loginResponse) {
        sharedPrefHelper.setStringData(USER_ID,loginResponse.getUser().getId().toString());
        sharedPrefHelper.setStringData(authenticationToken, loginResponse.getUser().getAuthenticationToken());
        sharedPrefHelper.setStringData(phoneNumber, loginResponse.getUser().getPhoneNumber());
        sharedPrefHelper.setStringData(location, loginResponse.getUser().getLocation());
        sharedPrefHelper.setStringData(name, loginResponse.getUser().getName());
        sharedPrefHelper.setStringData(code, loginResponse.getUser().getCode());
        sharedPrefHelper.setBooleanDataWithCommit(IS_LOGGED_IN,true);
        Log.e("shared",""+sharedPrefHelper.getStringData(authenticationToken));
        Intent intent = new Intent(getBaseContext(),SplashActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailure() {}

    private void initProgressDialog() {
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage(getString(R.string.please_wait));
        dialog.setCancelable(false);
    }

    @Override
    public void showSigningInDialog() {
        dialog.show();
    }

    @Override
    public void hideSigningInDialog() {
        dialog.dismiss();
    }

    @Override
    public boolean isUserOnline() {
        return AppUtils.isNetworkAvailable();
    }

}
