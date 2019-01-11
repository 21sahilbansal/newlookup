package com.loconav.lookup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.loconav.lookup.base.BaseActivity;

public class ScreenshotActivity extends BaseActivity {
    String bywhom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Whenever you call this activity pass a key 'bywhom' in bundles as string else the app will crash we
        //specify the action on back button accordingly.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshot);
        setTitle("Share");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bywhom=getIntent().getExtras().getString("bywhom");
        if(bywhom.equals("NewInstallationFragment")) {
            Intent i = new Intent(this, InstallLogsActivity.class);
            startActivity(i);
            finish();
        }
        else
        {
            finish();
        }
    }

    @Override
    public boolean showBackButton() {
        return true;
    }
}
