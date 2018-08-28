package com.loconav.lookup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.databinding.ActivityUserBinding;
import com.loconav.lookup.login.SplashActivity;
import com.loconav.lookup.base.BaseActivity;
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
        CommonFunction.setEditText(binding.userId,SharedPrefHelper.getInstance(getBaseContext()).getStringData(code));
    }

    @Override
    public void onClick(View view) {
            finish();
    }
}
