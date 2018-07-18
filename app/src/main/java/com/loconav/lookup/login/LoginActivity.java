package com.loconav.lookup.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.loconav.lookup.LookUpEntry;
import com.loconav.lookup.R;
import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.databinding.ActivityLoginBinding;
import com.loconav.lookup.login.model.Creds;
import com.loconav.lookup.login.model.LoginResponse;

import static com.loconav.lookup.Constants.USER_ID;
import static com.loconav.lookup.UserPrefs.location;
import static com.loconav.lookup.UserPrefs.name;
import static com.loconav.lookup.UserPrefs.authenticationToken;
import static com.loconav.lookup.UserPrefs.phoneNumber;

public class LoginActivity extends AppCompatActivity implements LoginView{
    private ActivityLoginBinding binding;
    private ProgressDialog dialog;
    private SharedPrefHelper sharedPrefHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        sharedPrefHelper=SharedPrefHelper.getInstance(getBaseContext());
        initProgressDialog();
        Creds creds = new Creds();
        binding.setCred(creds);
        binding.setPresenter(new LoginPresenterImpl(this));
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(getBaseContext(), resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginSuccess(LoginResponse loginResponse) {
        sharedPrefHelper.setStringData(USER_ID,loginResponse.getUser().getId().toString());
        sharedPrefHelper.setStringData(authenticationToken, loginResponse.getUser().getAuthenticationToken());
        sharedPrefHelper.setStringData(phoneNumber, loginResponse.getUser().getPhoneNumber());
        sharedPrefHelper.setStringData(location, loginResponse.getUser().getLocation());
        sharedPrefHelper.setStringData(name, loginResponse.getUser().getName());
        Log.e("shared",""+sharedPrefHelper.getStringData(authenticationToken));
        Intent intent=new Intent(getBaseContext(),LookUpEntry.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFailure() {}

    private void initProgressDialog() {
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Please wait..");
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

}
