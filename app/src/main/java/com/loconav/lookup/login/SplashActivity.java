package com.loconav.lookup.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.loconav.lookup.LookUpEntry;
import com.loconav.lookup.R;
import com.loconav.lookup.application.SharedPrefHelper;

import static com.loconav.lookup.Constants.IS_LOGGED_IN;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(SharedPrefHelper.getInstance(getBaseContext()).getBooleanData(IS_LOGGED_IN)) {
            Intent intent= new Intent(this, LookUpEntry.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
