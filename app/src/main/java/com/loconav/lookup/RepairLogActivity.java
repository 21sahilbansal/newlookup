package com.loconav.lookup;

import android.os.Bundle;

import com.loconav.lookup.base.BaseActivity;

public class RepairLogActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs_repair);
    }

    @Override
    public boolean showBackButton() {
        return true;
    }
}
