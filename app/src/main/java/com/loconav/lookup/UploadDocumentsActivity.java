package com.loconav.lookup;

import android.os.Bundle;

import com.loconav.lookup.base.BaseActivity;

public class UploadDocumentsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents_upload);
    }

    @Override
    public boolean showBackButton() {
        return true;
    }
}
