package com.loconav.lookup.login;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.loconav.lookup.R;
import com.loconav.lookup.databinding.ActivityLoginBinding;
import com.loconav.lookup.login.model.Creds;
import com.loconav.lookup.login.model.LoginResponse;

public class LoginActivity extends AppCompatActivity implements LoginView{
    private ActivityLoginBinding binding;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
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
