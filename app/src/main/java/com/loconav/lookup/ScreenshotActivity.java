package com.loconav.lookup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.loconav.lookup.base.BaseActivity;

public class ScreenshotActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshot);
        setTitle("Share");
    }



    @Override
    public boolean showBackButton() {
        return true;
    }
}
