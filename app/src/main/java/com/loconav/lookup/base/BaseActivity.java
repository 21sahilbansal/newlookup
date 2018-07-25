package com.loconav.lookup.base;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by prateek on 09/07/18.
 */

public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onResume() {
        super.onResume();
//            TODO: verify existing data and based on that throw to splash screen
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
