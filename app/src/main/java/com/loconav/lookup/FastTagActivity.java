package com.loconav.lookup;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.loconav.lookup.databinding.ActivityFastTagBinding;

public class FastTagActivity extends AppCompatActivity {
    private ActivityFastTagBinding binding;
    private final String fastTagUrl = "http://192.168.2.188:3000/installers";
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.look_up_entry);
        binding.wvFastTag.setWebViewClient(new MyBrowser());
        binding.wvFastTag.getSettings().setLoadsImagesAutomatically(true);
        binding.wvFastTag.getSettings().setJavaScriptEnabled(true);
        binding.wvFastTag.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.wvFastTag.loadUrl(fastTagUrl);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }
}
